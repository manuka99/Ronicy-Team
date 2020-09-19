package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;

public class ViewHolderSearchAds extends RecyclerView.ViewHolder {

    public TextView title, price, date;

    public ViewHolderSearchAds(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        date = itemView.findViewById(R.id.date);
    }

}