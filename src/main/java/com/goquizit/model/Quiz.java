package com.goquizit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.IdGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "QUIZ")
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createdAt", "updatedAt"})
public class Quiz{

  @Id
  @Type(type = "pg-uuid")
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(columnDefinition = "uuid")
  private UUID id;

  @NotNull
  private String token;

  @NotNull
  private String title;

  @NotNull
  private String state;

  private String startOnTime;

  @NotNull
  private String isKahoot;

  private Date endDate;

  //@NotNull
  private Date startDate;

  //@NotNull
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


  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }


  public String getStartOnTime() {
    return startOnTime;
  }

  public void setStartOnTime(String startOnTime) {
    this.startOnTime = startOnTime;
  }


  public String getIsKahoot() {
    return isKahoot;
  }

  public void setIsKahoot(String isKahoot) {
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

}
