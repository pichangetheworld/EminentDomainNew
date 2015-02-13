package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class ProduceTrade extends BaseCard {
    private final TargetCallbackInterface onActionSelectProduceTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            switch (index) {
                case 0: // PRODUCE
                    context.selectTargetConqueredPlanet(onActionProduceCallback);
                    break;
                case 1: // TRADE
                default:
                    context.selectTargetConqueredPlanet(onActionTradeCallback);
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

    public ProduceTrade() {
        super("ProduceTrade", R.drawable.producetrade);
    }

    @Override
    public void onAction() {
        super.onAction();

        context.letPlayerChooseProduceTrade(onActionSelectProduceTradeCallback);
    }

    @Override
    public void onRole() {
        super.onRole();

        // TODO
        context.endRolePhase();
    }
}
