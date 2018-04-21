package com.goquizit.services;

import com.goquizit.exception.ResourceNotFoundException;
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

    public ResponseEntity<?> deleteById(UUID questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        questionRepository.delete(question);
        return ResponseEntity.ok().build();
        //Todo delete answers!!!
    }
}
