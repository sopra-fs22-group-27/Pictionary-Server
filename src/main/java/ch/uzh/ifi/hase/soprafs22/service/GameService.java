package ch.uzh.ifi.hase.soprafs22.service;
import java.util.*;
import java.util.stream.Collectors;
import ch.uzh.ifi.hase.soprafs22.entity.GameRound;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.GameRepository;

@Service
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private UserService userService;
    private GameRoundService gameRoundService;
    private final UserRepository userRepository;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository , UserService userService, GameRoundService gameRoundService, @Qualifier("userRepository") UserRepository userRepository ) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.gameRoundService = gameRoundService;
        this.userRepository = userRepository;
    }
    
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    public Game getGameById(String id) {
        return this.gameRepository.findById(id);
    }    

    public void deleteGameByToken(String token) {
        this.gameRepository.deleteByGameToken(token);
    }

    public Game getGameByToken(String token) {
        Game game = gameRepository.findByGameToken(token);
        if (game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        return game;
    }
    
    public Game createGame (String userToken, Game newGame) {
        User user = userService.getUserByToken(userToken);
        newGame.addUserToIntegerMap(user);
        newGame.setGameToken(UUID.randomUUID().toString());

        newGame.setGameRoundList(new ArrayList<GameRound>());
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        return newGame;
    }

    public Game addPlayerToGame (String gameToken, String userToken, Game gameInput) {
        Game game = this.gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        if(!game.getIsPublic()) {
            // String password = gameInput.getPassword();
            System.out.println(gameInput.getPassword());
            System.out.println(game.getPassword());
            if(gameInput.getPassword() == ""){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Please input a valid password, your input is empty");
            }
            if(!gameInput.getPassword().equals(game.getPassword())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The password was wrong with this GameToken");
            }
        }
        User user = userService.getUserByToken(userToken);
        //String[] currentPlayers = game.getPlayerTokens();

        Map<User, Integer> userMap = game.getUserToIntegerMap();
        if (game.getGameStatus().equals("started") || game.getGameStatus().equals("finished")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The game has already started or is finished");
        } else if (userMap.containsKey(user)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The user is already in the game");
        } else if (userMap.size() == game.getNumberOfPlayersRequired()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The game is already full");
        } else if (game.getGameName() == null || user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The game or user does not exist");
        } else {
            //add userToken to currentPlayers
            //String[] newPlayers = Arrays.copyOf(currentPlayers, currentPlayers.length + 1);
            //newPlayers[currentPlayers.length] = userToken;
            //game.setPlayerTokens(newPlayers);
            game.addUserToIntegerMap(user);
            game.setNumberOfPlayers(game.getNumberOfPlayers() + 1);
            gameRepository.flush();
            return game;
        }
    }

    public void updateImg(String gameToken, String img){
        Game game = gameRepository.findByGameToken(gameToken);
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        currentGameRound.setImg(img);
        gameRepository.save(game);
        gameRepository.flush();
    }

    public String getImage(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        String img = currentGameRound.getImg();
        return img;
    }

    public void changeWord(String gameToken, String word){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound());
        //game.setCurrentGameRound(game.getCurrentGameRound() + 1); //next round
        currentGameRound.setWord(word);
        //Getting the current date
        Date date = new Date();
        //This method returns the time in millis
        long startTime = date.getTime();
        currentGameRound.setRoundStartingTime(startTime);
    }

    public Boolean getResultOfGuess(String gameToken, String userToken, String guessedWord){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        GameRound currentGameRound = game.getGameRoundList().get(game.getCurrentGameRound() - 1);
        if(Objects.equals(currentGameRound.getWord(), guessedWord)){
            User correctGuessUser = userRepository.findByToken(userToken);
            if(gameRoundService.checkIfUserAlreadyGuessed(currentGameRound, correctGuessUser)){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User already guessed correctly");
            }
            else{
                game.updatePointsUserMap(correctGuessUser, 10);
                gameRoundService.updateCorrectGuess(gameToken, userToken);
                gameRepository.save(game);
                gameRepository.flush();
                return true;
            }
        }
        else{
            return false;
        }
    }

    public Boolean isGameFull(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        if(game.getNumberOfPlayers() == game.getNumberOfPlayersRequired()){
            if (game.getGameRoundList().isEmpty()){
                ArrayList<String> stringList = new ArrayList<>();
                Map<User, Integer> userMap = game.getUserToIntegerMap();
                for (User user : userMap.keySet()) {
                    stringList.add(user.getToken());
                }
                String[] stringArray = stringList.toArray(new String[0]);
                game.setGameRoundList(gameRoundService.createGameRounds(game.getNumberOfRounds(), stringArray));
                game.setCurrentGameRound(0);
                game.setGameStatus("started");
            }
            return true;
        }
        else{
            return false;
        }
    }

    public void changeGameRound(String gameToken){
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        if(game.getCurrentGameRound()==game.getNumberOfRounds()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not more GameRounds");
        }
        int newGameRound = game.getCurrentGameRound() + 1;
        System.out.println(game.getCurrentGameRound());
        game.setCurrentGameRound(newGameRound); //next round
    }
    public Map<User, Integer> getGameScoreBoard(String gameToken){
        Game game = getGameByToken(gameToken);
        return game.getGameScoreBoard();

         /**if(currentGameRound.getWinner()==null){ //Only the first one get the points
         currentGameRound.setWinner(userToken);
         User winner = userRepository.findByToken(userToken);
         int newRanking_points = winner.getRanking_points()+10;
         winner.setRanking_points(newRanking_points);
         userRepository.save(winner);
         userRepository.flush();
         }*/
    }



    public List<Game> getJoinableGames() {
        List<Game> allGames = this.gameRepository.findAll();
        List<Game> joinableGames = allGames.stream().filter(game -> game.getNumberOfPlayers() < game.getNumberOfPlayersRequired()).collect(Collectors.toList());;
        return joinableGames;
    }

    public void finishGame(String gameToken) {
        Game game = gameRepository.findByGameToken(gameToken);
        if(game == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The Game was not found with this GameToken");
        }
        game.setGameStatus("finished"); // end the game
    }
}
