package server.models;

import java.io.Serializable;
import java.util.List;

public class BlackJackInfo implements Serializable {

    private List<Card> playerCardList;

    public BlackJackInfo(List<Card> playerCardList) {
        this.playerCardList = playerCardList;
    }

    public List<Card> getPlayerCardList() {
        return playerCardList;
    }

    public void setPlayerCardList(List<Card> playerCardList) {
        this.playerCardList = playerCardList;
    }
}
