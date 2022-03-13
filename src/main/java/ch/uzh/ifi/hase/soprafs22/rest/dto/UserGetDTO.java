package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.Column;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserGetDTO {

  private Long id;
//  private String password;
  private String username;
  private String token;
  private UserStatus status;
  private Date birthday;
  private Date creation_date;
//  private String creation_date;
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
    // get Date birthday
  public Date getBirthday() {return birthday;}
    // set Date birthday
  public void setBirthday(String birthday) {
      try {
          Date convertedCurrentDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);

          this.birthday = convertedCurrentDate;
      }
      catch (Exception e) {
          this.birthday = new Date(0);
      }

  }
    // get Date Creation_date
  public Date getCreation_date(){return creation_date;}
    // set Date Creation_date
  public void setCreation_date(String creation_date){

      try {
          Date convertedCurrentDate = new SimpleDateFormat("dd/MM/yyyy").parse(creation_date);

          this.creation_date = convertedCurrentDate;
      }
      catch (Exception e) {
          this.creation_date = new Date(100);
      }
//      String date = sdf.format(convertedCurrentDate);

  }

//  public String getCreation_date(){return creation_date;}
//    public void setCreation_date(String creation_date){this.creation_date = creation_date;}

  public Boolean getLogged_in(){return logged_in;}

  public void setLogged_in(Boolean logged_in){this.logged_in = logged_in;}

  public String getToken(){return token;}

  public void setToken(String token){this.token = token;}

}
