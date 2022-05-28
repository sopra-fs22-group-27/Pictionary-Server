package ch.uzh.ifi.hase.soprafs22.entity;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.service.GameRoundService;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private int numberOfPlayersRequired;

    @Column(nullable = false)
    private int numberOfPlayers;
    
    @Column(nullable = false)
    private int roundLength;

    @Column(nullable = false)
    private int numberOfRounds;

    @Column(nullable = false)
    private String gameStatus = "waiting"; //possile values: waiting, started, finished

    @Column(nullable = false, unique = true)
    private String gameToken;

    @OneToMany
    private List<GameRound> gameRoundList;

    @ElementCollection
    private Map<User, Integer> userToIntegerMap;

    @Column()
    private int currentGameRound;

    @Column()
    private Boolean isPublic;

    @Column()
    private String password = "";

    public Map<User, Integer> getUserToIntegerMap(){
        return this.userToIntegerMap;
    }

    public TreeMap<String, Integer> getScoreBoardMap() {
        TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>();
        for(Map.Entry<User, Integer> user : entriesSortedByValues((this.userToIntegerMap))) {
            treeMap.put(user.getKey().getUsername(), user.getValue());
        }
        return treeMap;
    }

    public void removeUserToIntegerMap(User user) {
        if(this.userToIntegerMap != null){
            this.userToIntegerMap.remove(user);
        }
        
    }

    public void addUserToIntegerMap(User user) {
        if(this.userToIntegerMap == null){
            this.userToIntegerMap = new HashMap<User, Integer>();
        }
        this.userToIntegerMap.put(user, 0);
    }

    static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public void updatePointsUserMap(User user, int points) {
       int currentPoints = this.userToIntegerMap.get(user);
       this.userToIntegerMap.put(user, points + currentPoints);
    }


     //Todo: implement scoreboard return function
    public Map<User, Integer> getGameScoreBoard(){
        return this.userToIntegerMap;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getNumberOfPlayersRequired() {
        return numberOfPlayersRequired;
    }

    public void setNumberOfPlayersRequired(int numberOfPlayersRequired) {
        this.numberOfPlayersRequired = numberOfPlayersRequired;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getRoundLength() {
        return roundLength;
    }

    public void setRoundLength(int roundLength) {
        this.roundLength = roundLength;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameToken() {
        return gameToken;
    }

    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    public List<GameRound> getGameRoundList() {
        return gameRoundList;
    }

    public void setGameRoundList(List<GameRound> gameRoundList) {
        this.gameRoundList = gameRoundList;
    }

    public int getCurrentGameRound() {
        return currentGameRound;
    }

    public void setCurrentGameRound(int currentGameRound) {
        this.currentGameRound = currentGameRound;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic){
        this.isPublic = isPublic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}