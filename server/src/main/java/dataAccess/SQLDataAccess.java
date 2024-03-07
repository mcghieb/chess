package dataAccess;

import dataAccess.sqldataaccess.SQLAuthDAO;
import dataAccess.sqldataaccess.SQLGameDAO;
import dataAccess.sqldataaccess.SQLUserDAO;

import java.sql.Connection;

public class SQLDataAccess extends DataAccess {

    public SQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        super.setAuthDAO(new SQLAuthDAO());
        super.setGameDAO(new SQLGameDAO());
        super.setUserDAO(new SQLUserDAO());
        super.setType(DataAccessType.SQL);
    }

}
