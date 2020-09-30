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
public class ViewHolderOrderItemHome extends RecyclerView.ViewHolder {

    public TextView deliveredLayout, orderID, order_time, orderType, orderStatus, orderPaymentMode, orderItemName, orderItemCat, orderItemPrice, address, email, mobile, orderPaymentTotal, estimatedDate;
    public ImageView imageView, clear;

    public ViewHolderOrderItemHome(@NonNull View itemView) {
        super(itemView);
        deliveredLayout = itemView.findViewById(R.id.deliveredLayout);
        orderID = itemView.findViewById(R.id.orderID);
        order_time = itemView.findViewById(R.id.order_time);
        orderType = itemView.findViewById(R.id.orderType);
        orderStatus = itemView.findViewById(R.id.orderStatus);
        orderPaymentMode = itemView.findViewById(R.id.orderPaymentMode);

        orderItemName = itemView.findViewById(R.id.orderItemName);
        orderItemCat = itemView.findViewById(R.id.orderItemCat);
        orderItemPrice = itemView.findViewById(R.id.orderItemPrice);

        address = itemView.findViewById(R.id.address);
        email = itemView.findViewById(R.id.email);
        mobile = itemView.findViewById(R.id.mobile);
        orderPaymentTotal = itemView.findViewById(R.id.orderPaymentTotal);

        estimatedDate = itemView.findViewById(R.id.estimatedDate);

        imageView = itemView.findViewById(R.id.orderItemImage);
        clear = itemView.findViewById(R.id.clear);
    }

}