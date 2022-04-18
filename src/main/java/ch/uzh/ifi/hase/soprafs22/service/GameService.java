package ch.uzh.ifi.hase.soprafs22.service;
import java.util.List;
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

    public Game createGame (Game newGame) {
        newGame.setGameToken(UUID.randomUUID().toString());
        newGame = gameRepository.save(newGame);
        gameRepository.flush();
        return newGame;
    }

    public Game addPlayerToGame (String gameToken, String userToken) {
        Game game = gameRepository.findById(gameToken);
        User user = userService.getUserByToken(userToken);
        List<String> currentPlayers = game.getPlayers();

        if (game.getGameStatus().equals("started") || game.getGameStatus().equals("finished")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game has already started or is finished");
        } else if (currentPlayers.contains(userToken)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user is already in the game");
        } else if (currentPlayers.size() == game.getNumberOfPlayersRequired()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game is already full");
        } else if (game == null || user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game or user does not exist");
        } else {
            currentPlayers.add(userToken);
            game.setPlayers(currentPlayers);
            gameRepository.flush();
            return game;
        }
    }
}