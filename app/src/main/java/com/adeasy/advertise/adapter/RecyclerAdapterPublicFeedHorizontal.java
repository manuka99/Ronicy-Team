package com.adeasy.advertise.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderAdds;
import com.adeasy.advertise.helper.ViewHolderAddsHorizontal;
import com.adeasy.advertise.helper.ViewHolderBannersHome;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.ui.home.Home;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/

public class RecyclerAdapterPublicFeedHorizontal extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> objects = new ArrayList<>();
    private Context context;

    public static final int ADVERTISEMENT = 1;

    private static final String TAG = "RecyclerAdapterPublicFe";

    public RecyclerAdapterPublicFeedHorizontal(Context context) {
        this.context = context;
        this.objects = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ADVERTISEMENT:
                View advertisementView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ad_menu_horizontal, parent, false);
                return new ViewHolderAddsHorizontal(advertisementView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        int viewType = getItemViewType(position);
        switch (viewType) {
            case ADVERTISEMENT:
                ViewHolderAddsHorizontal viewHolderAdds = (ViewHolderAddsHorizontal) holder;
                Advertisement advertisement = (Advertisement) objects.get(position);

                viewHolderAdds.titleView.setText(advertisement.getTitle());
                viewHolderAdds.dateView.setText(advertisement.getPreetyTime());
                viewHolderAdds.priceView.setText(advertisement.getPreetyCurrency());

                Picasso.get().load(advertisement.getImageUrls().get(0)).into(viewHolderAdds.imageView);

                if (advertisement.isBuynow())
                    viewHolderAdds.buyNow.setVisibility(View.VISIBLE);

                else
                    viewHolderAdds.buyNow.setVisibility(View.GONE);

                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (objects.get(position) instanceof Advertisement) {
                    Intent intent = new Intent(context, com.adeasy.advertise.ui.advertisement.Advertisement.class);
                    intent.putExtra("adID", ((Advertisement) objects.get(position)).getId());
                    intent.putExtra("adCID", ((Advertisement) objects.get(position)).getCategoryID());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (objects != null)
            return objects.size();
        else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return ADVERTISEMENT;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public void resetObjects() {
        objects.clear();
        notifyDataSetChanged();
    }

}
