package com.myna.myna.userdata;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myna.myna.R; // सुनिश्चित करें कि यह आपके प्रोजेक्ट के R क्लास को संदर्भित करता है

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WithdrawalHistoryAdapter extends RecyclerView.Adapter<WithdrawalHistoryAdapter.WithdrawalViewHolder> {

    private List<WithdrawalRequest> withdrawalList;
    public WithdrawalHistoryAdapter() {
        this.withdrawalList = new ArrayList<>();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setWithdrawalList(List<WithdrawalRequest> withdrawalList) {
        this.withdrawalList = withdrawalList;
        notifyDataSetChanged();
    }
    @NonNull @Override
    public WithdrawalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdrawal_request, parent, false);
        return new WithdrawalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull WithdrawalViewHolder holder, int position) {
        WithdrawalRequest request = withdrawalList.get(position);
        holder.bind(request);
    }
    @Override
    public int getItemCount() {
        return withdrawalList.size();
    }
    public static class WithdrawalViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount;
        TextView tvStatus;
        TextView tvDate;

        public WithdrawalViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_item_withdrawal_amount);
            tvStatus = itemView.findViewById(R.id.tv_item_withdrawal_status);
            tvDate = itemView.findViewById(R.id.tv_item_withdrawal_date);
        }
        public void bind(WithdrawalRequest request) {
            tvAmount.setText(String.format("₹ %s", request.getAmount()));
            tvStatus.setText(request.getStatus());

            if (request.getStatus() != null) {
                switch (request.getStatus().toLowerCase()) {
                    case "pending":
                        tvStatus.setTextColor(Color.parseColor("#FFA000"));
                        break;
                    case "failed":
                        tvStatus.setTextColor(Color.RED); // लाल
                        break;
                    case "completed":
                        tvStatus.setTextColor(Color.parseColor("#4CAF50"));
                        break;
                    default:
                        tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.text_dark_primary));
                        break;
                }
            } else {
                tvStatus.setTextColor(itemView.getContext().getResources().getColor(R.color.text_dark_primary));
            }
            if (request.getTimestamp() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                String dateString = sdf.format(new Date(request.getTimestamp()));
                tvDate.setText(dateString);
            } else {
                tvDate.setText("N/A");
            }
        }
    }
}