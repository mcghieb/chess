package dataAccess;

import dataAccess.interfaces.AuthDAO;
import dataAccess.interfaces.GameDAO;
import dataAccess.interfaces.UserDAO;
import dataAccess.memorydataaccess.MemoryAuthDAO;
import dataAccess.memorydataaccess.MemoryGameDAO;
import dataAccess.memorydataaccess.MemoryUserDAO;

public abstract class DataAccess {
    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;
    DataAccessType type;

    protected enum DataAccessType {
        MEMORY,
        SQL
    }

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

    void setType(DataAccessType type) {
        this.type = type;
    }

    public void clear() {
        if (type == DataAccessType.MEMORY) {
            this.setAuthDAO(new MemoryAuthDAO());
            this.setGameDAO(new MemoryGameDAO());
            this.setUserDAO(new MemoryUserDAO());
        } else if (type == DataAccessType.SQL) {
            authDAO.clearAuth();
        }
    }
}
