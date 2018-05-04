package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "QUIZ")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Quiz {

    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String token;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz")
    private List<Question> questionSet;

    private String title;

    @Enumerated(EnumType.STRING)
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

    public boolean getIsKahoot() {
        return isKahoot;
    }

    public void setIsKahoot(boolean isKahoot) {
        this.isKahoot = isKahoot;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Question> getQuestions() {
        return questionSet;
    }

    public void setQuestionSet(List<Question> questionSet) {
        this.questionSet = questionSet;
    }

    public Question getLastQuestion()
    {
       return questionSet.get(questionSet.size()-1);
    }
}
