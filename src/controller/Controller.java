package controller;

import model.BlackJack;
import model.EmptyDeckException;
import model.IObserver;
import model.User;
import network.ClientConnection;
import view.LoginDialog;
import view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller implements ActionListener, IObserver {
    private View view;
    private BlackJack bj;
    private ClientConnection clientConnection;
    private String userName;
    private LoginDialog loginDialog;

    public Controller() throws IOException {
        this.loginDialog = new LoginDialog(this);
        this.view = new View(this);
        this.bj = new BlackJack();
        this.clientConnection = new ClientConnection();
        updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "another":
                try {
                    clientConnection.anotherCard();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "noMore":
                try {
                    clientConnection.noMoreCards();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "reset":
                try {
                    bj.reset();
                } catch (EmptyDeckException ex) {
                    handleException(ex);
                }
                break;
            case "LOGIN":
                String [] data = loginDialog.getData();
                try {
                    userName = data[0];
                    this.clientConnection.connectToServer(data[1], new User(data[0]));
                    loginDialog.dispose();
                    view.setUserName(userName);
                    view.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                break;
        }
        updateUI();
    }

    private void updateUI() {
        //view.updatePlayerPanel(cards);
        //view.updateBankPanel();
    }

    private void handleException(Exception e) {
        e.printStackTrace();
    }

    public void setObserver() {
        clientConnection.setObserver(this);
    }

    @Override
    public void updateClientsConnection(ArrayList<User> users) {
        view.updateClientConnection(users);
    }

    @Override
    public void updateInitsCards(List<server.models.Card> cards, int bestPlayerHand) throws FileNotFoundException {
        view.updatePlayerPanel(cards, bestPlayerHand);
    }
}
