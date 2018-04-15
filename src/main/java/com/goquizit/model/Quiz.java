package com.goquizit.model;


public class Quiz {

  private String quizId;
  private String token;
  private String title;
  private String state;
  private String startOnTime;
  private String isKahoot;
  private java.sql.Date endDate;
  private java.sql.Date startDate;
  private String ownerId;
  private String userIdUser;


  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
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


  public java.sql.Date getEndDate() {
    return endDate;
  }

  public void setEndDate(java.sql.Date endDate) {
    this.endDate = endDate;
  }


  public java.sql.Date getStartDate() {
    return startDate;
  }

  public void setStartDate(java.sql.Date startDate) {
    this.startDate = startDate;
  }


  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }


  public String getUserIdUser() {
    return userIdUser;
  }

  public void setUserIdUser(String userIdUser) {
    this.userIdUser = userIdUser;
  }

}
