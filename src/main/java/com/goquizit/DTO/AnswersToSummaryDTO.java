package com.goquizit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;

import java.util.List;

public class AnswersToSummaryDTO {

    @JsonProperty("question")
    private String Question;

    @JsonProperty("playerAnswers")
    private List<String> playerAnswers;

    @JsonProperty("positiveAnswers")
    private List<String> positiveAnswers;

    public AnswersToSummaryDTO(String question, List<String> playerAnswers, List<String> positiveAnswers) {
        Question = question;
        this.playerAnswers = playerAnswers;
        this.positiveAnswers = positiveAnswers;
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

    public List<String> getPositiveAnswers() {
        return positiveAnswers;
    }

    public void setPositiveAnswers(List<String> positiveAnswers) {
        this.positiveAnswers = positiveAnswers;
    }
}
