package com.pichangetheworld.eminentdomainnew.player;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public interface ComputerPlayer {

    // Action phase
    int selectTargetHandCard();

    // Role phase
    int selectTargetRole();

    // Selecting targets
    int selectTargetUnconqueredPlanet(boolean allowNone);
    int selectTargetConqueredPlanet();

    // Discard phase
    List<Integer> selectTargetHandCardsToDiscard(int min);
}
