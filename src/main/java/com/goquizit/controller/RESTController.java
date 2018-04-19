package com.goquizit.controller;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Answer;
import com.goquizit.model.Note;
import com.goquizit.model.Question;
import com.goquizit.model.Quiz;
import com.goquizit.repository.AnswerRepository;
import com.goquizit.repository.NoteRepository;
import com.goquizit.repository.QuestionRepository;
import com.goquizit.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by rajeevkumarsingh on 27/06/17.
 */
@RestController
@RequestMapping("/api")
public class RESTController {

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuizRepository quizRepository;


    @GetMapping("")
    public String retu() {
        return "Hej";
    }


    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    @PostMapping("/notes")
    public Note createNote(@Valid @RequestBody Note note) {
        return noteRepository.save(note);
    }

    @GetMapping("/notes/{id}")
    public Note getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
    }

    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question) {
        return questionRepository.save(question);
    }

    @PostMapping("/answers")
    public Answer createAnswer(@Valid @RequestBody Answer answer) {
        return answerRepository.save(answer);
    }

    @PostMapping("/quiz")
    public Quiz createquiz(@Valid @RequestBody Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @PostMapping("/quiz/{quiz_id}/questions/")
    public Quiz createquiz(@PathVariable(value = "quiz_id") Long quiz_id, @Valid @RequestBody Question question) {
        Quiz oldQuiz = quizRepository.findById(quiz_id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quiz_id));
        question.setQuizId(quiz_id);
        questionRepository.save(question);
        return quizRepository.save(oldQuiz);
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

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/quiz")
    public List<Quiz> getAllQuizes() {
        return quizRepository.findAll();
    }


//
//    @DeleteMapping("/notes/{id}")
//    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
//        Note note = noteRepository.findById(noteId)
//                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
//
//        noteRepository.delete(note);
//
//        return ResponseEntity.ok().build();
//    }
}
