package com.goquizit.services;

import com.goquizit.DTO.CreateUpdateAnswersDTO;
import com.goquizit.DTO.CreateUpdateQuestionDTO;
import com.goquizit.DTO.QuestionWithAnswersInputDTO;
import com.goquizit.DTO.QuestionWithAnswersOutputDTO;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.DTO.outputDTO.QuizOutputDTO;
import com.goquizit.exception.InvalidContentException;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.Answer;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerService answerService;

    @Autowired
    QuizService quizService;


    public List<QuestionOutputDTO> getQuestionsByQuizId(UUID quizId) {
        try {
            List<Question> questions = quizService.getOne(quizId).getQuestions();
            List<QuestionOutputDTO> outputDTOS = new ArrayList<>();
            questions.forEach(question -> outputDTOS.add(mapQuestionToOutput(question, quizId)));
            return outputDTOS;
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    //ToDO check how to get quizId - query
    public QuestionOutputDTO getQuestionById(UUID questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        return mapQuestionToOutput(question, question.getQuiz().getId());
    }

    public List<QuestionOutputDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findAll();
        List<QuestionOutputDTO> outputDTOS = new ArrayList<>();
        questions.forEach(question -> outputDTOS.add(mapQuestionToOutput(question, question.getQuizId())));
        return outputDTOS;
    }

    public QuestionOutputDTO createQuestion(UUID quiz_id, @Valid CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        try {
            Question question = mapDtoToQuestion(quiz_id, createUpdateQuestionDTO);
            Quiz quiz = quizService.getOne(quiz_id);
            quiz.getQuestions().add(question);
            Quiz tempQuiz = quizService.save(quiz);
            Question newQuestion = tempQuiz.getQuestions().get(tempQuiz.getQuestions().size() - 1);
            return mapQuestionToOutput(newQuestion, quiz_id);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }


    public QuestionOutputDTO updateQuestionById(UUID questionId, @Valid CreateUpdateQuestionDTO question) {
        Question questionToUpdate = mapDtoToQuestionUpdate(questionId, question);
        questionRepository.save(questionToUpdate);
        return mapQuestionToOutput(questionToUpdate, questionToUpdate.getQuizId());
    }

    public ResponseEntity deleteById(UUID questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        questionRepository.delete(question);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public QuestionWithAnswersOutputDTO createQuestionWithAnswers(UUID quiz_id, @Valid QuestionWithAnswersInputDTO questionWithAnswersInputDTO) {
        try {
            CreateUpdateQuestionDTO createUpdateQuestionDTO = questionWithAnswersInputDTO.getQuestion();
            List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS = questionWithAnswersInputDTO.getAnswers();
            Question question = mapDtoToQuestion(quiz_id, createUpdateQuestionDTO);
            Quiz quiz = quizService.getOne(quiz_id);
            quiz.getQuestions().add(question);
            Quiz newQuiz = quizService.save(quiz);
            List<AnswerOutputDTO> outputAnswerDTOS = answerService.createAnswers(createUpdateAnswersDTOS, newQuiz.getLastQuestion().getQuestionId());
            QuestionOutputDTO outputDTO = mapQuestionToOutput(newQuiz.getLastQuestion(), quiz_id);
            return new QuestionWithAnswersOutputDTO(outputDTO, outputAnswerDTOS);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public QuestionWithAnswersOutputDTO updateQuestionWithAnswers(UUID question_id, @Valid QuestionWithAnswersInputDTO questionWithAnswersInputDTO) {
        try {
            CreateUpdateQuestionDTO createUpdateQuestionDTO = questionWithAnswersInputDTO.getQuestion();
            List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS = questionWithAnswersInputDTO.getAnswers();
            Question question = mapDtoToQuestionUpdate(question_id, createUpdateQuestionDTO);
            question.getAnswers().forEach(answer -> answerService.delete(answer));
            List<Answer> answers = answerService.mapDtoToAnswers(createUpdateAnswersDTOS);
            question.setAnswers(answers);
            Question newQuestion = questionRepository.save(question);
            return new QuestionWithAnswersOutputDTO(mapQuestionToOutput(newQuestion, newQuestion.getQuizId()), answerService.mapAnswersToOutput(answers, question_id));
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public List<QuestionWithAnswersOutputDTO> getQuestionWithAnswers(UUID quiz_id) {
        try {
            List<Question> questions = quizService.getOne(quiz_id).getQuestions();
            List<QuestionWithAnswersOutputDTO> outputQuestionWithAnswers = new ArrayList<>();
            questions.forEach(question ->
            {
                List<Answer> answers = question.getAnswers();
                QuestionOutputDTO outputQuestion = mapQuestionToOutput(question, quiz_id);
                List<AnswerOutputDTO> outputAnswers = answerService.mapAnswersToOutput(answers, question.getQuestionId());
                outputQuestionWithAnswers.add(new QuestionWithAnswersOutputDTO(outputQuestion, outputAnswers));
            });
            return outputQuestionWithAnswers;
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private int setDuration(UUID quiz_id, int duration) {
        QuizOutputDTO quiz = quizService.getQuizById(quiz_id);
        if (quiz.isKahoot()) {
            if (duration < 1)
                throw new InvalidContentException("Duration");
        }
        return duration;
    }

    public Question getOne(UUID quiestionId) {
        return questionRepository.getOne(quiestionId);
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    private Question mapDtoToQuestion(UUID quiz_id, @Valid CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        Question question = new Question();
        question.setType(createUpdateQuestionDTO.getType());
        question.setValue(createUpdateQuestionDTO.getValue());
        question.setDuration(setDuration(quiz_id, createUpdateQuestionDTO.getDuration()));
        return question;
    }

    private Question mapDtoToQuestionUpdate(UUID questionId, @Valid CreateUpdateQuestionDTO question) {
        Question questionToUpdate = questionRepository.getOne(questionId);
        questionToUpdate.setValue(question.getValue());
        questionToUpdate.setType(question.getType());
        if (question.getDuration() > 0)
            questionToUpdate.setDuration(question.getDuration());
        return questionToUpdate;
    }

    public List<AnswerOutputDTO> getAnswersByQuestionID(UUID questionId) {
        try {
            List<Answer> answers = questionRepository.getOne(questionId).getAnswers();
            return answerService.mapAnswersToOutput(answers);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private QuestionOutputDTO mapQuestionToOutput(Question question, UUID quizId) {
        QuestionOutputDTO outputDTO = new QuestionOutputDTO();
        outputDTO.setQuestionId(question.getQuestionId());
        outputDTO.setDuration(question.getDuration());
        outputDTO.setType(question.getType());
        outputDTO.setValue(question.getValue());
        outputDTO.setQuizId(quizId);
        return outputDTO;
    }


}
