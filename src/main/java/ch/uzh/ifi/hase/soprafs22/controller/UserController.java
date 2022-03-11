package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
//import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    //  @GetMapping(path = "/users/{id}")
//  public User findUserById(@PathVariable("id") Long id){
//      List<User> users = userService.getUsers();
//      return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
//  }
//    @GetMapping(path = "/users/{id}/**")
//    public void unauthorizedGet(@PathVariable("id") Long id) {
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are unauthorized to get the resource.");
//    }

    @GetMapping(path = "/users/{id}")
    public User findUserById(@PathVariable("id") Long id){
        User user = userService.getUserById(id);
        if(user != null){
            return user;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The resource is not found.");
        }

    }

    @PutMapping(path = "/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable("id") Long id, @RequestBody UserPostDTO userPostDTO){
//        User user = userService.getUserById(id);

        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User updatedUser = userService.updateUser(id, userInput);

//        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
    }

//    @PutMapping(path = "/users/{id}/**")
//    @ResponseBody
//    public void unauthorizedPut(@PathVariable("id") Long id, @RequestBody UserPostDTO userPostDTO) {
//        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are unauthorized to get the resource.");
//    }

    @PutMapping(path = "/status/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO updateUserStatus(@PathVariable("id") Long id, @RequestBody UserPostDTO userPostDTO){
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User updatedUser = userService.updateUserStatus(id, userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
    }


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO checkUserLogin(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User user = userService.checkUserLogin(userInput);

        // create user
//        User createdUser = userService.createUser(userInput);
////        return userInput;
//        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
    }

}
