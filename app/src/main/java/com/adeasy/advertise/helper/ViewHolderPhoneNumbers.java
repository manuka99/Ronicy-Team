package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ViewHolderPhoneNumbers extends RecyclerView.ViewHolder {

    public RelativeLayout layoytHold;
    public TextView numberView;
    public ImageView removeView;

    public ViewHolderPhoneNumbers(@NonNull View itemView) {
        super(itemView);
        numberView = itemView.findViewById(R.id.phoneNumber);
        removeView = itemView.findViewById(R.id.phoneNumberRemove);
        layoytHold = itemView.findViewById(R.id.layoytHold);
    }

}