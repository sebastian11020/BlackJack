package server.models;

import java.io.Serializable;
import java.util.List;

public class BlackJackInfo implements Serializable {

    private List<Card> playerCardList;
    private int playerBest;

    public BlackJackInfo(List<Card> playerCardList, int playerBest) {
        this.playerCardList = playerCardList;
        this.playerBest = playerBest;
    }

    public List<Card> getPlayerCardList() {
        return playerCardList;
    }

    public void setPlayerCardList(List<Card> playerCardList) {
        this.playerCardList = playerCardList;
    }

    public int getPlayerBest() {
        return playerBest;
    }

    public void setPlayerBest(int playerBest) {
        this.playerBest = playerBest;
    }
}
