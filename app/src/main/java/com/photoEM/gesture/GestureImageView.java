package com.photoEM.gesture;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GestureImageView extends RelativeLayout {

    private static final String TAG = GestureImageView.class.getSimpleName();

    private float mFingerCount;

    private float mTranslationX;
    private float mTranslationY;
    private float mActionX;
    private float mActionY;
    private int mMoveX;
    private int mMoveY;
    private int TRANSLATION_X_SCALE = 210;
    private int TRANSLATION_Y_SCALE = 360;

    // for slide offset and slide value parameters
    private int mValue;
    private int offset = 0;
    private int preOffset = 0;

    private float mScale;
    private float mSpacing;

    private AlertDialog alertDialog = null;
    private AlertDialog.Builder alertBuilder = null;

    private TextView mTextView;

    /**
     * Construction for Gesture Image View.
     **/
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

    /**
     * After finish inflate can get the view all the reference.
     **/
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mFingerCount = 0;
        mValue = 0;

        mTranslationX = 0;
        mTranslationY = 0;
        mActionX = 0;
        mActionY = 0;
        mScale = 0;
        mSpacing = 0;

        mTextView = (TextView) findViewById(R.id.EditText);

        //将从资源文件中加载的属性设置给子控件
        setPageTitleText(0);
    }

    /**
     * Get the moveX and moveY from on Measure after inflate.
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMoveX = getMeasuredWidth() / TRANSLATION_X_SCALE;
    }

    public void setPageTitleText(int value) {
        mTextView.setText(String.valueOf(value));
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
                Log.i(TAG, "onTouchEvent ACTION_DOWN");
                mFingerCount += 1;
                preOffset = 0;
                mActionX = event.getRawX();
                mActionY = event.getRawY();
                Log.i(TAG, "actionX:" + mActionX + ", actionY: " + mActionY);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_DOWN");
                mFingerCount += 1;
                mSpacing = getSpacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent ACTION_MOVE");
                mTranslationX = mTranslationX + event.getRawX() - mActionX;
                mTranslationY = mTranslationY + event.getRawY() - mActionY;
                mActionX = event.getRawX();
                mActionY = event.getRawY();
                SetSlideValue(getSlideOffset(mTranslationX), 100, -100);
                Log.i(TAG, "translationX:" + mTranslationX + ", translationY: " + mTranslationY);
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent ACTION_UP");
                mTranslationX = 0;
                mTranslationY = 0;
                mFingerCount -= 1;
                Log.i(TAG, "value: " + String.valueOf(mValue));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_UP");
                mFingerCount -= 1;
                break;
            default:
                Log.i(TAG, "onTouchEvent " + event.getAction());
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * Get two fingers points distance
     **/
    private float getSpacing(MotionEvent event) {
        // distance = sqrt(x^2 + y^2)
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Get slide screen offset
     **/
    private int getSlideOffset(float translationX) {
        int offset = (int) translationX / mMoveX;
        return offset;
    }

    /**
     * Set slide screen value
     * @param offset
     * @param max
     * @param min
     */
    private void SetSlideValue(int offset, int max, int min) {
        mValue = mValue + offset - preOffset;
        preOffset = offset;
        if (mValue > max) {
            mValue = max;
        } else if (mValue < min) {
            mValue = min;
        }
    }

    /**
     * Get slide screen value
     * @return
     */
    private int GetSlideValue() {
        return mValue;
    }
}
