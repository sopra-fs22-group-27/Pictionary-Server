package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Game")
public class Game {

    @Id
    @GeneratedValue
    private Long id;



}
