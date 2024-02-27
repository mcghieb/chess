package dataAccess;

import dataAccess.memorydataaccess.MemoryAuthDAO;
import dataAccess.memorydataaccess.MemoryGameDAO;
import dataAccess.memorydataaccess.MemoryUserDAO;

public class MemoryDataAccess extends DataAccess {
    public MemoryDataAccess(){
        super.setAuthDAO(new MemoryAuthDAO());
        super.setGameDAO(new MemoryGameDAO());
        super.setUserDAO(new MemoryUserDAO());
        super.setType(DataAccessType.MEMORY);
    }

}
