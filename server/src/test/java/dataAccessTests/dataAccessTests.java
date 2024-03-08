package dataAccessTests;

import chess.ChessGame;
import com.google.gson.Gson;
import com.sun.source.tree.AssertTree;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SQLDataAccess;
import dataAccess.interfaces.*;
import model.GameData;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class dataAccessTests {
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

    @BeforeEach
    public void init() throws SQLException, DataAccessException {
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
    public void SQLCreateGame() throws SQLException {
        Integer gameID = gameDAO.createGame(testGameName);
        Assertions.assertNotNull(gameID, "This should return gameID.");
    }

    @Test
    @Order(11)
    public void SQLCreateBadGame() throws SQLException {
        insertTestGame();
        Assertions.assertThrows(Exception.class, () -> gameDAO.createGame(testGameName));
    }

    @Test
    @Order(12)
    public void SQLUpdateGame() throws SQLException, DataAccessException {
        insertTestGame();
        gameDAO.updateGame(1, ChessGame.TeamColor.WHITE, username);

        String retrievedUsername = null;

        String statement = "select white_username from game where game_id = 1";
        try (var sql= conn.prepareStatement(statement)) {
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                retrievedUsername = result.getString("white_username");
            }
        }

        Assertions.assertEquals(username, retrievedUsername, "White username should be populated.");
    }


    @Test
    @Order(13)
    public void SQLUpdateBadGame() throws SQLException, DataAccessException {
        Assertions.assertThrows(Exception.class, () -> gameDAO.updateGame(1, ChessGame.TeamColor.WHITE, username)
                , "This should throw an exception. There are no games in the database.");
    }

    @Test
    @Order(14)
    public void makeObservers() throws SQLException, DataAccessException {
        insertTestGame();

        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(1, null, username)
                , "This should not throw an exception.");
    }

    @Test
    @Order(15)
    public void makeBadObservers() throws SQLException, DataAccessException {
        insertTestGame();

        Assertions.assertThrows(Exception.class, () -> gameDAO.updateGame(null, null, username)
                , "This should throw an exception.");
    }

    @Test
    @Order(16)
    public void SQLClearGameDAO() throws SQLException {
        insertTestGame();
        gameDAO.clearGame();

        Assertions.assertTrue(gameDAO.listGames().isEmpty(), "Games should be empty.");
    }


    @Test
    @Order(17)
    public void SQLCreateAuth() throws SQLException, DataAccessException {
        String authToken = authDAO.createAuth(username);

        Assertions.assertNotNull(authToken, "This should return an authToken");
    }

    @Test
    @Order(18)
    public void SQLCreateBadAuth() throws SQLException, DataAccessException {
        Assertions.assertThrows(Exception.class, () -> authDAO.createAuth(null),
                "This should throw an exception");
    }


    @Test
    @Order(19)
    public void SQLInAuth() throws SQLException, DataAccessException {
        String expectedToken = authDAO.createAuth(username);
        String actualToken = authDAO.in(expectedToken);

        Assertions.assertEquals(expectedToken,actualToken, "This should return the same token.");
    }

    @Test
    @Order(20)
    public void SQLBadInAuth() throws SQLException, DataAccessException {
        String token = authDAO.in("fakeToken");

        Assertions.assertNull(token, "This should return null.");
    }


    @Test
    @Order(21)
    public void SQLGetUsername() throws SQLException, DataAccessException {
        String statement = "insert into auth (username, auth_token) values (?,?)";
        try (var sql = conn.prepareStatement(statement)) {
            sql.setString(1,username);
            sql.setString(2, "dummyAuth");
            sql.executeUpdate();
        }

        String actualUsername = authDAO.getUsername("dummyAuth");
        Assertions.assertEquals(username, actualUsername, "Should return the same username.");
    }

    @Test
    @Order(22)
    public void SQLGetBadUsername() throws SQLException, DataAccessException {
        String actualUsername = authDAO.getUsername("dummyAuth");
        Assertions.assertNull(actualUsername, "Should return null.");
    }

    @Test
    @Order(23)
    public void SQLDeleteAuth() throws SQLException, DataAccessException {
        String statement = "insert into auth (username, auth_token) values (?,?)";
        try (var sql = conn.prepareStatement(statement)) {
            sql.setString(1,username);
            sql.setString(2, "dummyAuth");
            sql.executeUpdate();
        }

        authDAO.deleteAuth("dummyAuth");
        String token = authDAO.in("dummyAuth");
        Assertions.assertNull(token);
    }

    @Test
    @Order(24)
    public void SQLDeleteBadAuth() throws SQLException, DataAccessException {
        Assertions.assertThrows(Exception.class, () -> authDAO.deleteAuth(null)
                , "This should throw an exception.");
    }

    @Test
    @Order(25)
    public void SQLClearAuth() throws SQLException, DataAccessException {
        String statement = "insert into auth (username, auth_token) values (?,?)";
        try (var sql = conn.prepareStatement(statement)) {
            sql.setString(1,username);
            sql.setString(2, "dummyAuth");
            sql.executeUpdate();
        }

        authDAO.clearAuth();
        Assertions.assertNull(authDAO.in("dummyAuth"), "Auth should be empty.");

    }


}