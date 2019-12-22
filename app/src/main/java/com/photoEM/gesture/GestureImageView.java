package com.photoEM.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;


public class GestureImageView extends RelativeLayout {

    private static final String TAG = GestureImageView.class.getSimpleName();

    private static final int TRANSLATION_Y_SCALE = 360;
    private static final int SCREEN_GRID_COUNT = 6;
    private static final int SLIDE_DISTANCE = 50;

    private enum SLIDE_TYPE {UNKNOW, UP_DOWN, LEFT_RIGHT}

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
     * @mWidthGrid: mWidthGrid = getMeasuredWidth / SCREEN_GRID_COUNT
     */
    private int mCurGridIdx, mPreGirdIdx;

    /**
     * Signal finger slide up&down value parameters.
     */
    private int mCurValue, mPreValue;

    private boolean FlagUpDown;
    private boolean FlagLeftRight;

    /**
     * GridView show
     */
    ArrayList<GridView> mGridList;
    ArrayList<Integer> mGridListValue = new ArrayList<>();
    GridView mCurrentGridView;

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

        init();
    }

    private void init() {
        mFingerCount = 0;

        mTranslationX = mTranslationY = mActionX = mActionY = 0;

        mCurValue = mPreValue = 0;
        mCurGridIdx = mPreGirdIdx = 0;

        FlagUpDown = FlagLeftRight = false;
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    public void getGridList(ArrayList<GridView> gridList) {
        mGridList = gridList;
        mCurrentGridView = mGridList.get(0);
        mCurrentGridView.changeShapeBackground();

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
    }

    /**
     * Get the moveX and moveY from on Measure after inflate.
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mControlTranslationX = getMeasuredWidth() / SCREEN_GRID_COUNT;
        mControlTranslationY = getMeasuredHeight() / TRANSLATION_Y_SCALE;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.i(TAG, "mCurGridIdx:" + String.valueOf(mCurGridIdx) + ", mPreGirdIdx:" + String.valueOf(mPreGirdIdx));

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_DOWN");

                mFingerCount += 1;
                mPreValue = 0;
                mPreGirdIdx = 0;
                mActionX = event.getRawX();
                mActionY = event.getRawY();
                FlagUpDown = FlagLeftRight = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_DOWN");

                mFingerCount += 1;
                mSpacing = getSpacing(event);
                mDegree = getDegree(event);
                break;
            case MotionEvent.ACTION_MOVE:
                // Log.i(TAG, "onTouchEvent ACTION_MOVE");

                mTranslationX = mTranslationX + event.getRawX() - mActionX;
                mTranslationY = mTranslationY + event.getRawY() - mActionY;
                mActionX = event.getRawX();
                mActionY = event.getRawY();


                if (1 == mFingerCount) {
                    int offset = 0;

                    if (FlagUpDown) {
                        if (mTranslationY <= 0) {
                            offset = getSlideOffset(mTranslationY + SLIDE_DISTANCE, SLIDE_TYPE.UP_DOWN);
                        } else if (mTranslationY > 0) {
                            offset = getSlideOffset(mTranslationY - SLIDE_DISTANCE, SLIDE_TYPE.UP_DOWN);
                        }
                        setSlideValue(-offset, 100, -100);
                        changeGridViewWithValue(mCurValue, mCurGridIdx);
                        break;
                    }
                    if (FlagLeftRight) {
                        if (mTranslationX <= 0) {
                            offset = getSlideOffset(mTranslationX + SLIDE_DISTANCE, SLIDE_TYPE.LEFT_RIGHT);
                        } else if (mTranslationX > 0) {
                            offset = getSlideOffset(mTranslationX - SLIDE_DISTANCE, SLIDE_TYPE.LEFT_RIGHT);
                        }
                        setSlideGrid(offset, mGridList.size() - 1, 0);
                        changeGridView(mCurGridIdx);
                        break;
                    }

                    // slide up and down
                    if (getSlideType(mTranslationX, mTranslationY) == SLIDE_TYPE.UP_DOWN) {
                        FlagUpDown = true;
                        FlagLeftRight = false;
                    }
                    // slide left and right
                    else if (getSlideType(mTranslationX, mTranslationY) == SLIDE_TYPE.LEFT_RIGHT) {
                        FlagLeftRight = true;
                        FlagUpDown = false;
                    }
                    // slide error
                    else {
                        Log.e(TAG, "getSlideType return SLIDE_TYPE could not match.");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // action up clean the translation
                mTranslationX = mTranslationY = 0;

                // action up clean if grid index change then clean the value
                checkIfChangeGridIdx();

                // reverse shape background
                for (int i = 0; i < mGridList.size(); i++) {
                    if (i == mCurGridIdx) {
                        continue;
                    }
                    mGridList.get(i).setBackgroundResource(R.drawable.grid_shape);
                }

                mFingerCount -= 1;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "onTouchEvent ACTION_POINTER_UP");
                mFingerCount -= 1;
                break;
            default:
                Log.e(TAG, "onTouchEvent " + event.getAction());
                break;

        }
        return super.onTouchEvent(event);
    }

    private void checkIfChangeGridIdx() {
        if (mCurGridIdx != mPreGirdIdx) {
            mCurValue = mGridListValue.get(mCurGridIdx);
        } else {
            mGridListValue.set(mCurGridIdx, mCurValue);
        }
    }

    private void changeGridViewWithValue(int value, int gridIdx) {
        mGridList.get(gridIdx).changeValue(value, gridIdx);
    }

    private void changeGridView(int gridIdx) {
        mGridList.get(gridIdx).setBackgroundResource(R.drawable.grid_shape_select);
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
    private int getSlideOffset(float slide, SLIDE_TYPE slideType) {
        if (slideType == SLIDE_TYPE.LEFT_RIGHT) {
            return (int) slide / mControlTranslationX;
        } else if (slideType == SLIDE_TYPE.UP_DOWN) {
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
        mCurValue += offset - mPreValue;
        mPreValue = offset;
        if (mCurValue > max) {
            mCurValue = max;
        } else if (mCurValue < min) {
            mCurValue = min;
        }
    }

    private void setSlideGrid(int offset, int max, int min) {
        mCurGridIdx += offset - mPreGirdIdx;
        mPreGirdIdx = offset;
        if (mCurGridIdx > max) {
            mCurGridIdx = max;
        } else if (mCurGridIdx < min) {
            mCurGridIdx = min;
        }
    }

    /**
     * @return 0 for slide up and down, 1 for slide left and right
     */
    private SLIDE_TYPE getSlideType(float moveX, float moveY) {
        if (Math.abs(moveX) - Math.abs(moveY) > SLIDE_DISTANCE) {
            return SLIDE_TYPE.LEFT_RIGHT;
        } else if (Math.abs(moveY) - Math.abs(moveX) > SLIDE_DISTANCE) {
            return SLIDE_TYPE.UP_DOWN;
        } else {
            return SLIDE_TYPE.UNKNOW;
        }
    }
}
