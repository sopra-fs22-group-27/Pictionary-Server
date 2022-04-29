package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("gameRoundRepository")
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    GameRound findByGameToken(String gameToken);
    GameRound findById(String id);
}