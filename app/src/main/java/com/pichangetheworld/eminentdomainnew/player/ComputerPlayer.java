package com.pichangetheworld.eminentdomainnew.player;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public interface ComputerPlayer {

    int selectTargetHandCard();

    int selectTargetRole();

    int selectTargetUnconqueredPlanet(boolean allowNone);

    List<Integer> selectTargetHandCardsToDiscard(int min);
}
