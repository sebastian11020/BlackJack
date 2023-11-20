package server;

import model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class InfoConnections implements Serializable {

    private ArrayList<User> users;

    public InfoConnections() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
