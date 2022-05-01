package ch.uzh.ifi.hase.soprafs22.rest.dto;

public class GameRoundGetDTO {

    private String winner;
    private String drawerToken;
    private long roundStartingTime;
    private String word;


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

    public String getDrawerToken() {
        return drawerToken;
    }

    public void setDrawerToken(String drawerToken) {
        this.drawerToken = drawerToken;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
