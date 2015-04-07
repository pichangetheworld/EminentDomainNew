package com.pichangetheworld.eminentdomainnew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.application.EminentDomainApplication;


public class EndActivity extends Activity {
    TextView winnerNameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        winnerNameView = (TextView) findViewById(R.id.winner_id);

        EminentDomainApplication.getInstance().setEndGameActivity(this);
    }

    // Set the winner, so we can decide to show this after the activity has been loaded
    public void setWinner(String winner) {
        winnerNameView.setText(winner);
    }

    public void end(View v) {
        Intent intent = new Intent(EndActivity.this, StartActivity.class);
        finish();
        startActivity(intent);
    }
}
