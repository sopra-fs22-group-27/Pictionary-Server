package ch.uzh.ifi.hase.soprafs22.service;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository , UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }
    
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game getGameById(String id) {
        return this.gameRepository.findById(id);
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
        newGame.setWord("");
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
        game.setImg(img);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public String getImage(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        String img = game.getImg();
        return img;
    }

    public void changeWord(String gameToken, String word){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        game.setWord(word);
    }

    public Boolean getResultOfGuess(String gameToken, String guessedWord){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        return Objects.equals(game.getWord(), guessedWord);
    }
}
