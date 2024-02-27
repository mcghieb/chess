package handler;

import dataAccess.DataAccess;
import handler.response.ClearResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler {
    public ClearHandler(DataAccess dataAccess) {
        super(dataAccess);
    }

    public ClearResponse handleClear(Response response) {
        ClearService service = new ClearService(dataAccess);
        ClearResponse clearResponse = service.clear();

        response.status(200);

        return clearResponse;
    }
}
