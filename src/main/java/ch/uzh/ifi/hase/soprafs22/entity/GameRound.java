package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "GAMEROUND")
public class GameRound {

    @Id
    @GeneratedValue
    private Long id;

    @Column()
    @Lob
    private String img;

    @Column()
    private String word;

    @Column(nullable = false)
    private long roundStartingTime;

    @Column(nullable = false)
    private String drawerToken;

    @Column(nullable = false)
    private String[] guessersToken;

    @Column()
    private String gameToken;

    @ElementCollection
    private Map<User, Boolean> userToAlreadyGuessedMap;

    public void addUserToAlreadyGuessedMap(User user) {
        if(this.userToAlreadyGuessedMap == null){
            this.userToAlreadyGuessedMap = new HashMap<User, Boolean>();
        }
        this.userToAlreadyGuessedMap.put(user, false);
    }

    public void updateUserToAlreadyGuessedMap(User user, boolean bool) {
        this.userToAlreadyGuessedMap.put(user, bool);
    }

    public Boolean getUserGuessedInfo(User user){
        return this.userToAlreadyGuessedMap.get(user);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getRoundStartingTime() {
        return roundStartingTime;
    }

    public void setRoundStartingTime(long roundStartingTime) {
        this.roundStartingTime = roundStartingTime;
    }

    public String[] getGuessersToken() {
        return guessersToken;
    }

    public void setGuessersToken(String[] guessersToken) {
        this.guessersToken = guessersToken;
    }

    public String getDrawer() {
        return drawerToken;
    }

    public void setDrawer(String drawerToken) {
        this.drawerToken = drawerToken;
    }
}
