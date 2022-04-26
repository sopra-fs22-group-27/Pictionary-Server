package ch.uzh.ifi.hase.soprafs22.repository;
import ch.uzh.ifi.hase.soprafs22.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import ch.uzh.ifi.hase.soprafs22.entity.Game;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
        Game findById(String gameToken);

        Game findByGameToken(String gameToken);

        List<Game> findByGameName(String gameName);

}   
