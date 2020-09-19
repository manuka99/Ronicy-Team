package com.adeasy.advertise.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderPhoneNumbers;
import com.adeasy.advertise.helper.ViewHolderSearchAds;
import com.adeasy.advertise.model.Advertisement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class RecycleAdapterForSearchAds extends RecyclerView.Adapter<ViewHolderSearchAds> {

    private List<Advertisement> advertisementList;

    public RecycleAdapterForSearchAds(List<Advertisement> advertisementList) {
        if (advertisementList != null)
            this.advertisementList = advertisementList;
        else
            this.advertisementList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderSearchAds onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_search, parent, false);
        return new ViewHolderSearchAds(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderSearchAds holder, final int position) {
        holder.title.setText(advertisementList.get(position).getTitle());
        holder.price.setText(advertisementList.get(position).getPreetyCurrency());
        holder.date.setText(advertisementList.get(position).getPreetyTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advertisementList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

}
