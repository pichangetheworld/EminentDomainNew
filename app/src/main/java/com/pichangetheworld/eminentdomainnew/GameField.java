package com.pichangetheworld.eminentdomainnew;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.card.Colonize;
import com.pichangetheworld.eminentdomainnew.card.ProduceTrade;
import com.pichangetheworld.eminentdomainnew.card.Research;
import com.pichangetheworld.eminentdomainnew.card.Survey;
import com.pichangetheworld.eminentdomainnew.card.Warfare;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;
import com.pichangetheworld.eminentdomainnew.util.FieldDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class GameField {
    FieldDeck[] fieldDecks = {
            new FieldDeck(), // Survey
            new FieldDeck(), // Warfare
            new FieldDeck(), // Colonize
            new FieldDeck(), // ProduceTrade
            new FieldDeck()  // Research
    };
    List<BasePlanet> planetDeck;

    public GameField() {
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
            Log.d("GameField", "Field deck is null");
            // TODO broadcast to view that the deck is empty and scene should be redrawn
            switch (index) {
                case 0:
                    return new Survey();
                case 1:
                    return new Warfare();
                case 2:
                    return new Colonize();
                case 3:
                    return new ProduceTrade();
                case 4:
                    return new Research();
            }
            return null;
        }
        return fieldDecks[index].drawCardFromField(context, player);
    }
}
