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
import org.springframework.http.HttpStatus;
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

    @Mock
    private GameService gameServiceMock;

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
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(game, gameService.createGame(userToken, game));
    }

    @Test
    public void addPlayerToGame_validInputs() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setIsPublic(true);
        game.setGameStatus("");

        assertEquals(game, gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void addPlayerToGame_InvalidInputsStatus() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setIsPublic(true);

        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void addPlayerToGame_InvalidInputsUserAlreadyInGame() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        game.addUserToIntegerMap(user);
        game.setGameStatus("");
        game.setIsPublic(true);

        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void addPlayerToGame_InvalidInputsGameIsFull() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setIsPublic(true);
        game.setGameStatus("");
        game.setNumberOfPlayersRequired(1);

        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void removePlayerFromLobby_InvalidInputs() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.removePlayerFromLobby(gameToken, userToken));
    }
    @Test
    public void removePlayerFromLobby_validInputs() {
        game.addUserToIntegerMap(user);
        game.setNumberOfPlayers(1);
        game.setGameStatus("");
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);

        gameService.removePlayerFromLobby(gameToken, userToken);

        assertEquals(game.getNumberOfPlayers(), 0);
    }

    @Test
    public void addPlayerToGame_validInputsPrivate() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setPassword("testPassword");
        game.setGameStatus("");

        assertEquals(game, gameService.addPlayerToGame(gameToken, userToken, game));

    }
    @Test
    public void addPlayerToGame_InvalidInputsPrivateEmptyPassword() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);

        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void addPlayerToGame_InvalidInputsPrivatePasswordWrong() {
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        Game game2 = game;
        game2.setPassword("Other passwer");
        game.setPassword("testPassword");

        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game2));
    }


    @Test
    public void addPlayerToGame_InvalidUserDoesNotExist() {
        User user = null;
        Mockito.when(userService.getUserByToken(userToken)).thenReturn(user);
        Mockito.when(gameRepository.save(game)).thenReturn(game);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setIsPublic(true);
        game.setGameStatus("");


        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void addPlayerToGame_gameNull() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.addPlayerToGame(gameToken, userToken, game));
    }

    @Test
    public void isGameFull_validInputs() {
        game.setNumberOfPlayersRequired(2);
        game.addUserToIntegerMap(user);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setNumberOfPlayers(2);
        game.setGameRoundList(new ArrayList<>());
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(gameService.isGameFull(gameToken), true);
    }

    @Test
    public void isGameFull_validInputsButNotFull() {
        game.setNumberOfPlayersRequired(7);
        game.addUserToIntegerMap(user);
        User user2 = new User();
        game.addUserToIntegerMap(user2);
        game.setNumberOfPlayers(2);
        game.setGameRoundList(new ArrayList<>());
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(gameService.isGameFull(gameToken), false);
    }

    @Test
    public void isGameFull_InvalidInputs() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.isGameFull(gameToken));
    }

    @Test
    public void getGameScoreBoard_validInputs() {
        game.addUserToIntegerMap(user);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(game.getScoreBoardMap(), gameService.getGameScoreBoard(gameToken));
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

    @Test
    public void updateImg_validInputs() {
        GameRound currentGameRound = new GameRound();
        currentGameRound.setImg("testImg");
        game.getGameRoundList().add(currentGameRound);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        gameService.updateImg(gameToken, "newImage");
        assertEquals(gameService.getImage(gameToken), "newImage");
    }

    @Test
    public void changeWord_validInputs() {
        GameRound currentGameRound = new GameRound();
        currentGameRound.setWord("NotThisWord");
        game.getGameRoundList().add(currentGameRound);
        game.setCurrentGameRound(1);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        gameService.changeWord(gameToken, "TestWord");
        assertEquals(currentGameRound.getWord(), "TestWord");
    }

    @Test
    public void changeWord_InvalidInputs() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.changeWord(gameToken, "Test"));
    }

    @Test
    public void getResultOfGuess_validInputs() {
        GameRound currentGameRound = new GameRound();
        currentGameRound.setWord("correctWord");
        game.getGameRoundList().add(currentGameRound);
        game.setCurrentGameRound(1);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userRepository.findByToken(userToken)).thenReturn(user);
        game.addUserToIntegerMap(user);
        assertEquals(gameService.getResultOfGuess(gameToken, userToken, "correctWord"), true);
    }

    @Test
    public void getResultOfGuess_InvalidInputsGameToken() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.getResultOfGuess(gameToken, userToken, "Test"));
    }

    @Test
    public void getResultOfGuess_InvalidInputsWrongWord() {
        GameRound currentGameRound = new GameRound();
        currentGameRound.setWord("correctWord");
        game.getGameRoundList().add(currentGameRound);
        game.setCurrentGameRound(1);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userRepository.findByToken(userToken)).thenReturn(user);
        assertEquals(gameService.getResultOfGuess(gameToken, userToken, "wrongWord"), false);
    }

    @Test
    public void changeGameRound_validInputs() {
        GameRound currentGameRound = new GameRound();
        GameRound nextGameRound = new GameRound();
        game.getGameRoundList().add(currentGameRound);
        game.getGameRoundList().add(nextGameRound);
        game.setCurrentGameRound(1);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        gameService.changeGameRound(gameToken);
        assertEquals(game.getCurrentGameRound(), 2);
    }

    @Test
    public void changeGameRound_InvalidInputsWrongGameToken() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.changeGameRound(gameToken));
    }

    @Test
    public void changeGameRound_InvalidInputsLastRound() {
        game.setNumberOfRounds(1);
        game.setCurrentGameRound(1);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.changeGameRound(gameToken));
    }

    @Test
    public void finishGame_validInputs() {
        GameRound currentGameRound = new GameRound();
        game.getGameRoundList().add(currentGameRound);
        game.setCurrentGameRound(1);
        game.addUserToIntegerMap(user);
        game.updatePointsUserMap(user, 10);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        gameService.finishGame(gameToken);
        assertEquals(user.getRanking_points(), 10);
    }

    @Test
    public void finishGame_InvalidInputsWrongGameToken() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.finishGame(gameToken));
    }

    @Test
    public void finishGame_InvalidInputsWrongUsername() {
        GameRound currentGameRound = new GameRound();
        game.getGameRoundList().add(currentGameRound);
        game.setCurrentGameRound(1);
        game.addUserToIntegerMap(user);
        game.updatePointsUserMap(user, 10);
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> gameService.finishGame(gameToken));
    }

    @Test
    public void givePoints_validInputs() {
        Mockito.when(userRepository.findByToken(userToken)).thenReturn(user);
        gameService.givePoints(40, userToken);
        assertEquals(userRepository.findByToken(userToken).getRanking_points(), 40);

    }

    @Test
    public void givePoints_InvalidInputs() {
        User user = null;
        Mockito.when(userRepository.findByToken(userToken)).thenReturn(user);
        assertThrows(ResponseStatusException.class, () -> gameService.givePoints(10, userToken));
    }

/* Very hard to test
    @Test
    public void givePointsToDrawer_validInputs() {

    }*/

    @Test
    public void givePointsToDrawer_InvalidInputs() {
        Game game = null;
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameService.givePointsToDrawer(gameToken));
    }


}