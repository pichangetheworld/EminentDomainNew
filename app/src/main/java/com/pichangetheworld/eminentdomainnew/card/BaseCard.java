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

    BaseCard(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }

    // Called when the card is added to the player's deck, e.g. via Politics or Role
    public BaseCard toUser(EminentDomainApplication context, BasePlayer user) {
        this.context = context;
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
    public int getProduceTrade() { return 0; }
    public int getResearch() { return 0; }
}
