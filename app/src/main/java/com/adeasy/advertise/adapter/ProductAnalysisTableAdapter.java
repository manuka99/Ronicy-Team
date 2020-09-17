package com.adeasy.advertise.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeasy.advertise.model.ProductSales;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

public class ProductAnalysisTableAdapter extends TableDataAdapter<ProductSales> {

    private Context context;

    public ProductAnalysisTableAdapter(Context context, List<ProductSales> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ProductSales productSales = getRowData(rowIndex);
        View renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderProductID(productSales);
                break;
            case 1:
                renderedView = renderProductName(productSales);
                break;
            case 2:
                renderedView = renderProductSales(productSales);
                break;
            case 3:
                renderedView = renderProductTotalSales(productSales);
                break;
        }
        return renderedView;
    }

    private View renderProductID(ProductSales productSales) {
        TextView textView = new TextView(context);
        textView.setText(productSales.getProductID());
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(14);
        return textView;
    }

    private View renderProductName(ProductSales productSales) {
        TextView textView = new TextView(context);
        textView.setText(productSales.getProductName());
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(14);
        return textView;
    }

    private View renderProductSales(ProductSales productSales) {
        TextView textView = new TextView(context);
        textView.setText(String.valueOf(productSales.getSalesCount()));
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(14);
        return textView;
    }

    private View renderProductTotalSales(ProductSales productSales) {
        TextView textView = new TextView(context);
        textView.setText(productSales.getTotalSales().toString());
        textView.setPadding(10, 5, 10, 5);
        textView.setTextSize(14);
        return textView;
    }

}