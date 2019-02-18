package com.app.D1App.CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class LatoHeavyButton extends android.support.v7.widget.AppCompatButton {
    public LatoHeavyButton(Context context) {
        super(context);
        setTypeface(context);
    }

    public LatoHeavyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public LatoHeavyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/lato_heavy.ttf"));
    }
}
