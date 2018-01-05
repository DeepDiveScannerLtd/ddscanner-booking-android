package com.ddscanner.booking.screens.results.fundives;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.databinding.ItemFunDiveBinding;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.FunDiveDetails;

import java.util.ArrayList;

public class FunDivesListAdapter extends RecyclerView.Adapter<FunDivesListAdapter.FunDiveListItemViewHolder> {

    ArrayList<FunDiveDetails> funDives = new ArrayList<>();
    ListItemClickListener<FunDiveDetails> listItemClickListener;

    public FunDivesListAdapter(ListItemClickListener<FunDiveDetails> listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setFunDives(ArrayList<FunDiveDetails> funDives) {
        this.funDives = funDives;
        notifyDataSetChanged();
    }

    @Override
    public FunDiveListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFunDiveBinding binding = ItemFunDiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FunDiveListItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(FunDiveListItemViewHolder holder, int position) {
        holder.binding.setViewModel(new FunDiveListItemViewModel(funDives.get(position)));
    }

    @Override
    public int getItemCount() {
        return funDives.size();
    }

    class FunDiveListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemFunDiveBinding binding;

        public FunDiveListItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onItemClick(funDives.get(getAdapterPosition()));
        }
    }

}
