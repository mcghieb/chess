package dataAccess.interfaces;

import dataAccess.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    public String createAuth(String username) throws DataAccessException, SQLException;
    public String in(String authToken);
    public void deleteAuth(String authToken);
    public String getUsername(String authToken);

    void clearAuth();
}
