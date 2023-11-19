package view;

import controller.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginDialog extends JDialog {

    private JTextArea name, ip;
    public LoginDialog(ActionListener l) {
        setSize(new Dimension(300, 300));
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel login = new JLabel("Login");
        add(login, BorderLayout.NORTH);

        this.name = new JTextArea();
        this.ip = new JTextArea();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,1,10,10));

        panel.add(name);
        panel.add(ip);

        add(panel, BorderLayout.CENTER);

        JButton add = new JButton("Ingresar");
        add.addActionListener(l);
        add.setActionCommand(Command.LOGIN.name());

        add(add, BorderLayout.SOUTH);

        setVisible(true);
    }

    public String [] getData(){
        return new String[]{name.getText(),ip.getText()};
    }

}
