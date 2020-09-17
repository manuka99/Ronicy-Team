package com.adeasy.advertise.ui.administration.order;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.adapter.ProductAnalysisTableAdapter;
import com.adeasy.advertise.adapter.ProductAnalysisTableHeaderAdapter;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.ProductSales;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ProductStatisticsFragment extends Fragment implements OrderCallback, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Context context;
    List<ProductSales> productSalesList;
    List<Order> orderList;
    OrderManager orderManager;
    CustomDialogs customDialogs;
    FrameLayout snackBarLayout;
    ProgressDialog progressDialog;
    SortableTableView tableView;

    TextInputLayout yearLayout;
    TextInputEditText yearEditText;
    ProgressBar progressBar;
    Button update;

    int yearSelected = 2020;

    private static final String TAG = "ProductStatisticsFragme";

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

        snackBarLayout = view.findViewById(R.id.snackbarLayout);

        yearLayout = view.findViewById(R.id.yearLayout);
        yearEditText = view.findViewById(R.id.yearEditText);
        progressBar = view.findViewById(R.id.progressBar);
        update = view.findViewById(R.id.update);

        yearLayout.setOnClickListener(this);
        yearEditText.setOnClickListener(this);
        update.setOnClickListener(this);

        context = getActivity();
        productSalesList = new ArrayList<>();
        orderList = new ArrayList<>();
        orderManager = new OrderManager(this, context);
        customDialogs = new CustomDialogs(context);

