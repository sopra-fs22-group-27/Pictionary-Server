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
    private String token;

    @Column(nullable = false, unique = true)
    private String lobbyName;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isInGame = false;

    @OneToOne
    private User host;

    @OneToMany
    private List<User> lobbyUserList;

    @Column
    private int gameLength;

    

    public Long getId() {
        return id;
    }

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

    public List<User> getLobbyUserList() {
        List<User> lobbyListCopy = new ArrayList<User>();
        if (lobbyUserList != null) {
            lobbyListCopy.addAll(lobbyUserList);
        }
        return lobbyListCopy;
    }

    public void setLobbyUserList(List<User> lobbyUserList) {
        this.lobbyUserList = lobbyUserList;
    }

    /**
     * Adds User to a Lobby User List
     * @param user
     */
    public void addUserToLobbyUserList(User user) {
        List<User> lobbyUserListCopy = this.lobbyUserList;
        lobbyUserListCopy.add(user);
        this.lobbyUserList = lobbyUserListCopy;
    }

    public int getGameLength() {
        return gameLength;
    }

    public void setGameLength(int gameLength) {
        this.gameLength = gameLength;
    }
}
