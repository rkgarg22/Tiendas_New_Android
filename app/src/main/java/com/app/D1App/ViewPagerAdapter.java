package com.app.D1App;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context mContext;
    String[] titleName = {"CERCA DE MI", "TIENDAS", "NUEVO"};
    Integer[] tabIcon = {
            R.drawable.ic_location_on_black_24dp,
            R.drawable.ic_shop,
            R.drawable.setting
    };
    CercaDeMiFragment fragment = new CercaDeMiFragment();
    TiendasFragment tiendasFragment = new TiendasFragment();
    NuevoFragment nuevoFragment = new NuevoFragment();

    public ViewPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return fragment;
        } else if (position == 1) {
            return tiendasFragment;
        } else if (position == 2) {
            return nuevoFragment;
        } else {
            return fragment;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    View getTabView(int position) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.custom_tablayout, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        ViewGroup layout = (ViewGroup) view.findViewById(R.id.layout);

        icon.setImageResource(tabIcon[position]);
        title.setText(titleName[position]);
        return view;
    }
}

