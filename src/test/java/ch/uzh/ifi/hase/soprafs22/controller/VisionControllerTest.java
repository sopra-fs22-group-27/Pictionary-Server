package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.cloud.spring.vision.CloudVisionTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * GameControllerTest
 * This is a WebMvcTest which allows to test the GameController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the GameController works.
 */
@WebMvcTest(VisionController.class)
public class VisionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserService userService;
    @MockBean
    private CloudVisionTemplate cloudVisionTemplate;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameRoundService gameRoundService;

    private GamePostDTO gamePostDTO;
    private Game game;
    private GameRound currentGameRound;
    private User user;
    private String gameToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gamePostDTO = new GamePostDTO();
        gamePostDTO.setGameName("testGameName");
        gamePostDTO.setNumberOfPlayers(2);
        gamePostDTO.setNumberOfPlayersRequired(2);
        gamePostDTO.setNumberOfRounds(10);
        gamePostDTO.setRoundLength(60);
        gamePostDTO.setGameStatus("waiting");
        gamePostDTO.setGameToken("1-game");

        game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setCurrentGameRound(0);
        game.setPassword("");
        game.setIsPublic(true);
        game.setGameRoundList(new ArrayList<GameRound>());
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(game);

        currentGameRound = new GameRound();
        currentGameRound.setImg("testImg");
        game.getGameRoundList().add(currentGameRound);

        user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
    }

    @Test
    public void getRequest_givePointsToDrawer_test() throws Exception {
        Mockito.when(gameRepository.findByGameToken(gameToken)).thenReturn(game);
        MockHttpServletRequestBuilder getRequest = get("/vision/1-game")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        verify(gameService, times(1)).createGame(Mockito.anyString(), Mockito.any());
        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }
}
