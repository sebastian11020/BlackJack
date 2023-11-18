package model;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            // Conéctate al servidor
            System.out.println("Connecting to the server...");
            Socket socket = new Socket("localhost", 8888);
            System.out.println("Connected to the server!");

            // Crea objetos de flujo para enviar y recibir datos
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            // Crea y envía información del jugador al servidor
            Player player = new Player("PlayerName");  // Cambia "PlayerName" según el nombre del jugador
            oos.writeObject(player);
            oos.flush();

            // Envía una señal al servidor indicando que el jugador está listo
            oos.writeBoolean(true);  // Puedes definir tus propias señales según tu lógica
            oos.flush();

            // Muestra un mensaje indicando que el jugador está conectado
            System.out.println("Connected as: " + player.getPlayerName());

            // Puedes cerrar el socket aquí si no necesitas mantener la conexión abierta
            // socket.close();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to the server", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
