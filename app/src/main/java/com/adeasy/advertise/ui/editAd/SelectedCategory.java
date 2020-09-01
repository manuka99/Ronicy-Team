package com.adeasy.advertise.ui.editAd;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.Category;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectedCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectedCategory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView name;
    ImageView image;
    Category category;
    LinearLayout locationSelectedLayout;

    public SelectedCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectedCategory.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectedCategory newInstance(String param1, String param2) {
        SelectedCategory fragment = new SelectedCategory();
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
        View view = inflater.inflate(R.layout.manuka_fragment_category_selected, container, false);

        category = (Category) getArguments().getSerializable("category");

        locationSelectedLayout = view.findViewById(R.id.locationSelectedLayout);
        locationSelectedLayout.setBackgroundResource(R.color.colorBlueLite);

        name = view.findViewById(R.id.selectedCategoryTitle);
        image = view.findViewById(R.id.selectedCategoryImage);

        name.setText(category.getName());
        Picasso.get().load(category.getImageUrl()).into(image);

        return view;
    }

}