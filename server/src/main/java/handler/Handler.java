package handler;

import dataAccess.DataAccess;

import javax.xml.crypto.Data;

public abstract class Handler {
    DataAccess dataAccess;

    Handler(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

//    public String checkAuth(String authToken) {
//
//    }
}
