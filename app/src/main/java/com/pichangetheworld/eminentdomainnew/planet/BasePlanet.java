package com.pichangetheworld.eminentdomainnew.planet;

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
    int conquerCost;

    int produceCapacity;
    int currentlyProduced;

    BasePlayer owner;
    List<BaseCard> colonizeCount;
    boolean conquered;

    BasePlanet(int colonizeCost, int conquerCost, int canProduce) {
        // Temporarily
        this.name = "Planet";
        this.drawable = R.drawable.planet;

        this.colonizeCost = colonizeCost;
        this.conquerCost = conquerCost;
        this.produceCapacity = canProduce;

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
    public int getConquerCost() {
        return conquerCost;
    }
    public boolean isConquered() { return conquered; }
    public int getProduceCapacity() { return produceCapacity; }
    public int getCurProduceCount() { return currentlyProduced; }

    public void survey(BasePlayer player) {
        owner = player;
        colonizeCount = new ArrayList<>();
    }

    public void addColony(BaseCard card) {
        colonizeCount.add(card);
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
