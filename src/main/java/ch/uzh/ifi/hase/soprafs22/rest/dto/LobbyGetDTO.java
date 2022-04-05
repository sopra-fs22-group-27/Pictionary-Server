package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;

import java.util.ArrayList;

public class LobbyGetDTO {

    private String token;
    private String lobbyName;
    private Boolean isPublic;
    private Boolean isInGame;
    private User host;
    private ArrayList<User> lobbyUserList;
    private int gameLength;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsInGame() {
        return isInGame;
    }

    public void setIsInGame(Boolean isInGame) {
        this.isInGame = isInGame;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public ArrayList<User> getLobbyUserList() {
        return lobbyUserList;
    }

    public void setLobbyUserList(ArrayList<User> lobbyUserList) {
        this.lobbyUserList = lobbyUserList;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }

}
