package serviceTests;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import handler.request.GameCreateRequest;
import handler.request.GameJoinRequest;
import handler.response.GameCreateResponse;
import handler.response.GameListResponse;
import handler.response.ResponseContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.sql.SQLException;


class GameServiceTest {
    private static DataAccess dataAccess;
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static GameService gameService;


    @BeforeAll
    public static void init() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        gameService = new GameService(dataAccess);
        gameDAO = dataAccess.getGameDAO();
        authDAO = dataAccess.getAuthDAO();
    }

    @Test
    void listGamesGoodRequest() throws DataAccessException, SQLException {
        String authToken = authDAO.createAuth("username1");
        Integer gameID = gameDAO.createGame("gameName1");

        GameListResponse gameListResponse = gameService.listGames(authToken);
        Assertions.assertNull(gameListResponse.getMessage(), "listGames() populated an error message");
    }

    @Test
    void listGamesBadRequest() throws DataAccessException {
        Integer gameID = gameDAO.createGame("gameName1");

        GameListResponse gameListResponse = gameService.listGames("FAKEAUTHTOKEN");
        Assertions.assertNotNull(gameListResponse.getMessage(), "listGames() should have populated an error message");
    }

    @Test
    void createGameGoodRequest() throws DataAccessException, SQLException {
        String authToken = authDAO.createAuth("username1");
        GameCreateRequest gameCreateRequest = new GameCreateRequest("GAME1");

        GameCreateResponse gameCreateResponse = gameService.createGame(authToken, gameCreateRequest);
        Assertions.assertNull(gameCreateResponse.getMessage(), "createGame populated an error message");
    }

    @Test
    void createGameBadRequest() throws DataAccessException {
        GameCreateRequest gameCreateRequest = new GameCreateRequest("GAME1");

        GameCreateResponse gameCreateResponse = gameService.createGame("FAKEAUTHTOKEN", gameCreateRequest);
        Assertions.assertNotNull(gameCreateResponse.getMessage(), "createGame did not populate an error message");
    }


    @Test
    void joinGameGoodRequest() throws DataAccessException, SQLException {
        String authToken = authDAO.createAuth("username1");
        Integer gameID = gameDAO.createGame("GAME1");

        GameJoinRequest gameJoinRequest = new GameJoinRequest(ChessGame.TeamColor.WHITE, gameID);
        ResponseContainer responseContainer = gameService.joinGame(authToken, gameJoinRequest);
        Assertions.assertNull(responseContainer.getMessage(), "joinGame populated an error message");
    }

    @Test
    void joinGameBadRequest() throws DataAccessException {
        Integer gameID = gameDAO.createGame("GAME1");

        GameJoinRequest gameJoinRequest = new GameJoinRequest(ChessGame.TeamColor.WHITE, gameID);
        ResponseContainer responseContainer = gameService.joinGame("FAKEAUTHTOKEN", gameJoinRequest);
        Assertions.assertNotNull(responseContainer.getMessage(), "joinGame did not populate an error message");
    }
}