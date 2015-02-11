package com.pichangetheworld.eminentdomainnew;

import android.app.Application;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.util.Phase;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

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
            gameField = new GameField();
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

    public GameField getGameField() { return gameField; }

    public void updateViewNextPhase(Phase phase, String name, boolean isComputer) {
        Log.d("NextPhaseStart", "Got phase start: " + phase);
        switch (phase) {
            case ACTION_PHASE: actionPhase(name, isComputer); break;
            case ROLE_PHASE: rolePhase(name, isComputer); break;
            case DISCARD_DRAW_PHASE: discardDrawPhase(isComputer); break;
            default:
        }
    }

    // Action Phase
    private void actionPhase(String name, boolean isComputer) {
        if (isComputer) {
            int index = gameManager.letAISelectTargetHandCard();
            Log.d("ActionPhase", name + " plays card at " + index);
            playAction(index);
        } else {
            activity.actionPhase(name);
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
    private void rolePhase(String name, boolean isComputer) {
        if (isComputer) {
            int index = gameManager.letAISelectTargetRole();
            Log.d("RolePhase", name + " plays role at " + index);
            playRole(index);
        } else {
            activity.rolePhase();
        }
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
        if (isComputer) {
            List<Integer> toDiscard = gameManager.letAISelectHandCardsToDiscard();
            discardSelectedCards(toDiscard);
        } else {
            activity.discardDrawPhase();
        }
    }

    public void discardSelectedCards(List<Integer> selectedCards) {
        gameManager.curPlayerDiscardSelectedCards(selectedCards);
    }

    // selecting target planets
    public void selectTargetUnconqueredPlanet(boolean allowNone, TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAISelectTargetPlanet(allowNone);
            callback.callback(index);
        } else {
            activity.letPlayerChooseTargetUnconqueredPlanet(allowNone, callback);
        }
    }
}
