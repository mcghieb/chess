package dataAccess.sqldataaccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.UserDAO;
import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class SQLUserDAO implements UserDAO {
    private Vector<String> usernameList = new Vector<>();
    private HashMap<String, UserData> users = new HashMap<>();

    public SQLUserDAO() {
    }

    public String getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select username from user where username = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, username);

                return String.valueOf(sql.executeQuery());
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(String username, String password, String email) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "insert into user (username, password, email) values (?,?,?)";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, username);
                sql.setString(2,password);
                sql.setString(3,email);

                sql.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String checkCredentials(String username, String password) {
        UserData userData = users.get(username);
        if (userData != null && Objects.equals(userData.getPassword(), password)) {
            return username;
        }

        return null;
    }

    public void clearUser(){

    }
}
