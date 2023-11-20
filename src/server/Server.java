package server;

import model.User;
import server.models.EmptyDeckException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final int PORT = 3001;
    private static ConcurrentHashMap<Integer, ServerConnection> connections;
    private static InfoConnections infoConnections;
    private static int turn;
    private ServerSocket server;
    private int id;
    private boolean connectionPermited;

    public Server() throws IOException {
        this.server = new ServerSocket(PORT);
        this.connections = new ConcurrentHashMap<>();
        this.id = 1;
        this.turn = 0;
        this.infoConnections = new InfoConnections();
        this.connectionPermited = true;
        manageNewConnections();
        manageConnections();
        Logger.getGlobal().log(Level.INFO, "Server Run on port 3001....");
    }

    public static void addInfoConnection() throws InterruptedException {
        Thread.sleep(1000);
        infoConnections = new InfoConnections();
        if (turn < infoConnections.getUsers().size()) {
            infoConnections.getUsers().get(turn).setTurn(true);
        }
        for (ServerConnection connection : connections.values()) {
            infoConnections.addUser(connection.getUser());
        }
    }

    public static void sendInfoConnection() throws IOException {
        System.out.println("enviando");
        for (ServerConnection connection : connections.values()) {
            connection.sendUsers(infoConnections);
        }
    }

    public static void nextTurn() {
        for (int i = 0; i < infoConnections.getUsers().size(); i++) {
            if (infoConnections.getUsers().get(i).isTurn()) {
                infoConnections.getUsers().get(i).setTurn(false);
                turn = i + 1;
                break;
            }
        }
        if (turn < infoConnections.getUsers().size()) {
            infoConnections.getUsers().get(turn).setTurn(true);
        } else {
            for (User user : infoConnections.getUsers()) {
                user.setTurn(false);
            }
        }
    }

    public static int getTurn() {
        return turn;
    }

    public static InfoConnections getInfoConnections() {
        return infoConnections;
    }

    private void manageNewConnections() {
        Thread newConnectionsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (connectionPermited) {
                        if (id <= 1) {
                            Socket socket = server.accept();
                            ServerConnection serverConnection = new ServerConnection(socket);
                            connections.put(id, serverConnection);
                                addInfoConnection();
                                sendInfoConnection();
                                id++;

                        } else {
                            connectionPermited = false;
                            infoConnections.getUsers().get(0).setTurn(true);
                            sendInfoConnection();
                            sendCards();
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        newConnectionsThread.start();
    }

    private void manageConnections() {
        Thread connectionManagerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //manageAdministrator();
                    for (ServerConnection connection : connections.values()) {
                        try {
                            connection.manageRequest();
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (EmptyDeckException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        connectionManagerThread.start();
    }

    private void sendCards() throws IOException {
        for (ServerConnection connection : connections.values()) {
            connection.sendBlackJackInfo();
        }
    }

    public static ConcurrentHashMap<Integer, ServerConnection> getConnections() {
        return connections;
    }
}