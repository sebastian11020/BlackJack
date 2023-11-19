package model;

import server.models.Card;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public interface IObserver {
    void updateClientsConnection(ArrayList<User> users);

    void updateInitsCards(List<Card> cards) throws FileNotFoundException;
}
