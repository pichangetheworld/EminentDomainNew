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

    private PlanetDrawableData data = null;

    // Surveyed planets
    TextView colonizeCost;
    TextView warfareCost;
    TextView colonizeCount;

    // Conquered planets
    TextView vps;
    ProduceSlot produceCapacity;
    ProduceSlot produceCapacity2;

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
        colonizeCount = (TextView) findViewById(R.id.colonize_count);
    }

    // Getter
    public boolean isConquered() { return (data != null && data.conquered); }

    // Set all details at once
    public void setDetails(PlanetDrawableData data) {
        this.data = data;
        Log.d("PlanetView", "Drawing planet conquered: " + data.conquered);
        if (!data.conquered) {
            colonizeCost.setText(Integer.toString(data.colonizeCost));
            warfareCost.setText(Integer.toString(data.warfareCost));
            Log.d("PlanetView", "Drawing planet colonize count is: " + data.colonizeCount);
            if (data.colonizeCount > 0) {
                colonizeCount.setVisibility(VISIBLE);
                colonizeCount.setText(Integer.toString(data.colonizeCount));
            } else {
                colonizeCount.setVisibility(GONE);
            }
        } else {
            mInflater.inflate(R.layout.conquered_planet_view, this, true);

            vps = (TextView) findViewById(R.id.VPs);
            produceCapacity = (ProduceSlot) findViewById(R.id.produce_capacity);
            produceCapacity2 = (ProduceSlot) findViewById(R.id.produce_capacity2);

            vps.setText(Integer.toString(data.vps));
            if (data.produceCapacity == 0) {
                produceCapacity.setVisibility(GONE);
                produceCapacity2.setVisibility(GONE);
            } else if (data.produceCapacity == 1) {
                produceCapacity2.setVisibility(GONE);
                if (data.curProduceCount > 0) {
                    produceCapacity.produce();
                } else {
                    produceCapacity.trade();
                }
            } else {
                if (data.curProduceCount > 0) {
                    produceCapacity.produce();
                } else {
                    produceCapacity.trade();
                    produceCapacity2.trade();
                }
                if (data.curProduceCount > 1) {
                    produceCapacity2.produce();
                } else {
                    produceCapacity2.trade();
                }
            }
        }
    }
}
