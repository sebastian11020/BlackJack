package model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1000L;

    private String name;
    private boolean isTurn;

    public User(String name) {
        this.name = name;
        this.isTurn = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }
}
