package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.TargetCallbackInterface;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 12/02/2015
 */

// Popup to let the user choose between Produce and Trade when they play a Produce/Trade card
public class ProduceTradePopupView extends PopupWindow {
    TargetCallbackInterface mCallback;

    public ProduceTradePopupView(Context context, TargetCallbackInterface callback) {
        super((int) (300 * context.getResources().getDisplayMetrics().density),
                (int) (200 * context.getResources().getDisplayMetrics().density));
        View v = View.inflate(context, R.layout.produce_trade, null);
        setContentView(v);

        mCallback = callback;

        v.findViewById(R.id.chooseProduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallback.callback(0);
            }
        });
        v.findViewById(R.id.chooseTrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mCallback.callback(1);
            }
        });
    }

    public void show(View rootView) {
        showAtLocation(rootView, Gravity.CENTER, 0, 40);
    }
}