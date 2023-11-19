package server;

import model.User;
import network.Commands;
import server.models.BlackJack;
import server.models.BlackJackInfo;
import server.models.EmptyDeckException;
import server.persistence.ReadAndWrite;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public void manageRequest() throws IOException, ClassNotFoundException, EmptyDeckException, InterruptedException {
        if (input.available() > 0) {
            Request request = Request.valueOf(input.readUTF());
            switch (request) {
                case NEW_USER_INFO:
                    addUser();
                    break;
                case ANOTHER_CARD:
                    blackJack.playerDrawAnotherCard();
                    Thread.sleep(500);
                    if (blackJack.isGameFinished()) {
                        Server.nextTurn();
                        Server.addInfoConnection();
                        Server.sendInfoConnection();
                    }
                    sendBlackJackInfo();
                    if (Server.getTurn() == Server.getInfoConnections().getUsers().size()) {
                        writeScore();
                        writeFile();
                    }
                    break;
                case NO_MORE_CARDS:
                    Server.nextTurn();
                    Server.addInfoConnection();
                    Server.sendInfoConnection();
                    if (Server.getTurn() == Server.getInfoConnections().getUsers().size()) {
                        writeScore();
                        writeFile();
                    }
                    break;
            }
        }
    }


    private void addUser() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(input);
        this.user = (User) inputStream.readObject();
    }

    public void sendUsers(InfoConnections infoConnections) throws IOException {
        output.writeUTF(Commands.GET_USERS_CONNECTED.name());
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        outputStream.writeObject(infoConnections);
    }

    public void sendBlackJackInfo() throws IOException {
        output.writeUTF(Commands.GET_CARD_LIST.name());
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        BlackJackInfo blackJackInfo = new BlackJackInfo(blackJack.getPlayerCardList(), blackJack.getPlayerBest());
        outputStream.writeObject(blackJackInfo);
    }

    public void writeScore() throws IOException {
        ReadAndWrite readAndWrite = new ReadAndWrite();
        ArrayList<String> scores = new ArrayList<>();
        for (ServerConnection connection : Server.getConnections().values()) {
            scores.add(connection.getUser().getName() + "-" + connection.blackJack.getPlayerHandString());
        }
        readAndWrite.writeFile("scores", scores);
    }

    private BlackJack getBlackJack() {
        return blackJack;
    }

    private void writeFile(){
        byte file[] = null;
        try {
            file = Files.readAllBytes(Paths.get("./src/server/files/scores.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            if (file != null) {
                output.writeUTF(Request.READ_FILE.name());
                output.writeInt(file.length);
                output.write(file);
                output.flush();
            } else {
                Logger.getGlobal().log(Level.INFO, "No se pudo leer el archivo");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser() {
        return this.user;
    }
}
