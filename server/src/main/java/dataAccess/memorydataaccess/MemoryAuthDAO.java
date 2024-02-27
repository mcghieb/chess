package dataAccess.memorydataaccess;

import dataAccess.interfaces.AuthDAO;
import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    HashSet<AuthData> authList = new HashSet<>();


    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username, authToken);

        authList.add(authData);
        return authData;
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
