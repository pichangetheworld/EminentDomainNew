package com.pichangetheworld.eminentdomainnew.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.R;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class ProduceTrade extends BaseCard {
    // Handler to receive CHOSE_TARGET_ACTION broadcasts
    private final BroadcastReceiver mChooseActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            String roleOrAction = intent.getStringExtra("roleOrAction");
            int actionIndex = intent.getIntExtra("actionIndex", -1);
            Log.d("ProduceTrade", "Broadcast Received! Action chosen was " + actionIndex);

            if (roleOrAction.equals("ACTION")) {
                // Action
                switch (actionIndex) {
                    case 0: // PRODUCE
                        context.registerReceiver(mChoosePlanetProduceReceiver,
                                new IntentFilter("CHOSE_TARGET_PLANET"));
                        break;
                    case 1: // TRADE
                    default:
                        context.registerReceiver(mChoosePlanetTradeReceiver,
                                new IntentFilter("CHOSE_TARGET_PLANET"));
                        break;
                }
            } else {
                // TODO Role
            }
            context.unregisterReceiver(mChooseActionReceiver);
        }
    };

    // Handler to receive CHOSE_TARGET_PLANET broadcasts
    private BroadcastReceiver mChoosePlanetProduceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int planetIndex = intent.getIntExtra("planetIndex", -1);
            Log.d("Colonize", "Broadcast Received! Planet chosen was " + planetIndex);

            if (planetIndex >= 0 && user.getSurveyedPlanets().get(planetIndex).canProduce()) {
                user.getSurveyedPlanets().get(planetIndex).produce(1);
                context.unregisterReceiver(this);
                context.endActionPhase();
            }
        }
    };

    // Handler to receive CHOSE_TARGET_PLANET broadcasts
    private BroadcastReceiver mChoosePlanetTradeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int planetIndex = intent.getIntExtra("planetIndex", -1);
            Log.d("Colonize", "Broadcast Received! Planet chosen was " + planetIndex);

            if (planetIndex >= 0 && user.getSurveyedPlanets().get(planetIndex).canTrade()) {
                user.getSurveyedPlanets().get(planetIndex).trade(1);
                context.unregisterReceiver(this);
                context.endActionPhase();
            }
        }
    };

    public ProduceTrade() {
        super(R.drawable.producetrade);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChooseActionReceiver, new IntentFilter("CHOSE_TARGET_ACTION"));
    }

    @Override
    public void onRole() {

    }
}
