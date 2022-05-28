package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private User testUser2;
  private User testUser3;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("password");
    testUser.setUsername("testUsername");
    testUser.setCreation_date("01/01/2022");
    testUser.setToken("1");
    testUser.setEmail("test@gmail.com");
    testUser.setRanking_points(0);
    testUser.setIsInLobby(false);
    testUser.setStatus(UserStatus.ONLINE);

    testUser2 = new User();
    testUser2.setId(2L);
    testUser2.setPassword("testPassword2");
    testUser2.setUsername("testUsername2");
    testUser2.setCreation_date("01/01/2022");
    testUser2.setToken("2");
    testUser2.setEmail("test2@gmail.com");
    testUser2.setRanking_points(0);
    testUser2.setIsInLobby(false);
    testUser2.setStatus(UserStatus.ONLINE);

    testUser3 = new User();
    testUser3.setId(3L);
    testUser3.setPassword("testPassword3");
    testUser3.setUsername("testUsername3");
    testUser3.setCreation_date("01/01/2022");
    testUser3.setToken("3");
    testUser3.setEmail("test@gmail.com");
    testUser3.setRanking_points(0);
    testUser3.setIsInLobby(false);
    testUser3.setStatus(UserStatus.ONLINE);
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void getUserById_validInputs() {
      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(testUser);
      assertEquals(testUser, userService.getUserById(testUser.getId()));
  }

  @Test
  public void getUserByToken_validInputs() {
      Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);
      assertEquals(testUser, userService.getUserByToken(testUser.getToken()));
  }

    @Test
    public void getUserByToken_InvalidInputs() {
      User user = null;
      Mockito.when(userRepository.findByToken(testUser.getToken())).thenReturn(user);
      assertThrows(ResponseStatusException.class, () -> userService.getUserByToken(testUser.getToken()));
    }

  @Test
  public void updateUserStatus_validInputs() {
      Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);
      assertEquals(testUser, userService.updateUserStatus(testUser.getToken()));
      assertEquals(UserStatus.OFFLINE, testUser.getStatus());
  }

    @Test
    public void updateUserStatus_InvalidInputsToken() {
      User user = null;
      Mockito.when(userRepository.findByToken(testUser.getToken())).thenReturn(user);
      assertThrows(ResponseStatusException.class, () -> userService.updateUserStatus(testUser.getToken()));
    }

  @Test
  public void createUser_validInputs_success() {
    User createdUser = userService.createUser(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getRanking_points(), createdUser.getRanking_points());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    userService.createUser(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_duplicateEmail_throwsException() {
    userService.createUser(testUser);
    Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_InvalidInputEmptyPassword() {
      testUser.setPassword("");
      assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void updateUser_validInputs_success() {
      User user  = userService.createUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

      Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(user);
      User createdUser = userService.updateUser(user.getToken(), testUser2);
      Mockito.verify(userRepository, Mockito.times(4)).flush();

      assertEquals(testUser.getId(), createdUser.getId());
      assertEquals(testUser2.getPassword(), createdUser.getPassword());
      assertEquals(testUser2.getUsername(), createdUser.getUsername());
      assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
      assertEquals(testUser2.getEmail(), createdUser.getEmail());
      assertEquals(testUser.getRanking_points(), createdUser.getRanking_points());
      assertEquals(testUser.getToken(), createdUser.getToken());
      assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void updateUser_duplicateUsername_throwsException() {
      userService.createUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
      Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser3);
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      assertEquals("testUsername", testUser.getUsername());
      assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser3.getToken(), testUser));
  }

    @Test
    public void updateUser_InvalidInputUserNull() {
        User user = null;
        Mockito.when(userRepository.findByToken(testUser.getToken())).thenReturn(user);
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser.getToken(), testUser));
    }

    @Test
    public void updateUser_InvalidInputExistingEmail() {
        userService.createUser(testUser);
        userService.createUser(testUser3);
        testUser2.setEmail("test3@gmail.com");
        Mockito.when(userRepository.findByEmail("test3@gmail.com")).thenReturn(testUser3);
        Mockito.when(userRepository.findByToken(testUser.getToken())).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser.getToken(), testUser2));
    }

    @Test
    public void checkUserLogin_validInput () {
      userService.createUser(testUser);
      userService.createUser(testUser2);
      testUser.setStatus(UserStatus.OFFLINE);
      List<User> users = new ArrayList<User>();
      users.add(testUser);
      users.add(testUser2);
      Mockito.when(userRepository.findAll()).thenReturn(users);

      assertEquals(testUser, userService.checkUserLogin(testUser));
    }

    @Test
    public void checkUserLogin_validInputOnline () {
        userService.createUser(testUser);
        userService.createUser(testUser2);
        testUser.setStatus(UserStatus.ONLINE);
        List<User> users = new ArrayList<User>();
        users.add(testUser);
        users.add(testUser2);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        assertEquals(testUser, userService.checkUserLogin(testUser));
    }

    @Test
    public void checkUserLogin_InvalidInputEmptyList() {
        userService.createUser(testUser);
        userService.createUser(testUser2);
        testUser.setStatus(UserStatus.OFFLINE);
        List<User> users = new ArrayList<User>();
        Mockito.when(userRepository.findAll()).thenReturn(users);
        assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(testUser));
    }

    @Test
    public void checkUserLogin_InvalidInputWrongPassword() {
      User userToLogin = new User();
      userService.createUser(testUser);
      userService.createUser(testUser2);

      userToLogin.setPassword("wrongPassword");
      userToLogin.setId(1L);
      userToLogin.setUsername("testUsername");
      userToLogin.setCreation_date("01/01/2022");
      userToLogin.setToken("1");
      userToLogin.setEmail("test@gmail.com");
      userToLogin.setRanking_points(0);
      userToLogin.setIsInLobby(false);
      userToLogin.setStatus(UserStatus.ONLINE);

      testUser.setStatus(UserStatus.OFFLINE);
      List<User> users = new ArrayList<User>();
      users.add(testUser);
      users.add(testUser2);
      Mockito.when(userRepository.findAll()).thenReturn(users);

      assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(userToLogin));
    }

    @Test
    public void deleteUser() {
      userService.deleteUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.any());
      Mockito.verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    public void syncActiveTime() {
        userService.syncActiveTime(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).flush();
    }
}
