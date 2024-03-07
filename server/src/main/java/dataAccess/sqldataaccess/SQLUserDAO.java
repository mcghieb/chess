package dataAccess.sqldataaccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.interfaces.UserDAO;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
    }

    public String getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select username from user where username = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, username);

                try (ResultSet result = sql.executeQuery()){
                    if (result.next()) {
                        return result.getString("username");
                    } else {
                        return null;
                    }
                }

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
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "select password from user where username = ?";
            try (var sql = conn.prepareStatement(statement)) {
                sql.setString(1, username);

                String retrievedPassword = null;
                ResultSet result = sql.executeQuery();
                if (result.next()) {
                    retrievedPassword = result.getString("password");
                } else {
                    return null;
                }

                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(password, retrievedPassword)) {
                    return username;
                } else {
                    return null;
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearUser(){
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "truncate table user";
            try (var sql = conn.prepareStatement(statement)) {

                sql.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
