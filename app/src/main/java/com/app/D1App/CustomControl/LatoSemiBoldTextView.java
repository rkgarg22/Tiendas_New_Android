package com.app.D1App.CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LatoSemiBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public LatoSemiBoldTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public LatoSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public LatoSemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/lato_semibold.ttf"));
    }
}
