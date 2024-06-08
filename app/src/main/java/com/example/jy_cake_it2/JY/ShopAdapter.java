package com.example.jy_cake_it2.JY;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    private List<Shop> shopList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Shop shop);
    }

    public ShopAdapter(List<Shop> shopList, OnItemClickListener listener) {
        this.shopList = shopList;
        this.listener = listener;
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;

        public ShopViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.shop_name);
            addressTextView = itemView.findViewById(R.id.shop_address);
        }

        public void bind(final Shop shop, final OnItemClickListener listener) {
            nameTextView.setText(shop.getShopname());
            addressTextView.setText(shop.getAddress());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(shop);
                }
            });
        }
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        holder.bind(shopList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
