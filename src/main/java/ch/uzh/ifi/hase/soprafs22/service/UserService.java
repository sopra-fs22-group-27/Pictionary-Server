package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

//  public User updateUser(String username){
//      return userRepository.findByID(id);
//  }

  public User getUserById(Long id){
      return userRepository.findUserById(id);
  }

  public User updateUserStatus(Long id, User newUser){
      User oldUser = userRepository.findUserById(id);
      oldUser.setStatus(UserStatus.OFFLINE);
      oldUser.setLogged_in(newUser.getLogged_in());
      userRepository.flush();
      return oldUser;

  }
  public User updateUser(Long id, User newUser){
      User oldUser = userRepository.findUserById(id);
      if(oldUser == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The resource is not found.");
      }
      String newUsername = newUser.getUsername();
      String newBirthday = newUser.getBirthday();
      if(userRepository.findByUsername(newUsername) != null && !oldUser.getUsername().equals(newUsername)){
          throw new ResponseStatusException(HttpStatus.CONFLICT, "The username has been taken. Try to change another");
      }else{
          oldUser.setBirthday(newBirthday);
          oldUser.setUsername(newUsername);
          userRepository.flush();
          return oldUser;
      }

  }


  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    newUser.setLogged_in(true);
    checkIfUserExists(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
//    User userByPassword = userRepository.findByPassword(userToBeCreated.getPassword());

    String baseErrorMessage = "The %s has already been used. Therefore, the user could not be created!";
    if (userByUsername != null){
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage, "username"));
    }

//     else if (userByPassword != null) {
//      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "password", "is"));
//    }
  }
  public User checkUserLogin(@NotNull User userToLogin){
    List<User> users = getUsers();
    String username = userToLogin.getUsername();
    String password = userToLogin.getPassword();
    if(users.isEmpty()){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Database is empty since there is no user data stored.");
    }
    for (User user : users) {
        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
            user.setLogged_in(true);
            user.setStatus(UserStatus.ONLINE);
            return user;
        }
    }
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username or password is wrong, so you can't login.");


//    if (userByUsername != null && userByPassword != null) {
//        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, baseErrorMessage);
//    }else{
//        throw new ResponseStatusException(HttpStatus.OK, "login successfully");
//    }
  }

}
