package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;

public class ViewHolderAdImage extends RecyclerView.ViewHolder {

    public ImageView adImage;
    public ImageView imageRemove;

    public ViewHolderAdImage(@NonNull View itemView) {
        super(itemView);
        adImage = itemView.findViewById(R.id.adImage);
        imageRemove = itemView.findViewById(R.id.imageRemove);
    }

}
