package com.adeasy.advertise.ui.administration.order;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adeasy.advertise.R;

import java.util.ArrayList;
import java.util.Random;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductStatisticsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;

    public ProductStatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductStatisticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductStatisticsFragment newInstance(String param1, String param2) {
        ProductStatisticsFragment fragment = new ProductStatisticsFragment();
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
        View view = inflater.inflate(R.layout.manuka_admin_fragment_product_statistics, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Statistics - Products Analysis");

        context = getActivity();

        DataTable dataTable = view.findViewById(R.id.data_table);

        DataTableHeader header = new DataTableHeader.Builder()
                .item("ProductID", 4)
                .item("Name", 4)
                .item("Sales", 1)
                .item("Total", 4)
                .build();

        ArrayList<DataTableRow> rows = new ArrayList<>();
        // define 200 fake rows for table
        for (int i = 0; i < 200; i++) {
            Random r = new Random();
            int random = r.nextInt(i + 1);
            int randomDiscount = r.nextInt(20);
            DataTableRow row = new DataTableRow.Builder()
                    .value("9Nxh1jYqhejYVDjestL3")
                    .value(String.valueOf(random))
                    .value(String.valueOf(random * 1000).concat("$"))
                    .value(String.valueOf(randomDiscount).concat("%"))
                    .build();
            rows.add(row);

        }

        //dataTable.setTypeface(R.font.candal);
        dataTable.setHeader(header);
        dataTable.setRows(rows);
        dataTable.inflate(context);



        return view;
    }
}