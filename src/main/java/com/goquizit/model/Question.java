package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "QUESTION")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Question {

  @Id
  @Type(type = "pg-uuid")
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "uuid")
  private UUID questionId;

  private UUID quizId;

  @NotNull
  private String value;

  @NotNull
  @Enumerated(EnumType.STRING)
  private QuestionState type;

  @NotNull
  @Min(1)
  private int duration;


  public UUID getQuizId() {
    return quizId;
  }

  public void setQuizId(UUID quizId) {
    this.quizId = quizId;
  }


  public String getValue() {
    return value;
  }

  public UUID getQuestionId() {
    return questionId;
  }


  public void setQuestionId(UUID questionId) {
    this.questionId = questionId;
  }

  public void setValue(String value) {
    this.value = value;
  }


  public QuestionState getType() {
    return type;
  }

  public void setType(QuestionState type) {
    this.type = type;
  }


  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }
}
