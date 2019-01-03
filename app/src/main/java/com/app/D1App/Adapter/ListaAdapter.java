package com.app.D1App.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.D1App.API.ListaToAdapter;
import com.app.D1App.ApiObject.GetListObject;
import com.app.D1App.ApiResponse.GetListingResponse;
import com.app.D1App.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kartikeya on 09/11/2018.
 */

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.MyViewHolder> {

    Context mContext;
    List<GetListObject> result;
    ListaToAdapter listaToAdapterInterface;

    public ListaAdapter(Context mContext, List<GetListObject> result, ListaToAdapter listaToAdapterInterface) {
        this.mContext = mContext;
        this.result = result;
        this.listaToAdapterInterface = listaToAdapterInterface;
    }

    @NonNull
    @Override
    public ListaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_lista_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaAdapter.MyViewHolder holder, int position) {
        holder.headingText.setText(result.get(position).getTitle());
        holder.addressText.setText(result.get(position).getAddress());
        if (result.get(position).getDistance() != null)
            holder.distance.setText(String.format("%.2f", result.get(position).getDistance()) + "");
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.banner)
        ImageView banner;

        @BindView(R.id.headingText)
        TextView headingText;

        @BindView(R.id.addressText)
        TextView addressText;

        @BindView(R.id.distance)
        TextView distance;

        @BindView(R.id.parentLayout)
        RelativeLayout parentLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.parentLayout)
        void setOnParentClick() {
            listaToAdapterInterface.respond(getAdapterPosition(),
                    result.get(getAdapterPosition()).getTitle(),
                    String.valueOf(result.get(getAdapterPosition()).getDistance()),
                    result.get(getAdapterPosition()).getLatitude(),
                    result.get(getAdapterPosition()).getLongitude()
            );
        }
    }
}
