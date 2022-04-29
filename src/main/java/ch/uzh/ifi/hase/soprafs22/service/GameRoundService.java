package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.repository.GameRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class GameRoundService {
    private final GameRoundRepository gameRoundRepository;
    private UserService userService;

    @Autowired
    public GameRoundService(@Qualifier("gameRoundRepository") GameRoundRepository gameRepository) {
        this.gameRoundRepository = gameRepository;
    }

    public List<GameRound> createGameRounds(int numberOfRounds, String[] playerTokens){
        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(Arrays.asList(playerTokens));
        Collections.shuffle(playerList);

        List<GameRound> gameRoundList = new ArrayList<GameRound>();
        for(int i = 0; i < numberOfRounds; i++){
            GameRound gameRound = new GameRound();
            ArrayList<String> guesserList = playerList;
            int drawIndex = i % playerList.size();
            gameRound.setDrawer(playerList.get(drawIndex));
            String drawer = guesserList.get(drawIndex);
            guesserList.remove(drawIndex);
            guesserList.add(drawIndex,drawer);
            gameRound.setGuessersToken(guesserList.toArray(new String[guesserList.size()]));
            gameRoundList.add(gameRound);
            gameRoundRepository.save(gameRound);
            gameRoundRepository.flush();
        }
        return gameRoundList;
    }
}
