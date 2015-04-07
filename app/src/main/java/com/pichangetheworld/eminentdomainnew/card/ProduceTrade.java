package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.IconType;
import com.pichangetheworld.eminentdomainnew.util.Phase;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class ProduceTrade extends BaseCard {
    Phase curPhase;

    boolean produceNotTrade = true;
    private final TargetCallbackInterface onSelectProduceTradeCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            produceNotTrade = (index == 0);
            switch (curPhase) {
                case ACTION_PHASE:
                    context.selectTargetConqueredPlanet(produceNotTrade, onActionCallback);
                    break;
                case ROLE_PHASE:
                    context.matchRole((produceNotTrade ? IconType.PRODUCE : IconType.TRADE),
                            new RoleCountCallbackInterface() {
                                @Override
                                public void callback(List<BaseCard> matching) {
                                    matching.add(ProduceTrade.this);
                                    onRole(matching);
                                }
                            });
                    break;
            }
        }
    };

    // Callback after targeto planet has been selected
    private final TargetCallbackInterface onActionCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            owner.useCard(ProduceTrade.this);
            if (produceNotTrade) {
                if (index >= 0 && owner.getSurveyedPlanets().get(index).canProduce()) {
                    owner.getSurveyedPlanets().get(index).produce(1);
                    owner.broadcastPlanetsUpdated();
                }
            } else {
                if (index >= 0 && owner.getSurveyedPlanets().get(index).canTrade()) {
                    owner.getSurveyedPlanets().get(index).trade(1);
                    owner.broadcastPlanetsUpdated();
                }
            }
            owner.discardCard(ProduceTrade.this);
            context.endActionPhase();
        }
    };

    int toGain;
    List<BaseCard> matching;
    // Callback after target planet has been selected
    private final TargetCallbackInterface onRoleCallback = new TargetCallbackInterface() {
        @Override
        public void callback(int index) {
            if (produceNotTrade) {
                if (index >= 0 && owner.getSurveyedPlanets().get(index).canProduce()) {
                    toGain = owner.getSurveyedPlanets().get(index).produce(toGain);
                    if (toGain > 0) {
                        // do this again
                        owner.broadcastPlanetsUpdated();
                        context.selectTargetConqueredPlanet(produceNotTrade, onRoleCallback);
                    } else {
                        owner.broadcastPlanetsUpdated();
                        owner.discardCards(matching);
                        matching.clear();
                        context.endRolePhase();
                    }
                } else {
                    owner.discardCards(matching);
                    matching.clear();
                    context.endRolePhase();
                }
            } else {
                if (index >= 0 && owner.getSurveyedPlanets().get(index).canTrade()) {
                    toGain = owner.getSurveyedPlanets().get(index).trade(toGain);
                    if (toGain > 0) {
                        // do this again
                        owner.broadcastPlanetsUpdated();
                        context.selectTargetConqueredPlanet(produceNotTrade, onRoleCallback);
                    } else {
                        owner.broadcastPlanetsUpdated();
                        owner.discardCards(matching);
                        matching.clear();
                        context.endRolePhase();
                    }
                } else {
                    owner.discardCards(matching);
                    matching.clear();
                    context.endRolePhase();
                }
            }
        }
    };

    public ProduceTrade(EminentDomainApplication context) {
        super(context, "ProduceTrade", R.drawable.producetrade);
    }

    @Override
    public void onAction() {
        super.onAction();

        curPhase = Phase.ACTION_PHASE;

        context.chooseProduceTrade(onSelectProduceTradeCallback);
    }

    @Override
    public void onRole(List<BaseCard> matching) {
        super.onRole(matching);
        this.matching = matching;

        toGain = 0;
        for (BaseCard card : matching) {
            toGain += (produceNotTrade ? card.getProduce() : card.getTrade());
            if (card.inGame()) {
                owner.useCard(card);
            }
        }

        context.selectTargetConqueredPlanet(produceNotTrade, onRoleCallback);
    }

    @Override
    public void setUpMatchRole() {
        curPhase = Phase.ROLE_PHASE;
        context.chooseProduceTrade(onSelectProduceTradeCallback);
    }

    @Override
    public int getProduce() {
        return 1;
    }

    @Override
    public int getTrade() {
        return 1;
    }
}