//        for (int i = 0; i < 200; ++i) {
//            ProductSales productSales = new ProductSales();
//            productSales.setProductID("10e6CdJBXhQmZEeu5bcy" + i + 2);
//            productSales.setProductName("aaaaaaaaaaaaa sasdsdsd aaaaaaaa");
//            productSales.setSalesCount(i + 5);
//            productSales.setTotalSales(45565.00 + i * i * 100);
//            productSalesList.add(productSales);
//        }

        tableView = view.findViewById(R.id.tableView);

        showProgressDialog();
        orderManager.getAllOrdersByYear(2020);
        yearLayout.getEditText().setText(String.valueOf(yearSelected));

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == yearLayout || view == yearEditText || view == yearEditText.getHint())
            startYearSelector();
        else if (view == update) {
            showUpdatingUi();
            showProgressDialog();
            orderManager.getAllOrdersByYear(yearSelected);
        }
    }

    @Override
    public void onCompleteInsertOrder(Task<Void> task) {

    }

    @Override
    public void onGetOrderByID(Task<DocumentSnapshot> task) {

    }

    @Override
    public void onHideOrderByID(Task<Void> task) {

    }

    @Override
    public void onDeleteOrderByID(Task<Void> task) {

    }

    @Override
    public void getAllOrdersByYear(Task<QuerySnapshot> task) {
        endUpdatingUi();
        if (task != null && task.isSuccessful()) {
            progressDialog.setMessage("Calculating results using algorithms");
            orderList = task.getResult().toObjects(Order.class);
            loadData();
        } else if (task != null && task.getException() instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) task.getException()).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED)) {
            progressDialog.dismiss();
            customDialogs.showPermissionDeniedStorage();
        } else {
            progressDialog.dismiss();
            customDialogs.showErrorSnackbar(snackBarLayout, "Error: No data found for the selected year");
        }
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

    private class longClickListener implements TableDataLongClickListener<ProductSales> {
        @Override
        public boolean onDataLongClicked(int rowIndex, ProductSales clickedData) {
            Toast.makeText(getContext(), productSalesList.get(rowIndex).getOrder_item().getId(), Toast.LENGTH_SHORT).show();
            Log.i(TAG, productSalesList.get(rowIndex).getPriceRangersAnCount().toString());
            return true;
        }
    }

    private void loadData() {

        //initialize the list of statistic object
        productSalesList = new ArrayList<>();

        //create a map to store data related to a product id and its statistic object which is ProductSales
        Map<String, ProductSales> mapOfProductIDAndSalesCount = new HashMap<>();

        for (Order order : orderList) {
            String orderItemID = order.getItem().getId();

            int totalSalesCountOfProduct = 1;
            Double totalValueOfSalesAmout = 0.0;
            int salesCountAtParticularPrice = 1;
            ProductSales productSales = new ProductSales();

            //get the new Order item Price
            Double orderItemPrice = order.getItem().getPrice();

            if (mapOfProductIDAndSalesCount.containsKey(orderItemID)) {
                productSales = mapOfProductIDAndSalesCount.get(orderItemID);

                //increament the total salesCount
                totalSalesCountOfProduct += productSales.getSalesCount();
                productSales.setSalesCount(totalSalesCountOfProduct);

                //increment the totalValueOfSalesAmout
                totalValueOfSalesAmout = productSales.getTotalSales();
                productSales.setTotalSales(totalValueOfSalesAmout + orderItemPrice);

                //add price ranges of the product
                if (productSales.getPriceRangersAnCount().containsKey(orderItemPrice)) {
                    salesCountAtParticularPrice += productSales.getPriceRangersAnCount().get(orderItemPrice);
                    productSales.getPriceRangersAnCount().put(orderItemPrice, salesCountAtParticularPrice);
                } else
                    productSales.getPriceRangersAnCount().put(orderItemPrice, salesCountAtParticularPrice);

            } else {
                productSales.setOrder_item(order.getItem());

                //increament the total salesCount
                productSales.setSalesCount(totalSalesCountOfProduct);

                //increment the totalValueOfSalesAmout
                productSales.setTotalSales(orderItemPrice);

                //add price ranges of the product
                productSales.getPriceRangersAnCount().put(orderItemPrice, salesCountAtParticularPrice);
            }

            //update the map
            mapOfProductIDAndSalesCount.put(orderItemID, productSales);
        }

        for (String itemID : mapOfProductIDAndSalesCount.keySet()) {
            productSalesList.add(mapOfProductIDAndSalesCount.get(itemID));
        }

        if(productSalesList.size() == 0){
            tableView.setVisibility(View.GONE);
            LayoutInflater factory = LayoutInflater.from(context);
            View myView = factory.inflate(R.layout.manuka_empty_data_indicator, null);
            snackBarLayout.addView(myView);
        }else{
            snackBarLayout.removeAllViews();
            tableView.setVisibility(View.VISIBLE);
            showTable();
        }

        progressDialog.dismiss();
    }

    private void showTable() {
        tableView.setColumnCount(4);

        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(context, 4, 220);
        tableView.setColumnModel(columnModel);

        tableView.setDataAdapter(new ProductAnalysisTableAdapter(context, productSalesList));

        //tableView.setEmptyDataIndicatorView(myView);

        tableView.setHeaderAdapter(new ProductAnalysisTableHeaderAdapter(context, 4));

        tableView.addDataLongClickListener(new longClickListener());

        tableView.setColumnComparator(2, new SalesCountComparator());
        tableView.setColumnComparator(3, new SalesTotalComparator());
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Fetching matching results for the selected year");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showCancelProgressDialog();
            }
        });
        progressDialog.show();
    }

    private void showCancelProgressDialog() {
        new AlertDialog.Builder(context)

                .setMessage("Are you sure you want to cancel this process?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.dismiss();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.show();
                    }
                })

                .show();
    }

    private void startYearSelector() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(context,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                        Log.i(TAG, "Selected " + String.valueOf(selectedYear));
                        yearLayout.getEditText().setText(String.valueOf(selectedYear));
                        yearSelected = selectedYear;
                    }
                }, 2020, 1);

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(2018)
                .setActivatedYear(2020)
                .setMaxYear(2030)
                .setTitle("Select year to generate statistics")
                // .setMaxMonth(Calendar.OCTOBER)
                // .setYearRange(1890, 1890)
                // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                //.showMonthOnly()
                .showYearOnly()
                .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                    @Override
                    public void onMonthChanged(int selectedMonth) {
                    }
                })
                .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                    @Override
                    public void onYearChanged(int selectedYear) {

                    }
                })
                .build()
                .show();
    }

    private void showUpdatingUi() {
        update.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void endUpdatingUi() {
        update.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
