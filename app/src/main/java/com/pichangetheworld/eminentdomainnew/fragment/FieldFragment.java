package com.pichangetheworld.eminentdomainnew.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 17/01/2015
 */
public class FieldFragment extends Fragment {
    final int ROLE_CARDS[] = {
            R.id.survey,
            R.id.warfare,
            R.id.colonize,
            R.id.producetrade,
            R.id.research
    };

    TargetCallbackInterface mCallback;
    private final View.OnClickListener onClickCard = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = 0;
            for (; i < ROLE_CARDS.length; ++i) {
                if (v.getId() == ROLE_CARDS[i]) {
                    break;
                }
            }
            if (selectedRole == i) {
                if (mCallback != null) {
                    mCallback.callback(selectedRole);

                    mCallback = null;
                }
                selectedRole = -1;
            } else {
                selectedRole = i;
            }
            updateSelectedRole();
        }
    };

    ImageButton [] roles = new ImageButton[5];
    int selectedRole = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_field, container, false);

        for (int i = 0; i < ROLE_CARDS.length; ++i) {
            int id = ROLE_CARDS[i];
            ImageButton button = (ImageButton) v.findViewById(id);
            button.setOnClickListener(onClickCard);
            button.setClickable(false);
            roles[i] = button;
        }

        return v;
    }

    // Update the field for Action Phase
    public void onActionPhase() {
        selectedRole = -1;
        updateSelectedRole();
    }

    // Update the field for Role Phase
    public void onRolePhase() {
        selectedRole = -1;
        updateSelectedRole();
    }

    // Allow the user to choose a role
    public void chooseTargetRole(TargetCallbackInterface callback) {
        mCallback = callback;
        for (ImageButton button : roles) {
            button.setClickable(true);
        }
    }

    // Update which role is selected
    public void updateSelectedRole() {
        for (int i = 0; i < roles.length; ++i) {
            if (roles[i] == null) break;
            if (i == selectedRole) {
                roles[i].setAlpha(0.7f);
            } else {
                roles[i].setAlpha(1.0f);
            }
        }
    }

    // Update the field for Discard/Draw Phase
    public void onDiscardDrawPhase() {
        selectedRole = -1;
        updateSelectedRole();
    }
}
