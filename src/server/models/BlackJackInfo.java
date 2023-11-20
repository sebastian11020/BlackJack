package server.models;

import java.io.Serializable;
import java.util.List;

public class BlackJackInfo implements Serializable {

    private List<Card> playerCardList;
    private int playerBest;
    private boolean finishGame;

    public BlackJackInfo(List<Card> playerCardList, int playerBest, boolean finishGame) {
        this.playerCardList = playerCardList;
        this.playerBest = playerBest;
        this.finishGame = finishGame;
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

    public boolean isFinishGame() {
        return finishGame;
    }

    public void setFinishGame(boolean finishGame) {
        this.finishGame = finishGame;
    }
}
