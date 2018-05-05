package com.goquizit.controller;

import com.goquizit.DTO.*;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;
import com.goquizit.DTO.outputDTO.QuizOutputDTO;
import com.goquizit.DTO.outputDTO.TokenOutputDto;
import com.goquizit.exception.InvalidContentException;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Player;
import com.goquizit.model.QuizState;
import com.goquizit.model.Users;
import com.goquizit.repository.PlayerRepository;
import com.goquizit.repository.QuizRepository;
import com.goquizit.repository.UserRepository;
import com.goquizit.services.AnswerService;
import com.goquizit.services.QuestionService;
import com.goquizit.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    QuizRepository quizRepository;


    // Answers methods
    @PostMapping("/question/{question_id}/answers")
    public List<AnswerOutputDTO> createAnswers(@Valid @RequestBody List<CreateUpdateAnswersDTO> answers, @PathVariable("question_id") UUID questionId) {
        return answerService.createAnswers(answers, questionId);
    }

    @GetMapping("/question/{question_id}/answers")
    public List<AnswerOutputDTO> getAnswersByQuestionId(@PathVariable("question_id") UUID questionId) {
        return questionService.getAnswersByQuestionID(questionId);
    }

    @GetMapping("/answers")
    public List<AnswerOutputDTO> getAllAnswers() {
        return answerService.getAllAnswers();
    }

    @GetMapping("/answer/{answer_id}")
    public AnswerOutputDTO getAnswerById(@PathVariable(value = "answer_id") UUID answerId) throws ResourceNotFoundException {
        return answerService.getAnswerById(answerId);
    }

    @DeleteMapping("/answer/{answer_id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable(value = "answer_id") UUID answerId) throws ResourceNotFoundException {
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
    public QuestionOutputDTO createQuestion(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody CreateUpdateQuestionDTO question) {
        return questionService.createQuestion(quiz_id, question);
    }

    @GetMapping("/questions")
    public List<QuestionOutputDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/quiz/{quiz_id}/questions")
    public List<QuestionOutputDTO> getQuestionsByQuizId(@PathVariable(value = "quiz_id") UUID quizId) {
        return questionService.getQuestionsByQuizId(quizId);
    }

    @GetMapping("/question/{question_id}")
    public QuestionOutputDTO getQuestionById(@PathVariable(value = "question_id") UUID questionId) throws ResourceNotFoundException {
        return questionService.getQuestionById(questionId);
    }

    @PutMapping("/question/{question_id}")
    public QuestionOutputDTO updateQuestionById(@PathVariable(value = "question_id") UUID questionId, @Valid @RequestBody CreateUpdateQuestionDTO question) {
        return questionService.updateQuestionById(questionId, question);
    }

    @DeleteMapping("/question/{question_id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable(value = "question_id") UUID questionId) throws ResourceNotFoundException {
        return questionService.deleteById(questionId);
    }

    @PostMapping("/quiz/{quiz_id}/questionWithAnswers")
    public QuestionWithAnswersOutputDTO createQuestionWithAnswers(@RequestBody QuestionWithAnswersInputDTO questionWithAnswersInputDTO, @PathVariable("quiz_id") UUID quiz_id) {
        return questionService.createQuestionWithAnswers(quiz_id, questionWithAnswersInputDTO);
    }

    @PutMapping("/quiz/{question_id}/questionWithAnswers")
    public QuestionWithAnswersOutputDTO updateQuestionWithAnswers(@RequestBody QuestionWithAnswersInputDTO questionWithAnswersInputDTO, @PathVariable("question_id") UUID quiz_id) {
        return questionService.updateQuestionWithAnswers(quiz_id, questionWithAnswersInputDTO);
    }
    //--------------------------------------------------------------------

    // Quiz methods
    @PostMapping("/quiz")
    public QuizOutputDTO createQuiz(@Valid @RequestBody CreateUpdateQuizDTO quiz) {
        return quizService.createQuiz(quiz);
    }

    @GetMapping("/quiz")
    public List<QuizOutputDTO> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    @GetMapping("/quiz/{quiz_id}")
    public QuizOutputDTO getQuizById(@PathVariable(value = "quiz_id") UUID quizId) throws ResourceNotFoundException {
        return quizService.getQuizById(quizId);
    }

    @PutMapping("/quiz/{quiz_id}")
    public QuizOutputDTO updateQuizBuId(@PathVariable(value = "quiz_id") UUID quizId, @Valid @RequestBody CreateUpdateQuizDTO quiz) throws InvalidContentException {
        return quizService.updateQuizById(quizId, quiz);
    }

    @DeleteMapping("/quiz/{quiz_id}")
    public ResponseEntity<?> deleteQuizById(@PathVariable(value = "quiz_id") UUID quizId) throws ResourceNotFoundException {
        return quizService.deleteQuizById(quizId);
    }

    @GetMapping("/quiz/{quiz_id}/token")
    public TokenOutputDto getQuizToken(@PathVariable(value = "quiz_id") UUID quizId) throws ResourceNotFoundException {
        return quizService.getToken(quizId);
    }

    @PutMapping("/quiz/{quiz_id}/patch/{quiz_state}")
    public ResponseEntity getQuizToken(@PathVariable(value = "quiz_id") UUID quizId, @PathVariable(value = "quiz_state") QuizState quizState) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.changeState(quizId, quizState));
    }

    @GetMapping("/quiz/state/{quiz_state}")
    public ResponseEntity getQuizByStatus(@PathVariable(value = "quiz_state") QuizState quizState) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getByState(quizState));
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
