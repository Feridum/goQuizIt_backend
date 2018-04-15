package com.goquizit.model;


public class User {

  private String userId;
  private String login;
  private String passwordHash;
  private java.sql.Date registrationDate;
  private String isLoggedIn;


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }


  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }


  public java.sql.Date getRegistrationDate() {
    return registrationDate;
  }

  public void setRegistrationDate(java.sql.Date registrationDate) {
    this.registrationDate = registrationDate;
  }


  public String getIsLoggedIn() {
    return isLoggedIn;
  }

  public void setIsLoggedIn(String isLoggedIn) {
    this.isLoggedIn = isLoggedIn;
  }

}
