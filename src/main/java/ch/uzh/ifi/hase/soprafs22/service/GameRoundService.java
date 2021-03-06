package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.GameRoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class GameRoundService {
    private final GameRoundRepository gameRoundRepository;
    private final GameRepository gameRepository;
    private final UserService userService;

    @Autowired
    public GameRoundService(@Qualifier("gameRoundRepository") GameRoundRepository gameRoundRepository, UserService userService, @Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRoundRepository = gameRoundRepository;
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public List<GameRound> createGameRounds(int numberOfRounds, String[] playerTokens){
        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(Arrays.asList(playerTokens));
        Collections.shuffle(playerList);

        List<GameRound> gameRoundList = new ArrayList<GameRound>();
        for(int i = 0; i <= numberOfRounds; i++){
            GameRound gameRound = new GameRound();
            ArrayList<String> guesserList = playerList;
            int drawIndex = i % playerList.size();
            gameRound.setDrawer(playerList.get(drawIndex));
            String drawer = guesserList.get(drawIndex);
            guesserList.remove(drawIndex);
            guesserList.add(drawIndex,drawer);
            gameRound.setGuessersToken(guesserList.toArray(new String[guesserList.size()]));
            gameRound.setDrawerGotPoints(false);
            gameRoundList.add(gameRound);

            //setting up gameRound HashTable to keep track of guesses
            for(String token : guesserList){
                User user = userService.getUserByToken(token);
                gameRound.addUserToAlreadyGuessedMap(user);
            }
            gameRoundRepository.save(gameRound);
            gameRoundRepository.flush();
        }
        return gameRoundList;
    }

    public GameRound getGameRound(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        int currentRound = game.getCurrentGameRound();
        GameRound gameRound = game.getGameRoundList().get(currentRound);
        return gameRound;
    }

    public void updateCorrectGuess(String gameToken, String userToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        int currentRound = game.getCurrentGameRound();
        GameRound gameRound = game.getGameRoundList().get(currentRound);
        User user = userService.getUserByToken(userToken);
        gameRound.updateUserToAlreadyGuessedMap(user, true);
    }

    public Boolean checkIfUserAlreadyGuessed(GameRound gameRound, User user){
        return gameRound.getUserGuessedInfo(user);
    }
}
