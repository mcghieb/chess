package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import handler.request.GameCreateRequest;
import handler.response.GameCreateResponse;
import handler.response.GameListResponse;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

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
}
