package com.goquizit.DTO.outputDTO;

import java.util.UUID;

public class AnswerToPlayerOutputDTO {

    private UUID answerId;

    private String value;

    private UUID questionId;

    public UUID getAnswerId() {
        return answerId;
    }

    public void setAnswerId(UUID answerId) {
        this.answerId = answerId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }
}
