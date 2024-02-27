package handler;

import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import handler.request.GameCreateRequest;
import handler.request.GameJoinRequest;
import handler.response.GameCreateResponse;
import handler.response.GameListResponse;
import handler.response.ResponseContainer;
import service.GameService;
import spark.Request;


public class GameHandler extends Handler {
    GameService service;
    public GameHandler(DataAccess dataAccess) throws DataAccessException {
        super(dataAccess);
        this.service = new GameService(dataAccess);
    }

    public GameListResponse handleGameList(String authToken) throws DataAccessException {
        return service.listGames(authToken);
    }

    public GameCreateResponse handleGameCreate(String authToken, Request request) throws DataAccessException {
        GameCreateRequest gameCreateRequest = new Gson().fromJson(request.body(), GameCreateRequest.class);

        GameCreateResponse gameCreateResponse = service.createGame(authToken, gameCreateRequest);

        return gameCreateResponse;
    }


    public ResponseContainer handleGameJoin(String authToken, Request request) throws DataAccessException {
        GameJoinRequest gameJoinRequest = new Gson().fromJson(request.body(), GameJoinRequest.class);

        return service.joinGame(authToken, gameJoinRequest);
    }
}
