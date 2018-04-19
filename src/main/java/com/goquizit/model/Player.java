package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "players")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String playerId;

  private Long quizId;

  private String quizIdQuiz;


  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }


  public Long getQuizId() {
    return quizId;
  }

  public void setQuizId(Long quizId) {
    this.quizId = quizId;
  }

  public String getQuizIdQuiz() {
    return quizIdQuiz;
  }

  public void setQuizIdQuiz(String quizIdQuiz) {
    this.quizIdQuiz = quizIdQuiz;
  }

}
