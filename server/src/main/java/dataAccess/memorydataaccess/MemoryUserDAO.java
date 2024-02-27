package dataAccess.memorydataaccess;

import model.UserData;
import dataAccess.interfaces.UserDAO;
import java.util.HashMap;
import java.util.Vector;

public class MemoryUserDAO implements UserDAO {
    private Vector<String> usernameList = new Vector<>();
    private HashMap<String, UserData> users = new HashMap<>();

    public MemoryUserDAO() {
    }

    public String getUser(String username) {
        if (usernameList.contains(username)) {
            return username;
        }
        return null;
    }

    public String createUser(String username, String password, String email) {
        usernameList.add(username);
        users.put(username, new UserData(username, password, email));


        return "Dummy authToken";
    }
}
