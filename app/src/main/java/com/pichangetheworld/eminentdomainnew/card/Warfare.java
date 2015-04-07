package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.util.IconType;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Warfare extends BaseCard {
    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0 &&
                    owner.getNumShips() >= owner.getSurveyedPlanets().get(index).getWarfareCost()) {
                Warfare.this.conquerAction(owner.getSurveyedPlanets().get(index));
            } else {
                owner.gainShips(1);
            }
            owner.discardCard(Warfare.this);
            context.endActionPhase();
        }
    };

    public Warfare(EminentDomainApplication context) {
        super(context, "Warfare", R.drawable.warfare);
    }

    @Override
    public void onAction() {
        super.onAction();

        owner.useCard(Warfare.this);
        context.selectTargetUnconqueredPlanet(true, onActionCallback);
    }

    @Override
    public void onRole(List<BaseCard> matching) {
        super.onRole(matching);

        int toGain = 0;
        for (BaseCard card : matching) {
            toGain += card.getWarfare();
            if (card.inGame()) {
                owner.useCard(card);
            }
        }
        owner.gainShips(toGain);
        owner.discardCards(matching);
        matching.clear();
        context.endRolePhase();
    }

    @Override
    public void setUpMatchRole() {
        context.matchRole(IconType.WARFARE, new RoleCountCallbackInterface() {
            @Override
            public void callback(List<BaseCard> matching) {
                matching.add(Warfare.this);
                onRole(matching);
            }
        });
    }

    @Override
    public int getWarfare() {
        return 1;
    }

    public void conquerAction(BasePlanet targetPlanet) {
        owner.gainShips(-1 * targetPlanet.getWarfareCost());
        targetPlanet.conquer();
        owner.broadcastPlanetsUpdated();
    }
}
