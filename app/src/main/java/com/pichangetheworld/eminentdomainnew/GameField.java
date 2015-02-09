package com.pichangetheworld.eminentdomainnew;

import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.card.Colonize;
import com.pichangetheworld.eminentdomainnew.card.ProduceTrade;
import com.pichangetheworld.eminentdomainnew.card.Research;
import com.pichangetheworld.eminentdomainnew.card.Survey;
import com.pichangetheworld.eminentdomainnew.card.Warfare;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;
import com.pichangetheworld.eminentdomainnew.util.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class GameField {
    Deck[] fieldDecks = {
            new Deck(), // Survey
            new Deck(), // Warfare
            new Deck(), // Colonize
            new Deck(), // ProduceTrade
            new Deck()  // Research
    };
    List<BasePlanet> planetDeck;

    GameField() {
        int i;
        for (i = 0; i < 20; ++i) fieldDecks[0].addCardToDeck(new Survey());
        for (i = 0; i < 16; ++i) fieldDecks[1].addCardToDeck(new Warfare());
        for (i = 0; i < 20; ++i) fieldDecks[2].addCardToDeck(new Colonize());
        for (i = 0; i < 20; ++i) fieldDecks[3].addCardToDeck(new ProduceTrade());
        for (i = 0; i < 20; ++i) fieldDecks[4].addCardToDeck(new Research());

        planetDeck = new ArrayList<>();
    }

    public BaseCard drawCardFromFieldDeck(EminentDomainApplication context, BasePlayer player, int index) {
        if (fieldDecks[index].isEmpty()) {
            // TODO broadcast to view that the deck is empty and scene should be redrawn
            return null;
        }
        return fieldDecks[index].drawCardFromField(context, player);
    }
}
