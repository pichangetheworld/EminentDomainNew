package com.pichangetheworld.eminentdomainnew.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pichangetheworld.eminentdomainnew.R;
import com.pichangetheworld.eminentdomainnew.util.CallbackInterface;
import com.pichangetheworld.eminentdomainnew.util.CardDrawableData;

/**
 * Eminent Domain AS
 * Author: pchan
 * Date: 12/02/2015
 */
public class PopupView extends PopupWindow implements View.OnClickListener {
    TextView cardName;
    ImageView cardView;
    CallbackInterface mCallback;

    public PopupView(Context context, CallbackInterface callback) {
        super((int) (140 * context.getResources().getDisplayMetrics().density),
                (int) (200 * context.getResources().getDisplayMetrics().density));

        mCallback = callback;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mCallback != null) {
                    mCallback.callback();

                    mCallback = null;
                }
            }
        });

        // So the popup will close when you click outside
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), ""));
        setOutsideTouchable(true);
        init(View.inflate(context, R.layout.popup, null));
    }

    private PopupView init(View view) {
        setContentView(view);
        cardName = (TextView) view.findViewById(R.id.name);
        cardView = (ImageView) view.findViewById(R.id.card_view);
        view.setOnClickListener(this);
        return this;
    }

    public PopupView setDetails(CardDrawableData data) {
        Log.d("PopupView", "Setting data " + data + " to view " + data.name);
        cardName.setText(data.name);
        cardView.setBackgroundResource(data.drawable);
        return this;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    public void show(View rootView) {
        showAtLocation(rootView, Gravity.CENTER, 0, 40);
    }
}