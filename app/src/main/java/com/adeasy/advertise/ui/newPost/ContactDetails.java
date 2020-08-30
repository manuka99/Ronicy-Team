package com.adeasy.advertise.ui.newPost;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adeasy.advertise.R;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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

        //set listners
        addNewNumber.setOnClickListener(this);
        hideAllNumbers.setOnClickListener(this);

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

    private void loadVerifiedPhoneNumbers() {

        options = new FirestoreRecyclerOptions.Builder<VerifiedNumber>()
                .setQuery(verifiedNumbersManager.viewVerifiedNumbersByUser(firebaseAuth.getCurrentUser()), VerifiedNumber.class).build();

        final FirestoreRecyclerAdapter<VerifiedNumber, ViewHolderPhoneNumbers> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<VerifiedNumber, ViewHolderPhoneNumbers>(
                        options
                ) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolderPhoneNumbers holder, final int position, @NonNull VerifiedNumber verifiedNumber) {

                        holder.numberView.setText(verifiedNumber.getNumber());

                        holder.removeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //holder.layoytHold.setVisibility(View.GONE);

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ViewHolderPhoneNumbers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manuka_phone_numbers, parent, false);
                        return new ViewHolderPhoneNumbers(view);
                    }

                };

        firestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(firestoreRecyclerAdapter);

    }

    @Override
    public void onCompleteSearchNumberInUser(QuerySnapshot querySnapshotTask) {

    }

    @Override
    public void onCompleteRecieveAllNumbersInUser(QuerySnapshot queryDocumentSnapshots) {
        if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().isEmpty() == false) {
            List<VerifiedNumber> numbers = queryDocumentSnapshots.toObjects(VerifiedNumber.class);
            RecycleAdapterForVerifiedNumbers recycleAdapterForVerifiedNumbers = new RecycleAdapterForVerifiedNumbers(numbers);
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
        }
    }
}