package com.adeasy.advertise.adapter;

import android.content.Context;
import android.content.Intent;
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
    private Context context;

    public RecycleAdapterForSearchAds(Context context) {
        this.context = context;
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
                Intent intent = new Intent(context, com.adeasy.advertise.ui.advertisement.Advertisement.class);
                intent.putExtra("adID", advertisementList.get(position).getId());
                intent.putExtra("adCID", advertisementList.get(position).getCategoryID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

    public void setData(List<Advertisement> ads) {
        if (advertisementList != null)
            this.advertisementList = ads;
        else
            this.advertisementList = new ArrayList<>();
    }

}
