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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.activity.GameActivity;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.MultiTargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.SelectMode;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.view.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class PlayerFragment extends Fragment {
    // Views
    Button okayButton;
    Button noneButton;
    TextView shipCountView;
    ImageView deckView;
    RelativeLayout discardView;

    // Parent layout holding all the card views
    RelativeLayout handView;
    List<CardView> handCards;

    // Card data
    List<CardDrawableData> handCardData;
    List<CardDrawableData> discardData;

    int handLimit;

    // Layout display sizes for resizing hand cards
    DisplayMetrics displayMetrics;
    float handWidth = 0;


    // Select mode - single or multiple card select
    SelectMode curMode;

    // Index of selected card
    int selectedAction;
    List<Integer> selectedHandCards;

    MultiTargetCallbackInterface mMultiCallback;
    int multiCardLimit = 0;
    private final View.OnClickListener onOkay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Clicking okay in the player view
            // This will most likely be from playing cards in the hand
            // Disable the button to prevent multiple clicking
            okayButton.setVisibility(View.GONE);
            switch (curMode) {
                case ACTION_PLAY_PHASE:
                    // play the selected action
                    EminentDomainApplication.getInstance().playAction(selectedAction);
                    break;
                case DISCARD:
                case RESEARCH:
                    mMultiCallback.callback(selectedHandCards);
                    break;
            }

            reset();
        }
    };

    private final View.OnClickListener onCardClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Clicking a card
            Integer i;
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
                case RESEARCH:
                    // Show the selected card
                    if (selectedHandCards.contains(i)) {
                        selectedHandCards.remove(i);
                    } else if (selectedHandCards.size() < multiCardLimit) {
                        selectedHandCards.add(i);
                    }
                    break;
                case DISCARD:
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
        handCardData = new ArrayList<>();
        discardData = new ArrayList<>();
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

        noneButton = (Button) v.findViewById(R.id.allow_none);
        noneButton.setVisibility(View.GONE);

        deckView = (ImageView) v.findViewById(R.id.deck);
        discardView = (RelativeLayout) v.findViewById(R.id.discardPile);

        handView = (RelativeLayout) v.findViewById(R.id.hand);

        shipCountView = (TextView) v.findViewById(R.id.ship_count);

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
    public void updateHand(ArrayList<CardDrawableData> data, int handLimit) {
        this.handCardData.clear();
        this.handCardData.addAll(data);
        this.handLimit = handLimit;
        if (handWidth > 0) {
            redraw();
        }
    }

    // Display the number of ships player has
    public void updateShipCount(int shipCount) {
        shipCountView.setText("x" + shipCount);
    }

    // Update the top card of the discard pile and the list of cards in it
    public void updateDiscardPile(ArrayList<CardDrawableData> drawables) {
        discardData.clear();
        discardData.addAll(drawables);
        ImageView image = (ImageView) discardView.findViewById(R.id.image);
        TextView count = (TextView) discardView.findViewById(R.id.remaining_count);
        if (discardData.isEmpty()) {
            image.setImageResource(R.drawable.blank_card);
            count.setVisibility(View.GONE);
        } else {
            image.setImageResource(discardData.get(discardData.size()-1).drawable);
            count.setText(Integer.toString(drawables.size()));
            count.setVisibility(View.VISIBLE);
        }
    }

    private void redraw() {
        float cardWidth;
        if (handCardData.isEmpty()) {
            cardWidth = 75 * displayMetrics.density;
        } else {
            cardWidth = Math.min(handWidth/ handCardData.size(), 75 * displayMetrics.density);
        }
        Log.d("HandDeckFragment", "Setting card width to " + cardWidth);
        for (int i = 0; i < Math.max(handCardData.size(), handCards.size()); ++i) {
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
            if (i >= handCardData.size() || handCardData.get(i).drawable == -1) {
                cv.setVisibility(View.GONE);
            } else {
                cv.setLayoutParams(newParams);
                cv.setBackgroundResource(handCardData.get(i).drawable);
                cv.setVisibility(View.VISIBLE);

                switch (curMode) {
                    case ACTION_PLAY_PHASE:
                        // Show the selected card
                        if (selectedAction == i) {
                            cv.onCardSelected(true);
                        } else {
                            cv.onCardSelected(false);
                        }
                        break;
                    case DISCARD:
                    case RESEARCH:
                        if (selectedHandCards.contains(i)) {
                            cv.onCardSelected(true);
                        } else {
                            cv.onCardSelected(false);
                        }
                        break;
                    default:
                        cv.onCardSelected(false);
                }
            }
        }
        switch (curMode) {
            case ACTION_PLAY_PHASE:
                enableSkip(selectedAction == -1);
                break;
            case DISCARD:
                if (handCardData.size() - selectedHandCards.size() <= handLimit) {
                    enableSkip(selectedHandCards.isEmpty());
                    okayButton.setEnabled(true);
                } else
                    okayButton.setEnabled(false);
                break;
        }
    }

    public void onActionPhase() {
        curMode = SelectMode.ACTION_PLAY_PHASE;
        okayButton.setVisibility(View.VISIBLE);
        okayButton.setEnabled(true);
        noneButton.setVisibility(View.GONE);
    }

    public void onRolePhase() {
        curMode = SelectMode.MATCH_ROLE_PHASE;
        okayButton.setVisibility(View.GONE);
        noneButton.setVisibility(View.GONE);
        // TODO set it back to visible when we try to match

        redraw();
    }

    public void onDiscardDrawPhase() {
        curMode = SelectMode.DISCARD;
        okayButton.setVisibility(View.VISIBLE);
        noneButton.setVisibility(View.GONE);
        redraw();
    }

    public void onWindowFocusChanged() {
        Log.d("HandDeckFragment", "Window is changed! width is " + handView.getWidth());
        handWidth = handView.getWidth();
        redraw();
    }

    public void allowNone(final TargetCallbackInterface callback) {
        noneButton.setVisibility(View.VISIBLE);
        noneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.callback(-1);
                ((GameActivity) getActivity()).doneWarfare();
                noneButton.setVisibility(View.GONE);
            }
        });
    }

    // Let player select multiple cards in hand to remove from game
    public void selectCardsToRemove(int max, SelectMode mode,
                                    final MultiTargetCallbackInterface callback) {
        curMode = mode;
        mMultiCallback = callback;
        if (mode == SelectMode.RESEARCH)
            multiCardLimit = max;
        else
            multiCardLimit = handCardData.size();
        selectedHandCards.clear();

        okayButton.setVisibility(View.VISIBLE);
        okayButton.setEnabled(true);
        okayButton.setText("OK");
        noneButton.setVisibility(View.GONE);
        redraw();
    }

    private void reset() {
        selectedAction = -1;
        selectedHandCards.clear();
        mMultiCallback = null;
        multiCardLimit = 0;
        redraw();
    }
}
