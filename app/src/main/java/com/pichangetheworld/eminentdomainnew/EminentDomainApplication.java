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

    public void updateViewNextPhase(Phase phase, String name, boolean isComputerTurn) {
        Log.d("NextPhaseStart", "Got phase start: " + phase);
        switch (phase) {
            case ACTION_PHASE: actionPhase(name, isComputerTurn); break;
            case ROLE_PHASE: rolePhase(isComputerTurn); break;
            case DISCARD_DRAW_PHASE: discardDrawPhase(); break;
            default:
        }
    }

    private void actionPhase(String name, boolean isComputerTurn) {
        if (isComputerTurn) {
            int index = gameManager.letAISelectTargetHandCard();
            Log.d("ActionPhase", "AI plays card at " + index);
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

    private void rolePhase(boolean isComputerTurn) {
        if (isComputerTurn) {
            int index = gameManager.letAISelectTargetRole();
            Log.d("ActionPhase", "AI plays role at " + index);
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

    private void discardDrawPhase() {
        activity.discardDrawPhase();
    }

    public void discardSelectedCards(List<Integer> selectedCards) {
        gameManager.curPlayerDiscardSelectedCards(selectedCards);
    }

    // selecting target planets
    public void selectTargetPlanet(TargetCallbackInterface callback) {
        if (gameManager.isComputerTurn()) {
            // let computer select target
            int index = gameManager.letAISelectTargetPlanet();
            callback.callback(index);
        } else {
            activity.letPlayerChooseTargetPlanet(callback);
        }
    }
}
