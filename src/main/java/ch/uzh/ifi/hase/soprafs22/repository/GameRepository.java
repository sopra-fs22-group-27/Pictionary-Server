package ch.uzh.ifi.hase.soprafs22.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs22.entity.Game;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<Game, Long> {
        // Game findByToken(String gameToken);

        Game findById(String id);
}   
