package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;

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
    @Column(nullable = false, unique = true)
    private User host;

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

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
