package com.pichangetheworld.eminentdomainnew.player;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
// Dumb AI Player always plays the first card
public class DumbAIPlayer extends ComputerPlayer {
    public DumbAIPlayer(EminentDomainApplication context, String name, int id) {
        super(context, name, id);
    }
}
