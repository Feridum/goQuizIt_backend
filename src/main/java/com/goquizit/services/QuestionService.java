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
import com.goquizit.exception.ResponseException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.Answer;
import com.goquizit.model.Question;
import com.goquizit.model.QuestionState;
import com.goquizit.model.Quiz;
import com.goquizit.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.goquizit.model.QuestionState.*;

@Service
public class QuestionService implements Serializable {

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

    public QuestionOutputDTO getQuestionById(UUID questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        return mapQuestionToOutput(question, question.getQuiz().getId());
    }

    public QuestionOutputDTO createQuestion(UUID quiz_id, @Valid CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        try {
            Question question = mapDtoToQuestion(quiz_id, createUpdateQuestionDTO);

            Question newQuestion = createQuestionByDTO(quiz_id, question);

            return mapQuestionToOutput(newQuestion, quiz_id);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private Question createQuestionByDTO(UUID quiz_id, Question question) {
        Quiz quiz = quizService.getOne(quiz_id);
        question.setIndex(quiz.getQuestionSet().size());
        quiz.getQuestions().add(question);
        Quiz tempQuiz = quizService.save(quiz);
        return tempQuiz.getLastQuestion();
    }


    public QuestionOutputDTO updateQuestionById(UUID questionId, @Valid CreateUpdateQuestionDTO question) {
        Question questionToUpdate = mapDtoToQuestionUpdate(questionId, question);
        questionRepository.save(questionToUpdate);
        return mapQuestionToOutput(questionToUpdate, questionToUpdate.getQuizId());
    }

    public ResponseEntity deleteById(UUID questionId) throws ResourceNotFoundException {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        questionRepository.delete(question);
        this.reindexingQuestionsByQuizId(question.getIndex(), question.getQuiz().getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void reindexingQuestionsByQuizId(int index, UUID quizId) {
        try{
        Quiz quiz = quizService.getOne(quizId);
        List<Question> questions = quiz.getQuestionSet();
        questions.forEach(question ->
        {
            if (question.getIndex() > index)
                question.decrementIndex();
        });
        quiz.setQuestionSet(questions);
        quizService.save(quiz);
        }catch (EntityNotFoundException e)
        {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public QuestionWithAnswersOutputDTO createQuestionWithAnswers(UUID quiz_id, @Valid QuestionWithAnswersInputDTO questionWithAnswersInputDTO) {
        try {
            CreateUpdateQuestionDTO createUpdateQuestionDTO = questionWithAnswersInputDTO.getQuestion();
            List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS = questionWithAnswersInputDTO.getAnswers();

            switch (createUpdateQuestionDTO.getType()) {
                case SINGLE_CHOICE:
                    this.checkPositiveAnswerSingleChoice(createUpdateAnswersDTOS);
                    break;
                case MULTIPLE_CHOICE:
                    this.checkPositiveAnswersInMultipleChoice(createUpdateAnswersDTOS);
                    break;
                case OPEN:
                    break;
                case AXIS:
                case DATE:
                default:
                    break;
            }

            Question question = mapDtoToQuestion(quiz_id, createUpdateQuestionDTO);
            Question newQuestion = createQuestionByDTO(quiz_id,question);
            List<AnswerOutputDTO> outputAnswerDTOS = answerService.createAnswers(createUpdateAnswersDTOS, newQuestion.getQuestionId());

            QuestionOutputDTO outputDTO = mapQuestionToOutput(newQuestion, quiz_id);
            return new QuestionWithAnswersOutputDTO(outputDTO, outputAnswerDTOS);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    private void checkPositiveAnswerSingleChoice(List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS) {
        AtomicBoolean isPositiveAnswer = new AtomicBoolean(false);
        createUpdateAnswersDTOS.forEach(answer -> {
            if(answer.getIsPositive())
                isPositiveAnswer.set(true);
        });

       if (!isPositiveAnswer.get()) {
           throw new ResponseException("At least one answer should be positive");
       }
    }

    private void checkPositiveAnswersInMultipleChoice(List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS) {
        AtomicBoolean areAnswersOkay = new AtomicBoolean(false);
        AtomicInteger amountOfPositiveAnswers = new AtomicInteger(0);

        createUpdateAnswersDTOS.forEach(answer -> {
            if(answer.getIsPositive()) {
                amountOfPositiveAnswers.getAndIncrement();
            }
        });

        int positiveAnswers = amountOfPositiveAnswers.intValue();
        
        if (positiveAnswers < 2 || positiveAnswers > 4) {
            throw new ResponseException("There should be min 2 and man 4 positive answers");
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

    public Question getOne(UUID questionId) {
        try {
            return questionRepository.getOne(questionId);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public Question save(Question question) {
        return questionRepository.save(question);
    }

    private Question mapDtoToQuestion(UUID quiz_id, @Valid CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        Question question = new Question();
        if(createUpdateQuestionDTO.getType() == null)
            throw new InvalidContentException("Type");
        question.setType(createUpdateQuestionDTO.getType());
        question.setValue(createUpdateQuestionDTO.getValue());
        question.setDuration(setDuration(quiz_id, createUpdateQuestionDTO.getDuration()));
        return question;
    }

    private Question mapDtoToQuestionUpdate(UUID questionId, @Valid CreateUpdateQuestionDTO question) {
        try{
        Question questionToUpdate = this.getOne(questionId);
        questionToUpdate.setValue(question.getValue());
        questionToUpdate.setType(question.getType());
        if (question.getDuration() > 0)
            questionToUpdate.setDuration(question.getDuration());
        return questionToUpdate;
        }catch (EntityNotFoundException e)
        {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public List<AnswerOutputDTO> getAnswersByQuestionID(UUID questionId) {
        try {
            List<Answer> answers = this.getOne(questionId).getAnswers();
            return answerService.mapAnswersToOutput(answers);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public QuestionOutputDTO mapQuestionToOutput(Question question, UUID quizId) {
        QuestionOutputDTO outputDTO = new QuestionOutputDTO();
        outputDTO.setQuestionId(question.getQuestionId());
        outputDTO.setDuration(question.getDuration());
        outputDTO.setType(question.getType());
        outputDTO.setValue(question.getValue());
        outputDTO.setQuizId(quizId);
        outputDTO.setIndex(question.getIndex());
        return outputDTO;
    }

    public Question findByQuizIdAndIndex(UUID quizId, int index) {
        return questionRepository.findByQuizIdAndIndex(quizId, index);
    }

    public int getNumberOfQuestionsByQuizId(UUID quizId) {
        return questionRepository.getNumberOfQuestionsByQuizId(quizId);
    }
}
