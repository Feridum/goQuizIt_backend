package com.goquizit.services;

import com.goquizit.DTO.CreateUpdateAnswersDTO;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.exception.ResourceNotFoundException;
import com.goquizit.exception.UnknownRepositoryException;
import com.goquizit.model.Answer;
import com.goquizit.model.Question;
import com.goquizit.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionService questionService;

    public List<AnswerOutputDTO> createAnswers(@Valid List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS, UUID questionId) {
        try {
            List<Answer> answers = mapDtoToAnswers(createUpdateAnswersDTOS);
            Question question = questionService.getOne(questionId);
            question.setAnswers(answers);
            return mapAnswersToOutput(questionService.save(question).getAnswers(), questionId);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }

    public AnswerOutputDTO getAnswerById(UUID answerId) throws ResourceNotFoundException {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
        return mapAnswerToOutput(answer);
    }

    public ResponseEntity deleteAnswerById(UUID answerId) throws ResourceNotFoundException {
        Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new ResourceNotFoundException("Answer", "id", answerId));
        answerRepository.delete(answer);
        return ResponseEntity.ok().build();
    }

    public List<Answer> mapDtoToAnswers(List<CreateUpdateAnswersDTO> createUpdateAnswersDTOS) {
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < createUpdateAnswersDTOS.size(); ++i) {
            Answer newAnswer = new Answer();
            newAnswer.setValue(createUpdateAnswersDTOS.get(i).getValue());
            newAnswer.setIsPositive(createUpdateAnswersDTOS.get(i).getIsPositive());
            answers.add(newAnswer);
        }
        return answers;
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    public List<AnswerOutputDTO> mapAnswersToOutput(List<Answer> answers, UUID questionId) {
        List<AnswerOutputDTO> outputDTOs = new ArrayList<>();
        answers.forEach(answer ->
        {
            AnswerOutputDTO outputDTO = new AnswerOutputDTO();
            outputDTO.setAnswerId(answer.getAnswerId());
            outputDTO.setPositive(answer.getIsPositive());
            outputDTO.setValue(answer.getValue());
            outputDTO.setQuestionId(questionId);
            outputDTOs.add(outputDTO);
        });
        return outputDTOs;
    }

    public List<AnswerOutputDTO> mapAnswersToOutput(List<Answer> answers) {
        List<AnswerOutputDTO> outputDTOs = new ArrayList<>();
        answers.forEach(answer ->
        {
            AnswerOutputDTO outputDTO = new AnswerOutputDTO();
            outputDTO.setAnswerId(answer.getAnswerId());
            outputDTO.setPositive(answer.getIsPositive());
            outputDTO.setValue(answer.getValue());
            outputDTO.setQuestionId(answer.getQuestionId());
            outputDTOs.add(outputDTO);
        });
        return outputDTOs;
    }

    public List<AnswerToPlayerOutputDTO> mapAnswersToPlayerToOutput(List<Answer> answers) {
        List<AnswerToPlayerOutputDTO> outputDTOs = new ArrayList<>();
        answers.forEach(answer ->
        {
            AnswerToPlayerOutputDTO outputDTO = new AnswerToPlayerOutputDTO();
            outputDTO.setAnswerId(answer.getAnswerId());
            outputDTO.setValue(answer.getValue());
            outputDTO.setQuestionId(answer.getQuestionId());
            outputDTOs.add(outputDTO);
        });
        return outputDTOs;
    }


    private AnswerOutputDTO mapAnswerToOutput(Answer answer) {
        AnswerOutputDTO outputDTO = new AnswerOutputDTO();
        outputDTO.setAnswerId(answer.getAnswerId());
        outputDTO.setPositive(answer.getIsPositive());
        outputDTO.setValue(answer.getValue());
        outputDTO.setQuestionId(answer.getQuestionId());
        return outputDTO;
    }

    public boolean checkAnswerIdInQuestion(UUID answerId, UUID questionId) {
        if (answerRepository.checkAnswerIdInQuestion(answerId, questionId) == 0)
            return false;
        else
            return true;
    }

    public List<UUID> getCorrectAnswers(UUID questionId) {
        List<Answer> answers = answerRepository.getCorrectAnswers(questionId);
        List<UUID> answersUUID = new ArrayList<>();
        answers.forEach(answer -> answersUUID.add(answer.getAnswerId()));
        return answersUUID;
    }

    public Answer getOne(UUID playerAnswerId) {
        try {
            return answerRepository.getOne(playerAnswerId);
        } catch (PersistenceException e) {
            throw new UnknownRepositoryException(e.getMessage());
        }
    }


}