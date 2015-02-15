package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.CardType;
import com.pichangetheworld.eminentdomainnew.util.MultiTargetCallbackInterface;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Research extends BaseCard {
    // Handler to receive CHOSE_TARGET_HAND_CARD broadcasts
    private final MultiTargetCallbackInterface mActionCallback = new MultiTargetCallbackInterface() {
        @Override
        public void callback(List<Integer> targets) {
            if (targets != null) {
                user.removeFromHand(targets);
            }
            if (inGame()) {
                user.useCard(Research.this);
                user.discardCard(Research.this);
            }
            context.endActionPhase();
        }
    };

    public Research(EminentDomainApplication context) {
        super(context, "Research", R.drawable.research);
    }

    @Override
    public void onAction() {
        super.onAction();
        context.selectCardsToRemove(2, mActionCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        // TODO
        context.endRolePhase();
    }

    @Override
    public CardType getType() {
        return CardType.RESEARCH;
    }
}
