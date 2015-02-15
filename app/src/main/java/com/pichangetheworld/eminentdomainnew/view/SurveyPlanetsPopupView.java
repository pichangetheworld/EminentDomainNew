package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 12/02/2015
 */
public class SurveyPlanetsPopupView extends PopupWindow {
    LinearLayout planetLayout;
    TargetCallbackInterface mCallback;

    public SurveyPlanetsPopupView(Context context, List<PlanetDrawableData> planetData,
                                  TargetCallbackInterface callback) {
        super((int) (300 * context.getResources().getDisplayMetrics().density),
                (int) (300 * context.getResources().getDisplayMetrics().density));
        View v = View.inflate(context, R.layout.survey_planets, null);
        setContentView(v);

        planetLayout = (LinearLayout) v.findViewById(R.id.planets_layout);

        mCallback = callback;

        Log.d("PlanetsFragment", "Drawing " + planetData.size() + " planets");
        for (int i = 0; i < planetData.size(); ++i) {
            PlanetView pv = new PlanetView(context);
            planetLayout.addView(pv);
            if (planetData.get(i).drawable == -1) {
                pv.setVisibility(View.GONE);
            } else {
                final int j = i;
                pv.setVisibility(View.VISIBLE);
                pv.setDetails(planetData.get(i));
                pv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        mCallback.callback(j);
                    }
                });
            }
        }
    }

    public void show(View rootView) {
        showAtLocation(rootView, Gravity.CENTER, 0, 40);
    }
}