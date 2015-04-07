package com.pichangetheworld.eminentdomainnew.card;

import android.util.Log;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Politics extends BaseCard {
    // Handler to receive CHOSE_TARGET_ROLE broadcasts
    private final TargetCallbackInterface onActionCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int roleIndex) {
            Log.d("Politics", "Broadcast Received! Role chosen was " + roleIndex);

            if (roleIndex >= 0) {
                BaseCard card = context.getGameField()
                        .drawCardFromFieldDeck(context, owner, roleIndex);
                if (card != null)
                    owner.addCardToHand(card);
                context.endActionPhase();
            }
        }
    };

    public Politics(EminentDomainApplication context) {
        super(context, "Politics", R.drawable.politics);
    }

    @Override
    public void onAction() {
        super.onAction();

        owner.useCard(Politics.this);
        context.selectTargetRole(onActionCallback);
    }
}
