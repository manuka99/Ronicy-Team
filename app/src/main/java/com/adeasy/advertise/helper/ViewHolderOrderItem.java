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
public class ViewHolderOrderItem extends RecyclerView.ViewHolder {

    public TextView name, category, price, placed_date, payment_mode, payment_status, total;
    public ImageView imageView;

    public ViewHolderOrderItem(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.orderItemName);
        category = itemView.findViewById(R.id.orderItemCat);
        price = itemView.findViewById(R.id.orderItemPrice);
        placed_date = itemView.findViewById(R.id.orderPaymentDate);
        payment_mode = itemView.findViewById(R.id.orderPaymentMode);
        payment_status = itemView.findViewById(R.id.orderPaymentStatus);
        total = itemView.findViewById(R.id.orderPaymentTotal);
        imageView = itemView.findViewById(R.id.orderItemImage);
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getCategory() {
        return category;
    }

    public void setCategory(TextView category) {
        this.category = category;
    }

    public TextView getPrice() {
        return price;
    }

    public void setPrice(TextView price) {
        this.price = price;
    }

    public TextView getPlaced_date() {
        return placed_date;
    }

    public void setPlaced_date(TextView placed_date) {
        this.placed_date = placed_date;
    }

    public TextView getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(TextView payment_mode) {
        this.payment_mode = payment_mode;
    }

    public TextView getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(TextView payment_status) {
        this.payment_status = payment_status;
    }

    public TextView getTotal() {
        return total;
    }

    public void setTotal(TextView total) {
        this.total = total;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

}