package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private User user;
  private UserPostDTO userPostDTO;

  @BeforeEach
  void setup() {
    user = new User();
    user.setPassword("password");
    user.setId(1L);
    user.setUsername("testUsername");
    user.setEmail("test@email.com");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("1");
    user.setRanking_points(0);
    user.setIsInLobby(false);

    userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setEmail("test@email.com");
    userPostDTO.setPassword("Test Password");
    userPostDTO.setRanking_points(0);
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedCreation_date = formatter.format(today);
    userPostDTO.setCreation_date(formattedCreation_date);
  }

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {    
    List<User> allUsers = Collections.singletonList(user);
    given(userService.getUsers()).willReturn(allUsers);
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String formattedCreation_date = formatter.format(today);
    user.setCreation_date(formattedCreation_date);

    given(userService.getUserByToken(Mockito.anyString())).willReturn(user);

    MockHttpServletRequestBuilder getRequest = get("/users/1")
        .contentType(MediaType.APPLICATION_JSON);

    MvcResult result = mockMvc.perform(getRequest).andReturn();
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
    assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
  }

    @Test
    public void getRequest_usersToken_test() throws Exception {
        given(userService.getUserByToken(Mockito.anyString())).willReturn(user);
        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.GET.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(), result.getRequest().getContentType());
    }

    @Test
    public void getRequest_usersToken_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).getUserByToken(Mockito.anyString());
        MockHttpServletRequestBuilder getRequest = get("/users/10")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(getRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_usersToken_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        MvcResult result = mockMvc.perform(putRequest).andReturn();
        verify(userService,times(1)).updateUser(Mockito.anyString(),Mockito.any());
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_usersToken_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).updateUser(Mockito.anyString(),Mockito.any());
        MockHttpServletRequestBuilder putRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_usersToken_fail2_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT)).when(userService).updateUser(Mockito.anyString(),Mockito.any());
        MockHttpServletRequestBuilder putRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_updateStatus_test() throws Exception {
        MockHttpServletRequestBuilder putRequest = put("/status/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        verify(userService,times(1)).updateUserStatus(Mockito.anyString());
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_updateStatus_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).updateUserStatus(Mockito.anyString());
        MockHttpServletRequestBuilder putRequest = put("/status/1")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(putRequest).andReturn();
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }


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
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}
