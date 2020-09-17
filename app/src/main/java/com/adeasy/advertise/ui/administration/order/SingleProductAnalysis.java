package com.adeasy.advertise.ui.administration.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.ProductSales;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SingleProductAnalysis extends AppCompatActivity {

    ProductSales productSales;
    TextView orderItemName, orderItemCat, orderItemPrice, productID, noOfSales, salesLkr;
    ImageView orderItemImage;
    LineChart lineChart;
    CardView cardView;

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

        lineChart = findViewById(R.id.lineChart);

        orderItemName = findViewById(R.id.orderItemName);
        orderItemCat = findViewById(R.id.orderItemCat);
        orderItemPrice = findViewById(R.id.orderItemPrice);
        productID = findViewById(R.id.productID);
        noOfSales = findViewById(R.id.noOfSales);
        salesLkr = findViewById(R.id.salesLkr);
        orderItemImage = findViewById(R.id.orderItemImage);
        cardView = findViewById(R.id.cardView);

        if (productSales.getOrder_item().getId() != null) {
            orderItemName.setText(productSales.getOrder_item().getItemName());
            orderItemCat.setText(productSales.getOrder_item().getCategoryName());
            orderItemPrice.setText(String.valueOf(productSales.getOrder_item().getPrice()));
            productID.setText(productSales.getOrder_item().getId());
            noOfSales.setText(String.valueOf(productSales.getSalesCount()));
            salesLkr.setText(String.valueOf(productSales.getTotalSales()));

            Picasso.get().load(productSales.getOrder_item().getImageUrl()).fit().into(orderItemImage);

            if (productSales.getPriceRangersAnCount().size() > 1)
                showLineChart();
            else
                cardView.setVisibility(View.GONE);
        }
    }

    private void showLineChart() {
        List<Entry> entries = new ArrayList<>();
        String[] xData = new String[productSales.getPriceRangersAnCount().size()];

        int i = 0;
        for (Double key : productSales.getPriceRangersAnCount().keySet()) {
            entries.add(new Entry(i, productSales.getPriceRangersAnCount().get(key)));
            xData[i] = String.valueOf(key);
            ++i;
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Sales vs Product Price");
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
        lineChart.animateXY(1500, 1500);
        lineChart.invalidate();
    }

}