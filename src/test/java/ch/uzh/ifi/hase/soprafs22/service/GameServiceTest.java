package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;
    
    @Mock
    private GameRoundService gameRoundService;

    @InjectMocks
    private GameService gameService;

    private Game game;
    private User user;
    private GameRound currentGameRound;
    private String userToken; 
    private String gameToken;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        gameToken = "2";
        game = new Game();
        game.setNumberOfPlayersRequired(2);
        game.setGameName("testUsername");
        game.setGameToken(gameToken);
        game.setGameStatus("started");
        game.setGameRoundList(new ArrayList<>());
        game.setCurrentGameRound(0);
        game.setIsPublic(false);
        game.setPassword("");
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(game);

        userToken = "1";
        user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken(userToken);
        user.setStatus(UserStatus.ONLINE);    

        currentGameRound = new GameRound();
        currentGameRound.setImg("testImg");
        game.getGameRoundList().add(currentGameRound);
    }

    @Test
    public void createGame_validInputs() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Game newGame = gameService.createGame(userToken, game);
        assertEquals(newGame.getGameName(), "testUsername");
        assertEquals(newGame.getNumberOfPlayersRequired(), 2);
        assertNotNull(newGame.getGameToken());
    }

    @Test
    public void isGameFull_InvalidInputs() {
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertFalse(gameService.isGameFull(gameToken));
    }

    @Test
    public void getJoinableGames_validInputs() {
        Game game2 = new Game();
        game2.setNumberOfPlayersRequired(2);
        game2.setGameName("testUsername2");
        game2.setNumberOfPlayers(1);
        game2.setGameStatus("started");
        game2.setGameRoundList(new ArrayList<>());
        game2.setCurrentGameRound(0);
        Mockito.when(gameRepository.findAll()).thenReturn(new ArrayList<Game>() {{
            add(game);
            add(game2);
        }});
        assertEquals(gameService.getJoinableGames().size(), 2);
    }

    @Test
    public void getGameByToken_validInputs() {
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(gameService.getGameByToken(gameToken).getGameName(), "testUsername");
    }

    @Test
    public void getGameByToken_invalidInputs() {
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertNotEquals(gameService.getGameByToken(gameToken).getGameName(), "testUsername2");
    }

    @Test 
    public void getGameByToken_gameNull() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.getGameByToken(gameToken));
    }

    @Test 
    public void getImage_validInputs() {
        GameRound currentGameRound = new GameRound();
        currentGameRound.setImg("testImg");
        game.getGameRoundList().add(currentGameRound);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(gameService.getImage(gameToken), "testImg");
    }

    // @Test 
    // public void addPlayerToGame_validInputs() {
    //     String gameToken = "1";
    //     String userToken = "1";
    //     Game game = new Game();
    //     game.setNumberOfPlayersRequired(2);
    //     game.setGameName("testUsername");
    //     game.setGameToken(gameToken);
    //     game.setNumberOfPlayers(1);
    //     game.setGameStatus("started");
    //     game.setGameRoundList(new ArrayList<>());
    //     game.setCurrentGameRound(0);

    //     assertEquals(gameService.addPlayerToGame(gameToken, userToken, game).getNumberOfPlayers(), 2);
    // }
    

    @Test
    public void addPlayerToGame_gameNull() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    // @Test 
    // public void getResultOfGuess_validInputs() {
    //     String gameToken = "1";
    //     String userToken = "1";
    //     String guessedWord = "testWord";
    //     Game game = new Game();
    //     game.setNumberOfPlayersRequired(2);
    //     game.setGameName("testUsername");
    //     game.setGameToken(gameToken);
    //     game.setNumberOfPlayers(1);
    //     game.setGameStatus("started");
    //     game.setGameRoundList(new ArrayList<>());
    //     game.setCurrentGameRound(0);

    //     GameRound currentGameRound = new GameRound();
    //     currentGameRound.setWord("testWord");
    //     game.getGameRoundList().add(currentGameRound);

    //     Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);

    //     assertEquals(gameService.getResultOfGuess(gameToken, userToken, guessedWord), true);
    // }
}