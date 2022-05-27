![Pictionary_logo](https://user-images.githubusercontent.com/45404885/168472257-b2a7ea0e-91d3-4141-81be-97ef16add954.png)


## Information about the Project: 

https://pictionary-server-22.herokuapp.com/


## Current project status
| [![Deploy Project](https://github.com/sopra-fs22-group-27/Pictionary-Server/actions/workflows/deploy.yml/badge.svg)](https://github.com/sopra-fs22-group-27/Pictionary-Server/actions/workflows/deploy.yml)    | [![Test Project](https://github.com/sopra-fs22-group-27/Pictionary-Server/actions/workflows/pr.yml/badge.svg)](https://github.com/sopra-fs22-group-27/Pictionary-Server/actions/workflows/pr.yml)                |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs22-group-27_Pictionary-Server&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sopra-fs22-group-27_Pictionary-Server) | [![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=sopra-fs22-group-27_Pictionary-Server&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=sopra-fs22-group-27_Pictionary-Server) |

## Introduction: 
Pictionary is an interactive drawing game. The rules of this game are amazingly simple. A game consists of a custom number of rounds where someone must draw an offered word and the other players must guess it. If someone guesses the word correctly, the player gets 10 points. The player with the most points at the end wins the game and receives ranking points. One difference between our application and other Pictionary games is that the drawer is also able to get points. This is possible because we use the power of Google Vision AI which recognizes what the player is drawing.

## Technologies
Cloudinary,
Spring,
SonarQube,
React,
npm,
JSX,
Java,
Java Persistence,
heroku,
gradle,
GitHub Projects,
GitHub Actions

## High-level components

[GameController](https://github.com/sopra-fs22-group-27/Pictionary-Server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/GameController.java)
The game controller is critical to the app's functionality because it handles practically all API requests performed during the game. It, for example, manages the game's capacity. It determines if a game is already full or whether a user can join.

[UserController](https://github.com/sopra-fs22-group-27/Pictionary-Server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/controller/UserController.java)
Almost all API calls involving the user are handled by the user controller. It, for example, manages the api calls during the user registration procedure.

[UserService](https://github.com/sopra-fs22-group-27/Pictionary-Server/blob/master/src/main/java/ch/uzh/ifi/hase/soprafs22/service/UserService.java) 
The user service is in charge of a wide range of functions. It, for example, checks whether the user's email address already exists and sends an error message if it does.

## Launch & Deployment

You can use the local Gradle Wrapper to build the application.

Plattform-Prefix:

-   MAC OS X: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

More Information about [Gradle Wrapper](https://docs.gradle.org/current/Userguide/gradle_wrapper.html) and [Gradle](https://gradle.org/docs/).

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```


## API Endpoint Testing

### Postman

-   We highly recommend to use [Postman](https://www.getpostman.com) in order to test your API Endpoints.

## Roadmap

- Add a different game mode where the users can play some kind of drinking game.
- Create shapes which could be used by drag & drop during drawing.
- Make the application more secure.

## Acknowledgements

This project was started using this template -> [Server](https://github.com/HASEL-UZH/sopra-fs22-template-server)

## Team Members

- [Rafael Dubach](https://github.com/radubauzh)
- [Raphael Wäspi](https://github.com/sumsumcity)
- [Dylan Baumgartner](https://github.com/mrspacerobot)
- [Shaoyan Li](https://github.com/SyLi9527)
- [Solveig Helland](https://github.com/hellasol)

## License

[MIT license](https://github.com/sopra-fs22-group-27/Pictionary-Server/blob/master/LICENSE)
