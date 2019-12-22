package com.photoEM.gesture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView GridLuminance;
    private GridView GridContrast;
    private GridView GridExposure;
    private GridView GridHighLight;
    private GridView GridShadows;
    private GridView GridWhites;
    private GridView GridBlacks;

    private GestureImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLuminance = (GridView) findViewById(R.id.GridLuminance);
        GridContrast = (GridView) findViewById(R.id.GridContrast);
        GridExposure = (GridView) findViewById(R.id.GridExposure);
        GridHighLight = (GridView) findViewById(R.id.GridHighLight);
        GridShadows = (GridView) findViewById(R.id.GridShadows);
        GridWhites = (GridView) findViewById(R.id.GridWhites);
        GridBlacks = (GridView) findViewById(R.id.GridBlacks);

        ArrayList<GridView> gridList = new ArrayList<>();
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
