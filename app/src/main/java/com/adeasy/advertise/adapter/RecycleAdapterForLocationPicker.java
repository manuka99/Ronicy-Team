package com.adeasy.advertise.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderLocation;

import java.util.List;

public class RecycleAdapterForLocationPicker extends RecyclerView.Adapter<ViewHolderLocation> {

    private List<String> locationList;
    private Context context;
    private static final String LOCATION_SELECTED = "location_selected";

    public RecycleAdapterForLocationPicker(List<String> locationList, Context context) {
        this.locationList = locationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderLocation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_location_view, parent, false);
        return new ViewHolderLocation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderLocation holder, final int position) {
        holder.locationView.setText(locationList.get(position));
        System.out.println("Location: " + locationList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    Intent intent = new Intent();
                    System.out.println("Location: selected" + locationList.get(position));
                    intent.putExtra(LOCATION_SELECTED, locationList.get(position));
                    ((Activity) context).setResult(Activity.RESULT_OK, intent);
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

}