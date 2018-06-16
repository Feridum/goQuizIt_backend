package com.goquizit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.outputDTO.AnswerToPlayerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class QuestionWithAnswersAndPlayerIdDTO implements Serializable {

    @JsonProperty("Player")
    private UUID playerId;

    @JsonProperty("Question")
    private QuestionOutputDTO questionDTO;

    @JsonProperty("Answers")
    private List<AnswerToPlayerOutputDTO> answers;

    @JsonProperty("Index")
    private int index;

    @JsonProperty("NumberOfQuestions")
    private int numberOfQuestions;


    public QuestionWithAnswersAndPlayerIdDTO(UUID playerId, QuestionOutputDTO questionDTO, List<AnswerToPlayerOutputDTO> answers, int index, int numberOfQuestions) {
        this.playerId = playerId;
        this.questionDTO = questionDTO;
        this.answers = answers;
        this.index = index;
        this.numberOfQuestions = numberOfQuestions;
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

    public List<AnswerToPlayerOutputDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerToPlayerOutputDTO> answers) {
        this.answers = answers;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        numberOfQuestions = numberOfQuestions;
    }
}
