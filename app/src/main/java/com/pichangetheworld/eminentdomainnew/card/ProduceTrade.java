package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.Phase;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class ProduceTrade extends BaseCard {
    Phase curPhase;

    private final TargetCallbackInterface onSelectProduceTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            switch (index) {
                case 0: // PRODUCE
                    if (curPhase == Phase.ACTION_PHASE) {
                        context.selectTargetConqueredPlanet(onActionProduceCallback);
                    } else {
                        context.selectTargetConqueredPlanet(onRoleProduceCallback);
                    }
                    break;
                case 1: // TRADE
                default:
                    if (curPhase == Phase.ACTION_PHASE) {
                        context.selectTargetConqueredPlanet(onActionTradeCallback);
                    } else {
                        context.selectTargetConqueredPlanet(onRoleTradeCallback);
                    }
                    break;
            }
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionProduceCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            user.useCard(ProduceTrade.this);
            if (index >= 0 && user.getSurveyedPlanets().get(index).canProduce()) {
                user.getSurveyedPlanets().get(index).produce(1);
                user.broadcastPlanetsUpdated();
            }
            context.endActionPhase();
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onActionTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int planetIndex) {
            user.useCard(ProduceTrade.this);
            if (planetIndex >= 0 && user.getSurveyedPlanets().get(planetIndex).canTrade()) {
                user.getSurveyedPlanets().get(planetIndex).trade(1);
                user.broadcastPlanetsUpdated();
            }
            context.endActionPhase();
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleProduceCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (index >= 0 && user.getSurveyedPlanets().get(index).canProduce()) {
                user.getSurveyedPlanets().get(index).produce(1);
                user.broadcastPlanetsUpdated();
            }
            user.discardCard(ProduceTrade.this);
            context.endRolePhase();
        }
    };

    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int planetIndex) {
            if (planetIndex >= 0 && user.getSurveyedPlanets().get(planetIndex).canTrade()) {
                user.getSurveyedPlanets().get(planetIndex).trade(1);
                user.broadcastPlanetsUpdated();
            }
            user.discardCard(ProduceTrade.this);
            context.endRolePhase();
        }
    };

    public ProduceTrade() {
        super("ProduceTrade", R.drawable.producetrade);
    }

    @Override
    public void onAction() {
        super.onAction();

        curPhase = Phase.ACTION_PHASE;

        context.chooseProduceTrade(onSelectProduceTradeCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        curPhase = Phase.ROLE_PHASE;

        context.chooseProduceTrade(onSelectProduceTradeCallback);
    }
}
