package com.adeasy.advertise.ui.administration.order;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adeasy.advertise.R;

import java.util.ArrayList;
import java.util.Random;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
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

    private static final String[][] DATA_TO_SHOW = {{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"},{"10e6CdJBXhQmZEeu5bcy", "ssssssssssssssssdddddddddddd", "232", "234455"}};
    private static final String[] TABLE_HEADERS = {"ProductID", "Name", "Sales Count", "Total Sales"};

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

        TableView tableView = view.findViewById(R.id.tableView);
        tableView.setColumnCount(4);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 1);
        columnModel.setColumnWeight(3, 1);
        tableView.setColumnModel(columnModel);

        tableView.setDataAdapter(new SimpleTableDataAdapter(context, DATA_TO_SHOW));

        //tableView.setEmptyDataIndicatorView(view.findViewById(R.id.empty_data_indicator));

        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context, TABLE_HEADERS));

        tableView.addDataLongClickListener(new CarLongClickListener());

        return view;
    }

    private class CarLongClickListener implements TableDataLongClickListener<String[]> {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData) {
            Toast.makeText(getContext(), clickedData[0], Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}