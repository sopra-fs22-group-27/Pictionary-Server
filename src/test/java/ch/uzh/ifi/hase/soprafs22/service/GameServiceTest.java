package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Game;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class GameServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private GameService gameService;

    @InjectMocks
    private GameRoundService gameRoundService;

    private Game game;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        game = new Game();
        game.setNumberOfPlayersRequired(2);
        game.setGameName("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(game);
    }

    @Test
    public void createGame_validInputs_success() {

        Game createGame = gameService.createGame(game);

        // then
        Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(game.getGameName(), createGame.getGameName());
        // assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(game.getNumberOfPlayersRequired(), createGame.getNumberOfPlayersRequired());
        assertNotNull(createGame.getGameToken());
    }

    @Test
    public void addPlayerToGame() {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setRanking_points(0);
        user.setIsInLobby(false);
        //creation date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCreation_date = formatter.format(today);
        user.setCreation_date(formattedCreation_date);

        Game createGame = gameService.createGame(game);

        // ...
    }


}
