package com.goquizit.services;

import com.goquizit.DTO.CreateUpdateAnswersDTO;
import com.goquizit.DTO.CreateUpdateQuestionDTO;
import com.goquizit.DTO.QuestionWithAnswersInputDTO;
import com.goquizit.DTO.QuestionWithAnswersOutputDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Answer;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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


    public Question createQuestion(@Valid Question question) {
        return questionRepository.save(question);
    }

    public List<Question> findByQuizId(UUID quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    public Question getQuestionById(UUID questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public ResponseEntity<?> createQuestion(UUID quiz_id, @Valid CreateUpdateQuestionDTO createUpdateQuestionDTO) {
        try
        {
            Question question = new Question();
            question.setType(createUpdateQuestionDTO.getType());
            question.setValue(createUpdateQuestionDTO.getValue());
            question.setDuration(setDuration(quiz_id, createUpdateQuestionDTO.getDuration()));
            question.setQuizId(quiz_id);
            Question newQuestion = this.createQuestion(question);
           return ResponseEntity.status(HttpStatus.CREATED).body(newQuestion);
        }
        catch (Exception e)
        {
           return ResponseEntity.status(HttpStatus.CREATED).body(e.getMessage());
        }
    }

    public ResponseEntity<?> updateQuestionById(UUID questionId, @Valid CreateUpdateQuestionDTO question)
    {
        Question questionToUpdate = questionRepository.getOne(questionId);
        questionToUpdate.setValue(question.getValue());
        questionToUpdate.setType(question.getType());
        questionToUpdate.setDuration(question.getDuration());
        questionRepository.save(questionToUpdate);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<?> deleteById(UUID questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        List<Answer> answers = answerService.findByQuestionId(questionId);
        answers.forEach(answer -> answerService.deleteAnswerById(answer.getQuestionId()));
        questionRepository.delete(question);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> createQuestionWithAnswers(UUID quiz_id, @Valid QuestionWithAnswersInputDTO questionWithAnswersInputDTO)
    {
        try
        {
            CreateUpdateQuestionDTO createUpdateQuestionDTO = questionWithAnswersInputDTO.getQuestion();
            List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS = questionWithAnswersInputDTO.getAnswers();
            Question question = (Question) this.createQuestion(quiz_id,createUpdateQuestionDTO).getBody();
            question.setQuizId(quiz_id);
            questionRepository.save(question);
            final UUID questionId = question.getQuestionId();
            List<Answer> answers =  answerService.createAnswers(createUpdateAnswersDTOS, questionId);
            return ResponseEntity.status(HttpStatus.CREATED).body(new QuestionWithAnswersOutputDTO(question, answers));
        }
        catch(Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    public ResponseEntity<?> updateQuestionWithAnswers(UUID question_id, @Valid QuestionWithAnswersInputDTO questionWithAnswersInputDTO)
    {
        try
        {
            CreateUpdateQuestionDTO createUpdateQuestionDTO = questionWithAnswersInputDTO.getQuestion();
            List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS = questionWithAnswersInputDTO.getAnswers();
            this.updateQuestionById(question_id,createUpdateQuestionDTO);
            List<Answer> answersToDelete = answerService.findByQuestionId(question_id);
            answersToDelete.forEach(answer -> answerService.deleteAnswerById(answer.getAnswerId()));
            answerService.createAnswers(createUpdateAnswersDTOS,question_id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch(RuntimeException e)
        {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private int setDuration(UUID quiz_id, int duration) throws Exception {
        Quiz quiz = quizService.getQuizById(quiz_id);
        if(quiz.getIsKahoot())
        {
            if(duration < 1)
                throw new Exception("Duration must be correctly declared");
        }
        return duration;
    }
}
