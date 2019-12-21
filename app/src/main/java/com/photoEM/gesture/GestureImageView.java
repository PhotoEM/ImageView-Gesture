package com.photoEM.gesture;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class GestureImageView extends RelativeLayout {

    private static final String TAG = GestureImageView.class.getSimpleName();

    private int TRANSLATION_X_SCALE = 210;
    private int TRANSLATION_Y_SCALE = 360;
    private int SLIDE_DISTANCE = 50;

    private enum SLIDETYPE {UNKNOW, UP_DOWN, LEFT_RIGHT}

    /**
     * finger counter.
     */
    private int mFingerCount;

    /**
     * Record signal finger translation move.
     */
    private float mTranslationX, mTranslationY;

    /**
     * Record signal finger raw move.
     */
    private float mActionX, mActionY;

    /**
     * Temp parameter to control signal finger translation move range.
     */
    private int mControlTranslationX, mControlTranslationY;

    /**
     * Signal finger slide left&right grid parameters.
     *
     * @mWidthGridCount: change according to CustomGridView.
     * @mWidthGrid: mWidthGrid = getMeasuredWidth / mWidthGridCount
     */
    private int mCurGridIdx, mPreGirdIdx, mPreWidthGrid;
    private int mWidthGrid, mWidthGridCount;

    /**
     * Signal finger slide up&down value parameters.
     */
    private int mCurrentValue, mPreValue;

    /**
     * GridTextView show
     */
    ArrayList<GridTextView> mGridList;
    ArrayList<Integer> mGridListValue = new ArrayList<>();
    GridTextView mCurrentGridView;

    private float mScale;
    private float mDegree;
    private float mSpacing;

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

    public void getGridList(ArrayList<GridTextView> gridList) {
        mGridList = gridList;
        mCurrentGridView = mGridList.get(0);


        for (int i = 0; i < mGridList.size(); i++) {
            mGridListValue.add(0);
        }
    }

    /**
     * After finish inflate can get the view all the reference.
     **/
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mFingerCount = 0;

        mTranslationX = mTranslationY = 0;
        mActionX = mActionY = 0;

        mCurrentValue = mPreValue = 0;

        mPreWidthGrid = mCurGridIdx = mPreGirdIdx = 0;
        mWidthGrid = 0;
        mWidthGridCount = 4;
    }

    /**
     * Get the moveX and moveY from on Measure after inflate.
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mControlTranslationX = getMeasuredWidth() / TRANSLATION_X_SCALE;
        mControlTranslationY = getMeasuredHeight() / TRANSLATION_Y_SCALE;
        mWidthGrid = getMeasuredWidth() / mWidthGridCount;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "mCurGridIdx:" + String.valueOf(mCurGridIdx) + ", mPreGirdIdx:" + String.valueOf(mPreGirdIdx));
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_DOWN");
                mPreGirdIdx = mCurGridIdx;
                mFingerCount += 1;
                mPreValue = 0;
                mPreWidthGrid = 0;
                mActionX = event.getRawX();
                mActionY = event.getRawY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_DOWN");
                mFingerCount += 1;
                mSpacing = getSpacing(event);
                mDegree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent ACTION_MOVE");
                mTranslationX = mTranslationX + event.getRawX() - mActionX;
                mTranslationY = mTranslationY + event.getRawY() - mActionY;
                mActionX = event.getRawX();
                mActionY = event.getRawY();

                int offset;
                if (1 == mFingerCount) {
                    if (getSlideType(mTranslationX, mTranslationY) == SLIDETYPE.UP_DOWN) {
                        offset = getSlideOffset(mTranslationY, SLIDETYPE.UP_DOWN);
                        setSlideValue(offset, 100, -100);
                        changeCurrentGridViewValue();
                    } else if (getSlideType(mTranslationX, mTranslationY) == SLIDETYPE.LEFT_RIGHT) {
                        ChangeGridView((int) mTranslationX);
                    } else {
                        Log.e(TAG, "SLIDETYPE could not match.");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "onTouchEvent ACTION_UP");

                // action up clean the translation
                mTranslationX = mTranslationY = 0;

                // action up clean if grid index change then clean the value
                checkIfChangeGridIdx();
//                if (mCurGridIdx != mPreGirdIdx) {
//                    mCurrentValue = mCurrentGridView.getTextValue(mCurGridIdx);
//                }

                mFingerCount -= 1;
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

    private void checkIfChangeGridIdx() {
        Log.i(TAG, "mCurGridIdx:" + String.valueOf(mCurGridIdx) + ", mPreGirdIdx:" + String.valueOf(mPreGirdIdx));

        if (mCurGridIdx != mPreGirdIdx) {
            mCurrentValue = mGridListValue.get(mCurGridIdx);
        } else {
            mGridListValue.set(mCurGridIdx, mCurrentValue);
        }
    }

    private void changeCurrentGridViewValue() {
        mCurrentGridView = mGridList.get(mCurGridIdx);
        mCurrentGridView.changeValue(mCurrentValue, mCurGridIdx);
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
     * Get two fingers point degree
     */
    private float getDegree(MotionEvent event) {
        double delta_x = event.getX(0) - event.getX(1);
        double delta_y = event.getY(0) - event.getY(1);
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    /**
     * Get slide screen offset
     **/
    private int getSlideOffset(float slide, SLIDETYPE slideType) {
        if (slideType == SLIDETYPE.LEFT_RIGHT) {
            return (int) slide / mControlTranslationX;
        } else if (slideType == SLIDETYPE.UP_DOWN) {
            return (int) slide / mControlTranslationY;
        } else {
            Log.e(TAG, "Do not support slide flag: " + slideType);
            return 0;
        }
    }

    /**
     * Set slide screen value
     */
    private void setSlideValue(int offset, int max, int min) {
        mCurrentValue = mCurrentValue + offset - mPreValue;
        mPreValue = offset;
        if (mCurrentValue > max) {
            mCurrentValue = max;
        } else if (mCurrentValue < min) {
            mCurrentValue = min;
        }
    }

    /**
     * @return 0 for slide up and down, 1 for slide left and right
     */
    private SLIDETYPE getSlideType(float moveX, float moveY) {
        if (Math.abs(moveX) - Math.abs(moveY) > SLIDE_DISTANCE) {
            // Log.i(TAG, "Slide left and right");
            return SLIDETYPE.LEFT_RIGHT;
        } else if (Math.abs(moveY) - Math.abs(moveX) > SLIDE_DISTANCE) {
            // Log.i(TAG, "Slide up and down");
            return SLIDETYPE.UP_DOWN;
        }
        return SLIDETYPE.UNKNOW;
    }

    private void ChangeGridView(int value) {
        int offset = (value / mWidthGrid);
        mCurGridIdx += offset - mPreWidthGrid;
        if (mCurGridIdx > mWidthGridCount - 1) {
            mCurGridIdx = mWidthGridCount - 1;
        } else if (mCurGridIdx < 0) {
            mCurGridIdx = 0;
        }
        mPreWidthGrid = offset;
        Log.i(TAG, mWidthGrid + ", offset " + offset + " ,mCurGridIdx " + mCurGridIdx);
    }
}
