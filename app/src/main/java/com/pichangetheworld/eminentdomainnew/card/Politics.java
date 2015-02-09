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
public class Politics extends BaseCard {
    // Handler to receive CHOSE_TARGET_ROLE broadcasts
    private final BroadcastReceiver mChooseRoleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int roleIndex = intent.getIntExtra("roleIndex", -1);
            Log.d("Politics", "Broadcast Received! Role chosen was " + roleIndex);

            if (roleIndex >= 0) {
                user.useCard(Politics.this);
                user.addCardToHand(context.getGameField()
                        .drawCardFromFieldDeck(context, user, roleIndex));
                context.unregisterReceiver(this);
                context.endActionPhase();
            }
        }
    };

    public Politics() {
        super(R.drawable.blank_card);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChooseRoleReceiver, new IntentFilter("CHOSE_TARGET_ROLE"));
    }

    @Override
    public void onRole() {

    }
}
