package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.GameCreateRequest;
import handler.request.GameJoinRequest;
import handler.response.GameCreateResponse;
import handler.response.GameJoinResponse;
import handler.response.GameListResponse;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class GameHandler extends Handler {
    GameService service;
    public GameHandler(DataAccess dataAccess) {
        super(dataAccess);
        this.service = new GameService(dataAccess);
    }

    public GameListResponse handleGameList(String authToken, Response response) throws DataAccessException {
        GameListResponse gameListResponse = service.listGames(authToken);

        if (gameListResponse != null && Objects.equals(gameListResponse.getMessage(), null)) {
            response.status(200);
        } else if (gameListResponse != null && Objects.equals(gameListResponse.getMessage(), "Error: unauthorized")) {
            response.status(401);
        } else {
            response.status(500);
        }

        return gameListResponse;
    }

    public GameCreateResponse handleGameCreate(String authToken, Request request, Response response) throws DataAccessException {
        GameCreateRequest gameCreateRequest = new Gson().fromJson(request.body(), GameCreateRequest.class);

        GameCreateResponse gameCreateResponse = service.createGame(authToken, gameCreateRequest);
        if (gameCreateResponse != null && Objects.equals(gameCreateResponse.getMessage(), null)) {
            response.status(200);
        } else if (gameCreateResponse != null && Objects.equals(gameCreateResponse.getMessage(), "Error: bad request")) {
            response.status(400);
        } else if (gameCreateResponse != null && Objects.equals(gameCreateResponse.getMessage(), "Error: unauthorized")) {
            response.status(401);
        } else if (gameCreateResponse != null && Objects.equals(gameCreateResponse.getMessage(), "Error: already taken")) {
                response.status(403);
        } else {
            response.status(500);
        }

        return gameCreateResponse;
    }


    public GameJoinResponse handleGameJoin(String authToken, Request request, Response response) throws DataAccessException {
        GameJoinRequest gameJoinRequest = new Gson().fromJson(request.body(), GameJoinRequest.class);

        GameJoinResponse gameJoinResponse = service.joinGame(authToken, gameJoinRequest);

        if (gameJoinResponse != null && Objects.equals(gameJoinResponse.getMessage(), null)) {
            response.status(200);
        } else if (gameJoinResponse != null && Objects.equals(gameJoinResponse.getMessage(), "Error: bad request")) {
            response.status(400);
        } else if (gameJoinResponse != null && Objects.equals(gameJoinResponse.getMessage(), "Error: unauthorized")) {
            response.status(401);
        } else if (gameJoinResponse != null && Objects.equals(gameJoinResponse.getMessage(), "Error: already taken")) {
            response.status(403);
        } else {
            response.status(500);
        }

        return gameJoinResponse;
    }
}
