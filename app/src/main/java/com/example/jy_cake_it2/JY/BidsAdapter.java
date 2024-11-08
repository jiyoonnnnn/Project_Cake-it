package com.example.jy_cake_it2.JY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;

import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class BidsAdapter extends RecyclerView.Adapter<BidsAdapter.BidViewHolder> {
    private List<Bids> bidsList;
    private OnItemClickListener itemClickListener;


    public interface OnItemClickListener {
        void onItemClick(Bids bid);
    }
    public int getItemHeight(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_item, parent, false);
        itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return itemView.getMeasuredHeight();
    }
    public BidsAdapter(List<Bids> bidsList, OnItemClickListener itemClickListener) {
        this.bidsList = bidsList;
        this.itemClickListener = itemClickListener;
    }
    // 생성자
    //public BidsAdapter(List<Bids> bidsList) {
    //    this.bidsList = bidsList;
    //}

    // 뷰홀더 생성
    //@NonNull
    //@Override
    //public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_item, parent, false);
    //    return new BidViewHolder(view);
    //}
    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_item, parent, false);
        return new BidViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
        Bids bid = bidsList.get(position);
        holder.bind(bid, itemClickListener);  // 클릭 리스너를 바인딩
//        if (bid.getShop() != null) {
//            holder.bidShopName.setText("Shop Name: " + bid.getShop().getShopname());
//        } else {
//            holder.bidShopName.setText("Shop Name: N/A");
//        } // shop 객체에서 가게 이름 가져오기
//        holder.bidContent.setText("Content: " + bid.getContent());
//        holder.bidComment.setText("Comment: " + bid.getComment());
//        holder.bidId.setText("Bid ID: " + bid.getId());
//        holder.bidDate.setText("Date: " + bid.getCreateDate());
    }
//    @Override
//    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
//        Bids bid = bidsList.get(position);
//        holder.bind(bid, listener);
//        // Bids 객체의 속성을 각 TextView에 바인딩
//        if (bid.getShop() != null) {
//            holder.bidShopName.setText("Shop Name: " + bid.getShop().getShopname());
//        } else {
//            holder.bidShopName.setText("Shop Name: N/A");
//        } // shop 객체에서 가게 이름 가져오기
//        holder.bidContent.setText("Content: " + bid.getContent());
//        holder.bidComment.setText("Comment: " + bid.getComment());
//        holder.bidId.setText("Bid ID: " + bid.getId());
//        holder.bidDate.setText("Date: " + bid.getCreateDate());
//    }
    @Override
    public int getItemCount() {
        return bidsList.size();
    }

    public static class BidViewHolder extends RecyclerView.ViewHolder {
        TextView bidShopName, bidContent, bidComment, bidId, bidDate;

        public BidViewHolder(View view) {
            super(view);
            bidShopName = itemView.findViewById(R.id.bidShopname); // 가게 이름
            bidContent = itemView.findViewById(R.id.bidContent); // 입찰 내용
            bidComment = itemView.findViewById(R.id.bidComment); // 입찰 코멘트
            bidId = itemView.findViewById(R.id.bidId); // 입찰 번호
            bidDate = itemView.findViewById(R.id.bidDate); // 입찰 날짜
        }

        public void bind(final Bids bid, final OnItemClickListener itemClickListener) {
            bidShopName.setText("Shop Name: " + bid.getShop().getShopname());
            bidContent.setText("Content: " + bid.getContent());
            bidComment.setText("Comment: " + bid.getComment());
            bidId.setText("Bid ID: " + bid.getId());
            bidDate.setText("Date: " + bid.getCreateDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(bid);  // 클릭 시 bid 객체 전달
                }
            });
        }
    }
    // 데이터와 뷰를 바인딩


    public void updateBids(List<Bids> newBidsList) {
        this.bidsList.clear();
        this.bidsList.addAll(newBidsList);
        notifyDataSetChanged();
    }
    // 뷰홀더 정의

}