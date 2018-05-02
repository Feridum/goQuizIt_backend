package com.goquizit.services;

import com.goquizit.DTO.CreateUpdateQuizDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.model.QuizState;
import com.goquizit.repository.QuizRepository;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
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


    public Quiz createQuiz(@Valid CreateUpdateQuizDTO createQuizDTO) {
        //TODO: check if ownerId exist
        Quiz quiz = new Quiz();
        quiz.setTitle(createQuizDTO.getTitle());
        quiz.setOwnerId(createQuizDTO.getOwnerId());
        quiz.setIsKahoot(createQuizDTO.getIsKahoot());
        if(createQuizDTO.getIsKahoot())
        {
            quiz.setStartDate(createQuizDTO.getStartDate());
            quiz.setEndDate(createQuizDTO.getEndDate());
        }
        quiz.setToken(generateToken());
        quiz.setState(QuizState.INACTIVE);
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Quiz getQuizById(UUID quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
    }

    // TODO: consider changing method name: updateById -> updateQuizById
    public ResponseEntity<?> updateById(UUID quizId, @Valid CreateUpdateQuizDTO quiz) {
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

    // TODO: consider changing method name: deleteById -> deleteQuizById
    public ResponseEntity<?> deleteById(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        List<Question> questions = questionService.findByQuizId(quizId);
        questions.forEach(question -> questionService.deleteById(question.getQuestionId()));
        quizRepository.delete(quiz);
        return ResponseEntity.status(204).build();
    }

    public String generateToken() {
        RandomStringGenerator randomStringGenerator =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                        .build();
        return randomStringGenerator.generate(8);
    }

    public String getToken(UUID quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        return quiz.getToken();
    }
}
