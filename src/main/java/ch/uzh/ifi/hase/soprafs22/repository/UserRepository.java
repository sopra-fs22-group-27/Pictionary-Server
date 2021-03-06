package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
  User findByPassword(String name);

  User findByUsername(String username);

  User findUserById(Long id);

  User findByToken(String token);

  User findByEmail(String email);
}
