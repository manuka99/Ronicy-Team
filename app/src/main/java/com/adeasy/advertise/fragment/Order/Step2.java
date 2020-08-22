package com.adeasy.advertise.fragment.Order;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Order_Item;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Step2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Step2 extends Fragment implements View.OnClickListener, AdvertisementCallback, CategoryCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Step2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String advertisementID;
    private String categoryId;
    private Advertisement advertisement;
    private Category category;

    private TextView itemName, itemCat, itemPrice, itemPrice2, paymentTotal;
    private ImageView adImage;
    private Button placeOrder, creditCardBtn, CODBtn;
    boolean CODSelected = false;

    private AdvertisementManager advertisementManager;
    private CategoryManager categoryManager;
    private Order_Item item;

    public Step2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Step2.
     */
    // TODO: Rename and change types and number of parameters
    public static Step2 newInstance(String param1, String param2) {
        Step2 fragment = new Step2();
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
        advertisementID = getArguments().getString("aID");
        categoryId = getArguments().getString("cID");

        View view = inflater.inflate(R.layout.manuka_fragment_step2, container, false);

        itemName = view.findViewById(R.id.orderItemName);
        itemCat = view.findViewById(R.id.orderItemCat);
        itemPrice = view.findViewById(R.id.orderItemPrice);
        itemPrice2 = view.findViewById(R.id.orderPaymentPrice);
        paymentTotal = view.findViewById(R.id.orderPaymentTotal);
        adImage = view.findViewById(R.id.orderItemImage);
        creditCardBtn = view.findViewById(R.id.orderPaymentCredit);
        CODBtn = view.findViewById(R.id.orderPaymentCOD);

        creditCardBtn.setOnClickListener(this);
        CODBtn.setOnClickListener(this);

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);
        displayItem();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void displayItem(){
        advertisementManager.getAddbyID(advertisementID);
        categoryManager.getCategorybyID(categoryId);
    }

    @Override
    public void onClick(View view) {

        if (view == creditCardBtn) {
            creditCardBtn.setBackgroundResource(R.drawable.button_border_green);
            CODBtn.setBackgroundResource(R.drawable.order_border);
            CODSelected = false;
        }

        else if (view == CODBtn) {
            CODBtn.setBackgroundResource(R.drawable.button_border_green);
            creditCardBtn.setBackgroundResource(R.drawable.order_border);
            CODSelected = true;
        }

    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onSuccessInsertAd() {

    }

    @Override
    public void onFailureInsertAd() {

    }

    @Override
    public void onSuccessDeleteAd() {

    }

    @Override
    public void onFailureDeleteAd() {

    }

    @Override
    public void onSuccessUpdatetAd() {

    }

    @Override
    public void onFailureUpdateAd() {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                advertisement = new com.adeasy.advertise.model.Advertisement();
                advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);
                itemName.setText(advertisement.getTitle());
                itemPrice.setText("Rs. " + advertisement.getPrice());
                itemPrice2.setText("Rs. " + advertisement.getPrice());
                paymentTotal.setText("Rs. " + advertisement.getPrice());
                Picasso.get().load(advertisement.getImageUrl()).into(adImage);
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                category = new com.adeasy.advertise.model.Category();
                category = document.toObject(com.adeasy.advertise.model.Category.class);
                itemCat.setText(category.getName());
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    public Order_Item getItem(){
        item = new Order_Item();
        item.setItemName(advertisement.getTitle());
        item.setPrice(advertisement.getPrice());
        item.setImageUrl(advertisement.getImageUrl());
        item.setCategoryName(category.getName());
        return item;
    }

    public boolean isCODSelectedPaymentType(){
        return CODSelected;
    }

}