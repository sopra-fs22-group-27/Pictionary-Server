package ch.uzh.ifi.hase.soprafs22.entity;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private int numberOfPlayersRequired;

    @Column(nullable = false)
    private int numberOfPlayers;
    
    @Column(nullable = false)
    private int roundLength;

    @Column(nullable = false)
    private int numberOfRounds;

    @Column(nullable = false)
    private String gameStatus;

    @Column(nullable = false, unique = true)
    private String gameToken;

    @OneToMany
    private List<GameRound> gameRoundList;

    @ElementCollection
    @Column(nullable = false)
    private Map<User, Integer> userToIntegerMap;

    @Column()
    private int currentGameRound;

    public Map<User, Integer> getUserToIntegerMap() {
        return userToIntegerMap;
    }

    public void addUserToIntegerMap(User user) {
        if(this.userToIntegerMap == null){
            this.userToIntegerMap = new HashMap<User, Integer>();
        }
        this.userToIntegerMap.put(user, 0);
    }

    public void updatePointsUserMap(User user, int points) {
       int currentPoints = this.userToIntegerMap.get(user);
       this.userToIntegerMap.put(user, points + currentPoints);
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumberOfPlayersRequired() {
        return numberOfPlayersRequired;
    }

    public void setNumberOfPlayersRequired(int numberOfPlayersRequired) {
        this.numberOfPlayersRequired = numberOfPlayersRequired;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getRoundLength() {
        return roundLength;
    }

    public void setRoundLength(int roundLength) {
        this.roundLength = roundLength;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public List<GameRound> getGameRoundList() {
        return gameRoundList;
    }

    public void setGameRoundList(List<GameRound> gameRoundList) {
        this.gameRoundList = gameRoundList;
    }

    public int getCurrentGameRound() {
        return currentGameRound;
    }

    public void setCurrentGameRound(int currentGameRound) {
        this.currentGameRound = currentGameRound;
    }


}