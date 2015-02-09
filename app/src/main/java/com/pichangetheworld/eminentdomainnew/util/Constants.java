package com.pichangetheworld.eminentdomainnew.util;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class Constants {
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
}
