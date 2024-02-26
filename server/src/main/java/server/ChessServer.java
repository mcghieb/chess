package server;

import spark.*;

public class ChessServer {

    public ChessServer() {

    }

    public ChessServer run(int port) {
        Spark.port(port);

        Spark.externalStaticFileLocation("server/src/resources/public");

        Spark.get("/home", this::getHome);

        return this;
    }

    public int port() {
        return Spark.port();
    }

    public void stop(){
        Spark.stop();
    }

    private Object getHome(Request req, Response res) {
        return "HOME";
    }

}
