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
public class Colonize extends BaseCard {
    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0) {
                colonizeAction(owner.getSurveyedPlanets().get(index));
            }
            context.endActionPhase();
        }
    };

    List<BaseCard> matching;
    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0) {
                colonizeRole(owner.getSurveyedPlanets().get(index), matching);
            }
            context.endRolePhase();
        }
    };

    public Colonize(EminentDomainApplication context) {
        super(context, "Colonize", R.drawable.colonize);
    }

    @Override
    public void onAction() {
        super.onAction();

        owner.useCard(Colonize.this);
        context.selectTargetUnconqueredPlanet(false, onActionCallback);
    }

    @Override
    public void onRole(List<BaseCard> matching) {
        super.onRole(matching);
        this.matching = matching;

        context.selectTargetUnconqueredPlanet(false, onRoleCallback);
    }

    @Override
    public void setUpMatchRole() {
        context.matchRole(IconType.COLONIZE, new RoleCountCallbackInterface() {
            @Override
            public void callback(List<BaseCard> matching) {
                matching.add(Colonize.this);
                onRole(matching);
            }
        });
    }


    @Override
    public int getColonize() {
        return 1;
    }

    public void colonizeAction(BasePlanet targetPlanet) {
        if (targetPlanet.getColonizeCount() >= targetPlanet.getColonizeCost()) {
            owner.discardCard(this);
            targetPlanet.conquer();
        } else {
            targetPlanet.addColony(this);
        }
        owner.broadcastPlanetsUpdated();
    }

    public void colonizeRole(BasePlanet targetPlanet, List<BaseCard> colonies) {
        for (BaseCard card : colonies) {
            if (card.inGame()) {
                owner.useCard(card);
            }
            targetPlanet.addColony(card);
        }
        owner.broadcastPlanetsUpdated();
    }
}
