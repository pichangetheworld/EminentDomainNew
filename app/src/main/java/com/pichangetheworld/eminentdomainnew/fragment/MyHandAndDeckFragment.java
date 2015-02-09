package com.pichangetheworld.eminentdomainnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.SelectMode;
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

    // Card data
    List<CardDrawableData> cardData;


    // Layout display sizes for resizing hand cards
    DisplayMetrics displayMetrics;
    float handWidth = 0;


    // Select mode - single or multiple card select
    SelectMode curMode;

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
            switch (curMode) {
                case ACTION_PLAY_PHASE:
                    EminentDomainApplication.getInstance().playAction(selectedAction);
                    break;
                case SELECT_TO_DISCARD_DRAW_PHASE:
                    // discard the cards
                    break;
            }

            // Reset
            selectedAction = -1;
            selectedHandCards.clear();
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
            switch (curMode) {
                case ACTION_PLAY_PHASE:
                    // Show the selected card
                    if (selectedAction == i) {
                        selectedAction = -1;
                    } else {
                        selectedAction = i;
                    }
                    break;
                case SELECT_TO_DISCARD_DRAW_PHASE:
                    // Show the selected card
                    if (selectedHandCards.contains(i)) {
                        selectedHandCards.remove(i);
                    } else {
                        selectedHandCards.add(i);
                    }
                    break;
            }

            redraw();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handCards = new ArrayList<>();
        cardData = new ArrayList<>();
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
    public void updateHand(ArrayList<CardDrawableData> data) {
        this.cardData.clear();
        this.cardData.addAll(data);
        if (handWidth > 0) {
            redraw();
        }
    }

    private void redraw() {
        float cardWidth;
        if (cardData.isEmpty()) {
            cardWidth = 75 * displayMetrics.density;
        } else {
            cardWidth = Math.min(handWidth/cardData.size(), 75 * displayMetrics.density);
        }
        Log.d("HandDeckFragment", "Setting card width to " + cardWidth);
        for (int i = 0; i < cardData.size(); ++i) {
            RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                    Math.round(cardWidth),
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
            if (cardData.get(i).drawable == -1) {
                cv.setVisibility(View.GONE);
            } else {
                cv.setLayoutParams(newParams);
                cv.setBackgroundResource(cardData.get(i).drawable);
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
        curMode = SelectMode.ACTION_PLAY_PHASE;
        okayButton.setVisibility(View.VISIBLE);
    }

    public void onRolePhase() {
        curMode = SelectMode.MATCH_ROLE_PHASE;
        okayButton.setVisibility(View.GONE);
        // TODO set it back to visible when we try to match
    }

    public void onDiscardDrawPhase() {
        curMode = SelectMode.SELECT_TO_DISCARD_DRAW_PHASE;
        okayButton.setVisibility(View.VISIBLE);

        if (cardData.size() <= 5) // TODO actually depend on what hand limit is
            // If you don't discard
            enableSkip(selectedHandCards.isEmpty());
        else
            okayButton.setEnabled(false);
    }

    public void onWindowFocusChanged() {
        Log.d("HandDeckFragment", "Window is changed! width is " + handView.getWidth());
        handWidth = handView.getWidth();
        redraw();
    }
}
