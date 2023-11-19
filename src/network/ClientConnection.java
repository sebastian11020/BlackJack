package network;

import model.IObserver;
import model.User;
import server.InfoConnections;
import server.models.BlackJackInfo;
import server.models.JugadorGanador;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientConnection implements Runnable {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private IObserver iObserver;
    private Thread thread;
    private boolean aBoolean;

    public ClientConnection() throws IOException {
        this.aBoolean = false;
        thread = new Thread(this);
    }

    public void connectToServer(String host, User user) throws IOException {
        this.socket = new Socket(host, 3001);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        setInitialInfo(user);
        aBoolean = true;
        input.readUTF();
        boolean b = input.readBoolean();
        iObserver.validateUser(b);
        if (b == false) {
            thread.start();
        }
    }

    private void setInitialInfo(User user) throws IOException {
        output.writeUTF("NEW_USER_INFO");
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        outputStream.writeObject(user);
    }

    @Override
    public void run() {
        while (aBoolean) {
            try {
                if (input.available() > 0) {
                    Commands commands = Commands.valueOf(input.readUTF());
                    switch (commands) {
                        case GET_USERS_CONNECTED:
                            ObjectInputStream inputStream = new ObjectInputStream(input);
                            InfoConnections infoConnections = (InfoConnections) inputStream.readObject();
                            iObserver.updateClientsConnection(infoConnections.getUsers());
                            break;
                        case GET_CARD_LIST:
                            ObjectInputStream inputStream1 = new ObjectInputStream(input);
                            BlackJackInfo blackJackInfo = (BlackJackInfo) inputStream1.readObject();
                            iObserver.updateInitsCards(blackJackInfo.getPlayerCardList(), blackJackInfo.getPlayerBest(), blackJackInfo.isFinishGame());
                            break;
                        case GET_WINNER:
                            ObjectInputStream inputStream2 = new ObjectInputStream(input);
                            JugadorGanador jugadorGanador = (JugadorGanador) inputStream2.readObject();
                            iObserver.winner(jugadorGanador.getNombre(),jugadorGanador.getPuntos());
                            break;
                        case READ_FILE:
                            int size = input.readInt();
                            byte file[] = new byte[size];
                                OutputStream stream = Files.newOutputStream(Paths.get("./src/files/scores.txt"));
                                    int read = input.read(file);
                                    stream.write(file, 0, read);
                                    stream.flush();
                                stream.close();
                            break;
                        case EXIST_USER:
                            iObserver.validateUser(input.readBoolean());
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setObserver(IObserver observer) {
        this.iObserver = observer;
    }

    public void anotherCard() throws IOException {
        output.writeUTF(Commands.ANOTHER_CARD.name());
    }

    public void noMoreCards() throws IOException {
        output.writeUTF(Commands.NO_MORE_CARDS.name());
    }
    public Thread getThread() {
        return thread;
    }

    public void editUser(User user) throws IOException {
        output.writeUTF("EDIT_USER");
        ObjectOutputStream outputStream = new ObjectOutputStream(output);
        outputStream.writeObject(user);
        System.out.println(input.readUTF());
        //input.readUTF();
        boolean b = input.readBoolean();
        if (b == false) {
            thread.start();
        }
        iObserver.validateUser(b);
    }
}
