package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.IconType;
import com.pichangetheworld.eminentdomainnew.util.MultiTargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;

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
                owner.removeFromHand(targets);
            }
            if (inGame()) {
                owner.useCard(Research.this);
                owner.discardCard(Research.this);
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
    public void onRole(List<BaseCard> matching) {
        super.onRole(matching);

        if (user == null) user = owner;

        int toResearch = 0;
        for (BaseCard card : matching) {
            toResearch += card.getSurvey();
            if (card.inGame()) {
                user.useCard(card);
            }
        }
        user.discardCards(matching);
        matching.clear();

        // Expansion: use toResearch
        context.endRolePhase();
    }

    @Override
    public void setUpMatchRole() {
        context.matchRole(IconType.SURVEY, new RoleCountCallbackInterface() {
            @Override
            public void callback(List<BaseCard> matching) {
                matching.add(Research.this);
                onRole(matching);
            }
        });
    }

    @Override
    public int getResearch() {
        return 1;
    }
}
