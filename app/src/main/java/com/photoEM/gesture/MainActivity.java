package com.photoEM.gesture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

        GridLuminance.setOnClickListener(this);
        GridContrast.setOnClickListener(this);
        GridExposure.setOnClickListener(this);
        GridHighLight.setOnClickListener(this);
        GridShadows.setOnClickListener(this);
        GridWhites.setOnClickListener(this);
        GridBlacks.setOnClickListener(this);

        mImageView = (GestureImageView) findViewById(R.id.MainImageView);
        mImageView.setGridList(gridList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.GridLuminance:
                mImageView.selectedGrid(0);
                mImageView.updateGridBackground(0);
                break;
            case R.id.GridContrast:
                mImageView.selectedGrid(1);
                mImageView.updateGridBackground(1);
                break;
            case R.id.GridExposure:
                mImageView.selectedGrid(2);
                mImageView.updateGridBackground(2);
                break;
            case R.id.GridHighLight:
                mImageView.selectedGrid(3);
                mImageView.updateGridBackground(3);
                break;
            case R.id.GridShadows:
                mImageView.selectedGrid(4);
                mImageView.updateGridBackground(4);
                break;
            case R.id.GridWhites:
                mImageView.selectedGrid(5);
                mImageView.updateGridBackground(5);
                break;
            case R.id.GridBlacks:
                mImageView.selectedGrid(6);
                mImageView.updateGridBackground(6);
                break;
        }
    }
}
