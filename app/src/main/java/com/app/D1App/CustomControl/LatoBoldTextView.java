package com.app.D1App.CustomControl;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class LatoBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public LatoBoldTextView(Context context) {
        super(context);
        setTypeface(context);
    }

    public LatoBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(context);
    }

    public LatoBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(context);
    }
    private void setTypeface(Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "Font/lato_bold.ttf"));
    }
}
