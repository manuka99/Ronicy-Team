package com.adeasy.advertise.ui.administration.order;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.util.CommonConstants;
import com.adeasy.advertise.util.CustomDialogs;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class OrderSales extends Fragment implements View.OnClickListener, OrderCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    BarChart chart;
    PieChart pieChart, pieChart2, pieChart3;
    LineChart lineChart;

    TextInputLayout yearLayout;
    TextInputEditText yearEditText;

    ProgressBar progressBar;

    Button update;

    Context context;

    NestedScrollView scroller;

    int yearSelected = 2020;

    OrderManager orderManager;
    CustomDialogs customDialogs;

    //12 months data
    int[] months = new int[12];

    //delivered, cancelled
    int[] completedStatus = new int[2];

    //payhere, cod
    int[] otherFlagsFromDeliveredOrders = new int[2];

    //payhere, cod
    int[] otherFlagsFromCancelledOrders = new int[2];

    boolean isPieChartVisible = false;
    boolean isPieChart2Visible = false;
    boolean isPieChart3Visible = false;
    boolean isBarChartVisible = true;
    boolean isLineChartVisible = false;

    private static final String TAG = "OrderSales";

    public OrderSales() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderSales.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderSales newInstance(String param1, String param2) {
        OrderSales fragment = new OrderSales();
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
        View view = inflater.inflate(R.layout.manuka_admin_fragment_order_sales, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("Statistics - Sales Analysis");

        //init arays
        intitArrays();

        yearLayout = view.findViewById(R.id.yearLayout);
        yearEditText = view.findViewById(R.id.yearEditText);
        progressBar = view.findViewById(R.id.progressBar);
        update = view.findViewById(R.id.update);

        chart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);
        pieChart2 = view.findViewById(R.id.pieChart2);
        pieChart3 = view.findViewById(R.id.pieChart3);
        lineChart = view.findViewById(R.id.lineChart);
        scroller = view.findViewById(R.id.scrollbar);

        yearLayout.setOnClickListener(this);
        yearEditText.setOnClickListener(this);
        update.setOnClickListener(this);

        context = getActivity();
        orderManager = new OrderManager(this, getActivity());
        customDialogs = new CustomDialogs(getActivity());
        orderManager.getAllOrdersByYear(yearSelected);

        yearLayout.getEditText().setText(String.valueOf(yearSelected));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "created");

        final Rect scrollBounds = new Rect();
        scroller.getHitRect(scrollBounds);
        scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (pieChart.getLocalVisibleRect(scrollBounds)) {
                    if (!pieChart.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < pieChart.getHeight()) {
                        isPieChartVisible = false;
                        Log.i(TAG, "APPEAR PARCIALY");
                    } else if (!isPieChartVisible) {
                        Log.i(TAG, "APPEAR FULLY!!!");
                        isPieChartVisible = true;
                        pieChart.animateXY(1500, 1500);
                        pieChart.invalidate();
                    }
                } else {
                    Log.i(TAG, "No");
                }


                if (pieChart2.getLocalVisibleRect(scrollBounds)) {
                    if (!pieChart2.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < pieChart2.getHeight()) {

                        isPieChart2Visible = false;

                    } else if (!isPieChart2Visible) {

                        isPieChart2Visible = true;

                        pieChart2.animateXY(1500, 1500);
                        pieChart2.invalidate();
                    }
                }

                if (pieChart3.getLocalVisibleRect(scrollBounds)) {
                    if (!pieChart3.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < pieChart3.getHeight()) {

                        isPieChart3Visible = false;

                    } else if (!isPieChart3Visible) {

                        isPieChart3Visible = true;

                        pieChart3.animateXY(1500, 1500);
                        pieChart3.invalidate();
                    }
                }

                if (chart.getLocalVisibleRect(scrollBounds)) {
                    if (!chart.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < chart.getHeight()) {

                        isBarChartVisible = false;

                    } else if (!isBarChartVisible) {

                        isBarChartVisible = true;

                        chart.animateXY(1500, 1500);
                        chart.invalidate();
                    }
                }

                if (lineChart.getLocalVisibleRect(scrollBounds)) {
                    if (!lineChart.getLocalVisibleRect(scrollBounds)
                            || scrollBounds.height() < lineChart.getHeight()) {

                        isLineChartVisible = false;

                    } else if (!isLineChartVisible) {

                        isLineChartVisible = true;

                        lineChart.animateXY(1500, 1500);
                        lineChart.invalidate();
                    }
                }

            }


        });
    }

    private void intitArrays() {
        Arrays.fill(months, 0);
        Arrays.fill(completedStatus, 0);
        Arrays.fill(otherFlagsFromDeliveredOrders, 0);
        Arrays.fill(otherFlagsFromCancelledOrders, 0);
    }

    @Override
    public void onClick(View view) {
        if (view == yearLayout || view == yearEditText || view == yearEditText.getHint())
            startYearSelector();
        else if (view == update) {
            showUpdatingUi();
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
        if (task != null && task.isSuccessful() && task.getResult() != null) {
            List<Order> orders = task.getResult().toObjects(Order.class);
            //Log.i(TAG, advertisements.toString());
            if (orders.size() > 0)
                sortDataByAdvertisemntList(orders);
            else
                Toast.makeText(context, "No data available", Toast.LENGTH_LONG).show();
        } else if (task != null) {
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customDialogs.showPermissionDeniedStorage();
            }
        }
    }

    @Override
    public void onOrderCount(Task<QuerySnapshot> task) {

    }

    private void showUpdatingUi() {
        update.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void endUpdatingUi() {
        update.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void sortDataByAdvertisemntList(List<Order> orders) {
        intitArrays();
        for (Order order : orders) {
            if (order.getOrderStatus().equals(CommonConstants.ORDER_DELIVERED) && order.getPayment().getStatus().equals(CommonConstants.PAYMENT_PAID)) {
                completedStatus[0] += 1;

                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(order.getPlacedDate());
                    months[cal.get(Calendar.MONTH)] += 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (order.getPayment().getType().equals(CommonConstants.PAYMENT_PAYHERE))
                    otherFlagsFromDeliveredOrders[0] += 1;

                else if (order.getPayment().getType().equals(CommonConstants.PAYMENT_COD))
                    otherFlagsFromDeliveredOrders[1] += 1;

            } else if (order.getOrderStatus().equals(CommonConstants.ORDER_CANCELLED)) {
                completedStatus[1] += 1;

                if (order.getPayment().getType().equals(CommonConstants.PAYMENT_PAYHERE))
                    otherFlagsFromCancelledOrders[0] += 1;

                else if (order.getPayment().getType().equals(CommonConstants.PAYMENT_COD))
                    otherFlagsFromCancelledOrders[1] += 1;

            }

        }
        showBargraph();
        completedStatusPieDataSet();
        completedMoredataPieDataSet();
        cancelledMoredataPieDataSet();
        showLineChart();
    }

    private void showBargraph() {
        BarData data = new BarData(getBarDataSet());
        chart.setFitBars(true);
        chart.setData(data);
        chart.getDescription().setText("Orders completed based on months");
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                else
                    return String.valueOf((int) Math.floor(value));
            }
        });
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValuesBarGraph()));
        chart.animateXY(1500, 1500);
        chart.invalidate();
    }

    private BarDataSet getBarDataSet() {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i <= 11; ++i) {
            entries.add(new BarEntry(i, months[i]));
        }
        BarDataSet barDataSet1 = new BarDataSet(entries, "Orders count");
        barDataSet1.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet1.setValueTextColor(Color.BLACK);
        barDataSet1.setValueTextSize(16f);
        return barDataSet1;
    }

    public ArrayList<String> getXAxisValuesBarGraph() {
        final ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("Jan");
        xAxisLabel.add("Feb");
        xAxisLabel.add("Mar");
        xAxisLabel.add("Apr");
        xAxisLabel.add("May");
        xAxisLabel.add("Jun");
        xAxisLabel.add("Jul");
        xAxisLabel.add("Aug");
        xAxisLabel.add("Sep");
        xAxisLabel.add("Oct");
        xAxisLabel.add("Nov");
        xAxisLabel.add("Dec");
        return xAxisLabel;
    }

    private void completedStatusPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(completedStatus[0], "Completed"));
        entries.add(new PieEntry(completedStatus[1], "Cancelled"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Orders count");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                else
                    return String.valueOf((int) Math.floor(value));
            }
        });
        pieChart.setCenterText("Completed/Cancelled Orders");
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateXY(1500, 1500);
        pieChart.invalidate();
    }

    private void completedMoredataPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(otherFlagsFromDeliveredOrders[0], CommonConstants.PAYMENT_PAYHERE));
        entries.add(new PieEntry(otherFlagsFromDeliveredOrders[1], CommonConstants.PAYMENT_COD));

        PieDataSet pieDataSet = new PieDataSet(entries, "Orders count");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(14f);

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                else
                    return String.valueOf((int) Math.floor(value));
            }
        });
        pieChart2.setCenterText("Completed Orders");
        pieChart2.setData(data);
        pieChart2.setDrawEntryLabels(false);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.animateXY(1500, 1500);
        pieChart2.invalidate();
    }

    private void cancelledMoredataPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(otherFlagsFromCancelledOrders[0], CommonConstants.PAYMENT_PAYHERE));
        entries.add(new PieEntry(otherFlagsFromCancelledOrders[1], CommonConstants.PAYMENT_COD));

        PieDataSet pieDataSet = new PieDataSet(entries, "Orders count");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(14f);

        PieData data = new PieData(pieDataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                else
                    return String.valueOf((int) Math.floor(value));
            }
        });
        pieChart3.setCenterText("Cancelled Orders");
        pieChart3.setData(data);
        pieChart3.setDrawEntryLabels(false);
        pieChart3.getDescription().setEnabled(false);
        pieChart3.animateXY(1500, 1500);
        pieChart3.invalidate();
    }

    private void showLineChart() {
        LineDataSet lineDataSet = new LineDataSet(getData(), "Rate of Orders completed on selected year");
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        final String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        LineData data = new LineData(lineDataSet);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) Math.floor(value));
            }
        });
        lineChart.getDescription().setText("");
        lineChart.setData(data);
        lineChart.animateXY(1500, 1500);
        lineChart.invalidate();
    }

    private List getData() {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i <= 11; ++i) {
            entries.add(new Entry(i, months[i]));
        }
        return entries;
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

}