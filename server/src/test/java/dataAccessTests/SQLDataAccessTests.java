package dataAccessTests;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLDataAccess;
import dataAccess.interfaces.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import spark.utils.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLDataAccessTests {
    private static DataAccess dataAccess;
    private static UserDAO userDAO;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static Connection conn;
    static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    static String username = "fakeUsername";
    static String password = "fakePassword";
    static String email = "fakeEmail@gmail.com";
    static String encryptedPass = encoder.encode(password);
    static String createUserStatement = "insert into user (username, password, email) values (?,?,?)";
    static String gameString = new Gson().toJson(new ChessGame());
    static String createGameStatement = "insert into game (game_name, game) values (?,?);";
    static String testGameName = "testgame_1";

    static {
        try {
            dataAccess = new SQLDataAccess();
            userDAO = dataAccess.getUserDAO();
            authDAO = dataAccess.getAuthDAO();
            gameDAO = dataAccess.getGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void truncate(String table) throws SQLException {
        String createUserStatement = "truncate table " + table;

        try (var sql = conn.prepareStatement(createUserStatement)) {
            sql.executeUpdate();
        }
    }

    private static void insertTestUser() throws SQLException {
        try (var sql = conn.prepareStatement(createUserStatement)) {
            sql.setString(1, username);
            sql.setString(2, encryptedPass);
            sql.setString(3,email);

            sql.executeUpdate();
        }
    }

    private static void insertTestGame() throws SQLException {
        try (var sql = conn.prepareStatement(createGameStatement)) {
            sql.setString(1, testGameName);
            sql.setString(2, gameString);

            sql.executeUpdate();
        }
    }

    @BeforeAll
    public static void init() throws SQLException, DataAccessException {
        DatabaseManager.createDatabase();
        conn = DatabaseManager.getConnection();

        truncate("user");
        truncate("game");
        truncate("auth");
        truncate("observers");
    }

    @Test
    @Order(1)
    public void SQLGetUser() throws SQLException {
        insertTestUser();

        String retrievedUsername = userDAO.getUser(username);

        Assertions.assertEquals(username, retrievedUsername, "Did not return correct username.");

        String falseUsername = userDAO.getUser("nullUser");
        Assertions.assertNull(falseUsername, "Should return null.");
    }


    @Test
    @Order(2)
    public void SQLGetBadUser() {
        String falseUsername = userDAO.getUser("nullUser");
        Assertions.assertNull(falseUsername, "Should return null.");
    }

    @Test
    @Order(3)
    public void SQLCreateUser() {
        userDAO.createUser(username, encryptedPass, email);
        String retrievedUser = userDAO.getUser(username);

        Assertions.assertEquals(username, retrievedUser, "This should return the same username created.");
    }

    @Test
    @Order(4)
    public void SQLCreateBadUser() {
        Assertions.assertThrows(Exception.class, () -> userDAO.createUser(null, null, null)
                , "This should throw an exception.");
    }

    @Test
    @Order(5)
    public void SQLCheckCredentials() throws SQLException {
        insertTestUser();

        String retrievedUsername = userDAO.checkCredentials(username, password);
        Assertions.assertEquals(username, retrievedUsername, "This should return the same username inserted.");
    }

    @Test
    @Order(6)
    public void SQLCheckBadCredentials() throws SQLException {
        insertTestUser();

        String retrievedUsername = userDAO.checkCredentials("not_in_database", "notpassword");
        Assertions.assertNull(retrievedUsername, "This should return null.");
    }

    @Test
    @Order(7)
    public void SQLClearUser() throws SQLException {
        insertTestUser();

        userDAO.clearUser();
        String nullUsername = userDAO.getUser(username);
        Assertions.assertNull(nullUsername, "This should be null.");
    }

    @Test
    @Order(8)
    public void SQLListGames() throws SQLException {
        insertTestGame();

        HashMap<Integer, GameData> retrievedGames = gameDAO.listGames();
        Assertions.assertNotNull(retrievedGames, "This should not be null.");
    }

    @Test
    @Order(9)
    public void SQLListBadGames()  {
        Assertions.assertTrue(gameDAO.listGames().isEmpty(),  "This should return an empty list.");
    }

    @Test
    @Order(10)
    public void SQCreateGame() throws SQLException {
        Integer gameID = gameDAO.createGame(testGameName);
        Assertions.assertNotNull(gameID, "This should return gameID.");
    }

    @Test
    @Order(11)
    public void SQCreateBadGame() throws SQLException {
        insertTestGame();
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(testGameName));
    }



}
