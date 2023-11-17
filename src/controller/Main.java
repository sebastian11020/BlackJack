package controller;
import model.*;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class Main implements ActionListener {
    private View view;
    private BlackJack model;

    public Main(View view) {
        this.view = view;
        this.model = new BlackJack();
        this.view.getAnotherButton().addActionListener(this);
        this.view.getNoMoreButton().addActionListener(this);
        this.view.getResetButton().addActionListener(this);
        updateUI();
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
            view.updatePlayerPanel();
            view.updateBankPanel();
        } catch (FileNotFoundException ex) {
            handleException(ex);
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
