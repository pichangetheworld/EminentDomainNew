package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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

    boolean conquered = false;

    // Surveyed planets
    TextView colonizeCost;
    TextView warfareCost;
    TextView colonizeCount;

    // Conquered planets
    TextView vps;
    TextView produceCapacity;

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
        conquered = false;
    }

    // Getter
    public boolean isConquered() { return conquered; }

    // Set all details at once
    public void setDetails(PlanetDrawableData data) {
        Log.d("PlanetView", "Drawing planet conquered: " + data.conquered);
        if (!data.conquered) {
            mInflater.inflate(R.layout.planet_view, this, true);

            colonizeCost = (TextView) findViewById(R.id.colonize_cost);
            warfareCost = (TextView) findViewById(R.id.warfare_cost);
            colonizeCount = (TextView) findViewById(R.id.colonize_count);

            colonizeCost.setText(Integer.toString(data.colonizeCost));
            warfareCost.setText(Integer.toString(data.warfareCost));
            Log.d("PlanetView", "Drawing planet colonize count is: " + data.colonizeCount);
            if (data.colonizeCount > 0) {
                colonizeCount.setText(Integer.toString(data.colonizeCount));
                colonizeCount.setVisibility(VISIBLE);
            } else {
                colonizeCount.setVisibility(GONE);
            }
        } else {
            mInflater.inflate(R.layout.conquered_planet_view, this, true);

            conquered = true;
            vps = (TextView) findViewById(R.id.VPs);
            produceCapacity = (TextView) findViewById(R.id.produce_capacity);

            vps.setText(Integer.toString(data.vps));
            produceCapacity.setText(Integer.toString(data.produceCapacity));
        }
    }
}
