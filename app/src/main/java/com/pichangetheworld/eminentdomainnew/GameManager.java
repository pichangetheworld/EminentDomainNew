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
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.PlanetFactory;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class GameManager {
    private final String NAMES[] = {"Alf", "Bob", "Charles", "Dick"};

    EminentDomainApplication context;
    private List<BasePlayer> mPlayers;
    private List<BasePlanet> planetDeck;
    boolean gameEnding;
    int mStartingPlayer;
    int mCurrentPlayer;
    Phase mCurrentPhase;

    // Constructor
    public GameManager(EminentDomainApplication context) {
        this.context = context;

        mPlayers = new ArrayList<>();
        planetDeck = new ArrayList<>();
        mPlanetsBeingSurveyed = new ArrayList<>();
    }

    // Getter
    public int getPlayerCount() {
        return mPlayers.size();
    }

    // Starter planets (constant)
    private final BasePlanet[] STARTER_PLANETS = {
            PlanetFactory.getPlanet("Start1,Metallic,2,2,1,1"),
            PlanetFactory.getPlanet("Start2,Metallic,2,2,1,1"),
            PlanetFactory.getPlanet("Start3,Fertile,2,2,1,1"),
            PlanetFactory.getPlanet("Start4,Fertile,2,2,1,1"),
            PlanetFactory.getPlanet("Start5,Advanced,2,2,1,1"),
            PlanetFactory.getPlanet("Start6,Advanced,2,2,1,1")
    };

    // Initialize (setup) game
    public void init(int numPlayers) {
        ArrayList<BasePlanet> starterPlanets = new ArrayList<>();
        starterPlanets.addAll(Arrays.asList(STARTER_PLANETS));
        mPlayers.clear();
        mPlayers.add(new HumanPlayer(context, NAMES[0], 0).init());
        for (int i = 1; i < numPlayers; ++i) {
            mPlayers.add(new DumbAIPlayer(context, NAMES[i], i).init());
        }
        initPlanets();

        Random r = new Random();
        for (BasePlayer player : mPlayers) {
            int j = r.nextInt(starterPlanets.size());
            player.surveyPlanet(starterPlanets.remove(j));
        }
    }

    // Initialize Planets
    private void initPlanets() {
        planetDeck.clear();

        InputStream inputStream = context.getResources().openRawResource(R.raw.planet_list);
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isr);
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("#")) continue; // Skip lines starting with #
                BasePlanet planet = PlanetFactory.getPlanet(line);
                if (planet != null)
                    planetDeck.add(planet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.shuffle(planetDeck);
    }

    // Start the actual game
    public void startGame() {
        Log.d("GameManager", "Game started with " + mPlayers.size() + " players");

        mStartingPlayer = new Random().nextInt(mPlayers.size());

        mCurrentPlayer = mStartingPlayer;
        mCurrentPhase = Phase.ACTION_PHASE;

        gameEnding = false;

        mPlayers.get(mCurrentPlayer).updateAllViews();
        context.updateViewNextPhase(mCurrentPhase,
                mPlayers.get(mCurrentPlayer).getName(),
                isComputerTurn());
    }

    // When the end of the game is triggered, toggle the end of game
    public void startEndGameCountdown() {
        Log.d("GameManager", "End game condition reached, beginning end game countdown");
        gameEnding = true;
    }

    // End the game
    private void endGame() {
        // go to end of game page
        context.endGame();

        // check each player's score
        int[] scores = new int[mPlayers.size()];
        for (int i = 0; i < mPlayers.size(); ++i) {
            scores[i] = mPlayers.get(i).calculateFinalScore();
        }

        // highest score wins!
        int maxScore = 0;
        for (int i = 1; i < scores.length; ++i) {
            if (scores[i] > scores[maxScore]) {
                maxScore = i;
            }
        }

        // set winner
        context.setWinner(mPlayers.get(maxScore).getName());
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

                    // TODO: This should no longer happen
                    mPlayers.get(mCurrentPlayer).updateAllViews();
                }
                if (gameEnding && mCurrentPlayer == mStartingPlayer
                        && mCurrentPhase == Phase.ACTION_PHASE) {
                    // we're done
                    Log.d("GameManager", "Starting player was " + mStartingPlayer);
                    endGame();
                } else {
                    context.updateViewNextPhase(mCurrentPhase,
                            mPlayers.get(mCurrentPlayer).getName(),
                            isComputerTurn());
                }
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
                card.setUpMatchRole();
            }
        });
    }

    private List<BasePlanet> mPlanetsBeingSurveyed;
    public void surveyPlanets(int count) {
        if (count > 0) {
            for (int i = 0; i < count; ++i) {
                mPlanetsBeingSurveyed.add(planetDeck.remove(0));
            }
            broadcastSurveyPlanets();
        } else {
            endRolePhase();
        }
    }

    // Broadcast to view that planets has changed
    public void broadcastSurveyPlanets() {
        ArrayList<PlanetDrawableData> planetDrawables = new ArrayList<>();
        for (int i = 0; i < mPlanetsBeingSurveyed.size(); ++i) {
            PlanetDrawableData pd = new PlanetDrawableData();
            pd.setData(mPlanetsBeingSurveyed.get(i));
            planetDrawables.add(pd);
        }
        context.updateSurveyedPlanets(planetDrawables,
                new TargetCallbackInterface() {
                    @Override
                    public void callback(int index) {
                        mPlayers.get(mCurrentPlayer).surveyPlanet(mPlanetsBeingSurveyed.remove(index));
                        planetDeck.addAll(mPlanetsBeingSurveyed);
                        mPlanetsBeingSurveyed.clear();

                        endRolePhase();
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

    // Action
    public int letAISelectTargetHandCard() {
        return getComputerPlayer().selectTargetHandCard();
    }

    // Role
    public int letAISelectTargetRole() {
        return getComputerPlayer().selectTargetRole();
    }

    // Produce / Trade
    public int letAIChooseProduceTrade() {
        return getComputerPlayer().chooseProduceTrade();
    }

    // Select target planet
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

    public List<Integer> letAISelectHandCardsToRemove(int max) {
        return getComputerPlayer().selectTargetHandCardsToRemove(max);
    }

    public void onMatchRoleCallback(RoleCountCallbackInterface callback,
                                    List<Integer> targets) {
        mPlayers.get(mCurrentPlayer).onMatchRoleCallback(callback, targets);
    }
}
