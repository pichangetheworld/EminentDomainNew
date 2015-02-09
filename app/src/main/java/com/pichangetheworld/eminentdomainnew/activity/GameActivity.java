package com.pichangetheworld.eminentdomainnew.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.pichangetheworld.eminentdomainnew.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.fragment.FieldFragment;
import com.pichangetheworld.eminentdomainnew.fragment.MyHandAndDeckFragment;
import com.pichangetheworld.eminentdomainnew.fragment.MyPlanetsFragment;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;

import java.util.ArrayList;

/**
 * Eminent Domain AS
 *
 * Author: pchan
 * Date:   1/17/2015
 */
public class GameActivity extends FragmentActivity {
    // Handler to receive HAND_CHANGED broadcasts
    private final BroadcastReceiver mUpdateHandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<CardDrawableData> drawables = intent.getParcelableArrayListExtra("drawable");
            Log.d("GameActivity", "Received Hand Changed intent with " + drawables.size() + " items");
            myHandAndDeckFragment.updateHand(drawables);
        }
    };
    // Handler to receive PLANETS_CHANGED broadcasts
    private final BroadcastReceiver mUpdatePlanetsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<PlanetDrawableData> drawables = intent.getParcelableArrayListExtra("drawable");
            Log.d("GameActivity", "Received Hand Changed intent with " + drawables.size() + " items");
            myPlanetsFragment.updatePlanets(drawables);
        }
    };

    FieldFragment fieldFragment = new FieldFragment();
    MyPlanetsFragment myPlanetsFragment = new MyPlanetsFragment();
    MyHandAndDeckFragment myHandAndDeckFragment;

    int numPlayers = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        EminentDomainApplication.getInstance().setActivity(this);

        numPlayers = getIntent().getIntExtra("numPlayers", 3);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fieldFragment);
        ft.commit();

        myHandAndDeckFragment = (MyHandAndDeckFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_player);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mUpdateHandReceiver, new IntentFilter("HAND_UPDATED"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mUpdatePlanetsReceiver, new IntentFilter("PLANETS_UPDATED"));
        Thread thread = new Thread() {
            @Override
            public void run() {
                EminentDomainApplication.getInstance()
                        .startGame(numPlayers);
            }
        };
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        EminentDomainApplication.getInstance().setActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EminentDomainApplication.getInstance().setActivity(null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        myHandAndDeckFragment.onWindowFocusChanged();
        if (myPlanetsFragment.isVisible()) {
            myPlanetsFragment.onWindowFocusChanged();
        }
    }

    public void actionPhase(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, name + "'s turn! Action Phase", Toast.LENGTH_LONG).show();
                showField();
                fieldFragment.onActionPhase();
                myHandAndDeckFragment.onActionPhase();
            }
        });
    }

    public void rolePhase() {
        Toast.makeText(this, "Role Phase", Toast.LENGTH_LONG).show();
        showField();
        fieldFragment.onRolePhase();
        myHandAndDeckFragment.onRolePhase();
    }

    public void discardDrawPhase() {
        Toast.makeText(this, "Discard Phase", Toast.LENGTH_LONG).show();
        showField();
        fieldFragment.onDiscardDrawPhase();
        myHandAndDeckFragment.onDiscardDrawPhase();
    }

    public void showField() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fieldFragment);
        ft.commit();
    }

    public void showPlanets() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, myPlanetsFragment);
        ft.commit();
    }
}
