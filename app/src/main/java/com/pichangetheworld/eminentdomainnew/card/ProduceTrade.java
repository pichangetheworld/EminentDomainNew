package com.pichangetheworld.eminentdomainnew.card;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

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
                        context.selectTargetPlanet(onActionProduceCallback);
                        break;
                    case 1: // TRADE
                    default:
                        context.selectTargetPlanet(onActionTradeCallback);
                        break;
                }
            } else {
                // TODO Role
            }
            context.unregisterReceiver(mChooseActionReceiver);
        }
    };


    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionProduceCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0 && user.getSurveyedPlanets().get(index).canProduce()) {
                user.getSurveyedPlanets().get(index).produce(1);
            }
            context.endActionPhase();
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int planetIndex) {
            if (planetIndex >= 0 && user.getSurveyedPlanets().get(planetIndex).canTrade()) {
                user.getSurveyedPlanets().get(planetIndex).trade(1);
            }
            context.endActionPhase();
        }
    };

    public ProduceTrade() {
        super("ProduceTrade", R.drawable.producetrade);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChooseActionReceiver, new IntentFilter("CHOSE_TARGET_ACTION"));
    }

    @Override
    public void onRole() {

    }
}
