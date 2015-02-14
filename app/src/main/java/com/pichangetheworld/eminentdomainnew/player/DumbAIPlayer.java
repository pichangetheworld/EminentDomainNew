package com.pichangetheworld.eminentdomainnew.player;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
// Dumb AI Player always plays the first card
public class DumbAIPlayer extends BasePlayer implements ComputerPlayer {
    public DumbAIPlayer(EminentDomainApplication context, String name, int id) {
        super(context, name, id);
    }

    @Override
    public int selectTargetHandCard() {
        return 0;
    }

    @Override
    public int selectTargetRole() {
        return 0;
    }

    @Override
    public int chooseProduceTrade() {
        if (surveyedPlanets.get(0).canTrade()) {
            return 1;
        }
        return 0;
    }

    @Override
    public int selectTargetUnconqueredPlanet(boolean allowNone) {
        for (int i = 0; i < surveyedPlanets.size(); ++i) {
            if (!surveyedPlanets.get(i).isConquered())
                return i;
        }
        return -1;
    }

    @Override
    public int selectTargetConqueredPlanet() {
        for (int i = 0; i < surveyedPlanets.size(); ++i) {
            if (surveyedPlanets.get(i).isConquered())
                return i;
        }
        return -1;
    }

    @Override
    public List<Integer> selectTargetHandCardsToDiscard(int min) {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < min; ++i) {
            ret.add(i);
        }
        return ret;
    }
}
