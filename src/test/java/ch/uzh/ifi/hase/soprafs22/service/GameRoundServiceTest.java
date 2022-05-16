package ch.uzh.ifi.hase.soprafs22.service;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class GameRoundServiceTest {

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        game.setNumberOfPlayersRequired(2);
        game.setGameName("testUsername");
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(game);
    }

    @Test
    public void createGameRounds_validInputs() {
        String[] playerTokens = {"1", "2"};
        int numberOfRounds = 2;
        ArrayList<String> playerList = new ArrayList<String>();
        playerList.addAll(Arrays.asList(playerTokens));
        Collections.shuffle(playerList);

        GameRound gameRound = new GameRound();
        ArrayList<String> guesserList = playerList;
        int drawIndex = 0;
        gameRound.setDrawer(playerList.get(drawIndex));
        String drawer = guesserList.get(drawIndex);
        guesserList.remove(drawIndex);
        guesserList.add(drawIndex,drawer);
        gameRound.setGuessersToken(guesserList.toArray(new String[guesserList.size()]));

        Mockito.when(userService.getUserByToken(playerTokens[0])).thenReturn(new User());
        Mockito.when(userService.getUserByToken(playerTokens[1])).thenReturn(new User());
        Mockito.when(gameRoundService.createGameRounds(numberOfRounds, playerTokens)).thenReturn(Collections.singletonList(gameRound));

        GameRound gameRound1 = gameRoundService.createGameRounds(numberOfRounds, playerTokens).get(0);
        assertEquals(gameRound1.getDrawer(), gameRound.getDrawer());
        assertEquals(gameRound1.getGuessersToken(), gameRound.getGuessersToken());
    }

    @Test
    public void getGameRound_valid_input(){
        String gameToken = "2";
        Game game = new Game();
        game.setNumberOfPlayersRequired(3);
        game.setGameName("testUsername");
        game.setGameToken(gameToken);
        game.setNumberOfPlayers(2);
        game.setGameStatus("started");
        game.setGameRoundList(new ArrayList<>());
        game.setCurrentGameRound(0);

        GameRound currentGameRound = new GameRound();
        game.getGameRoundList().add(currentGameRound);
        currentGameRound.setDrawer("1");
    
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);

        Mockito.when(gameRoundService.getGameRound("1")).thenReturn(currentGameRound);
        GameRound gameRound1 = gameRoundService.getGameRound("1");

        assertEquals(gameRound1.getDrawer(), currentGameRound.getDrawer());
    }

    @Test
    public void checkIfUserAlreadyGuessed_valid_input(){
        GameRound gameRound = new GameRound();
        User user = new User();
        gameRound.addUserToAlreadyGuessedMap(user);
        Mockito.when(gameRoundService.checkIfUserAlreadyGuessed(gameRound, user)).thenReturn(true);
        Boolean userAlreadyGuessed = gameRoundService.checkIfUserAlreadyGuessed(gameRound, user);
        assertEquals(userAlreadyGuessed, true);
    }





}