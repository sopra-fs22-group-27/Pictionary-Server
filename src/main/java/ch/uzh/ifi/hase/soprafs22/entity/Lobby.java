package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOBBY")
public class Lobby {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String lobbyName;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isInGame;

    @OneToOne
    private User host;

    @OneToMany
    private List<User> lobbyUserList;

    @Column
    private int gameLength;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<User> getLobbyUserList() {
        return lobbyUserList;
    }

    public void setLobbyUserList(List<User> lobbyUserList) {
        this.lobbyUserList = lobbyUserList;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
