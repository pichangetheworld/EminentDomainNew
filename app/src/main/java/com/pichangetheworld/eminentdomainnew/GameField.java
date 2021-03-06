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

    EminentDomainApplication context;

    int[] fieldDeckCounts = new int[5];

    public GameField(EminentDomainApplication context) {
        this.context = context;

        int i;
        for (i = 0; i < 20; ++i) fieldDecks[0].addCardToDeck(new Survey(context));
        for (i = 0; i < 16; ++i) fieldDecks[1].addCardToDeck(new Warfare(context));
        for (i = 0; i < 20; ++i) fieldDecks[2].addCardToDeck(new Colonize(context));
        for (i = 0; i < 20; ++i) fieldDecks[3].addCardToDeck(new ProduceTrade(context));
        for (i = 0; i < 20; ++i) fieldDecks[4].addCardToDeck(new Research(context));

        planetDeck = new ArrayList<>();
    }

    public BaseCard drawCardFromFieldDeck(EminentDomainApplication context, BasePlayer player, int index) {
        if (fieldDecks[index].isEmpty()) {
            Log.d("GameField", "Field deck is empty");
            BaseCard card;
            switch (index) {
                case 0:
                    card = new Survey(context);
                    break;
                case 1:
                    card = new Warfare(context);
                    break;
                case 2:
                    card = new Colonize(context);
                    break;
                case 3:
                    card = new ProduceTrade(context);
                    break;
                case 4:
                    card = new Research(context);
                    break;
                default:
                    card = new Survey(context);
            }
            card.temporaryUser(player);
            return card;
        }
        BaseCard card = fieldDecks[index].drawCardFromField(player);
        broadcastFieldDecksUpdated();
        context.checkEndOfGame(fieldDeckCounts);
        return card;
    }

    // Broadcast to view that field decks has changed
    private void broadcastFieldDecksUpdated() {
        for (int i = 0; i < fieldDecks.length; ++i) {
            fieldDeckCounts[i] = fieldDecks[i].size();
        }
        context.updateField(fieldDeckCounts);
    }
}
