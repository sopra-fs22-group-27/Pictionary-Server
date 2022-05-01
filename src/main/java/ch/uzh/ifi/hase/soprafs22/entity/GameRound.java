package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

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
    private String winner;

    @Column()
    private String gameToken;

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

    public String getWinner() {
        return winner;
    }

    public void setWinner(String userToken) {
        this.winner = userToken;
    }

    public String getDrawer() {
        return drawerToken;
    }

    public void setDrawer(String drawerToken) {
        this.drawerToken = drawerToken;
    }
}
