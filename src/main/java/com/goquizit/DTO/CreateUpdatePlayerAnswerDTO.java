package com.goquizit.DTO;

import com.goquizit.model.Answer;

import java.util.List;

public class CreateUpdatePlayerAnswerDTO {

    List<PlayerAnswerDTO> answersGivenByUser;

    public List<PlayerAnswerDTO> getAnswersGivenByUser() {
        return answersGivenByUser;
    }

    public void setAnswersGivenByUser(List<PlayerAnswerDTO> answersGivenByUser) {
        this.answersGivenByUser = answersGivenByUser;
    }
}
