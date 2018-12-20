package com.app.D1App;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.D1App.Utils.GPSTracker;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView headerText;
    ViewPagerAdapter viewPagerAdapter;
    GPSTracker gpsTracker;
    String comingFrom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        headerText = findViewById(R.id.headerText);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        setupTabLayout(viewPager, viewPagerAdapter);
        gpsTracker = new GPSTracker(MainActivity.this);
        if (getIntent() != null) {
            comingFrom = getIntent().getStringExtra("tab");
        }
        switch (comingFrom) {
            case "1":
                viewPager.setCurrentItem(0);
                break;
            case "2":
                viewPager.setCurrentItem(1);
                break;
            case "3":
                viewPager.setCurrentItem(2);

                break;
        }
    }

    private void setupTabLayout(ViewPager viewPager, final ViewPagerAdapter viewPagerAdapter) {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(viewPagerAdapter.getTabView(i));
        }

        View view = tabLayout.getTabAt(0).getCustomView();
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setTextColor(getResources().getColor(R.color.tabSelectedIconColor));
        imageView.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.tabSelectedIconColor));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setTextColor(getResources().getColor(R.color.tabSelectedIconColor));
                headerText.setText(title.getText().toString().trim());
                imageView.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.tabSelectedIconColor));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setTextColor(getResources().getColor(R.color.tabUnselectedIconColor));
                imageView.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.tabUnselectedIconColor));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==1) {
                    TiendasFragment fragment = (TiendasFragment) viewPagerAdapter.getItem(tab.getPosition());
                    fragment.setBackBtn();
                }
            }
        });
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabSelectedIconColor);
//                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.tabUnselectedIconColor);
//                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }
}

