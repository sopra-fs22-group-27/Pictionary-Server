package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Img;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping(path = "/games/drawing")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateImage(String gameToken, @RequestBody GamePutDTO gamePutDTO){
        Img img = DTOMapper.INSTANCE.convertGamePutDTOToImg(gamePutDTO);
        gameService.updateImg(gameToken, img.getImg());
    }

    @GetMapping(path = "/games/drawing")
    ResponseEntity<String> getImage(String gameToken) {
        return ResponseEntity.ok(gameService.getImage(gameToken));
    }

    /**
     * Get a game by id
     * @param id
     * @return GameGetDTO
     */
    @GetMapping(path = "/games/{id}")
    public GameGetDTO getGameById(@PathVariable String id) {
        Game game = gameService.getGameById(id);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }
}
