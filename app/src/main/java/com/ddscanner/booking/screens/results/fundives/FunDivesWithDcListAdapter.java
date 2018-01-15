package com.ddscanner.booking.screens.results.fundives;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.databinding.ItemFunDiveWithDcBinding;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.FunDiveWithDc;

import java.util.ArrayList;

public class FunDivesWithDcListAdapter extends RecyclerView.Adapter<FunDivesWithDcListAdapter.FunDiveListItemViewHolder> {

    ArrayList<FunDiveWithDc> funDives = new ArrayList<>();
    ListItemClickListener<FunDiveWithDc> listItemClickListener;

    public FunDivesWithDcListAdapter(ListItemClickListener<FunDiveWithDc> listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void addFunDives(ArrayList<FunDiveWithDc> funDives) {
        for (FunDiveWithDc funDiveDetails : funDives) {
            this.funDives.add(funDiveDetails);
            notifyItemInserted(this.funDives.size() - 1);
        }
    }

    public void setFunDives(ArrayList<FunDiveWithDc> funDives) {
        this.funDives = funDives;
        notifyDataSetChanged();
    }

    @Override
    public FunDiveListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFunDiveWithDcBinding binding = ItemFunDiveWithDcBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FunDiveListItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(FunDiveListItemViewHolder holder, int position) {
        holder.binding.setViewModel(new FunDiveWithDcListItemViewModel(funDives.get(position)));
    }

    @Override
    public int getItemCount() {
        return funDives.size();
    }

    class FunDiveListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemFunDiveWithDcBinding binding;

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