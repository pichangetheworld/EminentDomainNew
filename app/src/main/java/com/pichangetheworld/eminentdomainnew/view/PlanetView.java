package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 19/01/2015
 */
public class PlanetView extends RelativeLayout {
    LayoutInflater mInflater;

    TextView colonizeCost;
    TextView warfareCost;

    public PlanetView(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public PlanetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public PlanetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        mInflater.inflate(R.layout.planet_view, this, true);

        colonizeCost = (TextView) findViewById(R.id.colonize_cost);
        warfareCost = (TextView) findViewById(R.id.warfare_cost);
    }

    public void setDetails(PlanetDrawableData data) {
        colonizeCost.setText(Integer.toString(data.colonizeCost));
        warfareCost.setText(Integer.toString(data.warfareCost));
    }
}
