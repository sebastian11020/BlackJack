package controller;
import model.*;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main implements ActionListener {
    private View view;
    private BlackJack model;

    private List<BlackJack> games;
    private List<String> playerResults;
    public Main(View view) {
        this.view = view;
        this.games = new ArrayList<>();
        this.playerResults = new ArrayList<>();
        initializeGames();
        this.model = new BlackJack();
        this.view.getAnotherButton().addActionListener(this);
        this.view.getNoMoreButton().addActionListener(this);
        this.view.getResetButton().addActionListener(this);
        updateUI();
    }

    private void initializeGames() {
        for (int i = 0; i < 4; i++) {
            games.add(new BlackJack());
            playerResults.add("");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "another":
                try {
                    model.playerDrawAnotherCard();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
                break;
            case "noMore":
                try {
                    model.bankLastTurn();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
                break;
            case "reset":
                try {
                    model.reset();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
                break;
        }
        updateUI();
    }

    private void updateUI() {
        try {
            // Implementa la lógica para actualizar la interfaz de usuario
            // Puedes utilizar el método updatePlayerPanel() de la clase View
            for (int i = 0; i < games.size(); i++) {
                view.updatePlayerPanel();
            }
            view.updateBankPanel();
        } catch (IOException ex) {
            handleException(ex);
        }
    }

    private void playAnotherTurn() {
        // Implementa lógica para permitir a cada jugador jugar otro turno
        for (int i = 0; i < games.size(); i++) {
            BlackJack game = games.get(i);
            if (!game.isGameFinished()) {
                try {
                    game.playerDrawAnotherCard();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
            }
        }

        // Actualiza los resultados de cada jugador
        updatePlayerResults();

        // Guarda los resultados en un archivo TXT después de cada turno
        saveResultsToFile();

        // Actualiza la interfaz de usuario
        updateUI();
    }

    private void finishGame() {
        // Implementa lógica para finalizar el juego
        for (int i = 0; i < games.size(); i++) {
            BlackJack game = games.get(i);
            if (!game.isGameFinished()) {
                try {
                    game.bankLastTurn();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
            }
        }

        // Actualiza los resultados de cada jugador
        updatePlayerResults();

        // Guarda los resultados en un archivo TXT al final del juego
        saveResultsToFile();

        // Actualiza la interfaz de usuario
        updateUI();
    }

    private void resetGame() {
        
        initializeGames();

        // Actualiza la interfaz de usuario
        updateUI();
    }

    private void updatePlayerResults() {
        // Implementa la lógica para actualizar los resultados de cada jugador
        for (int i = 0; i < games.size(); i++) {
            BlackJack game = games.get(i);
            String result = "Player " + (i + 1) + ": ";
            if (game.isPlayerWinner()) {
                result += "Winner";
            } else if (game.isBankWinner()) {
                result += "Loser";
            } else {
                result += "Draw";
            }
            playerResults.set(i, result);
        }
    }

    private void saveResultsToFile() {
        try (FileWriter writer = new FileWriter("results.txt")) {
            // Implementa la lógica para escribir los resultados en el archivo
            for (String result : playerResults) {
                writer.write(result + "\n");
            }
        } catch (IOException e) {
            handleException(e);
        }
    }


    private void handleException(Exception ex) {
        ex.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            new Main(view);
        });
    }
}
