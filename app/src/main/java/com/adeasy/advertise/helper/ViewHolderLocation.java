package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;

public class ViewHolderLocation extends RecyclerView.ViewHolder {

    public TextView locationView;

    public ViewHolderLocation(@NonNull View itemView) {
        super(itemView);
        locationView = itemView.findViewById(R.id.locationView);
    }

}
