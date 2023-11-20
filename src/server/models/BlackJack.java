package server.models;

import server.persistence.ReadAndWrite;

import java.io.FileNotFoundException;
import java.util.*;

public class BlackJack {
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
    public String getPlayerHandString() {
        return this.playerHand.toString();
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
            if (this.getPlayerBest() >= 21)
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

    public String getWinnerName() {
        ReadAndWrite readAndWrite = new ReadAndWrite();

        try {
            ArrayList<String> puntajes = readAndWrite.readFile("scores");
            Map<String, Integer> puntajesMap = new HashMap<>();

            for (String puntaje : puntajes) {
                String[] partes = puntaje.split(":");
                String jugador = partes[0].trim(); // Nombre del jugador
                String[] puntajePartes = partes[1].split("\\[|\\]");
                String opcionesPuntaje = puntajePartes[1].trim();
                if(opcionesPuntaje.contains(", ")){
                    String[] puntajeOopciones = opcionesPuntaje.split(", ");
                    int posiblidad = Integer.parseInt(puntajeOopciones[0]);
                    int posiblidad1 = Integer.parseInt(puntajeOopciones[1]);
                    int mayor = posiblidad>posiblidad1?posiblidad:posiblidad1;
                    puntajesMap.put(jugador, mayor);
                }else {
                    int puntajeTotal = Integer.parseInt(puntajePartes[1].trim()); // Puntaje total
                    puntajesMap.put(jugador, puntajeTotal);
                }
            }

            JugadorGanador jugadorGanador = encontrarGanador(puntajesMap);
            return jugadorGanador.getNombre();

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        }

        return null;
    }

    public int getWinnerPoints() {
        ReadAndWrite readAndWrite = new ReadAndWrite();

        try {
            ArrayList<String> puntajes = readAndWrite.readFile("scores");
            Map<String, Integer> puntajesMap = new HashMap<>();

            for (String puntaje : puntajes) {
                String[] partes = puntaje.split(":");
                String jugador = partes[0].trim();
                String[] puntajePartes = partes[1].split("\\[|\\]");
                int puntajeTotal = Integer.parseInt(puntajePartes[1].trim());
                puntajesMap.put(jugador, puntajeTotal);
            }

            JugadorGanador jugadorGanador = encontrarGanador(puntajesMap);
            return jugadorGanador.getPuntos();

        } catch (FileNotFoundException e) {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        }

        return 0;
    }


    private static JugadorGanador encontrarGanador(Map<String, Integer> puntajes) {
        String ganador = "No hay ganador ";
        int maxPuntaje = 0;

        for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
            String jugador = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje <= 21 && puntaje > maxPuntaje) {
                maxPuntaje = puntaje;
                ganador = jugador;
            }
        }

        return new JugadorGanador(ganador, maxPuntaje);
    }
}


