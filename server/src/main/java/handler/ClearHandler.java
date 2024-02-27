package handler;

import dataAccess.DataAccess;
import handler.response.ResponseContainer;
import service.ClearService;
import spark.Response;

public class ClearHandler extends Handler {
    public ClearHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ResponseContainer handleClear() {
        ClearService service = new ClearService(dataAccess);
        ResponseContainer clearResponse = service.clear();

        return clearResponse;
    }
}
