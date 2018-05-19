package com.goquizit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;

import java.io.Serializable;
import java.util.UUID;

public class QuestionWithPlayerIdDTO implements Serializable {

    @JsonProperty("Player")
    private UUID playerId;

    @JsonProperty("Question")
    private QuestionOutputDTO questionDTO;

    public QuestionWithPlayerIdDTO(UUID playerId, QuestionOutputDTO questionDTO) {
        this.playerId = playerId;
        this.questionDTO = questionDTO;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public QuestionOutputDTO getQuestionDTO() {
        return questionDTO;
    }

    public void setQuestionDTO(QuestionOutputDTO questionDTO) {
        this.questionDTO = questionDTO;
    }
}
