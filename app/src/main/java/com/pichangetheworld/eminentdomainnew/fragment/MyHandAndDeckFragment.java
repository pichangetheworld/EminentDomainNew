package com.pichangetheworld.eminentdomainnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.view.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class MyHandAndDeckFragment extends Fragment {
    // Views
    Button okayButton;
    Button viewPlanetButton;
    // Parent layout holding all the card views
    RelativeLayout handView;
    List<CardView> handCards;
    // Drawables for hand cards at the same index as hand cards
    List<Integer> handDrawables;

    DisplayMetrics displayMetrics;
    float handWidth = 400;

    // Index of selected card
    int selectedAction;
    List<Integer> selectedHandCards;

    private final View.OnClickListener onOkay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Clicking okay in the player view
            // This will most likely be from playing cards in the hand
            // Disable the button to prevent multiple clicking
            okayButton.setVisibility(View.GONE);
            EminentDomainApplication.getInstance().playAction(selectedAction);

            // Reset
            selectedAction = -1;
        }
    };

    boolean showPlanet = true;
    private final View.OnClickListener toggleFieldPlanet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (showPlanet) {
                // Clicking the show Planet View
                ((GameActivity) getActivity()).showPlanets();
            } else {
                ((GameActivity) getActivity()).showField();
            }
            showPlanet = !showPlanet;
            toggleFieldPlanetButton();
        }
    };
    private void toggleFieldPlanetButton() {
        if (showPlanet) {
            viewPlanetButton.setText(R.string.view_planet);
        } else {
            viewPlanetButton.setText(R.string.view_field);
        }
    }

    private final View.OnClickListener onCardClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Clicking a card
            int i;
            for (i = 0; i < handCards.size(); ++i) {
                if (handCards.get(i) == v) {
                    break;
                }
            }

            // Show the selected card
            if (selectedAction == i) {
                selectedAction = -1;
            } else {
                selectedAction = i;
            }

            redraw();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handCards = new ArrayList<>();
        handDrawables = new ArrayList<>();
        selectedHandCards = new ArrayList<>();
        displayMetrics = new DisplayMetrics();

        WindowManager windowManager = getActivity().getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player, container, false);
        okayButton = (Button) v.findViewById(R.id.okay_button);
        okayButton.setOnClickListener(onOkay);

        viewPlanetButton = (Button) v.findViewById(R.id.view_planet_button);
        viewPlanetButton.setOnClickListener(toggleFieldPlanet);

        handView = (RelativeLayout) v.findViewById(R.id.hand);
        handView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                handView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                handWidth = handView.getWidth();
                Log.d("PlayerHandFragment", "Updated hand width " + handWidth);
            }
        });

        selectedAction = -1;

        return v;
    }

    // Allow the user to do something or skip
    public void enableSkip(boolean enable) {
        if (enable) {
            okayButton.setText("Skip");
        } else {
            okayButton.setText("OK");
        }
    }

    // Update the hand view with cards in the user's hand
    public void updateHand(int [] handDrawables) {
        this.handDrawables.clear();
        for (int i : handDrawables) {
            this.handDrawables.add(i);
        }
        if (handView != null) {
            redraw();
        }
    }

    private void redraw() {
        float cardWidth;
        if (handDrawables.isEmpty()) {
            cardWidth = 75;
        } else {
            cardWidth = Math.min(handWidth/handDrawables.size(), 75);
        }
        for (int i = 0; i < handDrawables.size(); ++i) {
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                    Math.round(cardWidth * displayMetrics.density),
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            newParams.addRule(RelativeLayout.RIGHT_OF, i);
            if (i == handCards.size()) {
                CardView cv = new CardView(getActivity());
                cv.setId(i + 1);
                cv.setOnClickListener(onCardClicked);
                handView.addView(cv);
                handCards.add(cv);
            }
            CardView cv = handCards.get(i);
            if (handDrawables.get(i) == -1) {
                cv.setVisibility(View.GONE);
            } else {
                cv.setLayoutParams(newParams);
                cv.setBackgroundResource(handDrawables.get(i));
                cv.setVisibility(View.VISIBLE);

                // Show the selected card
                if (selectedAction == i) {
                    cv.onCardSelected(true);
                } else {
                    cv.onCardSelected(false);
                }
            }
        }
        enableSkip(selectedAction == -1);
    }

    public void onActionPhase() {
        okayButton.setVisibility(View.VISIBLE);
    }

    public void onRolePhase() {
        okayButton.setVisibility(View.GONE);
        // TODO set it back to visible when we try to match
    }

    public void onDiscardDrawPhase() {
        okayButton.setVisibility(View.VISIBLE);

        // If you don't discard
        enableSkip(selectedHandCards.isEmpty());
    }
}
