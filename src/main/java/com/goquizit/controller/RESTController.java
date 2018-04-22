package com.goquizit.controller;

import com.goquizit.model.*;
import com.goquizit.repository.PlayerRepository;
import com.goquizit.repository.UserRepository;
import com.goquizit.services.AnswerService;
import com.goquizit.services.QuestionService;
import com.goquizit.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Main controller of the application.
 */
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired
    AnswerService answerService;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuizService quizService;

    // TODO: refactor to service
    @Autowired
    PlayerRepository playerRepository;

    // TODO: refactor to service
    @Autowired
    UserRepository userRepository;

    //--------------------------------------------------------------------

    // Sample method of the application
    @GetMapping("")
    public String retu() {
        return "The best app ;)";
    }

    //--------------------------------------------------------------------

    // Answers methods
    //Czy jest to potrzebne???
    @PostMapping("/answer")
    public Answer createAnswer(@Valid @RequestBody Answer answer) {
        return answerService.createAnswer(answer);
    }

    @PostMapping("/question/{question_id}/answers")
    public List<Answer> createAnswers(@Valid @RequestBody List<Answer> answers, @PathVariable("question_id") UUID questionId) {
        return answerService.createAnswers(answers,questionId);
    }

    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    //What is that???
    @PostMapping("/answer/{answer_id}")
    public Answer getAnswerById(@PathVariable(value = "answer_id") UUID answerId) {
        return answerService.getAnswerById(answerId);
    }

    //--------------------------------------------------------------------

    // Players methods
    @PostMapping("/players")
    public Player createPlayer(@Valid @RequestBody Player player) {
        return playerRepository.save(player);
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    //--------------------------------------------------------------------

    // Questions methods
    @PostMapping("/quiz/{quiz_id}/questions/")
    public Question createQuestion(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody Question question) {
        return questionService.createQuestion(quiz_id, question);
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

    //--------------------------------------------------------------------

    // Quiz methods
    @PostMapping("/quiz")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/quiz")
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/quiz/{quiz_id}")
    public Quiz getQuizById(@PathVariable(value = "quiz_id") UUID quizId) {
        return quizService.getQuizById(quizId);
    }

    @DeleteMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> deleteQuizById(@PathVariable(value = "quiz_id") UUID quizId) {
        return quizService.deleteById(quizId);
    }

    @PutMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> updateQuizBuId(@PathVariable(value = "quiz_id") UUID quizId, @Valid @RequestBody Quiz quiz) {
        return quizService.updateById(quizId, quiz);
    }
    //--------------------------------------------------------------------

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
