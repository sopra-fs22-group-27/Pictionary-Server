package ch.uzh.ifi.hase.soprafs22.service;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.GameRoundRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class GameRoundServiceTest {

    @Mock
    private GameRoundRepository gameRoundRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private GameRoundService gameRoundService;

    private Game game;
    private GameRound currentGameRound;
    private String gameToken;
    private User user;
    private User user2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        gameToken = "1";
        game = new Game();
        game.setNumberOfPlayersRequired(2);
        game.setGameName("testUsername");
        game.setGameToken(gameToken);
        game.setNumberOfPlayers(2);
        game.setGameStatus("started");
        game.setGameRoundList(new ArrayList<>());
        game.setCurrentGameRound(0);

        currentGameRound = new GameRound();
        game.getGameRoundList().add(currentGameRound);
        currentGameRound.setDrawer("1");

        user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        user2 = new User();
        user2.setId(2L);
        user2.setPassword("Testt Password");
        user2.setUsername("testtUsername");
        user2.setEmail("testt@email.com");
        user2.setToken("2");
        user2.setStatus(UserStatus.ONLINE);
    }

    @Test
    public void createGameRounds_validInputs() {
        String[] playerTokens = {"1", "2"};
        int numberOfRounds = 2;
        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(Arrays.asList(playerTokens));
        Collections.shuffle(playerList);

        Mockito.when(userService.getUserByToken(playerTokens[0])).thenReturn(new User());
        Mockito.when(userService.getUserByToken(playerTokens[1])).thenReturn(new User());
        Mockito.when(gameRoundRepository.save(currentGameRound)).thenReturn(currentGameRound);

        assertEquals(3, gameRoundService.createGameRounds(numberOfRounds, playerTokens).size());
    }

    @Test
    public void getGameRound_valid_input(){
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertEquals(gameRoundService.getGameRound("1"), currentGameRound);
    }

    @Test
    public void getGameRound_valid_input_fail(){
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        assertThrows(ResponseStatusException.class, () -> gameRoundService.getGameRound("2"));
    }

    @Test
    public void updateCorrectGuess_valid_input() {
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        Mockito.when(userService.getUserByToken(user.getToken())).thenReturn(user);
        currentGameRound.addUserToAlreadyGuessedMap(user);

        gameRoundService.updateCorrectGuess(gameToken, user.getToken());
        assertEquals(currentGameRound.getUserGuessedInfo(user), true);
    }

    @Test
    public void checkIfUserAlreadyGuessed_valid_input(){
        currentGameRound.addUserToAlreadyGuessedMap(user);
        assertEquals(gameRoundService.checkIfUserAlreadyGuessed(currentGameRound, user), false);
    }





}