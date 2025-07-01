package com.myna.myna.ui.home; // Ya 'com.myna.myna.adapters' agar aap alag package mein rakhte hain

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myna.myna.Dashboard;
import com.myna.myna.R; // R file ko import karna zaroori hai
import com.myna.myna.userdata.News;
import com.myna.myna.userdata.WithdrawActivity;

import java.util.List;

public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder> {

    private List<OpportunityItem> opportunityList;

    public OpportunityAdapter(List<OpportunityItem> opportunityList) {
        this.opportunityList = opportunityList;
    }

    @NonNull
    @Override
    public OpportunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // yahan hum single opportunity item ke layout ko inflate karenge
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_opportunity_card, parent, false);
        return new OpportunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityViewHolder holder, int position) {
        OpportunityItem currentItem = opportunityList.get(position);
        holder.tvTitle.setText(currentItem.getTitle());
        holder.tvDescription.setText(currentItem.getDescription());
        holder.backgroundLayout.setBackgroundResource(currentItem.getBackgroundImageResId());

        // Agar aap chahen to click listener bhi yahan laga sakte hain har item par
        holder.itemView.setOnClickListener(v -> {
            // Jab kisi item par click ho to kya karna hai, yahan likhen
            // Jaise ki ek naya activity open karna
            v.getContext().startActivity(new Intent(v.getContext(), News.class));
            Toast.makeText(v.getContext(), "News " + currentItem.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return opportunityList.size();
    }

    // --- ViewHolder Class ---
    // Yeh inner class har single item ke UI elements ko hold karti hai
    public static class OpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        View backgroundLayout;

        public OpportunityViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_opportunity.xml mein jo TextViews hain, unki IDs ko yahan map karein
            tvTitle = itemView.findViewById(R.id.opportunity_card_title);
            tvDescription = itemView.findViewById(R.id.opportunity_card_desc);
            backgroundLayout = itemView.findViewById(R.id.opportunity_background);
        }
    }

    // --- Data Model Class ---
    // Yeh inner class har opportunity item ke data ko represent karti hai
    public static class OpportunityItem {
        private String title;
        private String description;
        private int backgroundImageResId;

        public OpportunityItem(String title, String description, int backgroundImageResId) {
            this.title = title;
            this.description = description;
            this.backgroundImageResId = backgroundImageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
        public int getBackgroundImageResId() {
            return backgroundImageResId;
        }
    }
}