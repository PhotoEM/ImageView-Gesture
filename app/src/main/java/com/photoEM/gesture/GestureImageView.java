package com.photoEM.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class GestureImageView extends RelativeLayout {

    private static final String TAG = GestureImageView.class.getSimpleName();

    private float translationX;
    private float translationY;
    private float scal = 1;

    private float actionX;
    private float actionY;
    private float spacing;

    private int upDP = 25;
    private int downDP = 25;
    private int leftDP = 25;
    private int rightDP = 25;

    // 0:not select, 1:double finger drag, 2:double finger scale
    private int moveType;

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                actionX = event.getX();
                actionY = event.getY();
                Log.i(TAG, "actionX:" + actionX + ", action Y: %f" + actionY);
                break;
            case MotionEvent.ACTION_MOVE:
                translationX = event.getX();
                translationY = event.getY();
                Log.i(TAG, "actionX:" + actionX + ", action Y: %f" + actionY);
                break;
            case MotionEvent.ACTION_UP:
                if (translationY - actionY > 0 && (Math.abs(translationY - actionY) > upDP)) {
                    //slide down
                    Log.i(TAG, "slide down");
                } else if (translationY - actionY < 0 && (Math.abs(translationY - actionY) > downDP)) {
                    //slide up
                    Log.i(TAG, "slide up");
                }
                if (translationX - actionX > 0 && (Math.abs(translationX - actionX) > leftDP)) {
                    // slide right
                    Log.i(TAG, "slide right");
                } else if (translationX - actionX < 0 && (Math.abs(translationX - actionX) < rightDP)) {
                    // slide left
                    Log.i(TAG, "slide left");
                }
            break;
        }
    }
}
