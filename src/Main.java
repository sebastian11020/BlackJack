import controller.Controller;
import view.View;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            View view = new View();
            try {
                new Controller(view).setObserver();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
