package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @Test
    public void givenLobbies_whenGetLobbies_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setPassword("password");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        lobby.setHost(user);
        lobby.setIsPublic(true);

        List<Lobby> allLobbies = Collections.singletonList(lobby);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(lobbyService.getLobbies()).willReturn(allLobbies);

        // when
        MockHttpServletRequestBuilder getRequest = get("/lobby").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                // .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].lobbyName", is(lobby.getLobbyName())))
                .andExpect(jsonPath("$[0].host.username", is(lobby.getHost().getUsername())))
                .andExpect(jsonPath("$[0].isPublic", is(lobby.getIsPublic())));
    }

    @Test
    public void createLobby_validInput_lobbyCreated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        lobby.setHost(user);
        lobby.setIsPublic(true);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName("testLobby");
        lobbyPostDTO.setIsPublic(true);

        given(lobbyService.createLobby(Mockito.any(), Mockito.any())).willReturn(lobby);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyName", is(lobby.getLobbyName())))
                // .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.host.username", is(user.getUsername())))
                .andExpect(jsonPath("$.isPublic", is(true)));
    }

    /*
    @Test
    public void updateLobby_addUserToLobby() throws Exception{
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        // given
        User user1 = new User();
        user1.setId(2L);
        user1.setPassword("Test Password");
        user1.setUsername("testUsername1");
        user1.setToken("2");
        user1.setStatus(UserStatus.ONLINE);

        Lobby lobby = new Lobby();
        lobby.setLobbyName("testLobby");
        lobby.setId(1L);
        lobby.setHost(user);
        lobby.setIsPublic(true);

        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName("testLobby");

        lobbyPostDTO.setIsPublic(true);
        lobbyPostDTO.setHost(user);

        given(lobbyService.addUserToLobby(2L, "2")).willReturn(lobby);

        // when
        MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/lobby/1L/adduser/2L");

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isPublic", is(lobby.getLobbyName())))
                // .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.host.username", is(user.getUsername())))
                .andExpect(jsonPath("$.isPublic", is(true)));
    }*/

    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}