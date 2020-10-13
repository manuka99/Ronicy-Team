package com.adeasy.advertise.ui.Promotion;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.PromotionsViewModel;
import com.adeasy.advertise.model.Promotion;

import java.util.HashMap;
import java.util.Map;

import worker8.com.github.radiogroupplus.RadioGroupPlus;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpotLightAd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotLightAd extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView checkbox;
    RadioGroupPlus radioGroup;
    RadioButton days3Radio, days7Radio, days15Radio;

    LinearLayout days3View, days7View, days15View;

    boolean isChecked = false;

    //viewModel
    PromotionsViewModel promotionsViewModel;

    LinearLayout layoutMain;

    public SpotLightAd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SpotLightAd.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotLightAd newInstance(String param1, String param2) {
        SpotLightAd fragment = new SpotLightAd();
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
        View view = inflater.inflate(R.layout.manuka_fragment_spot_light_ad, container, false);

        checkbox = view.findViewById(R.id.checkbox);
        radioGroup = view.findViewById(R.id.radioGroup);

        //radio buttons
        days3Radio = view.findViewById(R.id.days3Radio);
        days3View = view.findViewById(R.id.days3View);
        days7Radio = view.findViewById(R.id.days7Radio);
        days7View = view.findViewById(R.id.days7View);
        days15Radio = view.findViewById(R.id.days15Radio);
        days15View = view.findViewById(R.id.days15View);

        layoutMain = view.findViewById(R.id.layoutMain);
        layoutMain.setOnClickListener(this);

        promotionsViewModel = ViewModelProviders.of(getActivity()).get(PromotionsViewModel.class);

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view == layoutMain)
            toggleCheckBox();

    }

    private void toggleCheckBox() {
        if (isChecked) {
            isChecked = false;
            checkbox.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_checkbox_normal));
            radioGroup.clearCheck();
            radioGroup.setVisibility(View.GONE);
            layoutMain.setBackgroundResource(R.drawable.promotion_bundle_border);
        } else {
            isChecked = true;
            checkbox.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_checkbox_checked));
            radioGroup.setVisibility(View.VISIBLE);
            days7Radio.setChecked(true);
            promotionsViewModel.setOnFragmentSelected(new SpotLightAd());
            layoutMain.setBackgroundResource(R.drawable.promotion_bundle_border_main_active);
        }
        upDateActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        radioGroup.setOnCheckedChangeListener(new RadioGroupPlus.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroupPlus radioGroupPlus, int i) {
                days3View.setBackgroundResource(R.drawable.promotion_bundle_border);
                days7View.setBackgroundResource(R.drawable.promotion_bundle_border);
                days15View.setBackgroundResource(R.drawable.promotion_bundle_border);

                if (i == days3Radio.getId())
                    days3View.setBackgroundResource(R.drawable.promotion_bundle_border_active);
                else if (i == days7Radio.getId())
                    days7View.setBackgroundResource(R.drawable.promotion_bundle_border_active);
                else if (i == days15Radio.getId())
                    days15View.setBackgroundResource(R.drawable.promotion_bundle_border_active);

                upDateActivity();
            }
        });

        promotionsViewModel.getOnFragmentSelected().observe(this, new Observer<Fragment>() {
            @Override
            public void onChanged(Fragment fragment) {
                if (fragment instanceof SpotLightAd == false) {
                    radioGroup.setVisibility(View.GONE);
                }
            }
        });
    }

    private void upDateActivity() {
        int daysSelected = 0;

        if (isChecked) {
            int id = radioGroup.getCheckedRadioButtonId();

            if (id == days3Radio.getId())
                daysSelected = 3;
            else if (id == days7Radio.getId())
                daysSelected = 7;
            else if (id == days15Radio.getId())
                daysSelected = 15;

            //updateViewModel
            Map<Integer, Integer> promos = new HashMap<>();
            promos.put(Promotion.SPOTLIGHT_AD, daysSelected);
            promotionsViewModel.setSelectedPromo(promos);
        } else {
            //updateViewModel
            Map<Integer, Integer> promos = new HashMap<>();
            promos.put(Promotion.SPOTLIGHT_AD, daysSelected);
            promotionsViewModel.setSelectedPromo(promos);
        }

    }

}