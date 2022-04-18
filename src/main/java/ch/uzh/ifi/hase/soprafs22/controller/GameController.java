package ch.uzh.ifi.hase.soprafs22.controller;

import org.springframework.web.bind.annotation.*;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import java.util.List;
import java.util.ArrayList;

@RestController
public class GameController {
    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Create a game
     * @param GamePostDTO
     * @return GameGetDTO
     */

    @PostMapping(path = "/games")
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        Game game = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);

        Game createGame = gameService.createGame(game);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createGame);
    }

    /**
     * Get all games
     * @return List of GameGetDTO
     */
    @GetMapping(path = "/games")
    public List<GameGetDTO> getGames() {
        List<Game> gamesList = gameService.getGames();
        List<GameGetDTO> gameGetDTOs = new ArrayList<>();
        for (Game game : gamesList) {
            gameGetDTOs.add(DTOMapper.INSTANCE.convertEntityToGameGetDTO(game));
        }
        return gameGetDTOs;
    }

    /**
     * Get a game by id
     * @param id
     * @return GameGetDTO
     */
    @GetMapping(path = "/games/{gameToken}")
    public GameGetDTO getGameById(@PathVariable String gameToken) {
        Game game = gameService.getGameById(gameToken);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    /**
     * Add player to a game by id
     * @param id
     * @param userToken
     * @return GameGetDTO
     */   
    @PostMapping(path = "/games/{gameToken}/players/{userToken}")
    public GameGetDTO addPlayerToGame(@PathVariable String gameToken, @PathVariable String userToken) {
        Game game = gameService.addPlayerToGame(gameToken, userToken);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }
}
