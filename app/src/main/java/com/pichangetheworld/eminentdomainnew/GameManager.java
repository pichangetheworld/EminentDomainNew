package com.pichangetheworld.eminentdomainnew;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;
import com.pichangetheworld.eminentdomainnew.player.ComputerPlayer;
import com.pichangetheworld.eminentdomainnew.player.DumbAIPlayer;
import com.pichangetheworld.eminentdomainnew.player.HumanPlayer;
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
    GameManager(EminentDomainApplication context) {
        this.context = context;

        mPlayers = new ArrayList<>();
    }

    private final BasePlanet[] STARTER_PLANETS = {
            new BasePlanet(2, 2, 1),
            new BasePlanet(2, 2, 1),
            new BasePlanet(2, 2, 1),
            new BasePlanet(2, 2, 1),
            new BasePlanet(2, 2, 1),
            new BasePlanet(2, 2, 1)
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

        actionPhase();
    }

    public void nextPhase() {
        mCurrentPhase = mCurrentPhase.next();
        if (mCurrentPhase == null) {
            mCurrentPlayer = (mCurrentPlayer + 1) % mPlayers.size();
            mCurrentPhase = Phase.ACTION_PHASE;
        }
        switch (mCurrentPhase) {
            case ACTION_PHASE:
                actionPhase();
                break;
            case ROLE_PHASE:
                rolePhase();
                break;
            case DISCARD_DRAW_PHASE:
                discardDrawPhase();
                break;
            default:
                // should never get here
        }
    }

    public void actionPhase() {
        context.updateViewNextPhase(Phase.ACTION_PHASE, mPlayers.get(mCurrentPlayer).getName());
    }

    // Current player plays the card at index i
    public void playAction(int index) {
        mPlayers.get(mCurrentPlayer).playCard(index);
    }

    public void endActionPhase() {
        nextPhase();
    }

    public void rolePhase() {
        context.updateViewNextPhase(Phase.ROLE_PHASE, mPlayers.get(mCurrentPlayer).getName());
    }

    // Play the role at the selected index
    public void playRole(int index) {
        context.getGameField().drawCardFromFieldDeck(context, mPlayers.get(mCurrentPlayer), index)
                .onRole();
    }

    public void endRolePhase() {
        nextPhase();
    }

    public void discardDrawPhase() {
        context.updateViewNextPhase(Phase.DISCARD_DRAW_PHASE, mPlayers.get(mCurrentPlayer).getName());
    }

    public void curPlayerDiscardSelectedCards(List<Integer> selectedCards) {
        mPlayers.get(mCurrentPlayer).discardIndexCards(selectedCards);
        endDiscardDrawPhase();
    }

    private void endDiscardDrawPhase() {
        mPlayers.get(mCurrentPlayer).drawUpToCardLimit();
        nextPhase();
    }

    // Computer
    public boolean isComputerTurn() {
        return mPlayers.get(mCurrentPlayer) instanceof ComputerPlayer;
    };

    public int letAISelectTargetPlanet() {
        return ((ComputerPlayer) mPlayers.get(mCurrentPlayer)).selectTargetPlanet();
    }
}
