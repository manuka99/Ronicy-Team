package com.adeasy.advertise.ui.administration.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.ProductSales;
import com.adeasy.advertise.util.DoubleToCurrencyFormat;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class SingleProductAnalysis extends AppCompatActivity {

    ProductSales productSales;
    TextView orderItemName, orderItemCat, orderItemPrice, productID, noOfSales, salesLkr;
    ImageView orderItemImage;
    LineChart lineChart;
    PieChart pieChart;

    TextView statisticLineData, statisticPieData;
    CardView statisticLineDataCard, statisticPieDataCardView;

    NestedScrollView scroller;
    boolean isPieChartVisible = true;
    boolean isLineChartVisible = false;
    Toolbar toolbar;
    DoubleToCurrencyFormat doubleToCurrencyFormat;

    private static final String TAG = "SingleProductAnalysis";
    private static final String LABEL = "Sales vs Product Price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_admin_order_activity_single_product_analysis);

        try {
            productSales = (ProductSales) getIntent().getSerializableExtra("productSales");
        } catch (Exception e) {
            e.printStackTrace();
            productSales = new ProductSales();
        }

        toolbar = findViewById(R.id.toolbar);
        orderItemName = findViewById(R.id.orderItemName);
        orderItemCat = findViewById(R.id.orderItemCat);
        orderItemPrice = findViewById(R.id.orderItemPrice);
        productID = findViewById(R.id.productID);
        noOfSales = findViewById(R.id.noOfSales);
        salesLkr = findViewById(R.id.salesLkr);
        orderItemImage = findViewById(R.id.orderItemImage);
        scroller = findViewById(R.id.scrollbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Product Details");
        getSupportActionBar().setSubtitle(LABEL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doubleToCurrencyFormat = new DoubleToCurrencyFormat();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //statistic views
        statisticPieDataCardView = findViewById(R.id.statisticPieDataCardView);
        statisticLineDataCard = findViewById(R.id.statisticLineDataCard);
        statisticPieData = findViewById(R.id.statisticPieData);
        statisticLineData = findViewById(R.id.statisticLineData);

        lineChart = findViewById(R.id.lineChart);
        pieChart = findViewById(R.id.pieChart);

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

        if (productSales.getOrder_item().getId() != null) {
            orderItemName.setText(productSales.getOrder_item().getItemName());
            orderItemCat.setText(productSales.getOrder_item().getCategoryName());
            orderItemPrice.setText(productSales.getOrder_item().getPreetyCurrency());
            productID.setText(productSales.getOrder_item().getId());
            noOfSales.setText(String.valueOf(productSales.getSalesCount()));
            salesLkr.setText(productSales.getPreetyCurrency());

            Picasso.get().load(productSales.getOrder_item().getImageUrl()).fit().into(orderItemImage);

            if (productSales.getPriceRangersAnCount().size() > 1)
                calculateDataSet();
            else {
                statisticPieDataCardView.setVisibility(View.GONE);
                statisticLineDataCard.setVisibility(View.GONE);
                statisticPieData.setVisibility(View.GONE);
                statisticLineData.setVisibility(View.GONE);
            }
        }
    }

    private void calculateDataSet() {
        List<Entry> lineEntries = new ArrayList<>();
        List<PieEntry> pieEntries = new ArrayList<>();
        String[] xData = new String[productSales.getPriceRangersAnCount().size()];

        int i = 0;
        for (Double key : productSales.getPriceRangersAnCount().keySet()) {
            //linechaery
            lineEntries.add(new Entry(i, productSales.getPriceRangersAnCount().get(key)));
            xData[i] = doubleToCurrencyFormat.setStringValue(key.toString());

            //piechart
            pieEntries.add(new PieEntry(productSales.getPriceRangersAnCount().get(key), doubleToCurrencyFormat.setStringValue(key.toString())));
            ++i;
        }

        showLineChart(lineEntries, xData);
        showPieDataSet(pieEntries);
    }

    private void showLineChart(List<Entry> entries, String[] xData) {
        LineDataSet lineDataSet = new LineDataSet(entries, LABEL);
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final String[] months = xData;

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
        lineChart.animateXY(1000, 1000);
        lineChart.invalidate();
    }

    private void showPieDataSet(List<PieEntry> entries) {
        PieDataSet pieDataSet = new PieDataSet(entries, "Sales count");
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
        pieChart.setCenterText(LABEL);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.animateXY(1000, 1000);
        pieChart.invalidate();
    }

}