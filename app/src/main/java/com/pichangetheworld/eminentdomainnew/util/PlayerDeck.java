package com.pichangetheworld.eminentdomainnew.util;

import com.pichangetheworld.eminentdomainnew.card.BaseCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class PlayerDeck {
    List<BaseCard> deck;
    List<BaseCard> discards;

    public PlayerDeck() {
        super();
        deck = new ArrayList<>();
        discards = new ArrayList<>();
    }

    public boolean isEmpty() {
        return deck.isEmpty() && discards.isEmpty();
    }

    public BaseCard drawCard() {
        if (deck.isEmpty()) {
            deck.addAll(discards);
            discards.clear();
            Collections.shuffle(deck);
        }
        return deck.remove(0);
    }

    public void addDiscard(BaseCard card) {
        discards.add(card);
    }

    public void addDiscards(List<BaseCard> cards) {
        discards.addAll(cards);
    }
}
