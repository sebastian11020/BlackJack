package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;

    public Player(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}

