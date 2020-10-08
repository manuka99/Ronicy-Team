package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.google.android.gms.ads.AdView;
import com.santalu.aspectratioimageview.AspectRatioImageView;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ViewHolderBannersHome extends RecyclerView.ViewHolder {

    public AdView adView;

    public ViewHolderBannersHome(@NonNull View itemView) {
        super(itemView);
        adView = itemView.findViewById(R.id.adView);
    }

}
