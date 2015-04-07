package com.pichangetheworld.eminentdomainnew.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.pichangetheworld.eminentdomainnew.R;


public class StartActivity extends Activity {
    NumberPicker np;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        np = (NumberPicker) findViewById(R.id.number_picker);
        np.setMaxValue(4);
        np.setMinValue(2);
        np.setValue(3);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public void start(View v) {
        Intent intent = new Intent(StartActivity.this, GameActivity.class);
        intent.putExtra("numPlayers", np.getValue());
        finish();
        startActivity(intent);
    }
}
