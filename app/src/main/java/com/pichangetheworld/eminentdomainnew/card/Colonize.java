package com.pichangetheworld.eminentdomainnew.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Colonize extends BaseCard {
    // Handler to receive CHOSE_TARGET_PLANET broadcasts
    private final BroadcastReceiver mChoosePlanetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int planetIndex = intent.getIntExtra("planetIndex", -1);
            Log.d("Colonize", "Broadcast Received! Planet chosen was " + planetIndex);

            if (planetIndex >= 0) {
                Colonize.this.colonizeAction(user.getSurveyedPlanets().get(planetIndex));
                context.unregisterReceiver(this);
                context.endActionPhase();
            }
        }
    };

    public Colonize() {
        super(R.drawable.colonize);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChoosePlanetReceiver, new IntentFilter("CHOSE_TARGET_PLANET"));
    }

    @Override
    public void onRole() {

    }

    @Override
    public int getColonize() {
        return 1;
    }

    public void colonizeAction(BasePlanet targetPlanet) {
        user.useCard(this);
        if (targetPlanet.getColonizeCount() >= targetPlanet.getColonizeCost()) {
            user.discardCard(this);
            targetPlanet.conquer();
        } else {
            targetPlanet.addColony(this);
        }
    }
}
