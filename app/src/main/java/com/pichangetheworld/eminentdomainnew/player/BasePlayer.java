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
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public abstract class BasePlayer {
//    final int[] STARTING_DECK = { 0, 0, 1, 2, 2, 3, 3, 4, 4 };
    final int[] STARTING_DECK = { 0, 0, 0, 0, 0, 0 }; // TODO for testing

    private final Intent mHandChangedIntent = new Intent("HAND_UPDATED");
    private final Intent mPlanetsChangedIntent = new Intent("PLANETS_UPDATED");

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

    // Planets
    public void surveyPlanet(BasePlanet planet) {
        surveyedPlanets.add(planet);
        planet.survey(this);
        if (this instanceof HumanPlayer) {
            broadcastPlanetsUpdated();
        }
    }

    // Hand cards
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

    // Broadcast to view that hand has changed
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

    // Broadcast to view that planets has changed
    private void broadcastPlanetsUpdated() {
        ArrayList<PlanetDrawableData> planetDrawables = new ArrayList<>();
        for (int i = 0; i < surveyedPlanets.size(); ++i) {
            PlanetDrawableData pd = new PlanetDrawableData();
            pd.setData(surveyedPlanets.get(i));
            planetDrawables.add(pd);
        }
        mPlanetsChangedIntent.putParcelableArrayListExtra("drawable", planetDrawables);
        Log.d("BasePlayer", "Broadcasting planets changed " + planetDrawables.size());
        LocalBroadcastManager.getInstance(context).sendBroadcast(mPlanetsChangedIntent);
    }

    // Play the card at index i
    public void playCard(int index) {
        hand.remove(index).onAction();
    }
}
