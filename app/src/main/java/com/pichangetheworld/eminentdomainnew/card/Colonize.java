package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

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
                Colonize.this.colonizeAction(user.getSurveyedPlanets().get(index));
            }
            context.endActionPhase();
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0) {
                Colonize.this.colonizeAction(user.getSurveyedPlanets().get(index));
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

        user.useCard(Colonize.this);
        context.selectTargetUnconqueredPlanet(false, onActionCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        context.selectTargetUnconqueredPlanet(false, onRoleCallback);
    }

    @Override
    public int getColonize() {
        return 1;
    }

    public void colonizeAction(BasePlanet targetPlanet) {
        if (targetPlanet.getColonizeCount() >= targetPlanet.getColonizeCost()) {
            user.discardCard(this);
            targetPlanet.conquer();
        } else {
            targetPlanet.addColony(this);
        }
        user.broadcastPlanetsUpdated();
    }
}
