package com.adeasy.advertise.ui.editAd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.adapter.RecycleAdapterForImages;
import com.adeasy.advertise.model.Advertisement;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class AdDetails extends Fragment implements View.OnClickListener, TextWatcher, RecycleAdapterForImages.RecycleAdapterInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputLayout postTitle, postCondition, postDescription, postPrice;
    Button btn_add_photo;
    Advertisement advertisement;

    List<String> imagesUriArrayList;
    List<String> deletedFirebaseUriArrayList;
    ImageView imageCamera;
    RecyclerView imageRecycler;
    FrameLayout snackbarView;
    RecycleAdapterForImages recycleAdapterForImages;
    NewPostViewModel newPostViewModel;

    private static final String TAG = "AdDetails";
    private static final int MULTIPLE_IMAGE_SELECTOR = 23234;

    public AdDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static AdDetails newInstance(String param1, String param2) {
        AdDetails fragment = new AdDetails();
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
        View view = inflater.inflate(R.layout.manuka_fragment_advertisement_details, container, false);

        advertisement = (Advertisement) getArguments().getSerializable("advertisement");

        //text
        postTitle = view.findViewById(R.id.newAdTitle);
        postCondition = view.findViewById(R.id.newAdCondition);
        postDescription = view.findViewById(R.id.newAdDescription);
        postPrice = view.findViewById(R.id.newAdPrice);

        //images
        btn_add_photo = view.findViewById(R.id.btn_add_photo);
        imageCamera = view.findViewById(R.id.imageCamera);

        imageRecycler = view.findViewById(R.id.imageRecycler);

        //snackbar
        snackbarView = view.findViewById(R.id.snackbar_text);

        btn_add_photo.setOnClickListener(this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        imageRecycler = view.findViewById(R.id.imageRecycler);
        imageRecycler.setLayoutManager(layoutManager);

        //imagesUriArrayList.addAll() = advertisement.getImageUrls();

        newPostViewModel = ViewModelProviders.of(getActivity()).get(NewPostViewModel.class);

        //text watches
        postTitle.getEditText().addTextChangedListener(this);
        postCondition.getEditText().addTextChangedListener(this);
        postDescription.getEditText().addTextChangedListener(this);
        postPrice.getEditText().addTextChangedListener(this);

        imagesUriArrayList = advertisement.getImageUrls();
        deletedFirebaseUriArrayList = new ArrayList<>();

        displayAdDetails();
        displayImages();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newPostViewModel.getAdDetailsValidation().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                validateAdDetails();
            }
        });
    }

    private void displayAdDetails(){
        postTitle.getEditText().setText(advertisement.getTitle());
        postCondition.getEditText().setText(advertisement.getCondition());
        postDescription.getEditText().setText(advertisement.getDescription());
        postPrice.getEditText().setText(String.valueOf(advertisement.getPrice()));
    }

    private void displayImages() {
        imageCamera.setVisibility(View.GONE);
        recycleAdapterForImages = new RecycleAdapterForImages(imagesUriArrayList, this, getContext());
        imageRecycler.setAdapter(recycleAdapterForImages);
        recycleAdapterForImages.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view == btn_add_photo)
            multiple_image_selector();
    }

    private void multiple_image_selector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select images for your ad"), MULTIPLE_IMAGE_SELECTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == MULTIPLE_IMAGE_SELECTOR) {

            if (data.getClipData() != null && data.getClipData().getItemCount() + imagesUriArrayList.size() < 6) {
                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                    imagesUriArrayList.add(data.getClipData().getItemAt(i).getUri().toString());
                }
                displayImages();
            } else if (data.getData() != null && imagesUriArrayList.size() < 5) {
                imagesUriArrayList.add(data.getData().toString());
                displayImages();
            } else
                showErrorSnackBar(R.string.more_than5images);

        }

    }

    @Override
    public void itemRemoved() {
        imagesUriArrayList = recycleAdapterForImages.getSelectedImages();
        deletedFirebaseUriArrayList = recycleAdapterForImages.getDeletedFirebaseUriImages();
        if (imagesUriArrayList.size() == 0)
            imageCamera.setVisibility(View.VISIBLE);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        postTitle.setError(null);
        postCondition.setError(null);
        postDescription.setError(null);
        postPrice.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void showErrorSnackBar(int error){
        Snackbar snackbar = Snackbar
                .make(snackbarView, error, 4000)
                .setAction("x", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.setTextColor(getResources().getColor(R.color.colorWhite));
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    private void validateAdDetails(){
        if(imagesUriArrayList.size() < 1)
            showErrorSnackBar(R.string.post_add_1_image);
        else if(postTitle.getEditText().getText().length() < 10)
            postTitle.setError(getString(R.string.tile_error));
        else if(postCondition.getEditText().getText().length() < 3)
            postCondition.setError(getString(R.string.condition_error));
        else if(postDescription.getEditText().getText().length() < 20)
            postDescription.setError(getString(R.string.description_error));
        else if(postPrice.getEditText().getText().length() < 2)
            postPrice.setError(getString(R.string.price_error));
        else{
            if(advertisement == null)
                advertisement = new Advertisement();
            advertisement.setTitle(postTitle.getEditText().getText().toString());
            advertisement.setCondition(postCondition.getEditText().getText().toString());
            advertisement.setDescription(postDescription.getEditText().getText().toString());
            advertisement.setPrice(Double.valueOf(postPrice.getEditText().getText().toString()));
            advertisement.setImageUrls(imagesUriArrayList);

            newPostViewModel.setAdvertisement(advertisement);
            newPostViewModel.setDeletedFirebaseUriImages(deletedFirebaseUriArrayList);
        }
    }

}