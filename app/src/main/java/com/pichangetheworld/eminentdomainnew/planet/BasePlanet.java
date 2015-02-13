package com.pichangetheworld.eminentdomainnew.planet;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.card.BaseCard;
import com.pichangetheworld.eminentdomainnew.player.BasePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class BasePlanet {
    String name;
    int drawable;

    int colonizeCost;
    int warfareCost;

    int produceCapacity;
    int currentlyProduced;
    int VPs; // TODO PlanetType enum

    BasePlayer owner;
    List<BaseCard> colonizeCount;
    boolean conquered;

    public BasePlanet(int colonizeCost, int warfareCost, int canProduce, int vps) {
        // Temporarily
        this.name = "Planet";
        this.drawable = R.drawable.planet;

        this.colonizeCost = colonizeCost;
        this.warfareCost = warfareCost;
        this.colonizeCount = new ArrayList<>();
        this.produceCapacity = canProduce;
        this.VPs = vps;

        owner = null;
        conquered = false;
        currentlyProduced = 0;
    }

    // Getters
    public String getName() { return name; }
    public int getDrawable() { return drawable; }
    public int getColonizeCost() {
        return colonizeCost;
    }
    public int getWarfareCost() {
        return warfareCost;
    }
    public boolean isConquered() { return conquered; }
    public int getProduceCapacity() { return produceCapacity; }
    public int getCurProduceCount() { return currentlyProduced; }
    public int getVPs() { return VPs; }

    public void survey(BasePlayer player) {
        owner = player;
    }

    public void addColony(BaseCard card) {
        Log.d("BasePlanet", "Colonize count was " + getColonizeCount());
        colonizeCount.add(card);

        Log.d("BasePlanet", "Now colonize count is " + getColonizeCount());
    }

    public boolean canProduce() {
        return owner != null && produceCapacity - currentlyProduced > 0;
    }
    public boolean canTrade() {
        return owner != null && currentlyProduced > 0;
    }

    public int getColonizeCount() {
        int total = 0;
        for (BaseCard card : colonizeCount) {
            total += card.getColonize();
        }
        Log.d("BasePlanet", "Getting colonize count is " + total);
        return total;
    }

    public void conquer() {
        conquered = true;
        owner.discardCards(colonizeCount);
        colonizeCount.clear();
    }

    public int produce(int n) {
        int toProduce = Math.min(produceCapacity - currentlyProduced, n);
        currentlyProduced += toProduce;
        return n - toProduce;
    }

    public int trade(int n) {
        int toTrade = Math.min(currentlyProduced, n);
        currentlyProduced -= toTrade;
        return n - toTrade;
    }
}
