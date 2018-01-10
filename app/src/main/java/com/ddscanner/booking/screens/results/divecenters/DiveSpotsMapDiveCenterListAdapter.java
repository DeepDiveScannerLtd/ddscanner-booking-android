package com.ddscanner.booking.screens.results.divecenters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddscanner.booking.DDScannerBookingApplication;
import com.ddscanner.booking.R;
import com.ddscanner.booking.analytics.EventsTracker;
import com.ddscanner.booking.models.DiveCenterProfile;
import com.ddscanner.booking.screens.divecenter.profile.UserProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiveSpotsMapDiveCenterListAdapter extends RecyclerView.Adapter<DiveSpotsMapDiveCenterListAdapter.DiveCenterMapListItemViewHolder> {

    private ArrayList<DiveCenterProfile> list = new ArrayList<>();
    private Activity context;

    public void setList(ArrayList<DiveCenterProfile> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public DiveSpotsMapDiveCenterListAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public DiveCenterMapListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiveCenterMapListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_divecenter_list_mapfragment, parent, false));
    }

    @Override
    public void onBindViewHolder(DiveCenterMapListItemViewHolder holder, int position) {
        holder.dcName.setText(list.get(position).getName());
        holder.dcAddress.setText(list.get(position).getAddresses().get(0).getName());
        Picasso.with(context).load(DDScannerBookingApplication.getInstance().getString(R.string.base_photo_url, list.get(position).getPhoto(), "1")).error(R.drawable.avatar_dc_profile_def).into(holder.dcLogo);
        if (list.get(position).getLanguagesString().isEmpty()) {
            holder.languagesBlock.setVisibility(View.GONE);
        } else {
            holder.languagesBlock.setVisibility(View.VISIBLE);
            holder.dcLanguages.setText(list.get(position).getLanguagesString());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DiveCenterMapListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dcName;
        private TextView dcAddress;
        private TextView dcLanguages;
        private ImageView dcLogo;
        private LinearLayout languagesBlock;

        public DiveCenterMapListItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dcAddress = itemView.findViewById(R.id.dc_address);
            dcName = itemView.findViewById(R.id.dc_name);
            dcLanguages = itemView.findViewById(R.id.dc_languages);
            dcLogo = itemView.findViewById(R.id.dc_logo);
            languagesBlock = itemView.findViewById(R.id.languages_block);
        }

        @Override
        public void onClick(View v) {
            UserProfileActivity.show(context, String.valueOf(list.get(getAdapterPosition()).getId()), 0, list.get(getAdapterPosition()).getName(), EventsTracker.DiveCenterProfileScreenSource.DIVE_CENTERS_TAB);
        }
    }

}
