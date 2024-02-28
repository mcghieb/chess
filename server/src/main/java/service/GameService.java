package service;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.GameDAO;
import handler.request.GameCreateRequest;
import handler.request.GameJoinRequest;
import handler.response.GameCreateResponse;

import handler.response.GameListResponse;
import handler.response.ResponseContainer;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameService {
    private GameDAO gameDAO;
    private AuthService authService;

    public GameService(DataAccess dataAccess) throws DataAccessException {
        this.gameDAO = dataAccess.getGameDAO();
        this.authService = new AuthService(dataAccess);
    }

    public GameListResponse listGames(String authToken) throws DataAccessException {
        if (authService.authenticate(authToken)) {
            return new GameListResponse(null,"Error: unauthorized");
        }

        HashMap<Integer, GameData> gamesMap = gameDAO.listGames();
        ArrayList<GameData> gameList = new ArrayList<>();

        for (Integer key : gamesMap.keySet()) {
            gameList.add(gamesMap.get(key));
        }

        return new GameListResponse(gameList, null);
    }

    public GameCreateResponse createGame(String authToken, GameCreateRequest gameCreateRequest) throws DataAccessException {
        String gameName = gameCreateRequest.getGameName();

        Integer gameID = gameDAO.createGame(gameName);

        if (authService.authenticate(authToken)) {
            return new GameCreateResponse(null,"Error: unauthorized");
        }

        return new GameCreateResponse(gameID, null);
    }

    public ResponseContainer joinGame(String authToken, GameJoinRequest gameJoinRequest) throws DataAccessException {
        Integer gameID = gameJoinRequest.getGameID();
        ChessGame.TeamColor playerColor = gameJoinRequest.getPlayerColor();
        String username = authService.getUsername(authToken);


        if (authService.authenticate(authToken)) {
            return new ResponseContainer("Error: unauthorized");
        }

        try {
            gameDAO.updateGame(gameID, playerColor, username);
        } catch (DataAccessException ex){
            return new ResponseContainer(ex.getMessage());
        }


        return new ResponseContainer(null);

    }
}
