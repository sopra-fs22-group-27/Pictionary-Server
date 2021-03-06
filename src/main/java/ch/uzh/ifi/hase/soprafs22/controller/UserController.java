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

import java.util.*;
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

    //Dont know if we need this request. Look at REST.docx in Teams
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
        userGetDTOs.sort(Comparator.comparing(UserGetDTO::getRanking_points).reversed());
        return userGetDTOs;
    }

    @GetMapping(path = "/users/{token}")
    public UserGetDTO findUserByToken(@PathVariable("token") String token){
        User user = userService.getUserByToken(token);
        if(user != null){
            return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The resource is not found.");
        }

    }

    @PutMapping(path = "/users/{token}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO updateUser(@PathVariable("token") String token, @RequestBody UserPostDTO userPostDTO){
//        User user = userService.getUserById(id);

        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User updatedUser = userService.updateUser(token, userInput);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
//        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
    }

    /**
     * Update UserStatus to Offline
     * @param token
     */
    @PutMapping(path = "/status/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUserStatus(@PathVariable("token") String token){
        userService.updateUserStatus(token);
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

    @DeleteMapping("/users/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("token") String token){
        User user = userService.getUserByToken(token);
        userService.deleteUser(user);
    }

    @PutMapping("synctime/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void syncActiveTime(@PathVariable("token") String token) {
        User user = userService.getUserByToken(token);
        userService.syncActiveTime(user);
    }

}
