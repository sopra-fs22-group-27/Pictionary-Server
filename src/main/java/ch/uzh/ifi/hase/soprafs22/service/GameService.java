package ch.uzh.ifi.hase.soprafs22.service;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
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

        if (game.getGameStatus().equals("waiting")) {
            List players = game.getPlayers();
            players.add(userToken);
            game.setPlayers(players);
            game.setNumberOfPlayers(players.size());
            gameRepository.save(game);
            gameRepository.flush();
            return game;
        } else {
            return null;
        }   
    }
}