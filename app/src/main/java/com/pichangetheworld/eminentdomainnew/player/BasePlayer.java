package com.pichangetheworld.eminentdomainnew.player;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.card.Politics;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public abstract class BasePlayer {
//    final int[] STARTING_DECK = { 0, 0, 1, 2, 2, 3, 3, 4, 4 };
    final int[] STARTING_DECK = { 0, 0, 0, 0, 0, 0 };

    private final Intent mHandChangedIntent = new Intent("HAND_UPDATED");

    EminentDomainApplication context;
    String name;
    int id;

    int cardLimit;

    Deck deck;
    List<BaseCard> discardPile;
    List<BasePlanet> surveyedPlanets;

    List<BaseCard> hand;
    int numShips;

    // Getters
    public String getName() { return name; }
    public int getNumShips() { return numShips; }
    public List<BasePlanet> getSurveyedPlanets() { return surveyedPlanets; }

    // Constructor
    BasePlayer(EminentDomainApplication context, String name, int id) {
        this.context = context;
        this.name = name;
        this.id = id;

        cardLimit = 5;
        numShips = 0;

        deck = new Deck();
        discardPile = new ArrayList<>();
        hand = new ArrayList<>();
        surveyedPlanets = new ArrayList<>();
    }

    public BasePlayer init() {
        for (int index : STARTING_DECK)
            discardCard(EminentDomainApplication.getInstance()
                    .getGameField().drawCardFromFieldDeck(context, this, index));
        discardCard(new Politics().toUser(context, this));
        drawUpToCardLimit();
        return this;
    }

    public void drawUpToCardLimit() {
        drawCards(cardLimit - hand.size());
    }

    public void addCardToHand(BaseCard card) {
        hand.add(card);
    }
    // Remove a card from the user's hand
    public void useCard(BaseCard card) {
        hand.remove(card);
    }
    public void useCardIndex(int index) { hand.remove(index); }

    // Draw a card into the hand
    public void drawCards(int n) {
        for (int i = 0; i < n; ++i) {
            if (deck.isEmpty()) {
                deck.addCardsToDeck(discardPile);
                discardPile.clear();
                deck.shuffle();
            }
            if (deck.isEmpty()) break;
            hand.add(deck.drawCard());
        }
        if (this instanceof HumanPlayer) {
            broadcastHandUpdated();
        }
    }

    // Add a card to the discard pile
    public void discardCard(BaseCard card) {
        discardPile.add(card);
    }

    // Add cards to the discard pile
    public void discardCards(List<BaseCard> cards) {
        discardPile.addAll(cards);
    }

    public void gainShips(int n) {
        numShips += n;
    }

    // Broadcast to view that phase has ended
    private void broadcastHandUpdated() {
        ArrayList<CardDrawableData> handDrawables = new ArrayList<>();
        for (int i = 0; i < hand.size(); ++i) {
            CardDrawableData cd = new CardDrawableData();
            cd.setData(hand.get(i));
            handDrawables.add(cd);
        }
        mHandChangedIntent.putParcelableArrayListExtra("drawable", handDrawables);
        Log.d("BasePlayer", "Broadcasting hand changed " + handDrawables.size());
        LocalBroadcastManager.getInstance(context).sendBroadcast(mHandChangedIntent);
    }

    // Play the card at index i
    public void playCard(int index) {
        hand.remove(index).onAction();
    }
}
