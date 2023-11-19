package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final int PORT = 3001;
    private ServerSocket server;
    private ConcurrentHashMap<Integer, ServerConnection> connections;
    private int id;
    private InfoConnections infoConnections;
    private boolean connectionpermited;


    public Server() throws IOException {
        this.server = new ServerSocket(PORT);
        this.connections = new ConcurrentHashMap<>();
        this.id = 1;
        this.infoConnections = new InfoConnections();
        this.connectionpermited = true;
        manageNewConnections();
        manageConnections();
        Logger.getGlobal().log(Level.INFO, "Server Run on port 3001....");
    }

    private void manageNewConnections() {
        Thread newConnectionsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (connectionpermited) {
                        if (id <= 2) {
                            Socket socket = server.accept();
                            ServerConnection serverConnection = new ServerConnection(socket);
                            connections.put(id, serverConnection);
                            addInfoConnection();
                            System.out.println(infoConnections.getUsers().get(0));
                            sendInfoConnection();
                            id++;
                        } else {
                            connectionpermited = false;
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

    private void addInfoConnection() throws InterruptedException {
        Thread.sleep(1000);
        infoConnections = new InfoConnections();
        for (ServerConnection connection : connections.values()) {
            infoConnections.addUser(connection.getUser());
        }
    }

    private void sendInfoConnection() throws IOException {
        for (ServerConnection connection : connections.values()) {
            connection.sendUsers(infoConnections);
        }
    }

    private void sendCards() throws IOException {
        for (ServerConnection connection : connections.values()) {
            connection.sendBlackJackInfo();
        }
    }
}
