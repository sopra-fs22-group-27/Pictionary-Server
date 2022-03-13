package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

//  private UserRepository userRepository;

//  @BeforeEach
//  public void setup(){
//      MockitoAnnotations.openMocks(this);
//      User user = new User();
//      user.setId(1L);
//      user.setPassword("Password");
//      user.setUsername("Username");
//      user.setStatus(UserStatus.ONLINE);
//      user.setLogged_in(true);
//      user.setToken(UUID.randomUUID().toString());
//      user.setBirthday("Birthday");
//      user.setCreation_date("creation_date");
//
//      when(userRepository.save(Mockito.any())).thenReturn(user);
//  }

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

    User user = new User();
    user.setPassword("Password");
    user.setUsername("Username");
    user.setStatus(UserStatus.ONLINE);
    user.setLogged_in(true);
    user.setToken(UUID.randomUUID().toString());
    user.setBirthday("1998-03-01");
    user.setCreation_date("01/03/1998");

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$[0].logged_in", is(user.getLogged_in())))
        .andExpect(jsonPath("$[0].token", is(user.getToken())))
        .andExpect(jsonPath("$[0].birthday", is("1998-02-28T23:00:00.000+00:00")))
        .andExpect(jsonPath("$[0].creation_date", is("1998-02-28T23:00:00.000+00:00")));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {

      User user = new User();
      user.setId(1L);
      user.setPassword("Test User");
      user.setUsername("testUsername");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);
      user.setLogged_in(true);
      user.setBirthday("1998-03-01");
      user.setCreation_date("01/03/2022");

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setPassword("Test User");
      userPostDTO.setUsername("testUsername");
      userPostDTO.setBirthday("Test Birthday");
      userPostDTO.setCreation_date("Test Creation_date");

      given(userService.createUser(any())).willReturn(user);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/users")
          .contentType(MediaType.APPLICATION_JSON)
          .content(asJsonString(userPostDTO));
      // then
//      LocalDateTime date = LocalDateTime.of(1998, 3, 1, 0, 0, 0, 0);
//      ZonedDateTime cetTimeZoned = ZonedDateTime.of(date, ZoneId.of("CET"));
//      LocalDateTime convertedCurrentDate = cetTimeZoned.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
      mockMvc.perform(postRequest)
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id", is(user.getId().intValue())))
          .andExpect(jsonPath("$.username", is(user.getUsername())))
          .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
          .andExpect(jsonPath("$.birthday", is("1998-02-28T23:00:00.000+00:00")))
          .andExpect(jsonPath("$.token", is(user.getToken())))
          .andExpect(jsonPath("$.logged_in", is(user.getLogged_in())))
          .andExpect(jsonPath("$.creation_date", is("2022-02-28T23:00:00.000+00:00")));
      verify(userService, times(1)).createUser(any());
  }

    @Test
    public void createUser_duplicateUsernameInput() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("Test User");
        userPostDTO.setUsername("testUsername");
        userPostDTO.setBirthday("Test Birthday");
        userPostDTO.setCreation_date("Test Creation_date");
        userPostDTO.setLogged_in(true);

//        UserPostDTO userPostDTO2 = new UserPostDTO();
//        userPostDTO2.setPassword("Test User");
//        userPostDTO2.setUsername("testUsername");
//        userPostDTO2.setBirthday("Test Birthday");
//        userPostDTO2.setCreation_date("Test Creation_date");
//        User user2 = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO2);
        Exception conflict = new ResponseStatusException(HttpStatus.CONFLICT);
        when(userService.createUser(Mockito.any())).thenThrow(conflict);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest1 = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));



        mockMvc.perform(postRequest1)
                .andExpect(status().isConflict());
        Mockito.verify(userService, Mockito.times(1)).createUser(Mockito.any());

    }

    @Test
    public void updateUser_validInput_userUpdated() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");
        userPostDTO.setBirthday("Test Birthday");

//        User createdUser = userService.createUser(user);

        given(userService.updateUser(eq(user.getId()), Mockito.any()))
                .willReturn(user);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());


    }

    @Test
    public void updateUser_invalidInput_userNotExist() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");
        userPostDTO.setBirthday("Test Birthday");

//        User createdUser = userService.createUser(user);
        Exception not_found = new ResponseStatusException(HttpStatus.NOT_FOUND);
        given(userService.updateUser(anyLong(), Mockito.any())).willThrow(not_found);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());


    }
    @Test
    public void findUserById_valid() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testPassword");

//        User createdUser = userService.createUser(user);
        given(userService.getUserById(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = get("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).getUserById(Mockito.any());
    }

    @Test
    public void findUserById_invalid() throws Exception{
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testPassword");

//        User createdUser = userService.createUser(user);
        Exception not_found = new ResponseStatusException(HttpStatus.NOT_FOUND);
        given(userService.getUserById(Mockito.any())).willThrow(not_found);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = get("/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON);
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
        Mockito.verify(userService, Mockito.times(1)).getUserById(Mockito.any());
    }
    @Test
    public void loginUser_validInput_userLogin() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");
        userPostDTO.setPassword("testPassword");

//        User createdUser = userService.createUser(user);
        given(userService.checkUserLogin(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).checkUserLogin(Mockito.any());

    }
    @Test
    public void loginUser_emptyDatabase() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");
        userPostDTO.setPassword("testPassword");

//        User createdUser = userService.createUser(user);
        Exception not_found = new ResponseStatusException(HttpStatus.NOT_FOUND);
        given(userService.checkUserLogin(Mockito.any())).willThrow(not_found);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());

    }
    @Test
    public void loginUser_invalidInput() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername2");
        userPostDTO.setPassword("testPassword");

//        User createdUser = userService.createUser(user);
        Exception not_found = new ResponseStatusException(HttpStatus.NOT_FOUND);
        given(userService.checkUserLogin(Mockito.any())).willThrow(not_found);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());


    }

    @Test
    public void updateUserStatus_validInput_userStatusUpdate() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);
        user.setLogged_in(true);
        user.setBirthday("Test Birthday");
        user.setCreation_date("Test Creation_date");

        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setStatus(UserStatus.OFFLINE);
        userPostDTO.setLogged_in(false);

//        User createdUser = userService.createUser(user);
//        given(userService.getUsers()).willReturn(Collections.singletonList(user));
        given(userService.updateUserStatus(eq(user.getId()), Mockito.any())).willReturn(user);
        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/status/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));
        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).updateUserStatus(anyLong(), Mockito.any());

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