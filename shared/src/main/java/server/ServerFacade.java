package server;

import com.google.gson.Gson;
import exception.ResponseException;
import request.GameCreateRequest;
import request.GameJoinRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


public class ServerFacade {

    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";

        return this.makeRequest("POST", path, registerRequest, RegisterResponse.class, null );
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";

        return this.makeRequest("POST", path, loginRequest, LoginResponse.class, null);
    }

    public String logout(String authToken) throws ResponseException {
        var path = "/session";

        return this.makeRequest("DELETE", path,null, null , authToken);
    }

    public GameCreateResponse createGame(GameCreateRequest gameCreateRequest, String authToken) throws ResponseException {
        var path = "/game";

        return this.makeRequest("POST", path, gameCreateRequest, GameCreateResponse.class, authToken);
    }

    public GameListResponse listGames(String authToken) throws ResponseException {
        var path = "/game";

        return this.makeRequest("GET", path, null, GameListResponse.class, authToken);
    }

    public void joinGame(GameJoinRequest gameJoinRequest, String authToken) throws ResponseException {
        var path = "/game";

        this.makeRequest("PUT", path, gameJoinRequest, ResponseContainer.class, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";

        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
