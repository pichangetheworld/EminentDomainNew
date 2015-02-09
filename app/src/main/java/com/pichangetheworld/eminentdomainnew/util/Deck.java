package com.pichangetheworld.eminentdomainnew.util;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Deck {
    List<BaseCard> deck;

    public Deck() {
        deck = new ArrayList<>();
    }

    public void addCardToDeck(BaseCard card) {
        deck.add(card);
    }

    public void addCardsToDeck(List<BaseCard> cards) {
        deck.addAll(cards);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public BaseCard drawCard() {
        if (deck.isEmpty()) {
            // TODO add discards
        }
        return deck.remove(0);
    }

    public BaseCard drawCardFromField(EminentDomainApplication context, BasePlayer player) {
        if (!deck.isEmpty()) {
            return deck.remove(0).toUser(context, player);
        }
        return null;
    }
}
