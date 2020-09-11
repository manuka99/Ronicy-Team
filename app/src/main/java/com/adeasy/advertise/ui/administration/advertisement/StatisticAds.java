package com.adeasy.advertise.ui.administration.advertisement;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.manager.AdvertisementManager;
import com.adeasy.advertise.model.Advertisement;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticAds#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticAds extends Fragment implements View.OnClickListener, AdvertisementCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "StatisticAds";

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

    AdvertisementManager advertisementManager;

    //12 months data
    int[] months = new int[12];

    //approved, rejected
    int[] approvalStatus = new int[2];

    //live , not available, buynow
    int[] otherFlagsFromApprovedAds = new int[3];

    //live , not available, buynow
    int[] otherFlagsFromUnApprovedAds = new int[3];

    boolean isPieChartVisible = false;
    boolean isPieChart2Visible = false;
    boolean isPieChart3Visible = false;
    boolean isBarChartVisible = true;
    boolean isLineChartVisible = false;

    public StatisticAds() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticAds.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticAds newInstance(String param1, String param2) {
        StatisticAds fragment = new StatisticAds();
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
        View view = inflater.inflate(R.layout.manuka_admin_fragment_statistic_ads, container, false);

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
        advertisementManager = new AdvertisementManager(this);
        advertisementManager.getAllAdsByYear(yearSelected);

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

    private void showLineChart() {
        LineDataSet lineDataSet = new LineDataSet(getData(), "Rate of Ads posted on selected year");
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

    private void approvalStatusPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(approvalStatus[0], "Approved"));
        entries.add(new PieEntry(approvalStatus[1], "Rejected"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Ads count");
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
        pieChart.setCenterText("Approved/Rejected Ads");
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateXY(1500, 1500);
        pieChart.invalidate();
    }

    private void approvalMoredataPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(otherFlagsFromApprovedAds[0], "Live"));
        entries.add(new PieEntry(otherFlagsFromApprovedAds[1], "Un-available"));
        entries.add(new PieEntry(otherFlagsFromApprovedAds[2], "Buynow"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Ads count");
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
        pieChart2.setCenterText("Approved Ads");
        pieChart2.setData(data);
        pieChart2.setDrawEntryLabels(false);
        pieChart2.getDescription().setEnabled(false);
        pieChart2.animateXY(1500, 1500);
        pieChart2.invalidate();
    }

    private void rejectedMoredataPieDataSet() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(otherFlagsFromUnApprovedAds[0], "Live"));
        entries.add(new PieEntry(otherFlagsFromUnApprovedAds[1], "Un-available"));
        entries.add(new PieEntry(otherFlagsFromUnApprovedAds[2], "Buynow"));

        PieDataSet pieDataSet = new PieDataSet(entries, "Ads count");
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
        pieChart3.setCenterText("Rejected Ads");
        pieChart3.setData(data);
        pieChart3.setDrawEntryLabels(false);
        pieChart3.getDescription().setEnabled(false);
        pieChart3.animateXY(1500, 1500);
        pieChart3.invalidate();
        //Legend legend = pieChart3.getLegend();
        //legend.setEnabled(true);
        //legend.setExtra(ColorTemplate.MATERIAL_COLORS, new String[]{"Live", "Un-available", "Buynow"});

        //pieChart3.invalidate();
    }

    private void showBargraph() {
        BarData data = new BarData(getBarDataSet());
        chart.setFitBars(true);
        chart.setData(data);
        chart.getDescription().setText("Ads based on months");
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
        BarDataSet barDataSet1 = new BarDataSet(entries, "Ads count");
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

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "started");
    }

    private void intitArrays() {
        Arrays.fill(months, 0);
        Arrays.fill(approvalStatus, 0);
        Arrays.fill(otherFlagsFromApprovedAds, 0);
        Arrays.fill(otherFlagsFromUnApprovedAds, 0);
    }

    @Override
    public void onClick(View view) {
        if (view == yearLayout || view == yearEditText || view == yearEditText.getHint())
            startYearSelector();
        else if (view == update) {
            showUpdatingUi();
            advertisementManager.getAllAdsByYear(yearSelected);
        }
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

    private void sortDataByAdvertisemntList(List<Advertisement> advertisements) {
        intitArrays();
        for (Advertisement advertisement : advertisements) {
            if (advertisement.isApproved()) {
                approvalStatus[0] += 1;

                if (advertisement.isAvailability())
                    otherFlagsFromApprovedAds[0] += 1;

                if (!advertisement.isAvailability())
                    otherFlagsFromApprovedAds[1] += 1;

                if (advertisement.isBuynow())
                    otherFlagsFromApprovedAds[2] += 1;
            } else {
                approvalStatus[1] += 1;

                if (advertisement.isAvailability())
                    otherFlagsFromUnApprovedAds[0] += 1;

                if (!advertisement.isAvailability())
                    otherFlagsFromUnApprovedAds[1] += 1;

                if (advertisement.isBuynow())
                    otherFlagsFromUnApprovedAds[2] += 1;
            }

            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(advertisement.getPlacedDate());
                months[cal.get(Calendar.MONTH)] += 1;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        showBargraph();
        approvalStatusPieDataSet();
        approvalMoredataPieDataSet();
        rejectedMoredataPieDataSet();
        showLineChart();
    }

    private void showUpdatingUi() {
        update.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void endUpdatingUi() {
        update.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUploadImage(@NonNull Task<Uri> task) {

    }

    @Override
    public void onTaskFull(boolean result) {

    }

    @Override
    public void onSuccessInsertAd() {

    }

    @Override
    public void onFailureInsertAd() {

    }

    @Override
    public void onSuccessDeleteAd() {

    }

    @Override
    public void onFailureDeleteAd() {

    }

    @Override
    public void onSuccessUpdatetAd() {

    }

    @Override
    public void onFailureUpdateAd() {

    }

    @Override
    public void onAdCount(Task<QuerySnapshot> task) {

    }

    @Override
    public void getAdbyID(@NonNull Task<DocumentSnapshot> task) {

    }

    @Override
    public void onSuccessGetAllAdsByYear(QuerySnapshot queryDocumentSnapshots) {
        endUpdatingUi();
        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
            List<Advertisement> advertisements = queryDocumentSnapshots.toObjects(Advertisement.class);
            //Log.i(TAG, advertisements.toString());
            if (advertisements.size() > 0)
                sortDataByAdvertisemntList(advertisements);
            else
                Toast.makeText(context, "No data available", Toast.LENGTH_LONG).show();
        }
    }

}