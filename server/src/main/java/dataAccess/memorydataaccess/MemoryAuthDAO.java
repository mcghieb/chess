package dataAccess.memorydataaccess;

import dataAccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    HashSet<AuthData> authList = new HashSet<>();


    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();

        authList.add(new AuthData(username, authToken));

        return authToken;
    }

    public AuthData checkAuth(AuthData authData) {
        for (AuthData item : authList) {
            if (item == authData) {
                return authData;
            }
        }

        return null;
    }

}
