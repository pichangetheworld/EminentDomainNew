package com.pichangetheworld.eminentdomainnew.player;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;

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
    public int selectTargetPlanet() {
        return 0;
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
