package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LobbyController {
    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    /**
     * Create a Lobby with User Token. Adds User as Host.
     *
     * @param userToken
     * @param lobbyPostDTO
     * @return LobbyGetDTO
     */
    @PostMapping("/lobby/{userToken}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@PathVariable String userToken, @RequestBody LobbyPostDTO lobbyPostDTO) {
        // convert API user to internal representation
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // create lobby
        Lobby createdLobby = lobbyService.createLobby(lobby, userToken);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    /**
     * @return List of Lobbies
     */
    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getLobbies() {
        List<Lobby> lobbiesList = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbyGetDTOs = new ArrayList<>();
        // convert each user to the API representation
        for (Lobby lobby : lobbiesList) {
            lobbyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby));
        }
        return lobbyGetDTOs;
    }

    /**
     * Add User to an existing Lobby using user token
     * @param lobbyToken
     * @param userToken
     * @return LobbyGetDTO
     */
    @PutMapping("/lobby/{lobbyToken}/user/{userToken}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO addUserToLobby(@PathVariable("lobbyToken") String lobbyToken, @PathVariable("userToken") String userToken) {
        Lobby lobby = lobbyService.addUserToLobby(lobbyToken, userToken);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
    }

    /**
     * Delete user from an existing Lobby using user token. However, if the user is the host, close the lobby
     * @param lobbyToken
     * @param userToken
     */
    @DeleteMapping("/lobby/{lobbyToken}/user/{userToken}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserFromLobby(@PathVariable("lobbyToken") String lobbyToken, @PathVariable("userToken") String userToken) {
        lobbyService.removeUserFromLobby(lobbyToken, userToken);
    }
}
