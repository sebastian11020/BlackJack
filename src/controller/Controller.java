package controller;

import model.BlackJack;
import model.EmptyDeckException;
import model.IObserver;
import model.User;
import network.ClientConnection;
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
    private String host;

    public Controller(View view) throws IOException {
        this.view = view;
        this.bj = new BlackJack();
        userName = JOptionPane.showInputDialog("Nombre");
        host = JOptionPane.showInputDialog("Direccion ip");
        this.clientConnection = new ClientConnection(host, 3001, new User(userName));
        this.view.setUserName(userName);
        this.view.getAnotherButton().addActionListener(this);
        this.view.getNoMoreButton().addActionListener(this);
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
    public void updateInitsCards(List<server.models.Card> cards) throws FileNotFoundException {
        view.updatePlayerPanel(cards);
    }
}
