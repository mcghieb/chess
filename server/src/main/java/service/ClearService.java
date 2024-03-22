package service;

import dataAccess.DataAccess;
import response.ResponseContainer;

public class ClearService {
    private DataAccess dataAccess;
    public ClearService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ResponseContainer clear() {
        dataAccess.clear();
        return new ResponseContainer(null);
    }
}
