package com.app.D1App.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.D1App.ApiObject.GetBannerObject;
import com.app.D1App.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by kartikeya on 20/11/2018.
 */

public class SwipeStackAdapter extends BaseAdapter {

    private List<GetBannerObject> result ;
    Context mContext;

    public SwipeStackAdapter(Context mContext, List<GetBannerObject> result) {
        this.result = result;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public String getItem(int position) {
        return result.get(position).getImgUrl();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.nuevo_custom_layout, parent, false);
        SimpleDraweeView imageView = (SimpleDraweeView) convertView.findViewById(R.id.imageView);
//        textViewCard.setText(mData.get(position));
        imageView.setImageURI(result.get(position).getImgUrl());
        return convertView;
    }
}