package com.ddscanner.booking.screens.results.dailytours;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.databinding.ItemDailyTourBinding;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.DailyTourDetails;

import java.util.ArrayList;

public class DailyToursListAdapter extends RecyclerView.Adapter<DailyToursListAdapter.DailyTourItemViewHolder> {

    private DailyTourDetails dailyTour = new DailyTourDetails();
    private ListItemClickListener<DailyTourDetails> dailyTourListItemClickListener;
    private ArrayList<DailyTourDetails> dailyTours = new ArrayList<>();

    public DailyToursListAdapter(ListItemClickListener<DailyTourDetails> dailyTourListItemClickListener) {
        this.dailyTourListItemClickListener = dailyTourListItemClickListener;
    }

    public void addDailyTours(ArrayList<DailyTourDetails> dailyTours) {
        this.dailyTours.addAll(dailyTours);
        notifyDataSetChanged();
    }

    @Override
    public DailyTourItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemDailyTourBinding itemDailyTourBinding = ItemDailyTourBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DailyTourItemViewHolder(itemDailyTourBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(DailyTourItemViewHolder holder, int position) {

        holder.binding.setViewModel(new DailyTourListItemViewModel(dailyTours.get(position)));
    }

    public void setData(ArrayList<DailyTourDetails> dailyTours) {
        this.dailyTours = dailyTours;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dailyTours.size();
    }

    class DailyTourItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ItemDailyTourBinding binding;

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
