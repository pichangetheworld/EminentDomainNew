package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.planet.BasePlanet;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

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
                    user.getNumShips() > user.getSurveyedPlanets().get(index).getWarfareCost()) {
                Warfare.this.conquerAction(user.getSurveyedPlanets().get(index));
            } else {
                user.gainShips(1);
            }
            context.endActionPhase();
        }
    };

    public Warfare() {
        super("Warfare", R.drawable.warfare);
    }

    @Override
    public void onAction() {
        context.selectTargetPlanet(onActionCallback);
    }

    @Override
    public void onRole() {
        // TODO implement warfare
        context.endRolePhase();
    }

    @Override
    public int getWarfare() {
        return 1;
    }

    public void conquerAction(BasePlanet targetPlanet) {
        user.gainShips(-1 * targetPlanet.getWarfareCost());
        targetPlanet.conquer();
    }
}
