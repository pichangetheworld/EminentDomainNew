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
public class Research extends BaseCard {
    // Handler to receive CHOSE_TARGET_HAND_CARD broadcasts
    private final BroadcastReceiver mChooseHandCardReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context2, Intent intent) {
            int handCardIndex = intent.getIntExtra("handCardIndex", -1);
            int [] cardsLeftToRemove = intent.getIntArrayExtra("cardsLeftToRemove");
            Log.d("Research", "Broadcast Received! Card in hand chosen was " + handCardIndex);

            if (handCardIndex >= 0) {
                // Hack to 'use' the card by removing it without discarding to discard pile
                // TODO change this function name
                user.useCardIndex(handCardIndex);

                context.endActionPhase();
                context.unregisterReceiver(this);
            }
        }
    };

    public Research() {
        super(R.drawable.research);
    }

    @Override
    public void onAction() {
        context.registerReceiver(mChooseHandCardReceiver, new IntentFilter("CHOSE_TARGET_HAND_CARD"));
    }

    @Override
    public void onRole() {

    }
}
