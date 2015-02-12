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
            user.useCard(Warfare.this);
            if (index >= 0 &&
                    user.getNumShips() > user.getSurveyedPlanets().get(index).getWarfareCost()) {
                Warfare.this.conquerAction(user.getSurveyedPlanets().get(index));
            } else {
                user.gainShips(1);
            }
            user.discardCard(Warfare.this);
            context.endActionPhase();
        }
    };
    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0 &&
                    user.getNumShips() > user.getSurveyedPlanets().get(index).getWarfareCost()) {
                Warfare.this.conquerAction(user.getSurveyedPlanets().get(index));
            } else {
                user.gainShips(1);
            }
            user.discardCard(Warfare.this);
            context.endRolePhase();
        }
    };

    public Warfare() {
        super("Warfare", R.drawable.warfare);
    }

    @Override
    public void onAction() {
        super.onAction();
        context.selectTargetUnconqueredPlanet(true, onActionCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        context.selectTargetUnconqueredPlanet(true, onRoleCallback);
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
