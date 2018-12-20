package com.app.D1App.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.D1App.API.TiendasToAdapter;
import com.app.D1App.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kartikeya on 11/11/2018.
 */

public class TeindasAdapter extends RecyclerView.Adapter<TeindasAdapter.MyViewHolder> {
    Context mContext;
    List<String> result;
    TiendasToAdapter tiendasToAdapter;
    String comingFrom;

    int selectedIndex = -1;

    public TeindasAdapter(Context mContext, List<String> result, TiendasToAdapter tiendasToAdapter, String comingFrom) {
        this.mContext = mContext;
        this.result = result;
        this.tiendasToAdapter = tiendasToAdapter;
        this.comingFrom = comingFrom;
    }

    @NonNull
    @Override
    public TeindasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_teindas_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeindasAdapter.MyViewHolder holder, int position) {
        holder.displayText.setText(result.get(position));
        holder.displayText.setTag(Integer.toString(position));

        if(selectedIndex == position){
            holder.displayText.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.displayText.setBackgroundResource(R.drawable.curved_blue_background);
        }else{
            holder.displayText.setTextColor(mContext.getResources().getColor(R.color.underlineColor));
            holder.displayText.setBackgroundResource(R.drawable.curved_dotted_background);
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.displayText)
        TextView displayText;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.displayText)
        void setDisplayText(View v) {
            selectedIndex = Integer.parseInt(v.getTag().toString());
            displayText.setTextColor(mContext.getResources().getColor(R.color.white));
            displayText.setBackgroundResource(R.drawable.curved_blue_background);
            tiendasToAdapter.OnClickData(getAdapterPosition(),result.get(getAdapterPosition()),comingFrom);
            notifyDataSetChanged();
        }
    }
}
