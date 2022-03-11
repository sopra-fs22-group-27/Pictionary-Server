package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.Column;

public class UserGetDTO {

  private Long id;
//  private String password;
  private String username;
  private String token;
  private UserStatus status;
  private String birthday;
  private String creation_date;
  private Boolean logged_in;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

//  public String getPassword() {
//    return password;
//  }

//  public void setPassword(String name) {
//    this.password = name;
//  }
//
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public String getBirthday() {return birthday;}

  public void setBirthday(String birthday) {this.birthday = birthday;}

  public String getCreation_date(){return creation_date;}

  public void setCreation_date(String creation_date){this.creation_date = creation_date;}

  public Boolean getLogged_in(){return logged_in;}

  public void setLogged_in(Boolean logged_in){this.logged_in = logged_in;}

  public String getToken(){return token;}

  public void setToken(String token){this.token = token;}

}
