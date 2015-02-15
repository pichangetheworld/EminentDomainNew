package com.pichangetheworld.eminentdomainnew.application;

import android.app.Application;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.GameField;
import com.pichangetheworld.eminentdomainnew.GameManager;
import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.util.CallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.IconType;
import com.pichangetheworld.eminentdomainnew.util.MultiTargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.Phase;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class EminentDomainApplication extends Application {
    private static EminentDomainApplication instance;

    public static EminentDomainApplication getInstance() {
        return instance;
    }

    private GameManager gameManager = null;
    private GameField gameField = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (gameManager == null) {
            gameManager = new GameManager(this);
        }
        if (gameField == null) {
            gameField = new GameField(this);
        }
    }

    private GameActivity activity;

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public void startGame(int numPlayers) {
        gameManager.init(numPlayers);
        gameManager.startGame();
    }

    public GameField getGameField() {
        return gameField;
    }

    public void updateViewNextPhase(Phase phase, String name, boolean isComputer) {
        Log.d("NextPhaseStart", "Got phase start: " + phase);
        switch (phase) {
            case ACTION_PHASE:
                actionPhase(name, isComputer);
                break;
            case ROLE_PHASE:
                rolePhase();
                break;
            case DISCARD_DRAW_PHASE:
                discardDrawPhase(isComputer);
                break;
            default:
        }
    }

    // Action Phase
    private void actionPhase(String name, boolean isComputer) {
        activity.actionPhase(name, isComputer);

        if (isComputer) {
            int index = gameManager.letAISelectTargetHandCard();
            Log.d("ActionPhase", name + " plays card at " + index);
            playAction(index);
        }
    }

    // Play the card at index i
    // If i == -1 skip
    public void playAction(int index) {
        if (index == -1) {
            endActionPhase();
        } else {
            gameManager.playAction(index);
        }
    }

    public void endActionPhase() {
        gameManager.endActionPhase();
    }

    // Role Phase
    private void rolePhase() {
        activity.rolePhase();

        selectTargetRole(new TargetCallbackInterface() {
            @Override
            public void callback(int index) {
                playRole(index);
            }
        });
    }

    // Play the role at index i
    public void playRole(int index) {
        gameManager.playRole(index);
    }

    public void endRolePhase() {
        gameManager.endRolePhase();
    }

    // Discard/Draw Phase
    private void discardDrawPhase(boolean isComputer) {
        activity.discardDrawPhase(isComputer);

        chooseCardsToDiscard();
    }

    private void discardSelectedCards(List<Integer> selectedCards) {
        gameManager.curPlayerDiscardSelectedCards(selectedCards);
    }

    // Let current player decide whether to produce or trade after playing Produce/Trade
    public void chooseProduceTrade(TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAIChooseProduceTrade();
            callback.callback(index);
        } else {
            activity.letPlayerChooseProduceTrade(callback);
        }
    }

    // Let current player choose target role card, e.g. for Role phase or Politics
    public void selectTargetRole(TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAISelectTargetRole();
            callback.callback(index);
        } else {
            activity.letPlayerChooseTargetRole(callback);
        }
    }

    // Let player match cards from his hand matching selected role
    public void matchRole(IconType iconType, final RoleCountCallbackInterface callback) {
        MultiTargetCallbackInterface multiCallback = new MultiTargetCallbackInterface() {
            @Override
            public void callback(List<Integer> targets) {
                gameManager.onMatchRoleCallback(callback, targets);
            }
        };
        activity.letPlayerMatchRole(iconType, multiCallback);
    }

    // Survey Role
    public void surveyPlanets(int count) {
        gameManager.surveyPlanets(count);
    }

    private final MultiTargetCallbackInterface DISCARD_CALLBACK = new MultiTargetCallbackInterface() {
        @Override
        public void callback(List<Integer> targets) {
            discardSelectedCards(targets);
        }
    };

    // Let current player choose cards to discard from his hand for Discard Phase
    private void chooseCardsToDiscard() {
        if (gameManager.isComputerTurn()) {
            List<Integer> toDiscard = gameManager.letAISelectHandCardsToDiscard();
            discardSelectedCards(toDiscard);
        } else {
            activity.letPlayerChooseDiscards(DISCARD_CALLBACK);
        }
    }

    // Let current player select target unconquered planet, e.g. for Warfare, Colonize
    public void selectTargetUnconqueredPlanet(boolean allowNone,
                                              TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAISelectTargetUnconqueredPlanet(allowNone);
            callback.callback(index);
        } else {
            activity.letPlayerChooseTargetUnconqueredPlanet(allowNone, callback);
        }
    }

    // Let current player select target conquered planet, e.g. for Produce/Trade
    public void selectTargetConqueredPlanet(TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAISelectTargetConqueredPlanet();
            callback.callback(index);
        } else {
            activity.letPlayerChooseTargetConqueredPlanet(callback);
        }
    }

    // Let current player select cards to remove from the game
    public void selectCardsToRemove(int max, MultiTargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            List<Integer> toRemove = gameManager.letAISelectHandCardsToRemove(max);
            callback.callback(toRemove);
        } else {
            activity.selectCardsToRemove(max, callback);
        }
    }

    // Popup
    public void popupPrompt(CardDrawableData data, CallbackInterface callback) {
        activity.popupPrompt(data, callback);
    }

    // Views
    public void updateHand(ArrayList<CardDrawableData> drawables, int handLimit) {
        activity.updateHand(drawables, handLimit);
    }

    // Update ship count
    public void updateShipCount(int shipCount) {
        activity.updateShipCount(shipCount);
    }

    public void updatePlanets(ArrayList<PlanetDrawableData> drawables) {
        activity.updatePlanets(drawables);
    }

    public void updateDiscardPile(ArrayList<CardDrawableData> drawables) {
        activity.updateDiscardPile(drawables);
    }

    public void updateField(int[] fieldDeckCounts) {
        activity.updateField(fieldDeckCounts);
    }

    public void updateSurveyedPlanets(ArrayList<PlanetDrawableData> planetDrawables,
                                      TargetCallbackInterface callback) {
        activity.updateSurveyedPlanets(planetDrawables, callback);
    }
}