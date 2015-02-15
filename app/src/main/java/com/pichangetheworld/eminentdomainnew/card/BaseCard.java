package com.pichangetheworld.eminentdomainnew.card;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;

/**
 * Eminent Domain AS
 *
 * Author: pchan
 * Date: 17/01/2015
 */
public abstract class BaseCard {
    EminentDomainApplication context;
    BasePlayer user;
    String name;
    int drawable;

    BaseCard(EminentDomainApplication context, String name, int drawable) {
        this.context = context;
        this.name = name;
        this.drawable = drawable;
    }

    // Called when the card is added to the player's deck, e.g. via Politics or Role
    public BaseCard toUser(BasePlayer user) {
        this.user = user;
        return this;
    }

    public EminentDomainApplication getContext() {
        return context;
    }

    // Getters
    public String getName() { return name; }
    public int getDrawable() {
        return drawable;
    }

    public void onAction() {
        Log.d("BaseCard", "On Action playing " + name);
    }
    public void onRole() {
        Log.d("BaseCard", "On Role playing " + name);
    }

    public int getSurvey() { return 0; }
    public int getWarfare() { return 0; }
    public int getColonize() { return 0; }
    public int getProduce() { return 0; }
    public int getTrade() { return 0; }
    public int getResearch() { return 0; }

    public void removeFromGame() {
        user = null;
    }

    public boolean inGame() {
        return user != null;
    }
}
