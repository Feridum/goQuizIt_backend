package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.goquizit.exception.ResponseException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "player")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Player {

    @Id
    @Type(type = "pg-uuid")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "uuid")
    private UUID playerId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "player")
    private List<PlayerAnswer> playerAnswers;

    private String name;

    private String surname;

    private String telephoneNumber;

    private String mail;

    @ManyToOne
    private Quiz quiz;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public List<PlayerAnswer> getPlayerAnswers() {
        return playerAnswers;
    }

    public void setPlayerAnswers(List<PlayerAnswer> playerAnswers) {
        this.playerAnswers = playerAnswers;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public PlayerAnswer getLastPlayerAnswer() {
        int index = playerAnswers.size();
        if (index == 0)
            throw new ResponseException("Can not get Answer");
        else return playerAnswers.get(index-1);
    }
}
