package com.adeasy.advertise.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.model.ProductSales;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.model.TableColumnModel;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ProductAnalysisTableHeaderAdapter extends TableHeaderAdapter {

    private Context context;
    private static final String[] TABLE_HEADERS = {"ProductID", "Product Name", "Sales Count", "Total Sales(LKR)"};

    public ProductAnalysisTableHeaderAdapter(Context context, int columnCount) {
        super(context, columnCount);
        this.context = context;
    }

    @Override
    public View getHeaderView(int columnIndex, ViewGroup parentView) {
        return renderHeader(columnIndex);
    }

    private View renderHeader(int columnIndex) {
        TextView textView = new TextView(context);
        textView.setText(TABLE_HEADERS[columnIndex]);
        textView.setPadding(14, 20, 10, 20);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(getResources().getColor(R.color.colorBlackText));
        textView.setTextSize(14);
        return textView;
    }

}

