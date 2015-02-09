package com.pichangetheworld.eminentdomainnew.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Eminent Domain AS
 *
 * Author: pchan
 * Date:   1/17/2015
 */
public class GameActivity extends FragmentActivity {
    // Handler to receive PHASE_END broadcasts
    private final BroadcastReceiver mUpdateHandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int [] drawables = intent.getIntArrayExtra("drawable");
            Log.d("GameActivity", "Received Hand Changed intent with " + drawables.length + " items");
            playerHandAndDeckFragment.updateHand(drawables);
        }
    };

    final Fragment fragments[] = {
            new FieldFragment(),
            new MyPlanetsFragment()
    };
    MyHandAndDeckFragment playerHandAndDeckFragment;

    int numPlayers = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        EminentDomainApplication.getInstance().setActivity(this);

        numPlayers = getIntent().getIntExtra("numPlayers", 3);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, fragments[0]);
        ft.commit();

        playerHandAndDeckFragment = (MyHandAndDeckFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_player);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mUpdateHandReceiver, new IntentFilter("HAND_UPDATED"));
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

    public void actionPhase(final String name) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, name + "'s turn! Action Phase", Toast.LENGTH_LONG).show();
                showField();
                ((FieldFragment) fragments[0]).onActionPhase();
                playerHandAndDeckFragment.onActionPhase();
            }
        });
    }

    public void rolePhase() {
        Toast.makeText(this, "Role Phase", Toast.LENGTH_LONG).show();
        showField();
        ((FieldFragment) fragments[0]).onRolePhase();
        playerHandAndDeckFragment.onRolePhase();
    }

    public void discardDrawPhase() {
        Toast.makeText(this, "Discard Phase", Toast.LENGTH_LONG).show();
        showField();
        ((FieldFragment) fragments[0]).onDiscardDrawPhase();
        playerHandAndDeckFragment.onDiscardDrawPhase();
    }

    public void showField() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragments[0]);
        ft.commit();
    }

    public void showPlanets() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragments[1]);
        ft.commit();
    }
}
