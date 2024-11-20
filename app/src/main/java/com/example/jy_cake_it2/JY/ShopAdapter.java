package com.example.jy_cake_it2.JY;

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
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Shop shop);
    }

    public ShopAdapter(List<Shop> shopList, OnItemClickListener onItemClickListener) {
        this.shopList = shopList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.bind(shop, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public void updateShops(List<Shop> newShopList) {
        this.shopList = newShopList;
        notifyDataSetChanged();
    }
    static class ShopViewHolder extends RecyclerView.ViewHolder {
        private TextView shopName;
        private TextView addressTextView;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shop_name);
            addressTextView = itemView.findViewById(R.id.shop_address);
        }

        public void bind(final Shop shop, final OnItemClickListener listener) {
            shopName.setText(shop.getShopname());
            addressTextView.setText(shop.getAddress());
            itemView.setOnClickListener(v -> listener.onItemClick(shop));
        }
    }

}
