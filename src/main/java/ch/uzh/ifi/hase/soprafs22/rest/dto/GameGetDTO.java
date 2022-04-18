package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.List;

import ch.uzh.ifi.hase.soprafs22.entity.User;

public class GameGetDTO {
    private String gameName;
    private int numberOfPlayersRequired;
    private int numberOfPlayers;
    private int roundLength;
    private int numberOfRounds;
    private String gameStatus;
    private String gameToken;
    private List<User> players;
    
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
    public List<User> getPlayers() {
        return players;
    }
    public void setPlayers(List<User> players) {
        this.players = players;
    }

}
