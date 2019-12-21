package com.photoEM.gesture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridTextView GridLuminance;
    private GridTextView GridContrast;
    private GridTextView GridExposure;
    private GridTextView GridHighLight;
    private GridTextView GridShadows;
    private GridTextView GridWhites;
    private GridTextView GridBlacks;

    private GestureImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLuminance = (GridTextView) findViewById(R.id.GridLuminance);
        GridContrast = (GridTextView) findViewById(R.id.GridContrast);
        GridExposure = (GridTextView) findViewById(R.id.GridExposure);
        GridHighLight = (GridTextView) findViewById(R.id.GridHighLight);
        GridShadows = (GridTextView) findViewById(R.id.GridShadows);
        GridWhites = (GridTextView) findViewById(R.id.GridWhites);
        GridBlacks = (GridTextView) findViewById(R.id.GridBlacks);

        ArrayList<GridTextView> gridList = new ArrayList<>();
        gridList.add(GridLuminance);
        gridList.add(GridContrast);
        gridList.add(GridExposure);
        gridList.add(GridHighLight);
        gridList.add(GridShadows);
        gridList.add(GridWhites);
        gridList.add(GridBlacks);

        mImageView = (GestureImageView) findViewById(R.id.MainImageView);
        mImageView.getGridList(gridList);
    }
}
