package dataAccess.memorydataaccess;

import dataAccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    HashSet<AuthData> authList = new HashSet<>();

    public String createAuth(String username) {
        if (username == null) {
            return null;
        }

        String authToken=UUID.randomUUID().toString();

        authList.add(new AuthData(username, authToken));

        return authToken;
    }

    public String in(String authToken) {
        for (AuthData item: authList) {
            if (Objects.equals(item.getAuthToken(), authToken)) {
                return authToken;
            }
        }

        return null;
    }

    public String getUsername(String authToken) {
        for (AuthData item : authList) {
            if (Objects.equals(item.getAuthToken(), authToken)) {
                return item.getUsername();
            }
        }

        return null;
    }

    public void deleteAuth(String authToken) {
        authList.removeIf(item -> Objects.equals(item.getAuthToken(), authToken));
    }

}
