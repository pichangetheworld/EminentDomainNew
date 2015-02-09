package com.pichangetheworld.eminentdomainnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
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


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myplanets, container, false);

        planetLayout = (LinearLayout) v.findViewById(R.id.fragment_myplanets);

        return v;
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
        for (int i = 0; i < planetData.size(); ++i) {
            LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == planetViews.size()) {
                PlanetView pv = new PlanetView(getActivity());
                planetLayout.addView(pv);
                planetViews.add(pv);
            }
            PlanetView pv = planetViews.get(i);
            if (planetData.get(i).drawable == -1) {
                pv.setVisibility(View.GONE);
            } else {
                pv.setLayoutParams(newParams);
                pv.setBackgroundResource(planetData.get(i).drawable);
                pv.setVisibility(View.VISIBLE);
            }
        }
    }


    public void onWindowFocusChanged() {
        Log.d("HandDeckFragment", "Window is changed! width is " + planetLayout.getWidth());
        layoutWidth = planetLayout.getWidth();
        redraw();
    }
}
