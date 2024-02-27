package dataAccess.interfaces;

import model.AuthData;

public interface AuthDAO {
    public String createAuth(String username);
    public AuthData checkAuth(AuthData authData);
    public String in(String authToken);

    void deleteAuth(String authToken);

}
