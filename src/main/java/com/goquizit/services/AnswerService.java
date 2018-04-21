package com.goquizit.services;

import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Answer;
import com.goquizit.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    public Answer createAnswer(@Valid Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Answer getAnswerById(@PathVariable(value = "answer_id") UUID answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
    }

    public ResponseEntity<?> deleteAnswerById(@PathVariable(value = "answer_id") UUID answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
        answerRepository.delete(answer);
        return ResponseEntity.ok().build();
    }


}