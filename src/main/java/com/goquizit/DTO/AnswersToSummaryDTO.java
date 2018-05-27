package com.goquizit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AnswersToSummaryDTO {

    @JsonProperty("question")
    private String Question;

    @JsonProperty("playerAnswers")
    private List<String> playerAnswers;

    public AnswersToSummaryDTO(String question, List<String> playerAnswers) {
        Question = question;
        this.playerAnswers = playerAnswers;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List<String> getPlayerAnswers() {
        return playerAnswers;
    }

    public void setPlayerAnswers(List<String> playerAnswers) {
        this.playerAnswers = playerAnswers;
    }
}
