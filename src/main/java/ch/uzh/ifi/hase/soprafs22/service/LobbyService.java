package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
@Transactional
public class LobbyService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;

    private UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, @Qualifier("userRepository") UserRepository userRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public Lobby createLobby(Lobby newLobby, String userToken) throws ResponseStatusException {
        checkIfLobbyExists(newLobby);
        newLobby.setToken(UUID.randomUUID().toString());
        User user = userService.getUserByToken(userToken);

        // check if the user has already entered a room
        if(user.getIsInLobby()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user is already in a room, so he/she can't join in another room as a host.");
        }
        user.setIsInLobby(true);
        userRepository.flush();

        newLobby.setIsInGame(false);
        newLobby.setHost(user);
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        log.debug("Created Lobby: {}", newLobby);
        return newLobby;
    }

    public Lobby addUserToLobby(String lobbyToken, String userToken){
        Lobby lobby = getLobby(lobbyToken);
        User user = userService.getUserByToken(userToken);

        // check if the user has already entered a room
        if(user.getIsInLobby()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user is already in a room, so he/she can't join in another room as a non-host.");
        }
        user.setIsInLobby(true);
        userRepository.flush();

        if(lobby != null){
            lobby.addUserToLobbyUserList(user);
            return lobby;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby cannot be found.");
        }
    }

    public void removeUserFromLobby(String lobbyToken, String userToken){
        Lobby lobby = getLobby(lobbyToken);
        User user = userService.getUserByToken(userToken);
        List<User> users = lobby.getLobbyUserList();
        //check if user is the host
        if(lobby.getHost().getToken().equals(userToken)){
            // set isInLobby
            user.setIsInLobby(false);
            for(User joiner : users){
                joiner.setIsInLobby(false);
            }
            // delete the lobby
            lobbyRepository.delete(lobby);
            lobbyRepository.flush();
        }
        else if(users.isEmpty()){
            // no joiner except the host
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No joiner in the lobby so you can't remove any user.");
        }
        else if(checkIfUserInUserList(userToken, users)){
            // user is one of the joiners
            users.removeIf(joiner -> joiner.getToken().equals(userToken));
            user.setIsInLobby(false);
            lobby.setLobbyUserList(users);
        }
        else{
            // user is not in this lobby
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not in this lobby.");
        }
    }

    private Boolean checkIfUserInUserList(String userToken, List<User> userList){
        for(User joiner: userList){
            if(joiner.getToken().equals(userToken)){
                return true;
            }
        }
        return false;
    }

    private Lobby getLobby(String token){
        Lobby lobby = this.lobbyRepository.findByToken(token);
        if(lobby != null){
            return lobby;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby cannot be found.");
        }
    }

    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {

        Lobby lobbyByName = lobbyRepository.findByLobbyName(lobbyToBeCreated.getLobbyName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the Lobby could not be created!";

        if (lobbyByName != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "Lobby name", "is"));
        }
    }
}
