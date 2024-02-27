package service;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import handler.request.GameCreateRequest;
import handler.request.GameJoinRequest;
import handler.response.GameCreateResponse;
import handler.response.GameJoinResponse;
import handler.response.GameListResponse;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GameService {
    private DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public GameListResponse listGames(String authToken) throws DataAccessException {
        AuthDAO authDAO = dataAccess.getAuthDAO();

        if (authDAO.in(authToken) == null) {
            return new GameListResponse(null,"Error: unauthorized");
        }

        GameDAO gameDAO = dataAccess.getGameDAO();
        HashMap<Integer, GameData> gamesMap = gameDAO.listGames();
        ArrayList<GameData> gameList = new ArrayList<>();

        for (Integer key : gamesMap.keySet()) {
            gameList.add(gamesMap.get(key));
        }

        return new GameListResponse(gameList, null);
    }

    public GameCreateResponse createGame(String authToken, GameCreateRequest gameCreateRequest) throws DataAccessException {
        AuthDAO authDAO = dataAccess.getAuthDAO();
        GameDAO gameDAO = dataAccess.getGameDAO();
        String gameName = gameCreateRequest.getGameName();

        Integer gameID = gameDAO.createGame(gameName);

        if (authDAO.in(authToken) == null) {
            return new GameCreateResponse(null,"Error: unauthorized");
        }


        return new GameCreateResponse(gameID, null);
    }

    public GameJoinResponse joinGame(String authToken, GameJoinRequest gameJoinRequest) throws DataAccessException {
        AuthDAO authDAO = dataAccess.getAuthDAO();
        GameDAO gameDAO = dataAccess.getGameDAO();
        Integer gameID = gameJoinRequest.getGameID();
        ChessGame.TeamColor playerColor = gameJoinRequest.getPlayerColor();
        String username = authDAO.getUsername(authToken);


        if (authDAO.in(authToken) == null) {
            return new GameJoinResponse("Error: unauthorized");
        }

        try {
            gameDAO.updateGame(gameID, playerColor, username);
        } catch (DataAccessException ex){
            return new GameJoinResponse(ex.getMessage());
        }


        return new GameJoinResponse(null);

    }
}
