package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
//import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("password");
    userPostDTO.setUsername("username");
    userPostDTO.setBirthday("birthday");
    userPostDTO.setCreation_date("creation_date");


    // MAP -> Create user
    User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getPassword(), user.getPassword());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
    assertEquals(userPostDTO.getBirthday(), user.getBirthday());
    assertEquals(userPostDTO.getCreation_date(), user.getCreation_date());

  }
//  @Test
//  public void testUpdateUser_fromUserPutDTO_toUser_success(){
//      UserPutDTO userPutDTO = new UserPutDTO();
//
//      UserPutDTO.setBirthday("birthday");
//      UserPutDTO.setUsername("username");
//
//      User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
//
//      assertEquals(userPutDTO.getBirthday(), user.getBirthday());
//      assertEquals(userPutDTO.getUsername(), user.getUsername());
//  }
  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setPassword("Firstname Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setId(1L);
    user.setBirthday("3/3 1009");
    user.setCreation_date("3/2 1009");
    user.setLogged_in(true);
    user.setToken("1329242");
    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
//    assertEquals(user.getPassword(), userGetDTO.getPassword());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getBirthday(), userGetDTO.getBirthday());
    assertEquals(user.getLogged_in(), userGetDTO.getLogged_in());
    assertEquals(user.getCreation_date(), userGetDTO.getCreation_date());
//    assertEquals(user.getToken(), userGetDTO.getToken());
  }
}
