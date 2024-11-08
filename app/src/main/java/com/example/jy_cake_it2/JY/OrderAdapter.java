package com.example.jy_cake_it2.JY;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jy_cake_it2.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.QuestionViewHolder> {

    private List<Detail> questionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Detail detail);
    }

    public OrderAdapter(List<Detail> questionList, OnItemClickListener listener) {
        this.questionList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Detail detail = questionList.get(position);
        Log.d("OrderAdapter", "Order Status: " + detail.getOrderStatus());
        holder.bind(detail, listener);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
    public void updateOrders(List<Detail> newOrderList) {
        this.questionList.clear();
        this.questionList.addAll(newOrderList);
        notifyItemRangeChanged(0, newOrderList.size());
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        public TextView orderName, orderCreateDate, orderStatus;

        public QuestionViewHolder(View view) {
            super(view);
//            tvSubject = view.findViewById(R.id.tvSubject);
//            tvContent = view.findViewById(R.id.tvContent);
//            tvCreateDate = view.findViewById(R.id.tvCreateDate);
            orderName = view.findViewById(R.id.order_name);
            orderCreateDate = view.findViewById(R.id.order_Create_date);
            orderStatus = view.findViewById(R.id.order_status);
        }

        public void bind(final Detail detail, final OnItemClickListener listener) {
            orderName.setText(detail.getSubject());
            orderCreateDate.setText(detail.getCreateDate());

            if (detail.getOrderStatus() == 20)
                orderStatus.setText("입찰진행중");
            else if (detail.getOrderStatus() == 10)
                orderStatus.setText("주문접수");
            else if (detail.getOrderStatus() == 11)
                orderStatus.setText("주문 수락");
            else if (detail.getOrderStatus() == 12)
                orderStatus.setText("주문 거절");
            else if (detail.getOrderStatus() == 33)
                orderStatus.setText("입금대기중");
            else if (detail.getOrderStatus() == 30)
                orderStatus.setText("케이크 제작중");
            else if (detail.getOrderStatus() == 31)
                orderStatus.setText("제작 완료(픽업 대기)");
            else if (detail.getOrderStatus() == 32)
                orderStatus.setText("픽업 완료");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(detail);
                }
            });
        }
    }
}
