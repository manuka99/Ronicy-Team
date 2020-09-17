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
import com.adeasy.advertise.adapter.ProductAnalysisTableAdapter;
import com.adeasy.advertise.adapter.ProductAnalysisTableHeaderAdapter;
import com.adeasy.advertise.model.ProductSales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
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

    Context context;
    List<ProductSales> productSalesList;

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
        productSalesList = new ArrayList<>();

        for (int i = 0; i < 200; ++i) {
            ProductSales productSales = new ProductSales();
            productSales.setProductID("10e6CdJBXhQmZEeu5bcy");
            productSales.setProductName("aaaaaaaaaaaaa sasdsdsd aaaaaaaa");
            productSales.setSalesCount(i + 5);
            productSales.setTotalSales(45565.00 + i * i * 100);
            productSalesList.add(productSales);
        }

        SortableTableView tableView = view.findViewById(R.id.tableView);
        tableView.setColumnCount(4);

        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(context, 4, 160);
        columnModel.setColumnWidth(0, 200);
        columnModel.setColumnWidth(1, 200);
        columnModel.setColumnWidth(2, 120);
        tableView.setColumnModel(columnModel);

        tableView.setDataAdapter(new ProductAnalysisTableAdapter(context, productSalesList));

        //tableView.setEmptyDataIndicatorView(view.findViewById(R.id.empty_data_indicator));

        tableView.setHeaderAdapter(new ProductAnalysisTableHeaderAdapter(context, 4));

        tableView.addDataLongClickListener(new CarLongClickListener());

        tableView.setColumnComparator(2, new SalesCountComparator());
        tableView.setColumnComparator(3, new SalesTotalComparator());

        return view;
    }

    private class SalesCountComparator implements Comparator<ProductSales> {
        @Override
        public int compare(ProductSales productSales1, ProductSales productSales2) {
            return productSales1.getSalesCount().compareTo(productSales2.getSalesCount());
        }
    }

    private class SalesTotalComparator implements Comparator<ProductSales> {
        @Override
        public int compare(ProductSales productSales1, ProductSales productSales2) {
            return productSales1.getTotalSales().compareTo(productSales2.getTotalSales());
        }
    }

    private class CarLongClickListener implements TableDataLongClickListener<String[]> {
        @Override
        public boolean onDataLongClicked(int rowIndex, String[] clickedData) {
            Toast.makeText(getContext(), clickedData[0], Toast.LENGTH_SHORT).show();
            return true;
        }
    }

}
