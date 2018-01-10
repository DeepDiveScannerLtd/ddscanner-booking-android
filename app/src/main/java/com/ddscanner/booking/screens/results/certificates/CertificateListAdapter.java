package com.ddscanner.booking.screens.results.certificates;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ddscanner.booking.databinding.ItemListCertificateBinding;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.Certificate;

import java.util.ArrayList;

public class CertificateListAdapter extends RecyclerView.Adapter<CertificateListAdapter.CertificateItemViewHolder>{

    private ListItemClickListener<Certificate> listItemClickListener;
    private ArrayList<Certificate> certificates = new ArrayList<>();

    public CertificateListAdapter(ListItemClickListener<Certificate> listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setcertificates(ArrayList<Certificate> certificates) {
        this.certificates = certificates;
        notifyDataSetChanged();
    }

    public void addCourses(ArrayList<Certificate> certificates) {
        for (Certificate Certificate : certificates) {
            this.certificates.add(Certificate);
            notifyItemInserted(this.certificates.size() - 1);
        }
    }

    @Override
    public CertificateItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemListCertificateBinding itemListCertificateBinding = ItemListCertificateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CertificateItemViewHolder(itemListCertificateBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(CertificateItemViewHolder holder, int position) {
        holder.binding.setViewModel(new CertificateListItemViewModel(certificates.get(position)));
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    class CertificateItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemListCertificateBinding binding;

        public CertificateItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            binding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onItemClick(certificates.get(getAdapterPosition()));
        }
    }
    
}
