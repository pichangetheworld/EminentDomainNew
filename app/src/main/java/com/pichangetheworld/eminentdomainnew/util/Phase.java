package com.pichangetheworld.eminentdomainnew.util;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 09/02/2015
 */
public enum Phase {
    ACTION_PHASE,
    ROLE_PHASE,
    DISCARD_DRAW_PHASE {
        @Override
        public Phase next() {
            return null;
        }
    };

    public Phase next() {
        return values()[ordinal() + 1];
    }
}