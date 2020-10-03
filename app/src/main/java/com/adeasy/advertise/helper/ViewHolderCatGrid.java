package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ViewHolderCatGrid extends RecyclerView.ViewHolder {

    private TextView titleView;
    private ImageView imageView;

    public ViewHolderCatGrid(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.gridCatTitleSearch);
        imageView = itemView.findViewById(R.id.gridCatImageSearch);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleView(TextView titleView) {
        this.titleView = titleView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}