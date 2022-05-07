package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.*;
import ch.uzh.ifi.hase.soprafs22.rest.dto.*;
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


    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayersRequired", target = "numberOfPlayersRequired")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "roundLength", target = "roundLength")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "gameStatus", target = "gameStatus")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "gameName", target = "gameName")
    @Mapping(source = "numberOfPlayersRequired", target = "numberOfPlayersRequired")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "roundLength", target = "roundLength")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "gameToken", target = "gameToken")
    @Mapping(source = "userToIntegerMap", target = "userToIntegerMap")
    @Mapping(source = "currentGameRound", target= "currentGameRound")
    GameGetDTO convertEntityToGameGetDTO(Game gamePostDTO);

    @Mapping(source = "img", target = "img")
    Img convertGamePutDTOToImg(GamePutDTO gamePutDTO);

    @Mapping(source = "word", target = "word")
    @Mapping(source = "winner", target = "winner")
    @Mapping(source = "roundStartingTime", target = "roundStartingTime")
    @Mapping(source = "drawer", target = "drawerToken")
    GameRoundGetDTO convertEntityToGameRoundGetDTO(GameRound gameRound);

}
