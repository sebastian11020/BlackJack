package network;

import model.IObserver;
import model.User;
import server.InfoConnections;
import server.models.BlackJackInfo;

import java.io.*;
import java.net.Socket;

public class ClientConnection implements Runnable{

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private IObserver iObserver;
    private Thread thread;

    public ClientConnection(String host, int port, User user) throws IOException {
        this.socket = new Socket(host, port);
        thread = new Thread(this);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        setInitialInfo(user);
    }

    public static void main(String[] args) {
        try {
            new ClientConnection("localhost", 3001, new User("paco"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setInitialInfo(User user) throws IOException {
        output.writeUTF("NEW_USER_INFO");
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        outputStream.writeObject(user);
    }

    @Override
    public void run() {
        while (true){
            try {
                if (input.available() > 0) {
                    Commands commands = Commands.valueOf(input.readUTF());
                    switch (commands) {
                        case GET_USERS_CONNECTED:
                            ObjectInputStream inputStream = new ObjectInputStream(input);
                            InfoConnections infoConnections = (InfoConnections) inputStream.readObject();
                            infoConnections.getUsers().forEach(c -> System.out.println(c.getName()));
                            iObserver.updateClientsConnection(infoConnections.getUsers());
                            break;
                        case GET_CARD_LIST:
                            ObjectInputStream inputStream1 = new ObjectInputStream(input);
                            BlackJackInfo blackJackInfo = (BlackJackInfo) inputStream1.readObject();
                            iObserver.updateInitsCards(blackJackInfo.getPlayerCardList());
                            break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setObserver(IObserver observer) {
        this.iObserver = observer;
        thread.start();
    }
}
