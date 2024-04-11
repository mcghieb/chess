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
    private HashMap<Integer, String> observers;
    public SQLGameDAO(){}

    public HashMap<Integer, GameData> listGames() {
        games = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select * from game";
            try (var sql = conn.prepareStatement(statement)) {
                ResultSet gameSet = sql.executeQuery();

                while (gameSet.next()) {
                    int gameID = gameSet.getInt("game_id");
                    String gameName = gameSet.getString("game_name");
                    String whiteUsername = gameSet.getString("white_username");
                    String blackUsername = gameSet.getString("black_username");
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

                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    return result.getInt("game_id");
                } else {
                    return null;
                }
            }


        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBoard(String gameID, ChessGame chessGame) {
        try (var conn = DatabaseManager.getConnection()) {

            String statement = "update game set game = ? where game_id = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, String.valueOf(new Gson().toJson(chessGame)));
                sql.setString(2, String.valueOf(gameID));

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateGame(Integer gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException, SQLException {
        makeObservers();

        try (var conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false);

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
                        return;
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                } else {
                    if (playerColor == ChessGame.TeamColor.BLACK && (blackUsername == null || blackUsername.isEmpty())) {
                        String setUsername = "update game set black_username = ? where game_id = ?";
                        try (var sql2 = conn.prepareStatement(setUsername)) {
                            sql2.setString(1, username);
                            sql2.setString(2, String.valueOf(gameID));
                            sql2.executeUpdate();
                        }

                    } else if (playerColor == ChessGame.TeamColor.WHITE && whiteUsername == null) {
                        String setUsername = "update game set white_username = ? where game_id = ?";
                        try (var sql2 = conn.prepareStatement(setUsername)) {
                            sql2.setString(1, username);
                            sql2.setString(2, String.valueOf(gameID));
                            sql2.executeUpdate();
                        }
                    } else {
                        throw new DataAccessException("Error: already taken");
                    }
                }

            }

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void makeObservers() throws DataAccessException, SQLException {
        observers = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select * from observers";
            try (var sql = conn.prepareStatement(statement)) {
                ResultSet result = sql.executeQuery();

                while (result.next()) {
                    observers.put(result.getInt("game_id"), result.getString("username"));
                }
            }
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
