package server;

import java.io.*;
import java.net.Socket;

import model.User;
import network.Commands;
import server.models.BlackJack;
import server.models.BlackJackInfo;

public class ServerConnection {

    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;
    private User user;
    private BlackJack blackJack;

    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.blackJack = new BlackJack();
        input = new DataInputStream(this.socket.getInputStream());
        output = new DataOutputStream(this.socket.getOutputStream());
    }

    public void manageRequest() throws IOException, ClassNotFoundException {
        if (input.available() > 0) {
            Request request = Request.valueOf(input.readUTF());
            switch (request) {
                case NEW_USER_INFO:
                    addUser();
                    break;
            }
        }
    }

    private void addUser() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(input);
        this.user = (User) inputStream.readObject();
        System.out.println(user.getName());
    }

    public void sendUsers(InfoConnections infoConnections) throws IOException {
        output.writeUTF(Commands.GET_USERS_CONNECTED.name());
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        outputStream.writeObject(infoConnections);
    }

    public void sendBlackJackInfo() throws IOException {
        output.writeUTF(Commands.GET_CARD_LIST.name());
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        BlackJackInfo blackJackInfo = new BlackJackInfo(blackJack.getPlayerCardList());
        outputStream.writeObject(blackJackInfo);
    }

    public User getUser() {
        return this.user;
    }
}
