package com.pichangetheworld.eminentdomainnew.util;

import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class FieldDeck {
    List<BaseCard> deck;

    public FieldDeck() {
        deck = new ArrayList<>();
    }

    public void addCardToDeck(BaseCard card) {
        deck.add(card);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public BaseCard drawCardFromField(BasePlayer player) {
        if (!deck.isEmpty()) {
            return deck.remove(0).toUser(player);
        }
        return null;
    }

    public int size() {
        return deck.size();
    }
}
