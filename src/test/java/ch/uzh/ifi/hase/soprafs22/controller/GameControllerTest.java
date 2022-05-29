package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameRoundGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
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

    private GamePostDTO gamePostDTO;
    private Game game;
    private User user;
    private GameRoundGetDTO gameRoundGetDTO;
    private GameRound gameRound;

    @BeforeEach
    void setUp() {
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

        user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        gameRoundGetDTO = new GameRoundGetDTO();
        gameRoundGetDTO.setDrawerToken("1");
        gameRoundGetDTO.setWinner("1");
        gameRoundGetDTO.setWord("testWord");
        gameRoundGetDTO.setRoundStartingTime(1653810840412L);

        gameRound = new GameRound();
        gameRound.setWord("testWord");
        gameRound.setDrawer("1");
    }


    @Test
    public void postRequest_createGame_test() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/games?userToken=sss")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(gamePostDTO));
        MvcResult result = mockMvc.perform(postRequest).andReturn();
        verify(gameService, times(1)).createGame(Mockito.anyString(), Mockito.any());
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.POST.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void getRequest_games_test() throws Exception {
        List<Game> allGames = Collections.singletonList(game);
        given(gameService.getGames()).willReturn(allGames);
        MockHttpServletRequestBuilder getRequest = get("/games").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
    }

    @Test
    public void getRequest_game_test() throws Exception {
        given(gameService.getGameByToken(Mockito.anyString())).willReturn(game);
        MockHttpServletRequestBuilder getRequest = get("/games/1-game").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
    }

    @Test
    public void getRequest_game_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).getGameByToken(Mockito.anyString());
        MockHttpServletRequestBuilder getRequest = get("/games/2-game").contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_updateImage_test() throws Exception {
        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setImg("testImage");
        MockHttpServletRequestBuilder putRequest = put("/games/drawing/?gameToken=" + "1-game")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePutDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        verify(gameService,times(1)).updateImg(Mockito.anyString(), Mockito.anyString());
        assertEquals(HttpStatus.ACCEPTED.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_addPlayerToGame_test() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setPassword("");
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/player/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_addPlayerToGame_fail_test() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setPassword("");
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).addPlayerToGame(Mockito.anyString(), Mockito.anyString(), Mockito.any());
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/player/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_addPlayerToGame_fail2_test() throws Exception {
        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setPassword("");
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).addPlayerToGame(Mockito.anyString(), Mockito.anyString(), Mockito.any());
        MockHttpServletRequestBuilder putRequest = put("/games/2-game/player/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_addPlayerToGame_fail3_test() throws Exception {
        Game game = new Game();
        game.setGameName("testGameName");
        game.setNumberOfPlayers(2);
        game.setNumberOfPlayersRequired(2);
        game.setNumberOfRounds(10);
        game.setRoundLength(60);
        game.setGameStatus("waiting");
        game.setGameToken("1-game");
        game.setCurrentGameRound(0);       
        User user1 = new User();
        user1.setPassword("Test Password");
        user1.setUsername("testUsername");
        user1.setEmail("test@email.com");
        user1.setToken("1");
        user1.setStatus(UserStatus.ONLINE);
       
        User user2 = new User();
        user2.setId(2L);
        user2.setPassword("Test Password2");
        user2.setUsername("testUsername2");
        user2.setEmail("test2@email.com");
        user2.setToken("2");
        user2.setStatus(UserStatus.ONLINE);

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setPassword("");
        
        // when
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT)).when(gameService).addPlayerToGame(Mockito.anyString(), eq("2"), Mockito.any());

        MockHttpServletRequestBuilder putRequest1 = put("/games/1-game/player/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO));
            
        MvcResult result1 = mockMvc.perform(putRequest1).andReturn();

        assertEquals(HttpStatus.OK.value(), result1.getResponse().getStatus());
        
        

        MockHttpServletRequestBuilder putRequest2 = put("/games/1-game/player/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(gamePostDTO));

        // then
        MvcResult result2 = mockMvc.perform(putRequest2).andReturn();

        assertEquals(HttpStatus.CONFLICT.value(), result2.getResponse().getStatus());
    }

    @Test
    public void putRequest_addPlayerToGame_fail4_test() throws Exception {
        Game game1 = new Game();
        game1.setGameName("testGameName");
        game1.setNumberOfPlayers(2);
        game1.setNumberOfPlayersRequired(2);
        game1.setNumberOfRounds(10);
        game1.setRoundLength(60);
        game1.setGameStatus("waiting");
        game1.setGameToken("1-game");
        game1.setCurrentGameRound(0);

        Game game2 = new Game();
        game2.setGameName("testGameName");
        game2.setNumberOfPlayers(2);
        game2.setNumberOfPlayersRequired(2);
        game2.setNumberOfRounds(10);
        game2.setRoundLength(60);
        game2.setGameStatus("waiting");
        game2.setGameToken("2-game");
        game2.setCurrentGameRound(0);

        User user1 = new User();
        user1.setId(1L);
        user1.setPassword("Test Password");
        user1.setUsername("testUsername");
        user1.setEmail("test@email.com");
        user1.setCreation_date("01/01/2022");
        user1.setToken("1");
        user1.setStatus(UserStatus.ONLINE);
        user1.setRanking_points(0);        
        
        GamePostDTO gamePostDTO = new GamePostDTO();        
        gamePostDTO.setPassword("");
        MockHttpServletRequestBuilder putRequest1 = put("/games/1-game/player/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(gamePostDTO));
            
        MvcResult result1 = mockMvc.perform(putRequest1).andReturn();
        assertEquals(HttpStatus.OK.value(), result1.getResponse().getStatus());
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT)).when(gameService).addPlayerToGame(eq("1-game"), Mockito.anyString(), Mockito.any());

        MockHttpServletRequestBuilder putRequest2 = put("/games/1-game/player/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(gamePostDTO));
        MvcResult result2 = mockMvc.perform(putRequest2).andReturn();
        assertEquals(HttpStatus.CONFLICT.value(), result2.getResponse().getStatus());
    }


    @Test
    public void putRequest_changeWord_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/word/hahaha")
            .contentType(MediaType.APPLICATION_JSON);
                    MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_changeWord_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).changeWord(eq("2-game"), Mockito.anyString());
        MockHttpServletRequestBuilder putRequest = put("/games/2-game/word/hahaha")
            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void getRequest_controlGuessedWord_test() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/games/1-game/user/4/word/hahaha")
            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }
    
    @Test
    public void getRequest_controlGuessedWord_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(gameService).getResultOfGuess(eq("2-game"), Mockito.anyString(), Mockito.anyString());
        MockHttpServletRequestBuilder getRequest = get("/games/2-game/user/4/word/hahaha")
            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void getRequest_getImage_test() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/games/1-game")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void deleteRequest_deleteGame_test() throws Exception {
        MockHttpServletRequestBuilder deleteRequest = delete("/games/1-game")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(deleteRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.DELETE.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_removePlayerFromLobby_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/leave/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void getRequest_removePlayerFromLobby_test() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/games/1-game/full")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void getRequest_getGameRound_test() throws Exception {
        given(gameRoundService.getGameRound(game.getGameToken())).willReturn(gameRound);
        MockHttpServletRequestBuilder getRequest = get("/gameRound/1-game")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_changeGameRound_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/nextRound/1-game")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void getRequest_getJoinableGames_test() throws Exception {
        MockHttpServletRequestBuilder getRequest = get("/joinable-games")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_finishGame_test() throws Exception {
        MockHttpServletRequestBuilder getRequest = put("/games/1-game/updateStatus")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_givePoints_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/points?points=2")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_givePoints_fail() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/games/1-game/points")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
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