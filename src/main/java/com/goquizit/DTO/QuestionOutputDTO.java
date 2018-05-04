package com.goquizit.DTO;

import com.goquizit.model.QuestionState;

import java.util.UUID;

public class QuestionOutputDTO {

    private UUID questionId;

    private String value;

    private QuestionState type;

    private int duration;

    private UUID quizId;

    public UUID getQuestionId() {
        return questionId;
    }

    public void setQuestionId(UUID questionId) {
        this.questionId = questionId;
    }

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

    public UUID getQuizId() {
        return quizId;
    }

    public void setQuizId(UUID quizId) {
        this.quizId = quizId;
    }
}
