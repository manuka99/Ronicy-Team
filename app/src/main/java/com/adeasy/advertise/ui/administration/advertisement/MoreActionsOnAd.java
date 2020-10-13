package com.adeasy.advertise.ui.administration.advertisement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.AdvertisementSliderAdapter;
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.CategoryManager;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class MoreActionsOnAd extends AppCompatActivity implements View.OnClickListener, AdvertisementCallback, CategoryCallback {

    TextView adDetailsID, AdTitle, AdCondition, AdDescription, AdPrice, adTime, adCatName, adDetailsContacts;
    TextView isBuyNowTextView, isApprovedTextView, isAvailableTextView, hideAllNumbersTextView;
    Boolean isBuyNow = false, isApproved = false, isAvailable = false, hideAllNumbers = false;
    TextInputLayout unApprovalMessage;
    FrameLayout snackbarLayout;
    LinearLayout contactsLayout;
    SliderView sliderView;
    AdvertisementSliderAdapter advertisementSliderAdapter;
    String adID, adCID;
    Button delete, update;
    Context context;
    FirebaseAuth auth;
    AdvertisementManager advertisementManager;
    ProgressBar progressBar, progressBarDelete;
    CategoryManager categoryManager;
    com.adeasy.advertise.model.Advertisement advertisement;
    com.adeasy.advertise.model.Category category;
    Boolean isTrashDelete = false;
    private static final String TAG = "MoreActionsOnAd";
    CustomDialogs customDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_admin_activity_more_actions_on_ad);

        auth = FirebaseAuth.getInstance();
        context = this;

        try {
            adID = getIntent().getStringExtra("adID");
            adCID = getIntent().getStringExtra("adCID");
        } catch (Exception e) {
            e.printStackTrace();
            onBackPressed();
        }

        ///check if it is called from the trash files
        try {
            isTrashDelete = getIntent().getBooleanExtra("isTrashDelete", false);
        } catch (Exception e) {
            isTrashDelete = false;
            e.printStackTrace();
        }

        advertisementManager = new AdvertisementManager(this);
        categoryManager = new CategoryManager(this);
        customDialogs = new CustomDialogs(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ad details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sliderView = findViewById(R.id.imageSlider);

        adCatName = findViewById(R.id.adDetailsCategoryName);
        adTime = findViewById(R.id.adDetailsTime);
        adDetailsID = findViewById(R.id.adDetailsID);
        AdTitle = findViewById(R.id.adDetailsTitle);
        AdCondition = findViewById(R.id.adDetailsCondition);
        AdDescription = findViewById(R.id.adDetailsDescription);
        AdPrice = findViewById(R.id.adDetailsPrice);
        unApprovalMessage = findViewById(R.id.unApprovalMessage);
        contactsLayout = findViewById(R.id.contactsLayout);
        adDetailsContacts = findViewById(R.id.adDetailsContacts);

        isBuyNowTextView = findViewById(R.id.isBuyNow);
        isApprovedTextView = findViewById(R.id.isApproved);
        isAvailableTextView = findViewById(R.id.isAvailable);
        hideAllNumbersTextView = findViewById(R.id.hideAllNumbers);
        snackbarLayout = findViewById(R.id.snackbarLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBarDelete = findViewById(R.id.progressBarDelete);

        delete = findViewById(R.id.delete);
        update = findViewById(R.id.update);

        //listeners
        delete.setOnClickListener(this);
        update.setOnClickListener(this);

        isBuyNowTextView.setOnClickListener(this);
        isApprovedTextView.setOnClickListener(this);
        isAvailableTextView.setOnClickListener(this);
        hideAllNumbersTextView.setOnClickListener(this);

        contactsLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        //check if the ad is in trash
        if (!isTrashDelete)
            advertisementManager.getAddbyID(adID);
        else
            advertisementManager.getAddFromRashbyID(adID);

        categoryManager.getCategorybyID(adCID);
        advertisementSliderAdapter = new AdvertisementSliderAdapter();
    }

    @Override
    public void onBackPressed() {
        showExitAlert();
    }

    @Override
    public void onClick(View view) {
        if (view == delete)
            showDeleteDialog();
        else if (view == update)
            validateAndUpdate();
        else if (view == isApprovedTextView) {
            if (isApproved) {
                isApproved = false;
                isApprovedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
            } else {
                isApproved = true;
                isApprovedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
            }
        } else if (view == isAvailableTextView) {
            if (isAvailable) {
                isAvailable = false;
                isAvailableTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
            } else {
                isAvailable = true;
                isAvailableTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
            }
        } else if (view == isBuyNowTextView) {
            if (isBuyNow) {
                isBuyNow = false;
                isBuyNowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
            } else {
                isBuyNow = true;
                isBuyNowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
            }
        } else if (view == hideAllNumbersTextView) {
            if (hideAllNumbers) {
                hideAllNumbers = false;
                hideAllNumbersTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
            } else {
                hideAllNumbers = true;
                hideAllNumbersTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_ad_user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.user) {
            Toast.makeText(this, advertisement.getUserID(), Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void validateAndUpdate() {
        if (!isApproved && unApprovalMessage.getEditText().getText().length() < 12) {
            unApprovalMessage.setError("Please enter a valid ad rejected reason");
            showErrorSnackbar("Please enter a valid ad rejected reason");
        } else {
            showUpdatingUI();
            advertisement.setReviewed(true);
            advertisement.setBuynow(isBuyNow);
            advertisement.setApproved(isApproved);
            advertisement.setAvailability(isAvailable);
            if (hideAllNumbers) {
                advertisement.setNumbers(null);
            }
            advertisementManager.insertUpdateAdvertisement(advertisement);
        }
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onCompleteInsertAd(Task<Void> task) {
        endUpdatingUI();
        if (task != null && task.isSuccessful())
            showSuccessSnackbar("Advertisement was updated successfully");
        else if (task != null) {
            showErrorSnackbar("Error: Advertisement was not updated, please try again later");
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onCompleteDeleteAd(Task<Void> task) {
        endDeletingUI();
        if (task != null && task.isSuccessful()) {
            showSuccessSnackbar("Advertisement was deleted successfully");
            finish();
        } else if (task != null) {
            showErrorSnackbar("Error: Advertisement was not deleted");
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                try {
                    advertisement = new com.adeasy.advertise.model.Advertisement();
                    advertisement = document.toObject(com.adeasy.advertise.model.Advertisement.class);
                    adTime.setText(advertisement.getPreetyTime());
                    AdTitle.setText(advertisement.getTitle());
                    AdCondition.setText(advertisement.getCondition());
                    AdDescription.setText(advertisement.getDescription());
                    AdPrice.setText(advertisement.getPreetyCurrency());
                    adDetailsID.setText("Ad ID: " + advertisement.getId());
                    unApprovalMessage.getEditText().setText(advertisement.getUnapprovedReason());

                    if (advertisement.getNumbers() == null || advertisement.getNumbers().size() == 0) {
                        contactsLayout.setVisibility(View.GONE);
                        hideAllNumbers = true;
                    } else {
                        adDetailsContacts.setText(advertisement.getNumbers().toString());
                        contactsLayout.setVisibility(View.VISIBLE);
                        hideAllNumbers = false;
                    }

                    isApproved = advertisement.isApproved();
                    isAvailable = advertisement.isAvailability();
                    isBuyNow = advertisement.isBuynow();

                    if (isApproved)
                        isApprovedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
                    else
                        isApprovedTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);

                    if (isAvailable)
                        isAvailableTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
                    else
                        isAvailableTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);

                    if (isBuyNow)
                        isBuyNowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
                    else
                        isBuyNowTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);

                    if (hideAllNumbers)
                        hideAllNumbersTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
                    else
                        hideAllNumbersTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);

                    displayImageSlider();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "No such document");
            }
        } else {
            Log.d(TAG, "get failed with ", task.getException());
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onSuccessGetAllAdsByYear(Task<QuerySnapshot> task) {

    }

    @Override
    public void getCategoryByID(@NonNull Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                category = new com.adeasy.advertise.model.Category();
                category = document.toObject(com.adeasy.advertise.model.Category.class);

                adCatName.setText(category.getName());

            } else {
                Log.d(TAG, "No such document");
            }
        } else if (task != null) {
            Log.d(TAG, "get failed with ", task.getException());
        }
    }

    private void displayImageSlider() {
        advertisementSliderAdapter.renewItems(advertisement.getImageUrls());
        sliderView.setSliderAdapter(advertisementSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void showErrorSnackbar(String error) {
        Snackbar snackbar = Snackbar.make(snackbarLayout, error, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    private void showSuccessSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(snackbarLayout, message, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    private void showUpdatingUI() {
        progressBar.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
    }

    private void endUpdatingUI() {
        progressBar.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
    }

    private void showDeletingUI() {
        progressBarDelete.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
    }

    private void endDeletingUI() {
        progressBarDelete.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
    }

    public void showExitAlert() {
        new AlertDialog.Builder(this)

                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

                .setTitle("Are you sure you want to leave this page?")

                .setMessage("Please note that any details you have filled in will not be saved.")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

    private void showDeleteDialog() {
        String deleteAdTitle = "Are you sure you want to delete this ad?";

        if (isTrashDelete)
            deleteAdTitle = "Delete this ad permanently?";

        new AlertDialog.Builder(this)

                .setIcon(getResources().getDrawable(R.drawable.ic_baseline_info_24_red))

                .setTitle(deleteAdTitle)

                .setMessage("Please note that any changes made cannot be reverted.")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showDeletingUI();
                        if (isTrashDelete)
                            advertisementManager.deleteAddFromTrash(advertisement);
                        else
                            advertisementManager.moveAdToTrash(advertisement);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
    }

}