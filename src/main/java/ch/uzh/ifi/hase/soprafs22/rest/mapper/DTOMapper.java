package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "email", target = "email")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    // do not map password
    //  @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creation_date", target = "creation_date")
    @Mapping(source = "email", target = "email")
    // @Mapping(source = "logged_in", target = "logged_in")
    @Mapping(source = "token", target = "token")
    @Mapping(source = "isInLobby", target = "isInLobby")
    UserGetDTO convertEntityToUserGetDTO(User user);


    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "gameLength", target = "gameLength")
    @Mapping(source = "isPublic", target = "isPublic")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "isPublic", target = "isPublic")
    @Mapping(source = "isInGame", target = "isInGame")
    @Mapping(source = "host", target = "host")
    @Mapping(source = "gameLength", target = "gameLength")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayersRequired", target = "numberOfPlayersRequired")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "roundLength", target = "roundLength")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "playerTokens", target = "playerTokens")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayersRequired", target = "numberOfPlayersRequired")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "roundLength", target = "roundLength")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "gameToken", target = "gameToken")
    @Mapping(source = "playerTokens", target = "playerTokens")
    GameGetDTO convertEntityToGameGetDTO(Game gamePostDTO);
}
