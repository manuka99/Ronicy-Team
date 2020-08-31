package com.adeasy.advertise.ui.newPost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.NewPostViewModel;
import com.adeasy.advertise.adapter.RecycleAdapterForVerifiedNumbers;
import com.adeasy.advertise.callback.VerifiedNumbersCallback;
import com.adeasy.advertise.helper.ViewHolderPhoneNumbers;
import com.adeasy.advertise.helper.ViewHolderPostCats;
import com.adeasy.advertise.manager.VerifiedNumbersManager;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.VerifiedNumber;
import com.adeasy.advertise.ui.addphone.AddNewNumber;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactDetails extends Fragment implements VerifiedNumbersCallback, View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    LinearLayout addNewNumber, hideAllNumbers;
    ImageView hideNumbersBox;
    FirestoreRecyclerOptions<VerifiedNumber> options;
    VerifiedNumbersManager verifiedNumbersManager;
    FirebaseAuth firebaseAuth;
    Boolean isNumbersHidden = false;
    Button postNewAd;
    NewPostViewModel newPostViewModel;
    List<VerifiedNumber> verifiedNumbers;
    FrameLayout snackbarView;
    private static final String TAG = "ContactDetails";

    public ContactDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactDetails newInstance(String param1, String param2) {
        ContactDetails fragment = new ContactDetails();
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
        View view = inflater.inflate(R.layout.manuka_fragment_contact_details, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.phoneNumbersRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        verifiedNumbersManager = new VerifiedNumbersManager(this);
        addNewNumber = view.findViewById(R.id.addAnewNumber);
        hideAllNumbers = view.findViewById(R.id.hideAllNumbers);
        hideNumbersBox = view.findViewById(R.id.numbersHideBox);
        postNewAd = view.findViewById(R.id.postNewAd);
        snackbarView = view.findViewById(R.id.snackbar_text);

        //set listners
        addNewNumber.setOnClickListener(this);
        hideAllNumbers.setOnClickListener(this);
        postNewAd.setOnClickListener(this);

        verifiedNumbers = new ArrayList<>();

        //view model
        newPostViewModel = ViewModelProviders.of(getActivity()).get(NewPostViewModel.class);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        //loadVerifiedPhoneNumbers();
        verifiedNumbersManager.getVerifiedNumbersOfUser(firebaseAuth.getCurrentUser());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        verifiedNumbersManager.destroy();
    }

    @Override
    public void onCompleteSearchNumberInUser(QuerySnapshot querySnapshotTask) {

    }

    @Override
    public void onCompleteRecieveAllNumbersInUser(QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().isEmpty() == false) {
            verifiedNumbers = queryDocumentSnapshots.toObjects(VerifiedNumber.class);
            RecycleAdapterForVerifiedNumbers recycleAdapterForVerifiedNumbers = new RecycleAdapterForVerifiedNumbers(verifiedNumbers);
            recyclerView.setAdapter(recycleAdapterForVerifiedNumbers);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == addNewNumber)
            startActivity(new Intent(getContext(), AddNewNumber.class));

        else if(view == hideAllNumbers){
            if(isNumbersHidden){
                isNumbersHidden = false;
                hideNumbersBox.setBackgroundResource(R.drawable.ic_checkbox_normal);
            }else{
                isNumbersHidden = true;
                hideNumbersBox.setBackgroundResource(R.drawable.ic_checkbox_checked);
            }
        }else if(view == postNewAd)
            validateContactDetails();
    }

    private void validateContactDetails(){
        if(isNumbersHidden)
            newPostViewModel.setContactDetailsValidation(null);
        else if(verifiedNumbers.size() > 0)
            newPostViewModel.setContactDetailsValidation(verifiedNumbers);
        else
            showErrorSnackBar(R.string.contact_details_error);
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

}