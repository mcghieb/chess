package dataAccess.sqldataaccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.DatabaseManager.*;
import dataAccess.interfaces.AuthDAO;

import java.sql.Connection;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public String createAuth(String username) {
        if (username == null) {
            return null;
        }

        String authToken=UUID.randomUUID().toString();

        Connection db = DatabaseManager.getConnection();

//        authList.add(new AuthData(username, authToken));

        return authToken;
    }

    public String in(String authToken) {
//        for (AuthData item: authList) {
//            if (Objects.equals(item.getAuthToken(), authToken)) {
//                return authToken;
//            }
//        }

        return null;
    }

    public String getUsername(String authToken) {
//        for (AuthData item : authList) {
//            if (Objects.equals(item.getAuthToken(), authToken)) {
//                return item.getUsername();
//            }
//        }

        return null;
    }

    public void deleteAuth(String authToken) {
//        authList.removeIf(item -> Objects.equals(item.getAuthToken(), authToken));
    }

}
