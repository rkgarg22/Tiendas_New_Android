package com.app.D1App.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.D1App.ApiResponse.GetBannerResponse;
import com.app.D1App.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kartikeya on 04/12/2018.
 */

public class NuevoAdapter extends RecyclerView.Adapter<NuevoAdapter.MyViewHolder> {
    Context mContext;
    GetBannerResponse response;

    public NuevoAdapter(Context mContext, GetBannerResponse response) {
        this.mContext = mContext;
        this.response = response;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.nuevo_custom_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.imageView.setImageURI(response.getResult().get(i).getImgUrl());
//        if (i == 0) {
//            myViewHolder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.imag1));
//        } else {
//            myViewHolder.imageView.setBackground(mContext.getResources().getDrawable(R.drawable.imag2));
//        }
    }

    @Override
    public int getItemCount() {
        return response.getResult().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        SimpleDraweeView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
