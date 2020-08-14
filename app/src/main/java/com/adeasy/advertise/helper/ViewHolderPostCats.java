package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;

import java.text.BreakIterator;

public class ViewHolderPostCats extends RecyclerView.ViewHolder {

    public TextView titleView;
    public ImageView imageView;

    public ViewHolderPostCats(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.categoryTitlePost);
        imageView = itemView.findViewById(R.id.categoryImagePost);
    }

}
