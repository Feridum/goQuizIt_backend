package com.goquizit.DTO.outputDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishedQuiz {
    private boolean isFinish;

    @JsonProperty("isFinish")
    public boolean isFinish() {
        return isFinish;
    }

    public FinishedQuiz() {
        this.isFinish = true;
    }
}
