package com.goquizit.controller;

import com.goquizit.DTO.CreateUpdateAnswersDTO;
import com.goquizit.DTO.CreateUpdateQuestionDTO;
import com.goquizit.DTO.CreateUpdateQuizDTO;
import com.goquizit.DTO.QuestionWithAnswersInputDTO;
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

    // Answers methods
    @PostMapping("/question/{question_id}/answers")
    public List<Answer> createAnswers(@Valid @RequestBody List<CreateUpdateAnswersDTO> answers, @PathVariable("question_id") UUID questionId) {
        return answerService.createAnswers(answers,questionId);
    }

    @GetMapping("/question/{question_id}/answers")
    public List<Answer> getAnswersByQuestionId(@PathVariable("question_id") UUID questionId) {
        return answerService.findByQuestionId(questionId);
    }

    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    @GetMapping("/answer/{answer_id}")
    public Answer getAnswerById(@PathVariable(value = "answer_id") UUID answerId) {
        return answerService.getAnswerById(answerId);
    }

    @PutMapping("/answer/{answer_id}")
    public ResponseEntity<?> updateAnswerById(@PathVariable(value = "answer_id") UUID answerId, @Valid @RequestBody CreateUpdateAnswersDTO answer) {
        return answerService.updateAnswerById(answerId, answer);
    }

    @DeleteMapping("/answer/{answer_id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(value = "answer_id") UUID answerId) {
        return answerService.deleteAnswerById(answerId);
    }

    //--------------------------------------------------------------------

    // TODO: Implement Player API
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
    @PostMapping("/quiz/{quiz_id}/questions")
    public ResponseEntity<?> createQuestion(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody CreateUpdateQuestionDTO question) {
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

    @PutMapping("/question/{question_id}")
    public ResponseEntity<?> updateQuestionById(@PathVariable(value = "question_id") UUID questionId, @Valid @RequestBody CreateUpdateQuestionDTO question) {
        return questionService.updateQuestionById(questionId, question);
    }

    @DeleteMapping("/question/{question_id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable(value = "question_id") UUID questionId) {
        return questionService.deleteById(questionId);
    }

    @PostMapping("/quiz/{quiz_id}/questionWithAnswers")
    public ResponseEntity<?> createQuestionWithAnswers(@RequestBody QuestionWithAnswersInputDTO questionWithAnswersInputDTO, @PathVariable("quiz_id") UUID quiz_id) {
        return questionService.createQuestionWithAnswers(quiz_id, questionWithAnswersInputDTO);
    }

    @PutMapping("/quiz/{question_id}/questionWithAnswers")
    public ResponseEntity<?> updateQuestionWithAnswers(@RequestBody QuestionWithAnswersInputDTO questionWithAnswersInputDTO, @PathVariable("question_id") UUID quiz_id) {
        return questionService.updateQuestionWithAnswers(quiz_id, questionWithAnswersInputDTO);
    }
    //--------------------------------------------------------------------

    // Quiz methods
    @PostMapping("/quiz")
    public Quiz createQuiz(@Valid @RequestBody CreateUpdateQuizDTO quiz) {
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

    @PutMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> updateQuizBuId(@PathVariable(value = "quiz_id") UUID quizId, @Valid @RequestBody CreateUpdateQuizDTO quiz) {
        return quizService.updateById(quizId, quiz);
    }

    @DeleteMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> deleteQuizById(@PathVariable(value = "quiz_id") UUID quizId) {
        return quizService.deleteById(quizId);
    }

    @GetMapping("/quiz/{quiz_id}/token")
    public ResponseEntity<String> getQuizToken(@PathVariable(value = "quiz_id") UUID quizId) {
        return ResponseEntity.status(200).body(quizService.getToken(quizId));
    }

    //--------------------------------------------------------------------

    // TODO: Implement User API
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
