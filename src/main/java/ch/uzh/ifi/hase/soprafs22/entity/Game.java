package ch.uzh.ifi.hase.soprafs22.entity;
import javax.persistence.*;

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

    @Column(nullable = false)
    private String[] playerTokens;
    
    @Column()
    @Lob
    private String img;


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

    public String[] getPlayerTokens() {
        return playerTokens;
    }

    public void setPlayerTokens(String[] playerTokens) {
        this.playerTokens = playerTokens;
    }
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}