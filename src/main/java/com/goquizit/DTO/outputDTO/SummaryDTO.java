package com.goquizit.DTO.outputDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.goquizit.DTO.AnswersToSummaryDTO;

import java.util.List;

@JsonRootName("players")
public class SummaryDTO {
    @JsonProperty("player")
    private PlayerOutputDTO playerOutputDTO;

    @JsonProperty("result")
    private String point;

    @JsonProperty("quiz")
    private String quiz;

    @JsonProperty("answers")
    private List<AnswersToSummaryDTO> answers;

    public SummaryDTO(PlayerOutputDTO playerOutputDTO, String quiz, List<AnswersToSummaryDTO> answers, int maxPoints, int playerPoint) {
        this.playerOutputDTO = playerOutputDTO;
        this.setPoint(maxPoints, playerPoint);
        this.quiz = quiz;
        this.answers = answers;
    }

    public PlayerOutputDTO getPlayerOutputDTO() {
        return playerOutputDTO;
    }

    public void setPlayerOutputDTO(PlayerOutputDTO playerOutputDTO) {
        this.playerOutputDTO = playerOutputDTO;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(int maxPoint, int playerPoint) {
        this.point = playerPoint + "/" + maxPoint + "points";
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public List<AnswersToSummaryDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswersToSummaryDTO> answers) {
        this.answers = answers;
    }
}
