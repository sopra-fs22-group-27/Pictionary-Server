package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.repository.GameRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameRoundService {
    private final GameRoundRepository gameRoundRepository;
    private UserService userService;
    private GameRoundService gameRoundService;

    @Autowired
    public GameRoundService(@Qualifier("gameRepository") GameRoundRepository gameRepository , GameService gameService) {
        this.gameRoundRepository = gameRepository;
    }

    public List<GameRound> createGameRounds(int numberOfRounds, String[] playerTokens){
        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(Arrays.asList(playerTokens));
        Collections.shuffle(playerList);

        int drawerCount = numberOfRounds / playerList.size();

        List<GameRound> gameRoundList = new ArrayList<GameRound>();
        for(int i = 0; i <= numberOfRounds; i++){
            GameRound gameRound = new GameRound();
            ArrayList<String> guesserList = playerList;
            if(i > playerList.size() - 1){
                int drawIndex = i - drawerCount * playerList.size() - 1;
                gameRound.setDrawer(playerList.get(drawIndex));
                guesserList.remove(drawIndex);
            }
            else{
                gameRound.setDrawer(playerList.get(i));
                gameRound.setDrawer(playerList.get(i));
                guesserList.remove(i);
            }
            gameRound.setGuessersToken(guesserList.toArray(new String[guesserList.size()]));
            gameRoundList.add(gameRound);
        }
        return gameRoundList;
    }
}
