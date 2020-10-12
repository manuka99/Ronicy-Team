package com.adeasy.advertise.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.adeasy.advertise.R;
import com.adeasy.advertise.helper.ViewHolderAdds;
import com.adeasy.advertise.helper.ViewHolderBannersHome;
import com.adeasy.advertise.helper.ViewHolderListAdds;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.BundleAd;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.model.TopAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/

public class RecyclerAdapterSimillarAds extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> objects = new ArrayList<>();
    private Context context;

    public static final int BANNER = 0;
    public static final int ADVERTISEMENT = 1;
    public static final int BUNDLE_AD = 2;
    private static Float imageRatio = 0.8f;

    private static final String TAG = "RecyclerAdapterSimillarAds";

    public RecyclerAdapterSimillarAds(Context context) {
        this.context = context;
        this.objects = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case BANNER:
                View bannerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_banner, parent, false);
                return new ViewHolderBannersHome(bannerView);
            case ADVERTISEMENT:
                View advertisementView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ads_row, parent, false);
                return new ViewHolderListAdds(advertisementView);
            case BUNDLE_AD:
                View bundleAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_bundle_ads_row, parent, false);
                return new ViewHolderListAdds(bundleAdView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case BANNER:
                ViewHolderBannersHome banners = (ViewHolderBannersHome) viewHolder;
                AdView ad = banners.adView;
                ad.loadAd(new AdRequest.Builder().build());

                ViewGroup viewGroup = (ViewGroup) banners.itemView;

                if (viewGroup.getChildCount() > 0) {
                    viewGroup.removeAllViews();
                }

                if (viewGroup.getParent() != null) {
                    ((ViewGroup) ad.getParent()).removeView(ad);
                }

                viewGroup.addView(ad);

//                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
//                layoutParams.setFullSpan(true);
                break;

            default:
                ViewHolderListAdds holder = (ViewHolderListAdds) viewHolder;
                Advertisement advertisement = (Advertisement) objects.get(position);
                holder.getMyadsTitle().setText(advertisement.getTitle());
                holder.getMyadsPrice().setText(advertisement.getPreetyCurrency());
                holder.getMyaddsDate().setText(advertisement.getPreetyTime());
                Picasso.get().load(advertisement.getImageUrls().get(0)).fit().into(holder.getImageView());
                holder.getMyadsAprooved().setVisibility(View.GONE);

                break;

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        Log.i("reeecc", String.valueOf(getItemCount()));
        if (objects.get(position) instanceof BundleAd)
            return BUNDLE_AD;
        else if (objects.get(position) instanceof Advertisement)
            return ADVERTISEMENT;
        else
            return BANNER;
    }

    public void setObjects(List<Object> objects) {
        int start = getItemCount();
        this.objects.addAll(start, objects);
        notifyItemRangeInserted(start, start + objects.size());
    }

    public void resetObjects() {
        objects.clear();
        notifyDataSetChanged();
    }

}
