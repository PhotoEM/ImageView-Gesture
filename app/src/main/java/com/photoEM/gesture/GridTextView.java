package com.photoEM.gesture;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridTextView extends RelativeLayout {
    private static final String TAG = GestureImageView.class.getSimpleName();

    public GridTextView(Context context) {
        this(context, null);
    }

    public GridTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }


}
