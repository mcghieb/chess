package dataAccess.sqldataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SQLGameDAO implements GameDAO {
    private HashMap<Integer, GameData> games;
    HashMap<Integer, String> observers;

    public SQLGameDAO(){
        observers = new HashMap<>();
    }

    public HashMap<Integer, GameData> listGames() {
        games = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select * from game";
            try (var sql = conn.prepareStatement(statement)) {
                ResultSet gameSet = sql.executeQuery();

                while (gameSet.next()) {
                    int gameID = gameSet.getInt("game_id");
                    String whiteUsername = gameSet.getString("white_username");
                    String blackUsername = gameSet.getString("black_username");
                    String gameName = gameSet.getString("game_name");
                    String gameString = gameSet.getString("game");
                    ChessGame game = new Gson().fromJson(gameString, new TypeToken<ChessGame>(){}.getType());

                    GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, game);

                    games.put(gameID, gameData);
                }

            }

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

        return games;
    }

    public Integer createGame(String gameName) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "insert into game (game_name, game) values (?,?);";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, gameName);

                ChessGame newChessGame = new ChessGame();
                String gameString = new Gson().toJson(newChessGame);
                sql.setString(2,gameString);

                sql.executeUpdate();
            }

            String statement2 = "select game_id from game where game_name = ?";
            try (var sql = conn.prepareStatement(statement2)) {
                sql.setString(1, gameName);

                return Integer.valueOf(String.valueOf(sql.executeQuery()));
            }


        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateGame(Integer gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {

            String statement = "select * from game where game_id = ? ";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, String.valueOf(gameID));

                var result = sql.executeQuery();
                if (!result.next()) {
                    throw new DataAccessException("Error: bad request");
                }

                String blackUsername = result.getString("black_username");
                String whiteUsername = result.getString("white_username");

                if (playerColor == null) {
                    if (username != null && !observers.containsValue(username)) {
                        observers.put(gameID, username);

                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                } else {
                    if (playerColor == ChessGame.TeamColor.BLACK && blackUsername == null) {
                        String setUsername = "update game set black_username = ? where game_id = ?";
                        try (var sql2 = conn.prepareStatement(setUsername)) {
                            sql2.setString(1, username);
                            sql2.setString(2, String.valueOf(gameID));
                        }

                    } else if (playerColor == ChessGame.TeamColor.WHITE && whiteUsername == null) {
                        String setUsername = "update game set white_username = ? where game_id = ?";
                        try (var sql2 = conn.prepareStatement(setUsername)) {
                            sql2.setString(1, username);
                            sql2.setString(2, String.valueOf(gameID));
                        }
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                }

            }

        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }


    public void clearGame() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "truncate table game";
            try (var sql = conn.prepareStatement(statement)) {

                sql.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
