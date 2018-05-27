package com.goquizit.services;

import com.goquizit.DTO.AnswersToSummaryDTO;
import com.goquizit.DTO.CreateUpdateQuizDTO;
import com.goquizit.DTO.outputDTO.*;
import com.goquizit.exception.InvalidContentException;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.*;
import com.goquizit.repository.QuizRepository;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    PlayerService playerService;

    @Autowired
    AnswerService answerService;

    @Autowired
    PlayerAnswerService playerAnswerService;


    public QuizOutputDTO createQuiz(@Valid CreateUpdateQuizDTO createQuizDTO) {
        Quiz quiz = new Quiz();
        quiz = checkQuizIsKahoot(quiz, createQuizDTO);
        quiz.setTitle(createQuizDTO.getTitle());
        quiz.setIsKahoot(createQuizDTO.getIsKahoot());
        quiz.setToken(generateToken());
        quiz.setState(QuizState.INACTIVE);

        UUID ownerId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        Quiz newQuiz = userService.saveQuiz(quiz);
        System.out.println(newQuiz.getTitle());
        return mapQuizToOutput(newQuiz, ownerId);
    }

    public List<QuizOutputDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizOutputDTO> outputDTOS = new ArrayList<>();
        quizzes.forEach(quiz -> outputDTOS.add(mapQuizToOutput(quiz)));
        return outputDTOS;
    }

    public QuizOutputDTO getQuizById(UUID quizId) throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        return mapQuizToOutput(quiz);
    }

    public QuizOutputDTO updateQuizById(UUID quizId, @Valid CreateUpdateQuizDTO quiz) {
        try {
            Quiz quizToUpdate = this.getOne(quizId);
            quizToUpdate = checkQuizIsKahoot(quizToUpdate, quiz);
            quizToUpdate.setTitle(quiz.getTitle());
            quizToUpdate.setIsKahoot(quiz.getIsKahoot());
            quizToUpdate.setMailRequired(quiz.isMailRequired());
            quizToUpdate.setTelephoneNumberRequired(quiz.isTelephoneNumberRequired());
            if (quiz.getEndDate() != null)
                quizToUpdate.setEndDate(quiz.getEndDate());
            if (quiz.getState() != null)
                quizToUpdate.setState(quiz.getState());
            if (quiz.getStartDate() != null)
                quizToUpdate.setStartDate(quiz.getStartDate());
            Quiz newQuiz = quizRepository.save(quizToUpdate);
            return mapQuizToOutput(newQuiz);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public ResponseEntity deleteQuizById(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        quizRepository.delete(quiz);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private String generateToken() {
        RandomStringGenerator randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        return randomStringGenerator.generate(8);
    }

    public TokenOutputDTO getToken(UUID quizId) throws ResourceNotFoundException {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        return new TokenOutputDTO(quiz.getToken());
    }

    public Quiz getOne(UUID quizId) {
        try {
           return quizRepository.getOne(quizId);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public Quiz save(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public QuizOutputDTO changeState(UUID quizId, @Valid QuizState quizState) {
        Quiz quiz = this.getOne(quizId);
        quiz.setState(quizState);
        Quiz newQuiz = quizRepository.save(quiz);
        return mapQuizToOutput(newQuiz);
    }

    public List<QuizOutputDTO> getByState(@Valid QuizState quizState) {
        List<Quiz> quizzes = quizRepository.findByState(quizState);
        List<QuizOutputDTO> outputDTOS = new ArrayList<>();
        quizzes.forEach(quiz -> outputDTOS.add(mapQuizToOutput(quiz)));
        return outputDTOS;
    }


    public QuizOutputDTO getByToken(String token) {
        Quiz quiz = quizRepository.findByToken(token);
        checkQuizNullable(token, quiz);
        QuizOutputDTO outputDTO = mapQuizToOutput(quiz);
        if (quiz.getState() == QuizState.ACTIVE)
            return outputDTO;
        else throw new ResponseException("Quiz is not activated");
    }

    private void checkQuizNullable(String token, Quiz quiz) {
        if (quiz == null)
            throw new ResourceNotFoundException("Quiz", "token", token);
    }

    public QuestionOutputDTO getQuestionByQuizIdByIndex(UUID quizId, int index) {
        this.getOne(quizId);
        checkCountOfQuestions(quizId, index);
        Question question = questionService.findByQuizIdAndIndex(quizId, index);
        return questionService.mapQuestionToOutput(question, quizId);
    }

    private void checkCountOfQuestions(UUID quizId, int index) {
        int numberOfQuestion = questionService.getNumberOfQuestionsByQuizId(quizId);
        if (numberOfQuestion == 0)
            throw new ResponseException("Quiz have no questions yet.");
        else if (index < 0 || index >= numberOfQuestion)
            throw new ResponseException("Question with index: " + index + " does not exist for guiz id: " + quizId);
    }

    private QuizOutputDTO mapQuizToOutput(Quiz quiz) {
        QuizOutputDTO outputDTO = transQuizToOutput(quiz);
        outputDTO.setOwnerId(quiz.getOwner().getUserId());
        return outputDTO;
    }

    private QuizOutputDTO mapQuizToOutput(Quiz quiz, UUID ownerId) {
        QuizOutputDTO outputDTO = transQuizToOutput(quiz);
        outputDTO.setOwnerId(ownerId);
        return outputDTO;
    }

    private QuizOutputDTO transQuizToOutput(Quiz quiz) {
        QuizOutputDTO outputDTO = new QuizOutputDTO();
        outputDTO.setId(quiz.getId());
        outputDTO.setEndDate(quiz.getEndDate());
        outputDTO.setKahoot(quiz.getIsKahoot());
        outputDTO.setTitle(quiz.getTitle());
        outputDTO.setStartDate(quiz.getStartDate());
        outputDTO.setState(quiz.getState());
        outputDTO.setToken(quiz.getToken());
        outputDTO.setTelephoneNumberRequired(quiz.isTelephoneNumberRequired());
        outputDTO.setMailRequired(quiz.isMailRequired());
        return outputDTO;
    }

    private Quiz checkQuizIsKahoot(Quiz quiz, CreateUpdateQuizDTO createQuizDTO) throws InvalidContentException {
        if (createQuizDTO.getIsKahoot()) {
            if (createQuizDTO.getEndDate() != null && createQuizDTO.getStartDate() != null) {
                quiz.setStartDate(createQuizDTO.getStartDate());
                quiz.setEndDate(createQuizDTO.getEndDate());
                return quiz;
            } else {
                throw new InvalidContentException("StartDate,endDate");
            }
        }
        return quiz;
    }

    public List<SummaryDTO> getQuizSummary(UUID quizId) {
        try{
            List<SummaryDTO> summaryDTOS = new ArrayList<>();
            Quiz quiz = this.getOne(quizId);
            List<QuestionOutputDTO> questions = questionService.getQuestionsByQuizId(quizId);
            List<Player> players = quiz.getPlayers();
            for(Player player :players)
            {
                PlayerOutputDTO playerOutput = playerService.mapPlayerToOutputDTO(player);
                List<AnswersToSummaryDTO> answersToSummary = new ArrayList<>();
                for(QuestionOutputDTO question: questions)
                {
                    List<String> answersContent = new ArrayList<>();
                    List<PlayerAnswer> answersByQuestionID = playerAnswerService.getPlayerAnswersByByPlayerAndQuestion(question.getQuestionId(),player.getPlayerId());
                    answersByQuestionID.forEach(answerOutputDTO -> answersContent.add(answerOutputDTO.getValue()));
                    answersToSummary.add(new AnswersToSummaryDTO(question.getValue(),answersContent));
                }
                summaryDTOS.add(new SummaryDTO(playerOutput, quiz.getTitle(), answersToSummary,questions.size(),player.getResult()));
            }

            return summaryDTOS;
        }catch (PersistenceException e)
        {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }
}
