package dataAccess.sqldataaccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.AuthDAO;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public String createAuth(String username) throws DataAccessException, SQLException {
        if (username == null) {
            return null;
        }

        String authToken=UUID.randomUUID().toString();

        try (var conn = DatabaseManager.getConnection()) {
            String statement = "INSERT INTO auth (username, auth_token) values (?, ?)";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, username);
                sql.setString(2, authToken);

                sql.executeUpdate();
            }
        }

        return authToken;
    }

    public String in(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select auth_token from auth where auth_token = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, authToken);

                return String.valueOf(sql.executeQuery());
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select username from auth where auth_token = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, authToken);

                return String.valueOf(sql.executeQuery());
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAuth(String authToken) {
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
