package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.advertisement.Donations;
import com.adeasy.advertise.ui.advertisement.Myadds;
import com.adeasy.advertise.ui.athentication.LoginRegister;
import com.adeasy.advertise.ui.athentication.login;
import com.adeasy.advertise.ui.favaourite.divya_MActivity;
import com.adeasy.advertise.ui.getintouch.GetInTouchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    TextView username;
    Button myads, favaourite, membership, profile, faq, donateUs, logout;
    FrameLayout noAuthFragment;
    LinearLayout authContent;
    LoginRegister loginRegister;

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

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mAuth = FirebaseAuth.getInstance();

        noAuthFragment = view.findViewById(R.id.noAuthFragment);
        authContent = view.findViewById(R.id.authContent);

        username = view.findViewById(R.id.userName);
        myads = view.findViewById(R.id.myads);
        membership = view.findViewById(R.id.accountMember);
        favaourite = view.findViewById(R.id.favourite);
        profile = view.findViewById(R.id.myprofile);
        faq = view.findViewById(R.id.faq);
        donateUs = view.findViewById(R.id.donateUs);
        logout = view.findViewById(R.id.logout);

        loginRegister = new LoginRegister();

        //listeners
        myads.setOnClickListener(this);
        donateUs.setOnClickListener(this);
        logout.setOnClickListener(this);
        myads.setOnClickListener(this);
        faq.setOnClickListener(this);
        favaourite.setOnClickListener(this);

        if(mAuth.getCurrentUser() != null)
            showAuthContent();
        else
            showNoAuthContent();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.account);
    }

    @Override
    public void onClick(View view) {
        if(view == myads)
            startActivity(new Intent(getContext(), Myadds.class));
        else if(view == donateUs)
            startActivity(new Intent(getContext(), Donations.class));
        else if(view == logout){
            try {
                mAuth.signOut();
            }catch (Exception e){
                e.printStackTrace();
            }
            startActivity(new Intent(getContext(), MainActivity.class));
        }
        else if(view == faq)
            startActivity(new Intent(getContext(), GetInTouchActivity.class));
        else if(view == favaourite)
            startActivity(new Intent(getContext(), divya_MActivity.class));
    }

    private void showNoAuthContent(){
        authContent.setVisibility(View.GONE);
        noAuthFragment.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(noAuthFragment.getId(), loginRegister);
        fragmentTransaction.commit();
    }

    private void showAuthContent(){
        authContent.setVisibility(View.VISIBLE);
        noAuthFragment.setVisibility(View.GONE);
        username.setText(mAuth.getCurrentUser().getDisplayName());
    }

}