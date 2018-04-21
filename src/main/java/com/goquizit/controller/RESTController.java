package com.goquizit.controller;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.*;
import com.goquizit.repository.AnswerRepository;
import com.goquizit.repository.PlayerRepository;
import com.goquizit.repository.UserRepository;
import com.goquizit.services.QuestionService;
import com.goquizit.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired
    QuestionService questionService;
    @Autowired
    QuizService quizService;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public String retu() {
        return "The best app ;)";
    }

    // Answers methods
    @PostMapping("/answers")
    public Answer createAnswer(@Valid @RequestBody Answer answer) {
        return answerRepository.save(answer);
    }

    @GetMapping("/answers")
    public List<Answer> getAllAnswers(@Valid @RequestBody Answer answer) {
        return answerRepository.findAll();
    }

    @PostMapping("/answers/{answer_id}")
    public Answer getAnswerById(@PathVariable(value = "answer_id") Long answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
    }

    @DeleteMapping("/answers/{answer_id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(value = "answer_id") Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
        answerRepository.delete(answer);
        return ResponseEntity.ok().build();
    }


    // Players methods
    @PostMapping("/players")
    public Player createPlayer(@Valid @RequestBody Player player) {
        return playerRepository.save(player);
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }


    // Questions methods
    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/quiz/{quiz_id}/questions")
    public List<Question> getQuestionsByQuizId(@PathVariable(value = "quiz_id") UUID quizId) {
        return questionService.findByQuizId(quizId);
    }

    @GetMapping("/question/{question_id}")
    public Question getQuestionById(@PathVariable(value = "question_id") UUID questionId) {
        return questionService.getQuestionById(questionId);
    }

    @DeleteMapping("/question/{question_id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(value = "question_id") UUID questionId) {
        return questionService.deleteById(questionId);
    }


    // Quiz methods
    @PostMapping("/quiz")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/quiz")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @PostMapping("/quiz/{quiz_id}/questions/")
    public Quiz createQuestion(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody Question question) {
        return quizService.createQuestion(quiz_id, question);
    }

    @GetMapping("/quiz/{quiz_id}")
    public Quiz getQuizById(@PathVariable(value = "quiz_id") UUID quizId) {
        return quizService.getQuizById(quizId);
    }

    @DeleteMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> deleteQuizById(@PathVariable(value = "quiz_id") UUID quizId) {
        return quizService.deteleById(quizId);
    }

    // Users methods
    @PostMapping("/users")
    public Users createUser(@Valid @RequestBody Users users) {
        return userRepository.save(users);
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}
