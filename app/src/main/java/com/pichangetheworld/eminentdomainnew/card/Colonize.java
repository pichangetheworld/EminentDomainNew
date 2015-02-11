package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
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
            user.useCard(Colonize.this);
            if (index >= 0) {
                Colonize.this.colonizeAction(user.getSurveyedPlanets().get(index));
            }
            context.endActionPhase();
        }
    };

    public Colonize() {
        super("Colonize", R.drawable.colonize);
    }

    @Override
    public void onAction() {
        super.onAction();
        context.selectTargetPlanet(onActionCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        // TODO
        context.endRolePhase();
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
