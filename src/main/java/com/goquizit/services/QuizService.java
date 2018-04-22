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
        //Todo check if ownerId exist
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(UUID quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
    }

    public ResponseEntity<?> deleteById(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        List<Question> questions = questionService.findByQuizId(quizId);
        questions.forEach(question -> questionService.deleteById(question.getQuestionId()));
        quizRepository.delete(quiz);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<?> updateById(UUID quizId, @Valid Quiz quiz)
    {
        Quiz quizToUpdate = quizRepository.getOne(quizId);
        quizToUpdate.setTitle(quiz.getTitle());
        quizToUpdate.setState(quiz.getState());
        quizToUpdate.setIsKahoot(quiz.getIsKahoot());
        quizToUpdate.setEndDate(quiz.getEndDate());
        quizToUpdate.setOwnerId(quiz.getOwnerId());
        quizToUpdate.setStartDate(quiz.getStartDate());
        quizRepository.save(quizToUpdate);
        return ResponseEntity.status(204).build();
    }
}
