package dataAccess.sqldataaccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.AuthDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    public String createAuth(String username) throws DataAccessException, SQLException {
        if (username == null) {
            return null;
        }

        // check to see if in auth, if in auth, then delete auth, and create new auth
        try (var conn = DatabaseManager.getConnection()) {
            String authToken=UUID.randomUUID().toString();
            String createStatement = "INSERT INTO auth (username, auth_token) values (?, ?)";
            try (var sql2 = conn.prepareStatement(createStatement)) {
                sql2.setString(1, username);
                sql2.setString(2, authToken);

                sql2.executeUpdate();
                return authToken;
            }
        }
    }

    public String in(String authToken) {
        HashMap<String, String> authList = new HashMap<>();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select * from auth";
            try (var sql = conn.prepareStatement(statement)) {
                ResultSet result = sql.executeQuery();
                while (result.next()) {
                    authList.put(result.getString("auth_token"), result.getString("username"));
                }

            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

        if (!authList.isEmpty() && authList.containsKey(authToken)) {
            return authToken;
        } else {
            return null;
        }
    }

    public String getUsername(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select username from auth where auth_token = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, authToken);

                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    return result.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAuth(String authToken) {
        if (authToken == null) {
            try {
                throw new Exception();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "delete from auth where auth_token = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, authToken);
                sql.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAuth() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "truncate table auth";
            try (var sql = conn.prepareStatement(statement)) {

                sql.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
