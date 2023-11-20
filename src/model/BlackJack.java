package model;

import server.persistence.ReadAndWrite;

import java.io.FileNotFoundException;
import java.util.*;

public class BlackJack {
    //private Hand bankHand;
    public boolean gameFinished = false;
    private Deck deck;
    private Hand playerHand;

    public BlackJack() {
        this.deck = new Deck(4);
        //this.bankHand = new Hand();
        this.playerHand = new Hand();
        try {
            this.reset();
        } catch (EmptyDeckException ex) {
            ex = new EmptyDeckException("Error, the deck has insuffisent cards");
            System.exit(-1);
        }
    }

    public void reset() throws EmptyDeckException {
        try {
            this.playerHand.clear();
            this.gameFinished = false;
            this.playerHand.add(this.deck.draw());
            this.playerHand.add(this.deck.draw());
        } catch (EmptyDeckException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
    }
    public int getPlayerBest() {
        return this.playerHand.best();
    }

    public boolean isGameFinished() {
        if (gameFinished)
            return true;
        return false;
    }
    public void playerDrawAnotherCard() throws EmptyDeckException {
        try {
            if (!(this.isGameFinished()))
                this.playerHand.add(this.deck.draw());
            if (this.getPlayerBest() > 21)
                gameFinished = true;
        } catch (EmptyDeckException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
    }
    public List<Card> getPlayerCardList() {
        List<Card> originalList = playerHand.getCardList();
        LinkedList<Card> copyList = new LinkedList<Card>(originalList);
        return copyList;
    }

    public static String encontrarGanador(Map<String, Integer> puntajes) {
        String ganador = "";
        int maxPuntaje = -1;

        for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
            String jugador = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje <= 21 && puntaje > maxPuntaje) {
                maxPuntaje = puntaje;
                ganador = jugador;
            }
        }

        return ganador;
    }
}
