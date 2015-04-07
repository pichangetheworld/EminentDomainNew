package com.pichangetheworld.eminentdomainnew.card;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.util.IconType;
import com.pichangetheworld.eminentdomainnew.util.RoleCountCallbackInterface;

import java.util.List;

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

        owner.useCard(this);
        owner.drawCards(2);
        owner.discardCard(this);
        context.endActionPhase();
    }

    @Override
    public void onRole(List<BaseCard> matching) {
        super.onRole(matching);

        if (user == null) user = owner;

        int toSurvey = 0;
        for (BaseCard card : matching) {
            toSurvey += card.getSurvey();
            if (card.inGame()) {
                user.useCard(card);
            }
        }
        user.discardCards(matching);
        matching.clear();
        context.surveyPlanets(toSurvey - 1);
    }

    @Override
    public void setUpMatchRole() {
        context.matchRole(IconType.SURVEY, new RoleCountCallbackInterface() {
            @Override
            public void callback(List<BaseCard> matching) {
                matching.add(Survey.this);
                onRole(matching);
            }
        });
    }

    @Override
    public int getSurvey() {
        return 1;
    }
}
