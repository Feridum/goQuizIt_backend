package com.goquizit.services;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Answer;
import com.goquizit.model.Question;
import com.goquizit.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Question createQuestion(UUID quiz_id, @Valid Question question) {
        question.setQuizId(quiz_id);
       return this.createQuestion(question);
    }

    public ResponseEntity<?> updateQuestionById(UUID questionId, @Valid Question question)
    {
        Question questionToUpdate = questionRepository.getOne(questionId);
        questionToUpdate.setValue(question.getValue());
        questionToUpdate.setType(question.getType());
        questionToUpdate.setDuration(question.getDuration());
        questionRepository.save(questionToUpdate);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> deleteById(UUID questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        List<Answer> answers = answerService.findByQuestionId(questionId);
        answers.forEach(answer -> answerService.deleteAnswerById(answer.getQuestionId()));
        questionRepository.delete(question);
        return ResponseEntity.ok().build();
    }
}
