package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import model.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class View extends JFrame implements ActionListener {
    private ServerSocket serverSocket;
    private ArrayList<ObjectOutputStream> outputStreams;
    private ArrayList<Player> players;
    private BlackJack bj;
    private JPanel playerPanel;
    private JPanel bankPanel;

    public JButton getAnotherButton() {
        return anotherButton;
    }

    public JButton getNoMoreButton() {
        return noMoreButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    private JButton anotherButton;
    private JButton noMoreButton;
    private JButton resetButton;
    private JButton connectButton;

    public View() {
        bj = new BlackJack();
        JFrame frame = new JFrame("BlackJack GUI");
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        FlowLayout topPanelLay = new FlowLayout();
        topPanel.setLayout(topPanelLay);
        topPanel.add(this.connectButton = new JButton("Connect"));
        topPanel.add(this.anotherButton = new JButton("Another Card"));
        topPanel.add(this.noMoreButton = new JButton("No more Card"));
        topPanel.add(this.resetButton = new JButton("Reset"));

        this.connectButton.setActionCommand("connect");
        this.connectButton.addActionListener(this);
        this.anotherButton.setActionCommand("another");
        this.anotherButton.addActionListener(this);
        this.noMoreButton.setActionCommand("noMore");
        this.noMoreButton.addActionListener(this);
        this.resetButton.setActionCommand("reset");
        this.resetButton.addActionListener(this);


        GridLayout centerPanelLay = new GridLayout(2, 1);
        centerPanel.setLayout(centerPanelLay);
        this.bankPanel = new JPanel();
        bankPanel.setBorder(BorderFactory.createTitledBorder("Bank"));
        this.playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("Player"));
        centerPanel.add(bankPanel);
        centerPanel.add(playerPanel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        try {
            serverSocket = new ServerSocket(8888);
            outputStreams = new ArrayList<>();
            players = new ArrayList<>();

            System.out.println("Server is waiting for players...");

            for (int i = 0; i < 4; i++) {
                // Espera la conexión de hasta 4 jugadores
                System.out.println("Waiting for player " + (i + 1) + " to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Player " + (i + 1) + " connected!");
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                outputStreams.add(oos);

                // Recibe información del jugador desde el cliente
                Player player = (Player) new ObjectInputStream(clientSocket.getInputStream()).readObject();
                players.add(player);
            }

            startGame();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try
        {
            updateBankPanel();
            updatePlayerPanel();
        }
        catch(FileNotFoundException ex)
        {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }



        frame.pack();
        frame.setVisible(true);
    }
    private void startGame() {
        try {
            for (ObjectOutputStream oos : outputStreams) {
                oos.writeObject(bj);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public void addToPanel(JPanel p, String token) throws FileNotFoundException
    {
        File file = new File("./img/card_"+token+".png");
        if(!file.exists())
            throw new FileNotFoundException("Can't find "+file.getPath());
        ImageIcon icon = new ImageIcon(file.getPath());
        JLabel label = new JLabel(icon);
        p.add(label);
    }

    private void updateClients(BlackJack updatedGame) throws IOException {
        for (ObjectOutputStream oos : outputStreams) {
            oos.writeObject(updatedGame);
            oos.flush();
        }
    }

    public void updateBankPanel() throws FileNotFoundException {
        try {
            for (ObjectOutputStream oos : outputStreams) {
                oos.writeObject(bj);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.bankPanel.removeAll();
        for(Card c : this.bj.getBankCardList())
        {
            StringBuilder name = new StringBuilder();
            name.append(c.getColorName()+"_"+c.getValueSymbole());
            try
            {
                addToPanel(this.bankPanel,name.toString());
            }
            catch(FileNotFoundException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
        JLabel best = new JLabel("Player Best : "+this.bj.getBankBest());
        this.bankPanel.add(best);
        if(this.bj.getBankBest() == 21)
        {
            try
            {
                addToPanel(this.bankPanel,"blackjack");
            }
            catch(FileNotFoundException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
        if(this.bj.isGameFinished())
        {
            if(this.bj.isBankWinner())
            {
                try
                {
                    addToPanel(this.bankPanel,"winner");
                }
                catch(FileNotFoundException ex)
                {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            else
            {
                try
                {
                    addToPanel(this.bankPanel,"looser");
                }
                catch(FileNotFoundException ex)
                {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
        }
        this.bankPanel.updateUI();

        try {
            updateClients(bj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerPanel() throws FileNotFoundException
    {
        try {
            for (ObjectOutputStream oos : outputStreams) {
                oos.writeObject(bj);
                oos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.playerPanel.removeAll();
        for(Card c : this.bj.getPlayerCardList())
        {
            StringBuilder name = new StringBuilder();
            name.append(c.getColorName()+"_"+c.getValueSymbole());
            try
            {
                addToPanel(this.playerPanel,name.toString());
            }
            catch(FileNotFoundException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
        JLabel best = new JLabel("Player Best : "+this.bj.getPlayerBest());
        this.playerPanel.add(best);
        if(this.bj.getPlayerBest() == 21)
        {
            try
            {
                addToPanel(this.playerPanel,"blackjack");
            }
            catch(FileNotFoundException ex)
            {
                System.err.println(ex.getMessage());
                System.exit(-1);
            }
        }
        if(this.bj.isGameFinished())
        {
            if(this.bj.isPlayerWinner())
            {
                try
                {
                    addToPanel(this.playerPanel,"winner");
                }
                catch(FileNotFoundException ex)
                {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            else if(this.bj.getPlayerBest() == this.bj.getBankBest())
            {
                try
                {
                    addToPanel(this.playerPanel,"draw");
                }
                catch(FileNotFoundException ex)
                {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            else
            {
                try
                {
                    addToPanel(this.playerPanel,"looser");
                }
                catch(FileNotFoundException ex)
                {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
        }
        this.playerPanel.updateUI();
        try {
            updateClients(bj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void connectPlayer() {
        String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                Socket socket = new Socket("localhost", 8888);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                Player player = new Player(playerName);
                oos.writeObject(player);
                oos.flush();
                outputStreams.add(oos);


                players.add(player);

                JOptionPane.showMessageDialog(null, "Connected as: " + playerName, "Player Connected", JOptionPane.INFORMATION_MESSAGE);

                if (players.size() == 4) {
                    startGame();
                }

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error connecting to the server", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid player name", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "connect":
                connectPlayer();
                break;
            case "another":
                try {
                    this.bj.playerDrawAnotherCard();
                    if (this.bj.isGameFinished()) {
                        disableButtons();
                    } else {
                        enableButtons();
                    }
                } catch (EmptyDeckException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                try {
                    updatePlayerPanel();
                } catch (FileNotFoundException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                break;
            case "noMore":
                try {
                    this.bj.bankLastTurn();
                    disableButtons();
                } catch (EmptyDeckException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                try {
                    updateBankPanel();
                    updatePlayerPanel();
                } catch (FileNotFoundException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                break;
            case "reset":
                try {
                    this.bj.reset();
                    enableButtons();
                } catch (EmptyDeckException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                try {
                    updatePlayerPanel();
                    updateBankPanel();
                } catch (FileNotFoundException ex) {
                    handleErrorMessage(ex.getMessage());
                    System.exit(-1);
                }
                break;
        }

    }
    private void enableButtons() {
        this.anotherButton.setEnabled(true);
        this.noMoreButton.setEnabled(true);
    }

    private void disableButtons() {
        this.anotherButton.setEnabled(false);
        this.noMoreButton.setEnabled(false);
    }

    private void handleErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}