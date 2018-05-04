package com.goquizit.DTO;

import com.goquizit.model.Question;
import com.goquizit.model.QuizState;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class QuizOutputDTO {

    private UUID id;

    private String token;

    private String title;

    private QuizState state;

    private boolean isKahoot;

    private Date startDate;

    private Date endDate;

    private String ownerId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public QuizState getState() {
        return state;
    }

    public void setState(QuizState state) {
        this.state = state;
    }

    public boolean isKahoot() {
        return isKahoot;
    }

    public void setKahoot(boolean kahoot) {
        isKahoot = kahoot;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
