package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.IconType;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 19/01/2015
 */
public class CardView extends ImageButton {
    boolean survey;
    boolean warfare;
    boolean colonize;
    boolean produce;
    boolean trade;
    boolean research;

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

    public void setDetails(CardDrawableData data) {
        setBackgroundResource(data.drawable);
        survey = data.survey;
        warfare = data.warfare;
        colonize = data.colonize;
        produce = data.produce;
        trade = data.trade;
        research = data.research;
        setVisibility(View.VISIBLE);
    }

    public boolean getIcon(IconType type) {
        // TODO weird error
        switch (type) {
            case SURVEY:
                return survey;
            case WARFARE:
                return warfare;
            case COLONIZE:
                return colonize;
            case PRODUCE:
                return produce;
            case TRADE:
                return trade;
            case RESEARCH:
                return research;
            default:
                return false;
        }
    }
}
