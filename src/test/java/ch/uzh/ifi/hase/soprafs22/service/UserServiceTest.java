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
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  private User testUser2;

  private User testUser3;

  private User testUser4;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("testPassword");
    testUser.setUsername("testUsername");
    testUser.setCreation_date("testCreation_date");
    testUser.setBirthday("testBirthday");
    testUser.setLogged_in(true);
    testUser.setToken("1");
    testUser.setStatus(UserStatus.ONLINE);

    testUser2 = new User();
    testUser2.setId(1L);
    testUser2.setPassword("testPassword");
    testUser2.setUsername("testUsername2");
    testUser2.setCreation_date("testCreation_date");
    testUser2.setBirthday("testBirthday2");
    testUser2.setLogged_in(true);
    testUser2.setToken("1");
    testUser2.setStatus(UserStatus.ONLINE);

    testUser3 = new User();
    testUser3.setId(3L);
    testUser3.setPassword("testPassword");
    testUser3.setUsername("testUsername3");
    testUser3.setCreation_date("testCreation_date");
    testUser3.setBirthday("testBirthday3");
    testUser3.setLogged_in(true);
    testUser3.setToken("1");
    testUser3.setStatus(UserStatus.ONLINE);

    testUser4 = new User();
    testUser4.setId(1L);
    testUser4.setPassword("testPassword");
    testUser4.setUsername("testUsername");
    testUser4.setCreation_date("testCreation_date");
    testUser4.setBirthday("testBirthday");
    testUser4.setLogged_in(true);
    testUser4.setToken("1");
    testUser4.setStatus(UserStatus.OFFLINE);

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void getUsers_success(){
      User createdUser = userService.createUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
      List<User> allUsers = Collections.singletonList(createdUser);
      Mockito.when(userRepository.findAll()).thenReturn(allUsers);
      List<User> Users = userRepository.findAll();
      assertThat(Users)
          .hasSize(1)
          .extracting(User::getUsername)
          .contains(testUser.getUsername());

  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
    assertEquals(testUser.getBirthday(), createdUser.getBirthday());
    assertEquals(testUser.getLogged_in(), createdUser.getLogged_in());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    // when -> setup additional mocks for UserRepository
//    Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
//    Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);
    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));

  }

  @Test
  public void updateUser_validInputs_success() {
      // given -> a first user has already been created
      User user  = userService.createUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(user);
      User createdUser = userService.updateUser(user.getId(), testUser2);
      Mockito.verify(userRepository, Mockito.times(2)).flush();
      // when -> setup additional mocks for UserRepository
//   Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
//      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(createdUser);
//   Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);
      // then -> attempt to create second user with same user -> check that an error
      // is thrown
      assertEquals(testUser.getId(), createdUser.getId());
      assertEquals(testUser.getPassword(), createdUser.getPassword());
      assertEquals(testUser2.getUsername(), createdUser.getUsername());
      assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
      assertEquals(testUser2.getBirthday(), createdUser.getBirthday());
      assertEquals(testUser.getLogged_in(), createdUser.getLogged_in());
      assertNotNull(createdUser.getToken());
      assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }



  @Test
  public void updateUser_duplicateUsername_throwsException() {
      // given -> a first user has already been created
      userService.createUser(testUser);
      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(testUser3);
      Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
      // when -> setup additional mocks for UserRepository
//   Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
//      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(createdUser);
//   Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);
        // then -> attempt to create second user with same user -> check that an error
        // is thrown
      assertEquals("testUsername", testUser.getUsername());
      assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser3.getId(), testUser));
  }

    @Test
    public void updateUserStatus_validInputs_success() {
        // given -> a first user has already been created
        userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(testUser);
        User createdUser = userService.updateUserStatus(testUser.getId(), testUser4);
        Mockito.verify(userRepository, Mockito.times(2)).flush();
        // when -> setup additional mocks for UserRepository
//   Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
//      Mockito.when(userRepository.findUserById(Mockito.any())).thenReturn(createdUser);
//   Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);
        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
        assertEquals(testUser.getBirthday(), createdUser.getBirthday());
        assertEquals(testUser4.getLogged_in(), createdUser.getLogged_in());
        assertNotNull(createdUser.getToken());
        assertEquals(testUser4.getStatus(), createdUser.getStatus());
    }

  @Test
  public void loginUser_validInputs_success() {
      // given -> a first user has already been created
//      userService.createUser(testUser);
//      Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
      List<User> allUsers = new ArrayList<User>();
      allUsers.add(testUser);
      allUsers.add(testUser3);
      Mockito.when(userRepository.findAll()).thenReturn(allUsers);
      User user1 = userService.checkUserLogin(testUser);
//      assertEquals(user1.getId(), testUser.getId());
      assertEquals(user1.getPassword(), testUser.getPassword());
      assertEquals(user1.getUsername(), testUser.getUsername());
//      assertEquals(user1.getCreation_date(), testUser.getCreation_date());
//      assertEquals(user1.getBirthday(), testUser.getBirthday());
//      assertEquals(user1.getLogged_in(), testUser.getLogged_in());
//      assertNotNull(user1.getToken());
//      assertEquals(UserStatus.ONLINE, testUser.getStatus());

      User user2 = userService.checkUserLogin(testUser3);
//      assertEquals(user2.getId(), testUser3.getId());
      assertEquals(user2.getPassword(), testUser3.getPassword());
      assertEquals(user2.getUsername(), testUser3.getUsername());
//      assertEquals(user2.getCreation_date(), testUser3.getCreation_date());
//      assertEquals(user2.getBirthday(), testUser3.getBirthday());
//      assertEquals(user2.getLogged_in(), testUser3.getLogged_in());
//      assertNotNull(user2.getToken());
//      assertEquals(UserStatus.ONLINE, testUser3.getStatus());
//      assertThat(allUsers)
//              .hasSize(allUsers.size())
//              .extracting(User::getUsername, User::getPassword)
//              .contains(tuple("testUsername3", "testPassword")
//              );
  }

  @Test
  public void loginUser_duplicateUsername_throwsException() {
      // given -> a first user has already been created
      // given -> a first user has already been created
      List<User> allUsers = new ArrayList<User>();
      Mockito.when(userRepository.findAll()).thenReturn(allUsers);
      assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(testUser));

      allUsers.add(testUser);
      allUsers.add(testUser3);
      Mockito.when(userRepository.findAll()).thenReturn(allUsers);
      assertThrows(ResponseStatusException.class, () -> userService.checkUserLogin(testUser2));

  }
//?
  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByPassword(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

}
