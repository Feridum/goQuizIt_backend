package com.goquizit.services;

import com.goquizit.DTO.CreateUpdateAnswersDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.model.Answer;
import com.goquizit.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    public Answer createAnswer(@Valid Answer answer) {
        return answerRepository.save(answer);
    }

    public List<Answer> createAnswers(@Valid List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS, UUID questionId)
    {
        List<Answer> answers = new ArrayList<>();
        for(int i=0; i<createUpdateAnswersDTOS.size();++i)
        {
            Answer newAnswer = new Answer();
            newAnswer.setValue(createUpdateAnswersDTOS.get(i).getValue());
            newAnswer.setIsPositive(createUpdateAnswersDTOS.get(i).isPositive());
            newAnswer.setQuestionId(questionId);
            answerRepository.save(newAnswer);
            answers.add(newAnswer);
        }
        return answers;
    }

    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public List<Answer> findByQuestionId(UUID questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public Answer getAnswerById(UUID answerId) {
        return answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
    }

    public ResponseEntity<?> updateAnswerById(UUID answerId, @Valid CreateUpdateAnswersDTO answer)
    {
        Answer answerToUpdate = answerRepository.getOne(answerId);
        answerToUpdate.setValue(answer.getValue());
        answerToUpdate.setIsPositive(answer.isPositive());
        answerRepository.save(answerToUpdate);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<?> deleteAnswerById(UUID answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
        answerRepository.delete(answer);
        return ResponseEntity.ok().build();
    }
}