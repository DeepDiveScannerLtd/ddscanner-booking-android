package com.ddscanner.booking.screens.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddscanner.booking.R;
import com.ddscanner.booking.interfaces.ListItemClickListener;
import com.ddscanner.booking.models.SearchFeature;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.PlacesListViewHolder>{

    private ArrayList<SearchFeature> places = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private ListItemClickListener<SearchFeature> listItemClickListener;

    public PlacesListAdapter(GoogleApiClient googleApiClient, ListItemClickListener<SearchFeature> listItemClickListener) {
        this.googleApiClient = googleApiClient;
        this.listItemClickListener = listItemClickListener;
    }

    public void setPlaces(ArrayList<SearchFeature> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    @Override
    public PlacesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location, parent,false);
        return new PlacesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlacesListViewHolder holder, int position) {
        holder.placeName.setText(places.get(position).getPlaceName());
    }

    @Override
    public int getItemCount() {
        if (places != null) {
            return places.size();
        }
        return 0;
    }

    public class PlacesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView placeName;
        private Context context;

        public PlacesListViewHolder(View v) {
            super(v);
            context = v.getContext();
            v.setOnClickListener(this);
            placeName = v.findViewById(R.id.diveSpotName);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onItemClick(places.get(getAdapterPosition()));
//            DDScannerApplication.bus.post(new LocationChosedEvent(places.get(getAdapterPosition())));
        }
    }
}
