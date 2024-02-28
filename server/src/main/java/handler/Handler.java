package handler;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.interfaces.AuthDAO;

import javax.xml.crypto.Data;

public abstract class Handler {
    DataAccess dataAccess;

    Handler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

}
