package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.LobbyRepository;
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

@Service
@Transactional
public class LobbyService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final LobbyRepository lobbyRepository;

    private UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userService = userService;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public Lobby createLobby(Lobby newLobby, String userToken) throws ResponseStatusException {
        checkIfLobbyExists(newLobby);
        newLobby.setToken(UUID.randomUUID().toString());
        User user = userService.getUserByToken(userToken);
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
        if(lobby != null){
            lobby.addUserToLobbyUserList(user);
            return lobby;
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lobby cannot be found.");
        }
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
