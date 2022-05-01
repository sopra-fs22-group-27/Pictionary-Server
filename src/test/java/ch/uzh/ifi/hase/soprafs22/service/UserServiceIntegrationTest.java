package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("testPassword");
    testUser.setUsername("testUsername");
    testUser.setEmail("test@email.com");
    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("password1");
    testUser.setUsername("testUsername");
    testUser.setEmail("test@email.com");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setPassword("password2");
    testUser2.setUsername("testUsername");
    testUser.setEmail("test2@email.com");
    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void updateUser_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setId(1L);
      testUser.setPassword("password");
      testUser.setUsername("testUsername");
      testUser.setCreation_date("01/01/2022");
      testUser.setToken("1");
      testUser.setEmail("test@gmail.com");
      testUser.setRanking_points(0);
      testUser.setIsInLobby(false);
      testUser.setStatus(UserStatus.ONLINE);
      User createdUser1 = userService.createUser(testUser);

      User testUser2 = new User();
      // change the username
      testUser2.setId(2L);
      testUser2.setPassword("password");
      testUser2.setUsername("testUsername2");
      testUser2.setCreation_date("01/01/2022");
      testUser2.setToken("2");
      testUser2.setEmail("test3@gmail.com");
      testUser2.setRanking_points(0);
      testUser2.setIsInLobby(false);
      testUser2.setStatus(UserStatus.ONLINE);
      User createdUser2 = userService.createUser(testUser2);

      User testUser3 = new User();

      testUser3.setUsername("testUsername2");
      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> userService.updateUser(createdUser1.getToken(), testUser3));
  }
}
