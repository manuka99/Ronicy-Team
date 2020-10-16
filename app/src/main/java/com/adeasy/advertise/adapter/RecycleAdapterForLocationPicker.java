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

import java.util.Arrays;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class RecycleAdapterForLocationPicker extends RecyclerView.Adapter<ViewHolderLocation> {

    private List<String> locationList;
    private Context context;
    private static final String LOCATION_SELECTED = "location_selected";
    ContactActivityInterface contactActivityInterface;

    private String district;

    public RecycleAdapterForLocationPicker(List<String> locationList, Context context, ContactActivityInterface contactActivityInterface) {
        this.locationList = locationList;
        this.context = context;
        this.contactActivityInterface = contactActivityInterface;
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

                    //sub locations are selected
                    if (district != null) {
                        //all ads in the district selected
                        if (position == 0)
                            sendLocationSelected(district);
                            //sub location was selected
                        else
                            sendLocationSelected(locationList.get(position));
                    } else {
                        if (position == 0) {
                            district = locationList.get(position);
                            contactActivityInterface.mainDistrictSelected(district);
                            upDateItemList(Arrays.asList(context.getResources().getStringArray(R.array.locations_colombo)));
                            contactActivityInterface.toggleBackToAllAds(true);
                        } else if (position == 1) {
                            district = locationList.get(position);
                            contactActivityInterface.mainDistrictSelected(district);
                            upDateItemList(Arrays.asList(context.getResources().getStringArray(R.array.locations_kandy)));
                            contactActivityInterface.toggleBackToAllAds(true);
                        } else
                            sendLocationSelected(locationList.get(position));
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public void upDateItemList(List<String> locationList) {
        this.locationList = locationList;
        notifyDataSetChanged();
    }

    public void upDateAllLocations() {
        this.locationList = Arrays.asList(context.getResources().getStringArray(R.array.locations_main));
        district = null;
        notifyDataSetChanged();
    }

    private void sendLocationSelected(String location) {
        Intent intent = new Intent();
        System.out.println("Location: selected" + location);
        intent.putExtra(LOCATION_SELECTED, location);
        ((Activity) context).setResult(Activity.RESULT_OK, intent);
        ((Activity) context).finish();
    }

    public interface ContactActivityInterface {
        void toggleBackToAllAds(Boolean b);

        void mainDistrictSelected(String district);
    }

}