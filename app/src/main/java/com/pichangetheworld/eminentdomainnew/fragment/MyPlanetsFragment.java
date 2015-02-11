package com.pichangetheworld.eminentdomainnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.view.PlanetView;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class MyPlanetsFragment extends Fragment {
    // Planet views
    LinearLayout planetLayout;
    float layoutWidth = 0;
    List<PlanetView> planetViews = new ArrayList<>();

    // Planet data
    List<PlanetDrawableData> planetData = new ArrayList<>();

    // Callback
    TargetCallbackInterface mCallback = null;

    final View.OnClickListener onPlanetClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i;
            for (i = 0; i < planetViews.size(); ++i) {
                if (v == planetViews.get(i)) break;
            }

            if (i < planetViews.size()) {
                mCallback.callback(i);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_myplanets, container, false);

        planetLayout = (LinearLayout) v.findViewById(R.id.fragment_myplanets);

        v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (layoutWidth != planetLayout.getWidth()) {
                    Log.d("MyPlanetsFragment", "Window is changed! width is " + planetLayout.getWidth());
                    layoutWidth = planetLayout.getWidth();
                    redraw();
                }
            }
        });

        return v;
    }

    // Hide the view
    public void setVisibility(boolean visible) {
        if (getView() != null) {
            if (visible) {
                getView().setVisibility(View.VISIBLE);
            } else {
                getView().setVisibility(View.GONE);
            }
        }
    }

    // Update the planet view with the planets in the user's area
    public void updatePlanets(ArrayList<PlanetDrawableData> data) {
        this.planetData.clear();
        this.planetData.addAll(data);
        if (layoutWidth > 0) {
            redraw();
        }
    }

    // Redraw to the screen
    private void redraw() {
        // TODO resize planets if there are too many
//        float cardWidth;
//        if (planetData.isEmpty()) {
//            cardWidth = 75 * displayMetrics.density;
//        } else {
//            cardWidth = Math.min(layoutWidth/cardData.size(), 75 * displayMetrics.density);
//        }
//        Log.d("HandDeckFragment", "Setting card width to " + cardWidth);
        Log.d("PlanetsFragment", "Drawing " + planetData.size() + " planets");
        for (int i = 0; i < planetData.size(); ++i) {
            if (i == planetViews.size()) {
                PlanetView pv = new PlanetView(getActivity());
                planetLayout.addView(pv);
                planetViews.add(pv);
            }
            PlanetView pv = planetViews.get(i);
            if (planetData.get(i).drawable == -1) {
                pv.setVisibility(View.GONE);
            } else {
                pv.setDetails(planetData.get(i));
                pv.setVisibility(View.VISIBLE);
            }
        }
    }

    // Choosing target unconquered planet
    public void chooseTargetPlanet(TargetCallbackInterface callback) {
        Log.d("PlanetsFragment", "Choosing target planet " + planetViews.size());
        mCallback = callback;

        for (PlanetView pv : planetViews) {
            if (!pv.isConquered()) {
                pv.setOnClickListener(onPlanetClicked);
            } else {
                pv.setClickable(false);
            }
        }
    }
}
