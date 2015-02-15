package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.CardType;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Survey extends BaseCard {
    public Survey(EminentDomainApplication context) {
        super(context, "Survey", R.drawable.survey);
    }

    @Override
    public void onAction() {
        super.onAction();

        user.useCard(this);
        user.drawCards(2);
        user.discardCard(this);
        context.endActionPhase();
    }

    @Override
    public void onRole() {
        super.onRole();

        context.surveyPlanets(1);
    }

    @Override
    public CardType getType() {
        return CardType.SURVEY;
    }

    @Override
    public int getSurvey() {
        return 1;
    }
}
