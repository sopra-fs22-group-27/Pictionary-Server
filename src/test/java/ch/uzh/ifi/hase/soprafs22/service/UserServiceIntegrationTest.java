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
    testUser.setBirthday("testBirthday");
    testUser.setCreation_date("testCreation_date");
    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
    assertEquals(testUser.getBirthday(), createdUser.getBirthday());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("testPassword");
    testUser.setUsername("testUsername");
    testUser.setBirthday("testBirthday");
    testUser.setCreation_date("testCreation_date");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setPassword("testPassword2");
    testUser2.setUsername("testUsername");
    testUser2.setBirthday("testBirthday");
    testUser2.setCreation_date("testCreation_date");
    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void updateUser_validInputs_success() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      testUser.setBirthday("testBirthday");
      testUser.setCreation_date("testCreation_date");
      User createdUser = userService.createUser(testUser);
      assertNotNull(userRepository.findByUsername("testUsername"));
      // attempt to create second user with same username
      User testUser2 = new User();

      // change the name but forget about the username
      testUser2.setPassword("testPassword");
      testUser2.setUsername("testUsername2");
      testUser2.setBirthday("testBirthday2");
      testUser2.setCreation_date("testCreation_date");
      // check that an error is thrown
      User updatedUser = userService.updateUser(createdUser.getId(), testUser2);
      assertEquals(testUser.getUsername(), createdUser.getUsername());
      assertEquals(testUser.getBirthday(), createdUser.getBirthday());

  }

  @Test
  public void updateUser_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      testUser.setBirthday("testBirthday");
      testUser.setCreation_date("testCreation_date");
      User createdUser1 = userService.createUser(testUser);

      User testUser2 = new User();

      // change the name but forget about the username
      testUser2.setPassword("testPassword");
      testUser2.setUsername("testUsername2");
      testUser2.setBirthday("testBirthday2");
      testUser2.setCreation_date("testCreation_date");
      User createdUser2 = userService.createUser(testUser2);

      User testUser3 = new User();

      testUser3.setPassword("testPassword");
      testUser3.setUsername("testUsername2");
      testUser3.setBirthday("testBirthday");
      testUser3.setCreation_date("testCreation_date");
      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> userService.updateUser(createdUser1.getId(), testUser3));
  }

    @Test
    public void updateUserStatus_validInputs_success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser.setBirthday("testBirthday");
        testUser.setCreation_date("testCreation_date");
        User createdUser = userService.createUser(testUser);
        assertNotNull(userRepository.findByUsername("testUsername"));
        // attempt to create second user with same username
        User testUser2 = new User();

        // change the name but forget about the username
        testUser2.setLogged_in(false);
        testUser2.setStatus(UserStatus.OFFLINE);
        // check that an error is thrown
        User updatedUser = userService.updateUserStatus(createdUser.getId(), testUser2);
        assertEquals(false, updatedUser.getLogged_in());
        assertEquals(UserStatus.OFFLINE, updatedUser.getStatus());

    }

  @Test
  public void loginUser_validInputs_success() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      testUser.setBirthday("testBirthday");
      testUser.setCreation_date("testCreation_date");
      User createdUser = userService.createUser(testUser);
      assertNotNull(userRepository.findByUsername("testUsername"));
      // attempt to create second user with same username
      User testUser2 = new User();

      // change the name but forget about the username

      testUser2.setUsername("testUsername");
      testUser2.setPassword("testPassword");

      // check that an error is thrown
      User existedUser = userService.checkUserLogin(testUser2);
      assertEquals(testUser.getUsername(), existedUser.getUsername());
      assertEquals(testUser.getBirthday(), existedUser.getBirthday());
      assertEquals(UserStatus.ONLINE, existedUser.getStatus());
      assertEquals(true, existedUser.getLogged_in());
  }

  @Test
  public void loginUser_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      testUser.setBirthday("testBirthday");
      testUser.setCreation_date("testCreation_date");
      User createdUser = userService.createUser(testUser);
      assertNotNull(userRepository.findByUsername("testUsername"));
      // attempt to create second user with same username
      User testUser2 = new User();
      User testUser3 = new User();
      // change the name but forget about the username

      testUser2.setUsername("testUsername1");
      testUser2.setPassword("testPassword");

      testUser3.setUsername("testUsername");
      testUser3.setPassword("testPassword1");

      // check that an error is thrown

      assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(testUser2));
      assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(testUser3));
  }

}



