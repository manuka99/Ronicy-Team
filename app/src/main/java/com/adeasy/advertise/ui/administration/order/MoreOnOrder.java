package com.adeasy.advertise.ui.administration.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MoreOnOrder extends AppCompatActivity implements OrderCallback, AdapterView.OnItemSelectedListener, View.OnClickListener {

    Toolbar toolbar;
    String orderId;
    OrderManager orderManager;
    Order order;
    CustomDialogs customDialogs;
    Context context;
    TextInputEditText estimatedDateEditText;

    //customer
    TextView name, address, email, phone;

    //order
    TextView orderIDView, orderTitle, orderTotal, hidden, placedDate;
    TextView deliveredDate;
    LinearLayout deliveredDateLayout;

    //item
    TextView itemID, title, catName, price;
    ImageView imageView;

    //payment
    TextView total, paymentType;

    Spinner order_status, payment_status;
    ArrayAdapter<CharSequence> adapter_order_status, adapter_payment_status;

    List<String> order_status_list, payment_status_list;

    TextInputLayout orderDescription, estimatedDate;

    boolean isHidden = false;
    String selectedEstimatedDate;

    private static final String TAG = "MoreOnOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_admin_activity_more_on_order);

        toolbar = findViewById(R.id.toolbar);

        //customer
        name = findViewById(R.id.customerName);
        address = findViewById(R.id.customerAddress);
        email = findViewById(R.id.customerEmail);
        phone = findViewById(R.id.customerPhone);

        //order
        orderIDView = findViewById(R.id.orderID);
        orderTitle = findViewById(R.id.itemTitle);
        orderTotal = findViewById(R.id.orderTotal);
        hidden = findViewById(R.id.isHidden);
        placedDate = findViewById(R.id.orderPlacedDate);
        deliveredDateLayout = findViewById(R.id.layoutDelivered);
        deliveredDate = findViewById(R.id.password);

        //item
        itemID = findViewById(R.id.itemID);
        title = findViewById(R.id.itemName);
        catName = findViewById(R.id.category);
        price = findViewById(R.id.itemPrice);
        imageView = findViewById(R.id.orderImage);

        //payment
        total = findViewById(R.id.paymentAmount);
        paymentType = findViewById(R.id.paymentType);

        orderDescription = findViewById(R.id.orderDescription);
        estimatedDate = findViewById(R.id.estimatedDate);

        order_status_list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.order_status_array)));
        payment_status_list = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.payment_status_array)));

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.dashboardVersion));
        getSupportActionBar().setSubtitle("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {
            orderId = getIntent().getStringExtra("orderID");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }

        context = this;

        orderManager = new OrderManager(this, context);
        customDialogs = new CustomDialogs(context);

        orderManager.getOrderFromByID(orderId);

        order_status = findViewById(R.id.spinner_order_status);
        payment_status = findViewById(R.id.spinner_payment_status);
        estimatedDateEditText = findViewById(R.id.estimatedDateEditText);

        //set adapter for order status and payment status array
        adapter_order_status = ArrayAdapter.createFromResource(this,
                R.array.order_status_array, android.R.layout.simple_spinner_item);
        adapter_payment_status = ArrayAdapter.createFromResource(this,
                R.array.payment_status_array, android.R.layout.simple_spinner_item);

        adapter_order_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_payment_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        order_status.setAdapter(adapter_order_status);
        payment_status.setAdapter(adapter_payment_status);

        //listeners
        estimatedDate.setOnClickListener(this);
        estimatedDate.getEditText().setOnClickListener(this);
        estimatedDateEditText.setOnClickListener(this);
        hidden.setOnClickListener(this);
    }

    @Override
    public void onCompleteInsertOrder(Task<Void> task) {

    }

    @Override
    public void onGetOrderByID(Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful() && task.getResult().exists()) {
            order = task.getResult().toObject(Order.class);
            updateUIOnResult();
        } else if (task != null && !task.isSuccessful() && task.getException() instanceof FirebaseFirestoreException && ((FirebaseFirestoreException) task.getException()).getCode().equals(FirebaseFirestoreException.Code.PERMISSION_DENIED))
            customDialogs.showPermissionDeniedStorage();
    }

    @Override
    public void onHideOrderByID(Task<Void> task) {

    }

    private void updateUIOnResult() {
        if (order != null) {
            //customer
            name.setText(order.getCustomer().getName());
            address.setText(order.getCustomer().getAddress());
            email.setText(order.getCustomer().getEmail());
            phone.setText(String.valueOf(order.getCustomer().getPhone()));
            //order
            orderIDView.setText(order.getId());
            orderTitle.setText(order.getItem().getItemName());
            orderTotal.setText(String.valueOf(order.getPayment().getAmount()));

            isHidden = order.isAvalability();

            if (order.isAvalability())
                hidden.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
            else
                hidden.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);

            placedDate.setText(order.getPlacedDate().toString());
            orderDescription.getEditText().setText(order.getOrderDescription());

            if (order.getDeliveredDate() != null) {
                deliveredDateLayout.setVisibility(View.VISIBLE);
                deliveredDate.setText(order.getDeliveredDate().toString());
            } else {
                deliveredDateLayout.setVisibility(View.GONE);
            }

            int postion_of_order_status = 0;
            for (String order_status_string : order_status_list) {
                if (order.getOrderStatus().equals(order_status_string))
                    order_status.setSelection(postion_of_order_status);
                else
                    ++postion_of_order_status;
            }

            int postion_of_payment_status = 0;
            for (String payment_status_string : payment_status_list) {
                if (order.getPayment().getStatus().equals(payment_status_string))
                    payment_status.setSelection(postion_of_payment_status);
                else
                    ++postion_of_payment_status;
            }

            //item
            itemID.setText(order.getItem().getId());
            title.setText(order.getItem().getItemName());
            catName.setText(order.getItem().getCategoryName());
            price.setText(String.valueOf(order.getItem().getPrice()));
            Picasso.get().load(order.getItem().getImageUrl()).fit().into(imageView);
            //payment
            total.setText(String.valueOf(order.getPayment().getAmount()));
            paymentType.setText(order.getPayment().getType());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if (view == hidden)
            onHideOrderClicked();
        else if (view == estimatedDate || view == estimatedDateEditText || view == estimatedDateEditText.getHint())
            showDatePicker();
    }

    private void onHideOrderClicked() {
        if (isHidden) {
            isHidden = false;
            hidden.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_outline_blank_24_ad, 0, 0, 0);
        } else {
            isHidden = true;
            hidden.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_box_24_ad, 0, 0, 0);
        }
    }

    private void showDatePicker() {
        Log.i(TAG, "datePcker");
        final Calendar newCalendar = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
        DatePickerDialog StartTime = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                selectedEstimatedDate = format.format(newDate.getTime());
                estimatedDate.getEditText().setText(selectedEstimatedDate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

}