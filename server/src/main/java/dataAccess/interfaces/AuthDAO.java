package dataAccess.interfaces;

import model.AuthData;

public interface AuthDAO {
    public String createAuth(String username);
    public AuthData checkAuth(AuthData authData);
}
