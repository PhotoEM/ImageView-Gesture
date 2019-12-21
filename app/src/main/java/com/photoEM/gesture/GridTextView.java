package com.photoEM.gesture;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GridTextView extends RelativeLayout {
    private static final String TAG = GestureImageView.class.getSimpleName();

    private TextView mTextView;

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
        setCurTextView(gridIdx);
        mTextView.setText(String.valueOf(value));
    }
}
