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
public class PlayerDeck extends Deck {
    List<BaseCard> discards;

    public PlayerDeck() {
        super();
        discards = new ArrayList<>();
    }

    @Override
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

    public BaseCard drawCardFromField(EminentDomainApplication context, BasePlayer player) {
        if (!deck.isEmpty()) {
            return deck.remove(0).toUser(context, player);
        }
        return null;
    }

    public void addDiscard(BaseCard card) {
        discards.add(card);
    }

    public void addDiscards(List<BaseCard> cards) {
        discards.addAll(cards);
    }
}
