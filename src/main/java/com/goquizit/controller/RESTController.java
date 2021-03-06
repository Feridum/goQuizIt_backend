package com.goquizit.controller;

import com.goquizit.DTO.*;
import com.goquizit.DTO.outputDTO.*;
import com.goquizit.exception.InvalidContentException;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.QuizState;
import com.goquizit.model.User;
import com.goquizit.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Main controller of the application.
 */
//TODO Split to few smaller class
//TODO All method should return ResponseEntity class
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired
    AnswerService answerService;

    @Autowired
    QuestionService questionService;

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    @Autowired
    PlayerService playerService;

    @Autowired
    PlayerAnswerService playerAnswerService;


    // Answers methods
    @GetMapping("/question/{question_id}/answers")
    public List<AnswerOutputDTO> getAnswersByQuestionId(@PathVariable("question_id") UUID questionId) {
        return questionService.getAnswersByQuestionID(questionId);
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


    //PlayerAnswer methods
    @PostMapping("/players/{player_id}/question/{question_id}/answers")
    public ResponseEntity createPlayerAnswerToSingleMultipleQuestion(@PathVariable(value = "player_id") UUID player_id, @PathVariable(value = "question_id") UUID question_id, @Valid @RequestBody CreateUpdatePlayerAnswerDTO playerAnswerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerAnswerService.createPlayerAnswerToSingleMultipleQuestion(player_id, question_id, playerAnswerDTO));
    }

    @PostMapping("/players/{player_id}/question/{question_id}/open/answers")
    public ResponseEntity createPlayerAnswerToOpenQuestion(@PathVariable(value = "player_id") UUID player_id, @PathVariable(value = "question_id") UUID question_id, @Valid @RequestBody CreateUpdatePlayerAnswerOpenDTO playerAnswerOpenDTOS) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerAnswerService.createPlayerAnswerToOpenQuestion(player_id, question_id, playerAnswerOpenDTOS));
    }

    //--------------------------------------------------------------------

    // Players methods
    @PostMapping("/players/quiz/{quiz_id}")
    public QuestionWithAnswersAndPlayerIdDTO createPlayer(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody PlayerDTO player) {
        return playerService.create(player, quiz_id);
    }

    @GetMapping("/quiz/{quiz_id}/players")
    public List<PlayerOutputDTO> getPlayersByQuizId(@PathVariable(value = "quiz_id") UUID quiz_id) {
        return playerService.getPlayersByQuizId(quiz_id);
    }

    //--------------------------------------------------------------------
    // Questions methods
    @PostMapping("/quiz/{quiz_id}/questions")
    public QuestionOutputDTO createQuestion(@PathVariable(value = "quiz_id") UUID quiz_id, @Valid @RequestBody CreateUpdateQuestionDTO question) {
        return questionService.createQuestion(quiz_id, question);
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

    @GetMapping("/quiz/{quiz_id}/questionWithAnswers")
    public List<QuestionWithAnswersOutputDTO> getQuestionWithAnswers(@PathVariable("quiz_id") UUID quiz_id) {
        return questionService.getQuestionWithAnswers(quiz_id);
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
    public TokenOutputDTO getQuizToken(@PathVariable(value = "quiz_id") UUID quizId) throws ResourceNotFoundException {
        return quizService.getToken(quizId);
    }

    @PutMapping("/quiz/{quiz_id}/patch/{quiz_state}")
    public ResponseEntity changeStateByQuizId(@PathVariable(value = "quiz_id") UUID quizId, @PathVariable(value = "quiz_state") QuizState quizState) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.changeState(quizId, quizState));
    }

    @GetMapping("/quiz/state/{quiz_state}")
    public ResponseEntity getQuizByStatus(@PathVariable(value = "quiz_state") QuizState quizState) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getByState(quizState));
    }

    @GetMapping("/quiz/token/{token}")
    public ResponseEntity getQuizByToken(@PathVariable(value = "token") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getByToken(token));
    }

    @GetMapping("/quiz/{quiz_id}/questionNumber/{number}")
    public ResponseEntity getQuestionByQuizIdByIndex(@PathVariable(value = "quiz_id") UUID quizId, @PathVariable(value = "number") int index) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getQuestionByQuizIdByIndex(quizId, index));
    }

    @GetMapping("/quiz/{quiz_id}/summary")
    public ResponseEntity getQuizSummary(@PathVariable(value = "quiz_id") UUID quizId) {
        return ResponseEntity.status(HttpStatus.OK).body(quizService.getQuizSummary(quizId));
    }

    @GetMapping("/quiz/{quiz_id}/pdf")
    public ResponseEntity getQuizPDF(@PathVariable(value = "quiz_id") UUID quizId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + "quizReport.pdf");

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(quizService.getQuizPDF(quizId)));
    }

    //--------------------------------------------------------------------

    // User methods

    @PostMapping("/register")
    public User addUser(@Valid @RequestBody CreateUserDTO dto) {
        return userService.addUser(dto);
    }
}
