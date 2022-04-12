package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

public class UserPostDTO {

  private String password;

  private String username;

  private String email;

  private String creation_date;

  private int ranking_points;

  // private Boolean logged_in;


  public String getPassword() {
    return password;
  }

  public void setPassword(String name) {
    this.password = name;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {return email;}

  public void setEmail(String email) {this.email = email;}

  public String getCreation_date(){return creation_date;}

  public void setCreation_date(String creation_date){this.creation_date = creation_date;}

  public int getRanking_points() {
      return ranking_points;
  }

  public void setRanking_points(int ranking_points) {
      this.ranking_points = ranking_points;
  }



    // public Boolean getLogged_in() {return logged_in;}

  // public void setLogged_in(Boolean logged_in) {this.logged_in = logged_in;}

//  public UserStatus get
}
