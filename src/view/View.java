package view;

import model.BlackJack;
import model.Card;
import model.EmptyDeckException;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class View extends JFrame implements ActionListener {
    private BlackJack bj;
    private JPanel playerPanel;
    private JPanel playersPanel;

    public JButton getAnotherButton() {
        return anotherButton;
    }

    public JButton getNoMoreButton() {
        return noMoreButton;
    }

    private JButton anotherButton;
    private JButton noMoreButton;

    public View() {
        bj = new BlackJack();
        JFrame frame = new JFrame("BlackJack GUI");
        frame.setMinimumSize(new Dimension(640, 480));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        FlowLayout topPanelLay = new FlowLayout();
        topPanel.setLayout(topPanelLay);
        topPanel.add(this.anotherButton = new JButton("Pedir"));
        topPanel.add(this.noMoreButton = new JButton("Plantar"));

        this.anotherButton.setActionCommand("another");
        this.anotherButton.addActionListener(this);
        this.noMoreButton.setActionCommand("noMore");
        this.noMoreButton.addActionListener(this);


        GridLayout centerPanelLay = new GridLayout(2, 1);
        centerPanel.setLayout(centerPanelLay);
        this.playersPanel = new JPanel();
        playersPanel.setLayout(new BorderLayout());
        playersPanel.setBorder(BorderFactory.createTitledBorder("Jugadores"));
        this.playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("Mi mano"));
        centerPanel.add(playersPanel);
        centerPanel.add(playerPanel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);

        try {
            //updateBankPanel();
            updatePlayerPanel(null);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            System.exit(-1);
        }


        frame.pack();
        frame.setVisible(true);
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

    public void updatePlayerPanel(List<server.models.Card> cards) throws FileNotFoundException {
        this.playerPanel.removeAll();
        if (cards != null) {
            for (Card c : this.bj.getPlayerCardList()) {
                StringBuilder name = new StringBuilder();
                name.append(c.getColorName() + "_" + c.getValueSymbole());
                try {
                    addToPanel(this.playerPanel, name.toString());
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            JLabel best = new JLabel("Player Best : " + this.bj.getPlayerBest());
            this.playerPanel.add(best);
            if (this.bj.getPlayerBest() == 21) {
                try {
                    addToPanel(this.playerPanel, "blackjack");
                } catch (FileNotFoundException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(-1);
                }
            }
            if (this.bj.isGameFinished()) {
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

    public void actionPerformed(ActionEvent e) {

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
                //updatePlayerPanel(cards);
                break;
            case "noMore":
                //this.bj.bankLastTurn();
                this.anotherButton.setEnabled(false);
                this.noMoreButton.setEnabled(false);
                //updateBankPanel();
                //updatePlayerPanel(cards);
                break;
        }
    }

    public void updateClientConnection(ArrayList<User> users) {
        playersPanel.removeAll();
        JPanel aux = new JPanel();
        if (users.size() < 2) {
            playersPanel.add(new JLabel("Esperando jugadores..."), BorderLayout.NORTH);
        } else {
            playersPanel.add(new JLabel("Juego Iniciado!"), BorderLayout.NORTH);
        }
        aux.setLayout(new GridLayout(users.size() + 1, 1));
        for (User user : users) {
            if (user.isTurn()) {
                aux.add(new JLabel("Turno del jugador " + user.getName()));
            } else {
                aux.add(new JLabel(user.getName()));
            }
        }
        playersPanel.add(aux, BorderLayout.CENTER);
        revalidate();
        repaint();
        playersPanel.updateUI();
    }
}