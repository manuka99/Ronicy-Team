package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.santalu.aspectratioimageview.AspectRatioImageView;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ViewHolderAdds extends RecyclerView.ViewHolder {

    public TextView titleView, priceView, dateView, buyNow;
    public AspectRatioImageView imageView;
    public ImageView urgentWatermark, dailyBoastWatermark;

    public ViewHolderAdds(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.adMenuTitle);
        priceView = itemView.findViewById(R.id.adMenuPrice);
        dateView = itemView.findViewById(R.id.adMenuDate);
        imageView = itemView.findViewById(R.id.adMenuImage);
        urgentWatermark = itemView.findViewById(R.id.urgentWatermark);
        dailyBoastWatermark = itemView.findViewById(R.id.dailyBoastWatermark);
        buyNow = itemView.findViewById(R.id.adMenuBuyNow);
    }

}
