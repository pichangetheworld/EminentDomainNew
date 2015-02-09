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
public class Warfare extends BaseCard {
    // Handler to receive CHOSE_TARGET_PLANET broadcasts
    private final BroadcastReceiver mChoosePlanetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int planetIndex = intent.getIntExtra("planetIndex", -1);
            Log.d("Warfare", "Broadcast Received! Planet chosen was " + planetIndex);

            if (planetIndex >= 0 &&
                    user.getNumShips() > user.getSurveyedPlanets().get(planetIndex).getWarfareCost()) {
                Warfare.this.conquerAction(user.getSurveyedPlanets().get(planetIndex));
            } else {
                user.gainShips(1);
            }
            context.unregisterReceiver(this);
            context.endActionPhase();
        }
    };

    public Warfare() {
        super("Warfare", R.drawable.warfare);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChoosePlanetReceiver, new IntentFilter("CHOSE_TARGET_PLANET"));
    }

    @Override
    public void onRole() {
        // TODO implement warfare
        context.endRolePhase();
    }

    @Override
    public int getWarfare() {
        return 1;
    }

    public void conquerAction(BasePlanet targetPlanet) {
        user.gainShips(-1 * targetPlanet.getWarfareCost());
        targetPlanet.conquer();
    }
}
