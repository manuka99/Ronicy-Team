package com.adeasy.advertise.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;

public class ViewHolderListAdds extends RecyclerView.ViewHolder {

    private TextView myadsTitle, myadsPrice, myaddsDate, myadsAprooved;
    private ImageView imageView;

    public ViewHolderListAdds(@NonNull View itemView) {
        super(itemView);
        myadsTitle = itemView.findViewById(R.id.myaddsTitle);
        myadsPrice = itemView.findViewById(R.id.myaddsPrice);
        myaddsDate = itemView.findViewById(R.id.myaddsDate);
        myadsAprooved = itemView.findViewById(R.id.myaddsAprovedStatus);
        imageView = itemView.findViewById(R.id.myaddsImage);
    }

    public TextView getMyadsTitle() {
        return myadsTitle;
    }

    public void setMyadsTitle(TextView myadsTitle) {
        this.myadsTitle = myadsTitle;
    }

    public TextView getMyadsPrice() {
        return myadsPrice;
    }

    public void setMyadsPrice(TextView myadsPrice) {
        this.myadsPrice = myadsPrice;
    }

    public TextView getMyadsAprooved() {
        return myadsAprooved;
    }

    public void setMyadsAprooved(TextView myadsAprooved) {
        this.myadsAprooved = myadsAprooved;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getMyaddsDate() {
        return myaddsDate;
    }

    public void setMyaddsDate(TextView myaddsDate) {
        this.myaddsDate = myaddsDate;
    }

}