package com.ddscanner.booking.screens.results.dailytours;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.databinding.ItemDailyTourWithDcBinding;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.DailyTourWithDc;

import java.util.ArrayList;

public class DailyToursWithDcListAdapter extends RecyclerView.Adapter<DailyToursWithDcListAdapter.DailyTourItemViewHolder> {

    private ListItemClickListener<DailyTourWithDc> dailyTourListItemClickListener;
    private ArrayList<DailyTourWithDc> dailyTours = new ArrayList<>();

    public DailyToursWithDcListAdapter(ListItemClickListener<DailyTourWithDc> dailyTourListItemClickListener) {
        this.dailyTourListItemClickListener = dailyTourListItemClickListener;
    }

    public void addDailyTours(ArrayList<DailyTourWithDc> dailyTours) {
        for (DailyTourWithDc dailyTourDetails : dailyTours) {
            this.dailyTours.add(dailyTourDetails);
            notifyItemInserted(this.dailyTours.size() -1);
        }
    }

    @Override
    public DailyTourItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDailyTourWithDcBinding itemDailyTourBinding = ItemDailyTourWithDcBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DailyTourItemViewHolder(itemDailyTourBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(DailyTourItemViewHolder holder, int position) {

        holder.binding.setViewModel(new DailyTourWithDcListItemViewModel(dailyTours.get(position)));
    }

    public void setData(ArrayList<DailyTourWithDc> dailyTours) {
        this.dailyTours = dailyTours;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dailyTours.size();
    }

    class DailyTourItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemDailyTourWithDcBinding binding;

        public DailyTourItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View v) {
            dailyTourListItemClickListener.onItemClick(dailyTours.get(getAdapterPosition()));
        }
    }

}