package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.pichangetheworld.eminentdomainnew.R;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 19/01/2015
 */
public class CardView extends ImageButton {
    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.blank_card);
    }

    public void onCardSelected(boolean selected) {
        if (selected) {
            setAlpha(0.7f);
        } else {
            setAlpha(1.0f);
        }
    }
}
