import server.Server;

import java.io.IOException;

public class ServerRunner {

    public static void main(String[] args) {
        try {
            new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
