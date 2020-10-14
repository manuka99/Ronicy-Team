package com.adeasy.advertise.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ui.newPost.NewAd;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoData#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoData extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "NoData";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String Post = "Please sign up to post your ad:";
    private static final String Chat = "Log in to chat with buyers and sellers on ronicy.lk";
    private static final String Account = "Log in to manage your account:";

    TextView header, allAds, postAds;

    public NoData() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoData.
     */
    // TODO: Rename and change types and number of parameters
    public static NoData newInstance(String param1, String param2) {
        NoData fragment = new NoData();
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
        View view = inflater.inflate(R.layout.manuka_fragment_no_data, container, false);

        header = view.findViewById(R.id.header);
        allAds = view.findViewById(R.id.allAds);
        postAds = view.findViewById(R.id.postAd);

        postAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewAd.class));
            }
        });

        allAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}