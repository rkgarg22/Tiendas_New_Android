package com.app.D1App;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.app.D1App.Utils.GPSTracker;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TabSelectionActivity extends AppCompatActivity {

    GPSTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tab_selection);
        ButterKnife.bind(this);
        gpsTracker=new GPSTracker(TabSelectionActivity.this);
    }

    @OnClick(R.id.tab1)
    void tab1() {
        startActivity(new Intent(this, MainActivity.class).putExtra("tab", "1"));
    }

    @OnClick(R.id.tab2)
    void tab2() {
        startActivity(new Intent(this, MainActivity.class).putExtra("tab", "2"));
    }

    @OnClick(R.id.tab3)
    void tab3() {
        startActivity(new Intent(this, MainActivity.class).putExtra("tab", "3"));
    }

}