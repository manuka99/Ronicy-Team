package com.adeasy.advertise.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderPhoneNumbers;
import com.adeasy.advertise.model.VerifiedNumber;

import java.util.List;

public class RecycleAdapterForVerifiedNumbers extends RecyclerView.Adapter<ViewHolderPhoneNumbers> {

    private List<VerifiedNumber> verifiedNumberList;

    public RecycleAdapterForVerifiedNumbers(List<VerifiedNumber> verifiedNumberList){
        this.verifiedNumberList = verifiedNumberList;
    }

    @NonNull
    @Override
    public ViewHolderPhoneNumbers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_phone_numbers, parent, false);
        return new ViewHolderPhoneNumbers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPhoneNumbers holder, final int position) {
        holder.numberView.setText(verifiedNumberList.get(position).getNumber());
        holder.removeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               verifiedNumberList.remove(position);
               notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return verifiedNumberList.size();
    }

    public List<VerifiedNumber> getSelectedNumbers(){
        return verifiedNumberList;
    }

}
