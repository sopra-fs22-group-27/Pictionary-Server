package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = false, unique = true)
    private String email;

    @Column()
    @JsonFormat(pattern = "dd/MM/yyyy")
    private String creation_date;

    @Column(nullable = false)
    private int ranking_points;

    @Column(nullable = false)
    private Boolean isInLobby = false;
    
    @Column(nullable = false)
    private Boolean isInGame = false;

    @Column()
    private Date lastActiveTime =  null;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email != null){
            if(validate(email)){
                this.email = email;
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is not a valid email.");
            }
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public int getRanking_points() {
        return ranking_points;
    }

    public void setRanking_points(int ranking_points) {
        this.ranking_points = ranking_points;
    }

    public Boolean getIsInLobby() {
        return isInLobby;
    }
    public void setIsInLobby(Boolean isInLobby) {
        this.isInLobby = isInLobby;
    }

    public Boolean getisInGame() {
        return isInGame;
    }
    public void setisInGame(Boolean isInGame) {
        this.isInGame = isInGame;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
}

//User:
//        id<long>,
//        username<string>,
//        creation_date<Date>,
//        logged_in<boolean>,
//        birthday<Date>