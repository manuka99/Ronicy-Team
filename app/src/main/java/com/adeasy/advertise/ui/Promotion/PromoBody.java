package com.adeasy.advertise.ui.Promotion;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.PromotionsViewModel;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Promotion;
import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.adeasy.advertise.util.Promotions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PromoBody extends Fragment implements AdvertisementCallback, CategoryCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //invalid promos
    TextView invalid_promos;

    //ad layout
    ImageView adImage;
    TextView adTitle, adCategory, adPrice;
    Advertisement advertisement;

    //promotions layouts
    LinearLayout bundle_layout, daily_bump_layout, top_ad_layout, urgent_layout, spotlight_layout;

    //total
    TextView totalView;
    double totalSum;

    AdvertisementManager advertisementManager;
    CategoryManager categoryManager;

    Map<Integer, Integer> promos;
    Map<String, Integer> promosString;

    //view model
    PromotionsViewModel promotionsViewModel;

    String advertisementID;

    private static final String PROMOS_ADDED = "promos_added";
    private static final String ADVERTISEMENT_ID = "adID";

    public PromoBody() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromoBody.
     */
    // TODO: Rename and change types and number of parameters
    public static PromoBody newInstance(String param1, String param2) {
        PromoBody fragment = new PromoBody();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View paymentBody = inflater.inflate(R.layout.manuka_fragment_payment_body, container, false);

        invalid_promos = paymentBody.findViewById(R.id.invalid_promos);

        //ad layout
        adTitle = paymentBody.findViewById(R.id.title);
        adImage = paymentBody.findViewById(R.id.adImage);
        adCategory = paymentBody.findViewById(R.id.category);
        adPrice = paymentBody.findViewById(R.id.adPrice);

        //promo layouts
        bundle_layout = paymentBody.findViewById(R.id.bundle_layout);
        daily_bump_layout = paymentBody.findViewById(R.id.daily_bump_layout);
        top_ad_layout = paymentBody.findViewById(R.id.top_ad_layout);
        urgent_layout = paymentBody.findViewById(R.id.urgent_layout);
        spotlight_layout = paymentBody.findViewById(R.id.spotlight_layout);

        //total
        totalView = paymentBody.findViewById(R.id.total);

        //hide
        invalid_promos.setVisibility(View.GONE);

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);

        //checkIntent values
        try{
            promos = (Map<Integer, Integer>) getArguments().getSerializable(PROMOS_ADDED);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            advertisementID = getArguments().getString(ADVERTISEMENT_ID);
        }catch (Exception e){
            e.printStackTrace();
        }

        promotionsViewModel = ViewModelProviders.of(getActivity()).get(PromotionsViewModel.class);

        advertisementManager.getAddbyID(advertisementID);

        return paymentBody;
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onCompleteInsertAd(Task<Void> task) {

    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful()) {
            advertisement = task.getResult().toObject(Advertisement.class);

            //get the category details
            categoryManager.getCategorybyID(advertisement.getCategoryID());

            //add layouts and views
            adTitle.setText(advertisement.getTitle());
            Picasso.get().load(advertisement.getImageUrls().get(0)).into(adImage);
            adPrice.setText(advertisement.getPreetyCurrency());

            //complete the promotions
            showPromotions();

            promotionsViewModel.setTotal(totalSum);
        }
    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful()) {
            Category category = task.getResult().toObject(Category.class);
            adCategory.setText(category.getName());
        }
    }

    private void showPromotions() {
        hideAllPromosLayouts();
        if (promos != null) {
            totalSum = 0;
            for (Integer promoType : promos.keySet()) {

                int daysSelected = promos.get(promoType);

                if (promoType == Promotion.BUNDLE_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.BUNDLE_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.BUNDLE_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.BUNDLE_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = bundle_layout.findViewById(R.id.description);
                        TextView priceView = bundle_layout.findViewById(R.id.price);

                        description.setText(Promotions.BUNDLE_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        bundle_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.DAILY_BUMP_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.DAILY_BUMP_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = daily_bump_layout.findViewById(R.id.description);
                        TextView priceView = daily_bump_layout.findViewById(R.id.price);

                        description.setText(Promotions.DAILY_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        daily_bump_layout.setVisibility(View.VISIBLE);
                    }
                }


                if (promoType == Promotion.TOP_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.TOP_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.TOP_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.TOP_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = top_ad_layout.findViewById(R.id.description);
                        TextView priceView = top_ad_layout.findViewById(R.id.price);

                        description.setText(Promotions.TOP_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        top_ad_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.URGENT_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.URGENT_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.URGENT_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.URGENT_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = urgent_layout.findViewById(R.id.description);
                        TextView priceView = urgent_layout.findViewById(R.id.price);

                        description.setText(Promotions.URGENT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        urgent_layout.setVisibility(View.VISIBLE);
                    }
                }

                if (promoType == Promotion.SPOTLIGHT_AD) {
                    if (daysSelected == 3) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_3_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 7) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_7_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    } else if (daysSelected == 15) {
                        double price = Promotions.SPOTLIGHT_AD_PRICE_15_DAYS;
                        totalSum += price;

                        TextView description = spotlight_layout.findViewById(R.id.description);
                        TextView priceView = spotlight_layout.findViewById(R.id.price);

                        description.setText(Promotions.SPOTLIGHT_AD_DESCRIPTION + daysSelected + Promotions.DAYS);
                        priceView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(price)));

                        spotlight_layout.setVisibility(View.VISIBLE);
                    }
                }

            }

            totalView.setText(new DoubleToCurrencyFormat().setStringValue(String.valueOf(totalSum)));
            if (totalSum > 0) {
                invalid_promos.setVisibility(View.GONE);
            } else {
                invalid_promos.setVisibility(View.VISIBLE);
            }
        }
    }

    private void hideAllPromosLayouts() {
        bundle_layout.setVisibility(View.GONE);
        daily_bump_layout.setVisibility(View.GONE);
        top_ad_layout.setVisibility(View.GONE);
        urgent_layout.setVisibility(View.GONE);
        spotlight_layout.setVisibility(View.GONE);
    }

}