package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUsername_success() {
    // given
    User user = new User();
    user.setPassword("password");
    user.setUsername("firstname@lastname");
    user.setCreation_date("creation_date");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setLogged_in(true);
    user.setBirthday("birthday");

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getCreation_date(), user.getCreation_date());
    assertEquals(found.getLogged_in(), user.getLogged_in());
    assertEquals(found.getBirthday(), user.getBirthday());
  }
    @Test
    public void findById_success() {
        // given
        User user = new User();
        user.setPassword("password");
        user.setUsername("firstname@lastname");
        user.setCreation_date("creation_date");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("1");
        user.setLogged_in(true);
        user.setBirthday("birthday");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findUserById(user.getId());

        // then
        assertEquals(found.getId(), user.getId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getCreation_date(), user.getCreation_date());
        assertEquals(found.getLogged_in(), user.getLogged_in());
        assertEquals(found.getBirthday(), user.getBirthday());
    }

}
