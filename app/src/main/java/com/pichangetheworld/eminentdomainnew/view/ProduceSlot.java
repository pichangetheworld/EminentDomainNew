package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.pichangetheworld.eminentdomainnew.R;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 13/02/2015
 */
public class ProduceSlot extends ImageView {
    private static final int[] STATE_PRODUCED = { R.attr.state_produced };

    boolean produced = false;
    public ProduceSlot(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.produce_slot, 0, 0);
        produced = ta.getBoolean(R.styleable.produce_slot_state_produced, false);
        ta.recycle(); // recycle the array
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        // If the message is unread then we merge our custom message unread state into
        // the existing drawable state before returning it.
        if (produced) {
            // We are going to add 1 extra state.
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, STATE_PRODUCED);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

    public void produce() {
        this.produced = true;
        refreshDrawableState();
    }

    public void trade() {
        this.produced = false;
        refreshDrawableState();
    }
}
