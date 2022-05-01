package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
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

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setPassword("password");
    user.setUsername("firstname@lastname");
    user.setEmail("test@email.com");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        // .andExpect(jsonPath("$[0].password", is(user.getPassword())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
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

    given(userService.getUserByToken(Mockito.anyString())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder getRequest = get("/users/1")
        .contentType(MediaType.APPLICATION_JSON);

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
    public void getRequest_usersToken_test() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test Password");
        user.setUsername("testUsername");
        user.setEmail("test@email.com");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        given(userService.getUserByToken(Mockito.anyString())).willReturn(user);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

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
    public void getRequest_usersToken_fail_test() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).getUserByToken(Mockito.anyString());

        MockHttpServletRequestBuilder getRequest = get("/users/10")
                .contentType(MediaType.APPLICATION_JSON);
        // then
        MvcResult result = mockMvc.perform(getRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_usersToken_test() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@email.com");
        userPostDTO.setPassword("Test Password");
        userPostDTO.setRanking_points(0);
        //creation date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCreation_date = formatter.format(today);
        userPostDTO.setCreation_date(formattedCreation_date);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // verify that userService.updateStatus(id) is called one time!
        verify(userService,times(1)).updateUser(Mockito.anyString(),Mockito.any());

        // test the http status of the response
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_usersToken_fail_test() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@email.com");
        userPostDTO.setPassword("Test Password");
        userPostDTO.setRanking_points(0);
        //creation date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCreation_date = formatter.format(today);
        userPostDTO.setCreation_date(formattedCreation_date);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).updateUser(Mockito.anyString(),Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_usersToken_fail2_test() throws Exception {
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setEmail("test@email.com");
        userPostDTO.setPassword("Test Password");
        userPostDTO.setRanking_points(0);
        //creation date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedCreation_date = formatter.format(today);
        userPostDTO.setCreation_date(formattedCreation_date);

        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT)).when(userService).updateUser(Mockito.anyString(),Mockito.any());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // test the http status of the response
        assertEquals(HttpStatus.CONFLICT.value(), result.getResponse().getStatus());
    }

    @Test
    public void putRequest_updateStatus_test() throws Exception {

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/status/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // verify that userService.updateStatus(id) is called one time!
        verify(userService,times(1)).updateUserStatus(Mockito.anyString());

        // test the http status of the response
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

        // test the http method ()
        assertEquals(HttpMethod.PUT.name(), result.getRequest().getMethod());

        // test the content type of the response
        assertEquals(MediaType.APPLICATION_JSON.toString(),result.getRequest().getContentType());
    }

    @Test
    public void putRequest_updateStatus_fail_test() throws Exception {

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).updateUserStatus(Mockito.anyString());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/status/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        MvcResult result = mockMvc.perform(putRequest).andReturn();

        // test the http status of the response
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