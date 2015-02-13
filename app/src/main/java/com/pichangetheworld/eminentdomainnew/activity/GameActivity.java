package com.pichangetheworld.eminentdomainnew.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;
import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.fragment.FieldFragment;
import com.pichangetheworld.eminentdomainnew.fragment.PlayerFragment;
import com.pichangetheworld.eminentdomainnew.fragment.MyPlanetsFragment;
import com.pichangetheworld.eminentdomainnew.util.CallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;
import com.pichangetheworld.eminentdomainnew.util.PlanetDrawableData;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;
import com.pichangetheworld.eminentdomainnew.view.PopupView;
import com.pichangetheworld.eminentdomainnew.view.ProduceTradePopupView;

import java.util.ArrayList;

/**
 * Eminent Domain AS
 *
 * Author: pchan
 * Date:   1/17/2015
 */
public class GameActivity extends FragmentActivity {
    // Fragment View Pager for planets/field
    MyFragmentAdapter mAdapter;
    ViewPager mPager;

    static MyPlanetsFragment myPlanetsFragment = new MyPlanetsFragment();
    static FieldFragment fieldFragment = new FieldFragment();

    // Current phase
    ImageView currentPhase;
    PlayerFragment playerFragment;

    int numPlayers = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.fragment_container);
        mPager.setAdapter(mAdapter);

        EminentDomainApplication.getInstance().setActivity(this);

        numPlayers = getIntent().getIntExtra("numPlayers", 3);

        currentPhase = (ImageView) findViewById(R.id.current_phase);
        playerFragment = (PlayerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_player);

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
        playerFragment.onWindowFocusChanged();
    }

    public void doneWarfare() {
        myPlanetsFragment.resetPlanetsClickable();
    }

    public void popupPrompt(final CardDrawableData data,
                            final CallbackInterface callback) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new PopupView(GameActivity.this, callback)
                        .setDetails(data)
                        .show(getWindow().getDecorView());
            }
        });
    }

    private static class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return myPlanetsFragment;
            } else {
                return fieldFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public void actionPhase(final String name, final boolean isComputer) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPhase.setImageDrawable(getResources().getDrawable(R.drawable.action_phase));
                Toast.makeText(GameActivity.this, name + "'s turn! Action Phase", Toast.LENGTH_LONG).show();

                if (!isComputer) {
                    showPlanets();
                    fieldFragment.onActionPhase();
                    playerFragment.onActionPhase();
                }
            }
        });
    }

    public void rolePhase() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPhase.setImageDrawable(getResources().getDrawable(R.drawable.role_phase));

                fieldFragment.onRolePhase();
                playerFragment.onRolePhase();
            }
        });
    }

    public void discardDrawPhase(final boolean isComputer) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPhase.setImageDrawable(getResources().getDrawable(R.drawable.discard_phase));

                if (!isComputer) {
                    showField();
                    fieldFragment.onDiscardDrawPhase();
                    playerFragment.onDiscardDrawPhase();
                }
            }
        });
    }

    public void showPlanets() {
        mPager.setCurrentItem(0);
    }

    public void showField() {
        mPager.setCurrentItem(1);
    }

    // Let player choose between produce and trade after selecting produce/trade
    public void letPlayerChooseProduceTrade(TargetCallbackInterface callback) {
        new ProduceTradePopupView(this, callback).show(getWindow().getDecorView());
    }

    // Let player choose role
    public void letPlayerChooseTargetRole(TargetCallbackInterface callback) {
        showField();
        fieldFragment.chooseTargetRole(callback);
    }

    // Let the player choose target planet
    public void letPlayerChooseTargetUnconqueredPlanet(boolean allowNone,
                                                       TargetCallbackInterface callback) {
        Log.d("GameActivity", "Letting player choose target unconquered planet.");
        showPlanets();
        myPlanetsFragment.chooseTargetUnconqueredPlanet(callback);
        if (allowNone) {
            // let hand be selectable too
            playerFragment.allowNone(callback);
        }
    }

    public void letPlayerChooseTargetConqueredPlanet(TargetCallbackInterface callback) {
        showPlanets();
        myPlanetsFragment.chooseTargetConqueredPlanet(callback);
    }

    // Update hand view
    public void updateHand(final ArrayList<CardDrawableData> drawables) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerFragment.updateHand(drawables);
            }
        });
    }

    // Update ship count
    public void updateShipCount(final int shipCount) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerFragment.updateShipCount(shipCount);
            }
        });
    }

    // Update planet view
    public void updatePlanets(final ArrayList<PlanetDrawableData> drawables) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myPlanetsFragment.updatePlanets(drawables);
            }
        });
    }
}
