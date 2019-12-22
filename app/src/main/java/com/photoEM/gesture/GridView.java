package com.photoEM.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridView extends RelativeLayout {
    private static final String TAG = GestureImageView.class.getSimpleName();

    private TextView mTextView;

    private float mViewWidth;
    private float mViewHeight;
    private float mViewHalfHeight;

    private Paint mRectPaint;
    private Paint mEdgePaint;

    private int mValue;
    private int mGirdId;

    public GridView(Context context) {
        this(context, null);
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        mRectPaint = new Paint();
        mEdgePaint = new Paint();
        mValue = 0;
        mGirdId = 0;

        mEdgePaint.setStyle(Paint.Style.STROKE);
        mEdgePaint.setStrokeWidth(10);
        mEdgePaint.setColor(Color.WHITE);

        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setColor(Color.LTGRAY);
        mRectPaint.setAlpha(150);
        mRectPaint.setAntiAlias(true);
    }

    public GridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        mViewHalfHeight = mViewHeight / 2.0f;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float offset = (float) mValue * mViewHalfHeight / 100;
        if (mValue > 0) {
            // canvas.drawRect(0, mViewHalfHeight - offset, mViewWidth, mViewHalfHeight, mRectPaint);
            RectF oval = new RectF(0, mViewHalfHeight - offset, mViewWidth, mViewHalfHeight);
            canvas.drawRoundRect(oval, 15, 15, mRectPaint);
        } else if (mValue < 0) {
            // canvas.drawRect(0, mViewHalfHeight, mViewWidth, mViewHalfHeight - offset, mRectPaint);
            RectF oval = new RectF(0, mViewHalfHeight, mViewWidth, mViewHalfHeight - offset);
            canvas.drawRoundRect(oval, 15, 15, mRectPaint);
        }

        canvas.drawRoundRect(2.5f, 2.5f, mViewWidth - 2.5f, mViewHeight - 2.5f, 20, 20, mEdgePaint);
    }

    /**
     * order by :
     * gridList.add(GridLuminance);
     * gridList.add(GridContrast);
     * gridList.add(GridExposure);
     * gridList.add(GridHighLight);
     * gridList.add(GridShadows);
     * gridList.add(GridWhites);
     * gridList.add(GridBlacks);
     *
     * @param gridId
     */
    private void setCurTextView(int gridId) {
        mGirdId = gridId;
        switch (gridId) {
            case 0:
                mTextView = (TextView) findViewById(R.id.GridLuminanceValue);
                break;
            case 1:
                mTextView = (TextView) findViewById(R.id.GridContrastValue);
                break;
            case 2:
                mTextView = (TextView) findViewById(R.id.GridExposureValue);
                break;
            case 3:
                mTextView = (TextView) findViewById(R.id.GridHighLightValue);
                break;
            case 4:
                mTextView = (TextView) findViewById(R.id.GridShadowsValue);
                break;
            case 5:
                mTextView = (TextView) findViewById(R.id.GridWhitesValue);
                break;
            case 6:
                mTextView = (TextView) findViewById(R.id.GridBlacksValue);
                break;
            default:
                Log.e(TAG, "Cannot find the grid index!");
                break;
        }
    }

    public void changeValue(int value, int gridId) {
        mValue = value;
        setCurTextView(gridId);
        mTextView.setText(String.valueOf(mValue));

        invalidate();
    }

    public void changeShapeBackground() {
        setBackgroundResource(R.drawable.grid_shape_select);
    }

    public int getValue() {
        return this.mValue;
    }

}
