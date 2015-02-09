package com.pichangetheworld.eminentdomainnew;

import android.app.Application;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.util.Phase;

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

    public void updateViewNextPhase(Phase phase, String name) {
        Log.d("NextPhaseStart", "Got phase start: " + phase);
        switch (phase) {
            case ACTION_PHASE: actionPhase(name); break;
            case ROLE_PHASE: rolePhase(); break;
            case DISCARD_DRAW_PHASE: discardDrawPhase(); break;
            default:
        }
    }

    private void actionPhase(String name) {
        activity.actionPhase(name);
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

    private void rolePhase() {
        activity.rolePhase();
    }

    // Play the card at index i
    // If i == -1 skip
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
}
