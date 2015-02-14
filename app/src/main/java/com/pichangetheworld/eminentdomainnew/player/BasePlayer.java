package com.pichangetheworld.eminentdomainnew.player;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.card.Politics;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.PlayerDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public abstract class BasePlayer {
    final int[] STARTING_DECK = { 0, 0, 1, 2, 2, 3, 3, 4, 4 };
//    final int[] STARTING_DECK = { 2, 2, 2, 2, 2, 2 }; // for testing

    EminentDomainApplication context;
    String name;
    int id;

    PlayerDeck deck;
    List<BasePlanet> surveyedPlanets;

    int handCardLimit;
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

        handCardLimit = 5;
        numShips = 0;

        deck = new PlayerDeck();
        hand = new ArrayList<>();
        surveyedPlanets = new ArrayList<>();
    }

    public BasePlayer init() {
        for (int index : STARTING_DECK) {
            BaseCard card = context.getGameField()
                    .drawCardFromFieldDeck(context, this, index);
            if (card != null)
                discardCard(card);
        }
        discardCard(new Politics().toUser(context, this));
        drawUpToCardLimit();
        return this;
    }

    // Planets
    public void surveyPlanet(BasePlanet planet) {
        surveyedPlanets.add(planet);
        planet.survey(this);
//        if (this instanceof HumanPlayer) {
            broadcastPlanetsUpdated();
//        }
    }

    // Hand cards
    public void drawUpToCardLimit() {
        drawCards(handCardLimit - hand.size());
    }

    public void addCardToHand(BaseCard card) {
        hand.add(card);
        broadcastHandUpdated();
    }
    // Remove a card from the user's hand
    public void useCard(BaseCard card) {
        hand.remove(card);
        broadcastHandUpdated();
    }
    public void removeFromHand(int index) { hand.remove(index); }

    // Draw a card into the hand
    public void drawCards(int n) {
        for (int i = 0; i < n; ++i) {
            if (deck.isEmpty()) break;
            hand.add(deck.drawCard());
        }
//        if (this instanceof HumanPlayer) {
            broadcastHandUpdated();
//        }
    }

    // Add a card to the discard pile
    public void discardCard(BaseCard card) {
        deck.addDiscard(card);
        broadcastDiscardPileUpdated();
    }

    // Add cards to the discard pile
    public void discardCards(List<BaseCard> cards) {
        deck.addDiscards(cards);
        broadcastDiscardPileUpdated();
    }

    public void discardIndexCards(List<Integer> indices) {
        List<BaseCard> toDiscard = new ArrayList<>();
        for (int i : indices) {
            toDiscard.add(hand.get(i));
        }
        for (BaseCard card : toDiscard) {
            hand.remove(card);
        }
        discardCards(toDiscard);
        broadcastHandUpdated();
    }

    public void gainShips(int n) {
        numShips += n;
        context.updateShipCount(numShips);
    }

    // Broadcast to view that hand has changed
    private void broadcastHandUpdated() {
        ArrayList<CardDrawableData> handDrawables = new ArrayList<>();
        for (int i = 0; i < hand.size(); ++i) {
            CardDrawableData cd = new CardDrawableData();
            cd.setData(hand.get(i));
            handDrawables.add(cd);
        }
        context.updateHand(handDrawables);
    }

    private void broadcastDiscardPileUpdated() {
        context.updateDiscardPile(deck.getDiscardDrawables());
    }

    // Broadcast to view that planets has changed
    public void broadcastPlanetsUpdated() {
        ArrayList<PlanetDrawableData> planetDrawables = new ArrayList<>();
        for (int i = 0; i < surveyedPlanets.size(); ++i) {
            PlanetDrawableData pd = new PlanetDrawableData();
            pd.setData(surveyedPlanets.get(i));
            planetDrawables.add(pd);
        }
        context.updatePlanets(planetDrawables);
    }

    // Play the card at index i
    public void playCard(int index) {
        hand.remove(index).onAction();
    }

    public void updateAllViews() {
        broadcastHandUpdated();
        broadcastPlanetsUpdated();
        broadcastDiscardPileUpdated();
    }

    public int numCardsAboveLimit() {
        return Math.max(0, hand.size() - handCardLimit);
    }

    public CardDrawableData getCardData(int index) {
        CardDrawableData cd = new CardDrawableData();
        cd.setData(hand.get(index));
        return cd;
    }
}
