package com.adeasy.advertise.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.activity.advertisement.Donations;
import com.adeasy.advertise.activity.advertisement.MainActivity;
import com.adeasy.advertise.activity.advertisement.Myadds;
import com.adeasy.advertise.activity.athentication.login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    TextView username;
    Button myads, favaourite, membership, profile, faq, donateUs, logout, started;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.account);

        mAuth = FirebaseAuth.getInstance();

        username = view.findViewById(R.id.userName);

        myads = view.findViewById(R.id.myads);
        membership = view.findViewById(R.id.accountMember);
        favaourite = view.findViewById(R.id.favourite);
        profile = view.findViewById(R.id.myprofile);
        faq = view.findViewById(R.id.faq);
        donateUs = view.findViewById(R.id.donateUs);
        logout = view.findViewById(R.id.logout);
        started = view.findViewById(R.id.started);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            username.setVisibility(View.GONE);
            myads.setVisibility(View.GONE);
            membership.setVisibility(View.GONE);
            favaourite.setVisibility(View.GONE);
            profile.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);

            //LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.accountMain);
            //LayoutInflater inflater = getLayoutInflater();
            //View myLayout = inflater.inflate(R.layout.activity_login, mainLayout, false);
            //mainLayout.addView(myLayout);


        }else {
            started.setVisibility(View.GONE);
            username.setText(currentUser.getDisplayName());
        }
        myads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Myadds.class));
            }
        });

        donateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Donations.class));
            }
        });

        started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), login.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    mAuth.signOut();
                }catch (Exception e){
                    e.printStackTrace();
                }

                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


    }
}