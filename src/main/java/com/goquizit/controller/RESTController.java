package com.goquizit.controller;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.*;
import com.goquizit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */
@RestController
@RequestMapping("/api")
public class RESTController {

    @GetMapping("")
    public String retu() {
        return "The best app ;)";
    }

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserRepository userRepository;

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
        return questionRepository.save(question);
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }


    // Quiz methods
    @PostMapping("/quiz")
    public Quiz createQuiz(@Valid @RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping("/quiz")
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @PostMapping("/quiz/{quiz_id}/questions/")
    public Quiz createQuestion(@PathVariable(value = "quiz_id") Long quiz_id, @Valid @RequestBody Question question) {
        Quiz quiz = quizRepository.findById(quiz_id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quiz_id));
        question.setQuizId(quiz_id);
        questionRepository.save(question);
        return quizRepository.save(quiz);
    }

    @GetMapping("/quiz/{id}/questions")
    public List<Question> getQuestionsById(@PathVariable(value = "id") Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    @GetMapping("/quiz/{id}")
    public Quiz getQuizById(@PathVariable(value = "id") Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
    }

    // User methods
    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
