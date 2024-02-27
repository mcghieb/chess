package service;

import dataAccess.DataAccess;
import handler.response.ClearResponse;

public class ClearService {
    private DataAccess dataAccess;
    public ClearService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ClearResponse clear() {
        dataAccess.clear();
        return new ClearResponse(null);
    }
}
