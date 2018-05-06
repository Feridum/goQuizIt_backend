package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "playersanswers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class PlayerAnswer {

  @Id
  @Type(type = "pg-uuid")
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "uuid")
  private UUID id;

//  @NotNull
 // private List<Answer> answers;

  @NotNull
  private String value;


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

//  public List<Answer> getAnswers() {
//    return answers;
//  }

//  public void setAnswers(List<Answer> answers) {
//    this.answers = answers;
//  }
}
