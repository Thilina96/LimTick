package com.proj.limtick.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.proj.limtick.Interface.ItemClickListener;
import com.proj.limtick.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RouteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtRouteName,txtRouteDescription,txtRoutePrice;
    public ImageView imageView;
    public ItemClickListener listener;

    public RouteViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.hroute_pic);
        txtRouteName=(TextView) itemView.findViewById(R.id.hroute_name);
        txtRouteDescription=(TextView) itemView.findViewById(R.id.hroute_desc);
        txtRoutePrice=(TextView) itemView.findViewById(R.id.hroute_price);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener =listener;

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);

    }
}