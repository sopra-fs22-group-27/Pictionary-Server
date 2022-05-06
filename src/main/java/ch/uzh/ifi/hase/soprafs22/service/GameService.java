package ch.uzh.ifi.hase.soprafs22.service;
import java.util.*;

import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private UserService userService;
    private GameRoundService gameRoundService;
    private final UserRepository userRepository;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository , UserService userService, GameRoundService gameRoundService, @Qualifier("userRepository") UserRepository userRepository ) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.gameRoundService = gameRoundService;
        this.userRepository = userRepository;
    }
    
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game getGameById(String id) {
        return this.gameRepository.findById(id);
    }    

    public void deleteGameByToken(String token) {
        this.gameRepository.deleteByGameToken(token);
    }

    public Game getGameByToken(String token) {
        Game game = gameRepository.findByGameToken(token);
        if (game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        return game;
    }
    
    public Game createGame (Game newGame) {
        newGame.setGameToken(UUID.randomUUID().toString());
        newGame = gameRepository.save(newGame);
        gameRepository.flush();
        return newGame;
    }

    public Game addPlayerToGame (String gameToken, String userToken) {
        Game game = this.gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        User user = userService.getUserByToken(userToken);
        String[] currentPlayers = game.getPlayerTokens();

        if (game.getGameStatus().equals("started") || game.getGameStatus().equals("finished")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The game has already started or is finished");
        } else if (Arrays.stream(currentPlayers).anyMatch(userToken::equals)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The user is already in the game");
        } else if (currentPlayers.length == game.getNumberOfPlayersRequired()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The game is already full");
        } else if (game.getGameName() == null || user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game or user does not exist");
        } else {
            //add userToken to currentPlayers
            String[] newPlayers = Arrays.copyOf(currentPlayers, currentPlayers.length + 1);
            newPlayers[currentPlayers.length] = userToken;
            game.setPlayerTokens(newPlayers);
            game.setNumberOfPlayers(game.getNumberOfPlayers() + 1);
            gameRepository.flush();
            return game;
        }
    }

    public void updateImg(String gameToken, String img){
        Game game = gameRepository.findByGameToken(gameToken);
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        currentGameRound.setImg(img);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public String getImage(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        String img = currentGameRound.getImg();
        return img;
    }

    public void changeWord(String gameToken, String word){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        //game.setCurrentGameRound(game.getCurrentGameRound() + 1); //next round
        currentGameRound.setWord(word);
        //Getting the current date
        Date date = new Date();
        //This method returns the time in millis
        long startTime = date.getTime();
        currentGameRound.setRoundStartingTime(startTime);
    }

    public Boolean getResultOfGuess(String gameToken, String userToken, String guessedWord){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound() - 1);
        if(Objects.equals(currentGameRound.getWord(), guessedWord)){
            if(currentGameRound.getWinner()==null){ //Only the first one get the points
                currentGameRound.setWinner(userToken);
                User winner = userRepository.findByToken(userToken);
                int newRanking_points = winner.getRanking_points()+10;
                winner.setRanking_points(newRanking_points);
                userRepository.save(winner);
                userRepository.flush();
            }
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isGameFull(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        if(game.getNumberOfPlayers() == game.getNumberOfPlayersRequired()){
            if (game.getGameRoundList().isEmpty()){
                game.setGameRoundList(gameRoundService.createGameRounds(game.getNumberOfRounds(), game.getPlayerTokens()));
                game.setCurrentGameRound(0);
            }
            return true;
        }
        else{
            return false;
        }
    }

    public void changeGameRound(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        if(game.getCurrentGameRound()==game.getNumberOfRounds()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not more GameRounds");
        }
        int newGameRound = game.getCurrentGameRound() + 1;
        System.out.println(game.getCurrentGameRound());
        game.setCurrentGameRound(newGameRound); //next round
    }
}
