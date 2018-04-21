package com.goquizit.services;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionService questionService;


    public Quiz createQuiz(@Valid Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz createQuestion(UUID quiz_id, @Valid Question question) {
        Quiz quiz = quizRepository.findById(quiz_id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quiz_id));
        question.setQuizId(quiz_id);
        questionService.createQuestion(question);
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(UUID quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
    }

    public ResponseEntity<?> deteleById(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        List <Question> questions = questionService.findByQuizId(quizId);
        questions.forEach(question -> questionService.deleteById(question.getQuestionId()));
        quizRepository.delete(quiz);
        return ResponseEntity.ok().build();
    }
}
