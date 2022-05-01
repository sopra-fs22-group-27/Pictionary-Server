package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @MockBean
    private GameRoundService gameRoundService;

    @Test
    public void postRequest_createGame_test() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setGameName("testGameName");
        gamePostDTO.setNumberOfPlayers(2);
        gamePostDTO.setNumberOfPlayersRequired(2);
        gamePostDTO.setNumberOfRounds(10);
        gamePostDTO.setRoundLength(60);
        gamePostDTO.setGameStatus("waiting");
        gamePostDTO.setGameToken("1-game");
        gamePostDTO.setPlayerTokens(new String[] {"1", "2", "3"});

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(gamePostDTO));

        // then
        MvcResult result = mockMvc.perform(postRequest).andReturn();


        verify(gameService, times(1)).createGame(Mockito.any());

        // test the http status of the response
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
        }

    @Test
    public void getRequest_games_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        List<Game> allGames = Collections.singletonList(game);
        // when/then -> do the request + validate the result
        given(gameService.getGames()).willReturn(allGames);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games").contentType(MediaType.APPLICATION_JSON);


        // then
        MvcResult result = mockMvc.perform(getRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
    }

    @Test
    public void getRequest_game_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        // when/then -> do the request + validate the result
        given(gameService.getGameByToken(Mockito.anyString())).willReturn(game);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/1-game").contentType(MediaType.APPLICATION_JSON);


        // then
        MvcResult result = mockMvc.perform(getRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
    }

    @Test
    public void getRequest_game_fail_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).getGameByToken(Mockito.anyString());
        // when
        MockHttpServletRequestBuilder getRequest = get("/games/2-game").contentType(MediaType.APPLICATION_JSON);


        // then
        MvcResult result = mockMvc.perform(getRequest).andReturn();

        // test the http method ()
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_updateImage_test() throws Exception {

        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setImg("testImage");


        // when
        MockHttpServletRequestBuilder putRequest = put("/games/drawing/?gameToken=" + "1-game")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePutDTO));


        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        verify(gameService,times(1)).updateImg(Mockito.anyString(), Mockito.anyString());

        // test the http status of the response
        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_addPlayerToGame_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
       
        // when
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/player/1")
            .contentType(MediaType.APPLICATION_JSON);


        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_addPlayerToGame_fail_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
       

        // when
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).addPlayerToGame(Mockito.anyString(), Mockito.anyString());

        MockHttpServletRequestBuilder putRequest = put("/games/1-game/player/2")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_addPlayerToGame_fail2_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setPlayerTokens(new String[] {"1", "2", "3"});
        game.setCurrentGameRound(0);

        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
       

        // when
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).addPlayerToGame(Mockito.anyString(), Mockito.anyString());

        MockHttpServletRequestBuilder putRequest = put("/games/2-game/player/1")
            .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }


    private String asJsonString(final Object object) {
        try {
        return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            String.format("The request body could not be created.%s", e.toString()));
        }
    }
}