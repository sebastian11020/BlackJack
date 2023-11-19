package view;

import model.BlackJack;
import model.User;
import server.models.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame {
    private BlackJack bj;
    private JPanel playerPanel;
    private JPanel playersPanel;
    private JButton anotherButton;
    private JButton noMoreButton;
    private boolean enableButtom;
    private String userName;

    public View(ActionListener listener) {
        this.enableButtom = false;

        bj = new BlackJack();
        setMinimumSize(new Dimension(640, 480));
        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        FlowLayout topPanelLay = new FlowLayout();
        topPanel.setLayout(topPanelLay);
        this.anotherButton = new JButton("Pedir");
        this.noMoreButton = new JButton("Plantar");
        this.anotherButton.setEnabled(enableButtom);
        this.noMoreButton.setEnabled(enableButtom);
        topPanel.add(anotherButton);
        topPanel.add(noMoreButton);

        this.anotherButton.setActionCommand("another");
        this.anotherButton.addActionListener(listener);
        this.noMoreButton.setActionCommand("noMore");
        this.noMoreButton.addActionListener(listener);


        GridLayout centerPanelLay = new GridLayout(2, 1);
        centerPanel.setLayout(centerPanelLay);
        this.playersPanel = new JPanel();
        playersPanel.setLayout(new BorderLayout());
        playersPanel.setBorder(BorderFactory.createTitledBorder("Jugadores"));
        this.playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("Mi mano"));
        centerPanel.add(playersPanel);
        centerPanel.add(playerPanel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        try {
            updatePlayerPanel(null, 0, false);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }


        pack();
    }

    public JButton getAnotherButton() {
        return anotherButton;
    }

    public JButton getNoMoreButton() {
        return noMoreButton;
    }

    public void addToPanel(JPanel p, String token) throws FileNotFoundException {
        File file = new File("./img/card_" + token + ".png");
        if (!file.exists())
            throw new FileNotFoundException("Can't find " + file.getPath());
        ImageIcon icon = new ImageIcon(file.getPath());
        JLabel label = new JLabel(icon);
        p.add(label);
    }

    /*public void updateBankPanel() throws FileNotFoundException
    {
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
    }*/

    public void updatePlayerPanel(List<Card> cards, int bestPlayerHand, boolean gameFinished) throws FileNotFoundException {
        this.playerPanel.removeAll();
        if (cards != null) {
            for (Card c : cards) {
                StringBuilder name = new StringBuilder();
                name.append(c.getColorName() + "_" + c.getValueSymbole());
                try {
                    addToPanel(this.playerPanel, name.toString());
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            JLabel best = new JLabel("Player Best : " + bestPlayerHand);
            this.playerPanel.add(best);
            if (bestPlayerHand == 21) {
                try {
                    addToPanel(this.playerPanel, "blackjack");
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (gameFinished) {
                anotherButton.setEnabled(false);
                noMoreButton.setEnabled(false);
            /*if(this.bj.isPlayerWinner())
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
            }*/
            }
            this.playerPanel.updateUI();
        }
    }

    /*public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "another":
                try {
                    this.bj.playerDrawAnotherCard();
                    if (this.bj.isGameFinished()) {
                        this.anotherButton.setEnabled(false);
                        this.noMoreButton.setEnabled(false);
                    } else {
                        this.anotherButton.setEnabled(true);
                        this.noMoreButton.setEnabled(true);
                    }
                } catch (EmptyDeckException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                break;
            case "noMore":
                this.anotherButton.setEnabled(false);
                this.noMoreButton.setEnabled(false);
                break;
            case "reset":
                try {
                    this.bj.reset();
                    this.anotherButton.setEnabled(true);
                    this.noMoreButton.setEnabled(true);
                } catch (EmptyDeckException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
                break;
        }
    }*/

    public void updateClientConnection(ArrayList<User> users) {
        playersPanel.removeAll();
        JPanel aux = new JPanel();
        if (users.size() < 4) {
            playersPanel.add(new JLabel("Esperando jugadores..."), BorderLayout.NORTH);
        } else {
            playersPanel.add(new JLabel("Juego Iniciado!"), BorderLayout.NORTH);
        }
        aux.setLayout(new GridLayout(users.size() + 1, 1));
        for (User user : users) {
            if (user != null) {
                if (user.isTurn()) {
                    aux.add(new JLabel("Turno del jugador " + user.getName()));
                } else {
                    aux.add(new JLabel(user.getName()));
                }
                if (user.getName().equals(userName) && user.isTurn()) {
                    this.anotherButton.setEnabled(true);
                    this.noMoreButton.setEnabled(true);
                }
            }
        }
        playersPanel.add(aux, BorderLayout.CENTER);
        revalidate();
        repaint();
        playersPanel.updateUI();
    }

    public void setUserName(String userName) {
        this.userName = userName;
        revalidate();
        repaint();
    }
}