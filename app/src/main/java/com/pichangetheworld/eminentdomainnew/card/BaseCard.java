package com.pichangetheworld.eminentdomainnew.card;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;

import java.util.List;

/**
 * Eminent Domain AS
 *
 * Author: pchan
 * Date: 17/01/2015
 */
public abstract class BaseCard {
    EminentDomainApplication context;
    BasePlayer owner;
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
        this.owner = user;
        return this;
    }

    // Called when a card is temporarily played but not owned
    //  e.g. using Survey when the field is out of Survey
    public void temporaryUser(BasePlayer user) {
        this.user = user;
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
    public void onRole(List<BaseCard> matching) {
        Log.d("BaseCard", "On Role playing " + matching.size() + " " + name + "s");
    }

    public int getSurvey() { return 0; }
    public int getWarfare() { return 0; }
    public int getColonize() { return 0; }
    public int getProduce() { return 0; }
    public int getTrade() { return 0; }
    public int getResearch() { return 0; }

    // Let player match role
    public void setUpMatchRole() {}

    public void removeFromGame() {
        owner = null;
    }

    public boolean inGame() {
        return owner != null;
    }
}
