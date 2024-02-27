package handler;

import dataAccess.DataAccess;
import handler.response.ResponseContainer;
import service.ClearService;
import spark.Response;

public class ClearHandler extends Handler {
    public ClearHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ResponseContainer handleClear(Response response) {
        ClearService service = new ClearService(dataAccess);
        ResponseContainer clearResponse = service.clear();

        response.status(200);

        return clearResponse;
    }
}
