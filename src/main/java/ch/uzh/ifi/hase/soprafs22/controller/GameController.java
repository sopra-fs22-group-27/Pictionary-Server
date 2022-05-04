package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.Img;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameRoundGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
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
    private final GameRoundService gameRoundService;

    GameController(GameService gameService, GameRoundService gameRoundService) {
        this.gameService = gameService;
        this.gameRoundService = gameRoundService;
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
    @GetMapping(path = "/games/{gameToken}")
    public GameGetDTO getGameByToken(@PathVariable("gameToken") String gameToken) {
        Game game = gameService.getGameByToken(gameToken);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    /**
     * Add player to a game by id
     * @param gameToken
     * @param userToken
     * @return GameGetDTO
     */   
    @PutMapping(path = "/games/{gameToken}/player/{userToken}")
    @ResponseBody
    public GameGetDTO addPlayerToGame(@PathVariable("gameToken") String gameToken, @PathVariable("userToken") String userToken, @RequestBody GamePostDTO gamePostDTO) {
        Game gameInput = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        Game game = gameService.addPlayerToGame(gameToken, userToken, gameInput);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping(path = "/games/{gameToken}/word/{word}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeWord(@PathVariable("gameToken") String gameToken, @PathVariable("word") String word){
        gameService.changeWord(gameToken, word);
    }

    @GetMapping(path = "/games/{gameToken}/user/{userToken}/word/{guessedWord}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean controlGuessedWord(@PathVariable("gameToken") String gameToken, @PathVariable("userToken") String userToken, @PathVariable("guessedWord") String guessedWord){
        return gameService.getResultOfGuess(gameToken, userToken, guessedWord);
    }

    @GetMapping(path = "/games/{gameToken}/full")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isGameFull(@PathVariable("gameToken") String gameToken){
        return gameService.isGameFull(gameToken);
    }

    @GetMapping(path = "/gameRound/{gameToken}")
    @ResponseStatus(HttpStatus.OK)
    public GameRoundGetDTO getGameRound(@PathVariable("gameToken") String gameToken){
        GameRound gameRound = gameRoundService.getGameRound(gameToken);
        return DTOMapper.INSTANCE.convertEntityToGameRoundGetDTO(gameRound);
    }

    @PutMapping(path = "/nextRound/{gameToken}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeGameRound(@PathVariable("gameToken") String gameToken){
        gameService.changeGameRound(gameToken);

    }


    /**@GetMapping(path = "/games/{gameToken}/timeGameRound")
    @ResponseStatus(HttpStatus.OK)
    public long startedGameTime(@PathVariable("gameToken") String gameToken){
        return gameService.
    }*/
}
