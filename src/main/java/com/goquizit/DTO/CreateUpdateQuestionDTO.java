package com.goquizit.DTO;

import com.goquizit.model.QuestionState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateUpdateQuestionDTO {
    @NotNull
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionState type;

    private int duration;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public QuestionState getType() {
        return type;
    }

    public void setType(QuestionState type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
