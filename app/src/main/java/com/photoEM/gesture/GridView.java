package com.photoEM.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridView extends RelativeLayout {
    private static final String TAG = GestureImageView.class.getSimpleName();

    private TextView mTextView;

    private float mViewWidth;
    private float mViewHeight;
    private float mViewHalfHeight;

    private Paint mPaint;
    private Paint mEdgePaint;

    private int mValue;

    public GridView(Context context) {
        this(context, null);
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        mPaint = new Paint();
        mEdgePaint = new Paint();
        mValue = 0;

        mEdgePaint.setStyle(Paint.Style.STROKE);
        mEdgePaint.setStrokeWidth(10);
        mEdgePaint.setColor(Color.WHITE);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);
        mPaint.setAlpha(150);
        mPaint.setAntiAlias(true);
    }

    public GridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
            // canvas.drawRect(0, mViewHalfHeight - offset, mViewWidth, mViewHalfHeight, mPaint);
            RectF oval = new RectF(0, mViewHalfHeight - offset, mViewWidth, mViewHalfHeight);
            canvas.drawRoundRect(oval, 15, 15, mPaint);
        } else if (mValue < 0) {
            // canvas.drawRect(0, mViewHalfHeight, mViewWidth, mViewHalfHeight - offset, mPaint);
            RectF oval = new RectF(0, mViewHalfHeight, mViewWidth, mViewHalfHeight - offset);
            canvas.drawRoundRect(oval, 15, 15, mPaint);
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
     * @param gridIdx
     */
    private void setCurTextView(int gridIdx) {
        switch (gridIdx) {
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

    public void changeValue(int value, int gridIdx) {
        mValue = value;
        setCurTextView(gridIdx);
        mTextView.setText(String.valueOf(mValue));

        invalidate();
    }

    public void changeShapeBackground() {
        setBackgroundResource(R.drawable.grid_shape_select);
    }

}
