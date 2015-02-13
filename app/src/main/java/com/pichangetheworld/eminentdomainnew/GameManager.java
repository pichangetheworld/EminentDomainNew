package com.pichangetheworld.eminentdomainnew;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;
import com.pichangetheworld.eminentdomainnew.player.ComputerPlayer;
import com.pichangetheworld.eminentdomainnew.player.DumbAIPlayer;
import com.pichangetheworld.eminentdomainnew.player.HumanPlayer;
import com.pichangetheworld.eminentdomainnew.util.CallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.Phase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class GameManager {
    private final String NAMES[] = { "Alf", "Bob", "Charles", "Dick" };

    EminentDomainApplication context;
    private List<BasePlayer> mPlayers;
    int mCurrentPlayer;
    Phase mCurrentPhase;

    // Constructor
    public GameManager(EminentDomainApplication context) {
        this.context = context;

        mPlayers = new ArrayList<>();
    }

    private final BasePlanet[] STARTER_PLANETS = {
            new BasePlanet(2, 2, 1, 1),
            new BasePlanet(2, 2, 1, 1),
            new BasePlanet(2, 2, 1, 1),
            new BasePlanet(2, 2, 1, 1),
            new BasePlanet(2, 2, 1, 1),
            new BasePlanet(2, 2, 1, 1)
    };

    public void init(int numPlayers) {
        ArrayList<BasePlanet> starterPlanets = new ArrayList<>();
        starterPlanets.addAll(Arrays.asList(STARTER_PLANETS));
        mPlayers.clear();
        mPlayers.add(new HumanPlayer(context, NAMES[0], 0).init());
        for (int i = 1; i < numPlayers; ++i) {
            mPlayers.add(new DumbAIPlayer(context, NAMES[i], i).init());
        }

        Random r = new Random();
        for (BasePlayer player : mPlayers) {
            int j = r.nextInt(starterPlanets.size());
            player.surveyPlanet(starterPlanets.remove(j));
        }
    }

    public void startGame() {
        Log.d("GameManager", "Game started with " + mPlayers.size() + " players");

        mCurrentPlayer = 0;
        mCurrentPhase = Phase.ACTION_PHASE;

        mPlayers.get(mCurrentPlayer).updateAllViews();
        context.updateViewNextPhase(mCurrentPhase,
                mPlayers.get(mCurrentPlayer).getName(),
                isComputerTurn());
    }

    // Next Phase
    private void nextPhase() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mCurrentPhase = mCurrentPhase.next();
                if (mCurrentPhase == null) {
                    mCurrentPlayer = (mCurrentPlayer + 1) % mPlayers.size();
                    mCurrentPhase = Phase.ACTION_PHASE;
                    mPlayers.get(mCurrentPlayer).updateAllViews();
                }
                context.updateViewNextPhase(mCurrentPhase,
                        mPlayers.get(mCurrentPlayer).getName(),
                        isComputerTurn());
            }
        };
        thread.start();
    }

// ACTION PHASE
    // Current player plays the card at index i
    public void playAction(final int index) {
        final BasePlayer player = mPlayers.get(mCurrentPlayer);
        context.popupPrompt(player.getCardData(index), new CallbackInterface() {
            @Override
            public void callback() {
                player.playCard(index);
            }
        });
    }

    public void endActionPhase() {
        if (mCurrentPhase == Phase.ACTION_PHASE)
            nextPhase();
        else {
            Log.e("GameManager", "Trying to end action phase but we are in " + mCurrentPhase);
        }
    }

// ROLE PHASE
    // Play the role at the selected index
    public void playRole(int index) {
        final BaseCard card = context.getGameField()
                .drawCardFromFieldDeck(context, mPlayers.get(mCurrentPlayer), index);
        CardDrawableData cardData = new CardDrawableData();
        cardData.setData(card);
        context.popupPrompt(cardData, new CallbackInterface() {
            @Override
            public void callback() {
                card.onRole();
            }
        });
    }

    public void endRolePhase() {
        if (mCurrentPhase == Phase.ROLE_PHASE)
            nextPhase();
        else {
            Log.e("GameManager", "Trying to end role phase but we are in " + mCurrentPhase);
        }
    }

// DISCARD / DRAW PHASE
    // Discard selected cards
    public void curPlayerDiscardSelectedCards(List<Integer> selectedCards) {
        mPlayers.get(mCurrentPlayer).discardIndexCards(selectedCards);
        endDiscardDrawPhase();
    }

    private void endDiscardDrawPhase() {
        if (mCurrentPhase == Phase.DISCARD_DRAW_PHASE) {
            mPlayers.get(mCurrentPlayer).drawUpToCardLimit();
            nextPhase();
        } else {
            Log.e("GameManager", "Trying to end discard phase but we are in " + mCurrentPhase);
        }
    }

    // Computer AI
    public boolean isComputerTurn() {
        return mPlayers.get(mCurrentPlayer) instanceof ComputerPlayer;
    }

    private ComputerPlayer getComputerPlayer() {
        return ((ComputerPlayer) mPlayers.get(mCurrentPlayer));
    }

    public int letAISelectTargetHandCard() {
        return getComputerPlayer().selectTargetHandCard();
    }

    public int letAISelectTargetRole() {
        return getComputerPlayer().selectTargetRole();
    }

    public int letAISelectTargetUnconqueredPlanet(boolean allowNone) {
        return getComputerPlayer().selectTargetUnconqueredPlanet(allowNone);
    }

    public int letAISelectTargetConqueredPlanet() {
        return getComputerPlayer().selectTargetConqueredPlanet();
    }

    public List<Integer> letAISelectHandCardsToDiscard() {
        int min = mPlayers.get(mCurrentPlayer).numCardsAboveLimit();
        return getComputerPlayer().selectTargetHandCardsToDiscard(min);
    }
}
