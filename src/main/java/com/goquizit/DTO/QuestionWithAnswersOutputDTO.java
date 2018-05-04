package com.goquizit.DTO;

import com.goquizit.model.Answer;
import com.goquizit.model.Question;

import java.util.List;

public class QuestionWithAnswersOutputDTO {
    private QuestionOutputDTO questionOutputDTO;
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
