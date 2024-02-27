package dataAccess.interfaces;

import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(String username);
    public AuthData checkAuth(AuthData authData);
}
