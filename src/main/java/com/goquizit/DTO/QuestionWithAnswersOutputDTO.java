package com.goquizit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.goquizit.DTO.outputDTO.AnswerOutputDTO;
import com.goquizit.DTO.outputDTO.QuestionOutputDTO;

import java.io.Serializable;
import java.util.List;

public class QuestionWithAnswersOutputDTO implements Serializable {
    @JsonProperty("question")
    private QuestionOutputDTO questionOutputDTO;

    @JsonProperty("answers")
    private List<AnswerOutputDTO> answerOutputDTOS;

    public QuestionWithAnswersOutputDTO(QuestionOutputDTO questionOutputDTO, List<AnswerOutputDTO> answerOutputDTOS) {
        this.questionOutputDTO = questionOutputDTO;
        this.answerOutputDTOS = answerOutputDTOS;
    }

    public QuestionOutputDTO getQuestionOutputDTO() {
        return questionOutputDTO;
    }

    public void setQuestionOutputDTO(QuestionOutputDTO questionOutputDTO) {
        this.questionOutputDTO = questionOutputDTO;
    }

    public List<AnswerOutputDTO> getAnswerOutputDTOS() {
        return answerOutputDTOS;
    }

    public void setAnswerOutputDTOS(List<AnswerOutputDTO> answerOutputDTOS) {
        this.answerOutputDTOS = answerOutputDTOS;
    }
}
