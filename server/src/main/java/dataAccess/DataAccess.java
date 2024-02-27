package dataAccess;

import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import dataAccess.interfaces.UserDAO;

public abstract class DataAccess {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    public AuthDAO getAuthDAO() throws DataAccessException {
        return authDAO;
    }
    public GameDAO getGameDAO() throws DataAccessException {
        return gameDAO;
    }
    public UserDAO getUserDAO() throws DataAccessException {
        return userDAO;
    }

    public void setAuthDAO(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void setGameDAO(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
