package com.goquizit.DTO;

import com.goquizit.model.QuizState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CreateUpdateQuizDTO {
    @NotNull
    private String title;

    @NotNull
    private boolean isKahoot;

    @NotNull
    private String ownerId;

    private Date startDate;

    private Date endDate;

    @Enumerated(EnumType.STRING)
    private QuizState state;

    public QuizState getState() {
        return state;
    }

    public void setState(QuizState state) {
        this.state = state;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsKahoot() {
        return isKahoot;
    }

    public void setIsKahoot(boolean isKahoot) {
        this.isKahoot = isKahoot;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
