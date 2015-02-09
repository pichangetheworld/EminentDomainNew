package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
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

    public abstract void onAction();
    public abstract void onRole();

    public int getSurvey() { return 0; }
    public int getWarfare() { return 0; }
    public int getColonize() { return 0; }
    public int getProduceTrade() { return 0; }
    public int getResearch() { return 0; }
}
