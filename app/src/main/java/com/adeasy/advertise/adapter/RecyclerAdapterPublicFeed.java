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
import com.adeasy.advertise.helper.ViewHolderPhoneNumbers;
import com.adeasy.advertise.model.Advertisement;
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

public class RecyclerAdapterPublicFeed extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> objects = new ArrayList<>();
    private Context context;

    public static final int BANNER = 0;
    public static final int ADVERTISEMENT = 1;
    public static final int TOP_AD = 2;
    private static Float imageRatio = 0.8f;

    private static final String TAG = "RecyclerAdapterPublicFe";

    public RecyclerAdapterPublicFeed(Context context) {
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
                View advertisementView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_ad_menu, parent, false);
                return new ViewHolderAdds(advertisementView);
            case TOP_AD:
                View topAdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_top_ad_menu, parent, false);
                return new ViewHolderAdds(topAdView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case BANNER:
                ViewHolderBannersHome banners = (ViewHolderBannersHome) holder;
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

                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);
                break;

            default:
                ViewHolderAdds viewHolderAdds = (ViewHolderAdds) holder;
                Advertisement advertisement = (Advertisement) objects.get(position);

                viewHolderAdds.titleView.setText(advertisement.getTitle());
                viewHolderAdds.dateView.setText(advertisement.getPreetyTime());
                viewHolderAdds.priceView.setText(advertisement.getPreetyCurrency());

                try {
                    if (imageRatio >= 1.4f)
                        imageRatio = 0.8f;

                    if (viewType == TOP_AD)
                        imageRatio = 1.2f;

                    viewHolderAdds.imageView.setRatio(imageRatio);
                    imageRatio += 0.1f;

                    Picasso.get().load(advertisement.getImageUrls().get(0)).fit().centerCrop().into(viewHolderAdds.imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (advertisement.isBuynow())
                    viewHolderAdds.buyNow.setVisibility(View.VISIBLE);

                else
                    viewHolderAdds.buyNow.setVisibility(View.GONE);

                //check for promotions

                viewHolderAdds.urgentWatermark.setVisibility(View.GONE);
                viewHolderAdds.dailyBoastWatermark.setVisibility(View.GONE);

                if (viewType != TOP_AD)
                    viewHolderAdds.dateView.setGravity(Gravity.END);

                Date currentDate = new Date();

                for (String promoType : advertisement.getPromotions().keySet()) {
                    Log.i(TAG, promoType + " current date: " + currentDate + " promoDate: " + advertisement.getPromotions().get(promoType));

                    Log.i(TAG, " date diff: " + advertisement.getPromotions().get(promoType).compareTo(currentDate));

                    if ((promoType.equals(String.valueOf(Promotion.URGENT_AD)) || promoType.equals(String.valueOf(Promotion.BUNDLE_AD))) && advertisement.getPromotions().get(promoType).compareTo(currentDate) > 0) {
                        viewHolderAdds.urgentWatermark.setVisibility(View.VISIBLE);
                    }

                    if (promoType.equals(String.valueOf(Promotion.DAILY_BUMP_AD)) && advertisement.getPromotions().get(promoType).compareTo(currentDate) > 0) {
                        Log.i(TAG, promoType + " daily boast: ");
                        viewHolderAdds.dailyBoastWatermark.setVisibility(View.VISIBLE);
                        viewHolderAdds.dateView.setGravity(Gravity.START);
                    }
                }

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
        Log.i("reeecc", String.valueOf(getItemCount()));
        if (objects.get(position) instanceof TopAds)
            return TOP_AD;
        else if (objects.get(position) instanceof Advertisement)
            return ADVERTISEMENT;
        else
            return BANNER;
    }

    public void setObjects(List<Object> objects) {
        int start = getItemCount();
        this.objects.addAll(objects);
        Log.i(TAG, "asasasasass " + start + objects.size());
        Log.i(TAG, "asasasasass " + start );
        Log.i(TAG, "asasasasass " + objects.size());

        notifyItemRangeChanged(start, objects.size());
    }

    public void resetObjects() {
        objects.clear();
        notifyDataSetChanged();
    }

}
