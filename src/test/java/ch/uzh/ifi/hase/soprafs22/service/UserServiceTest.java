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
}
