package com.adeasy.advertise.ui.administration.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.manager.OrderManager;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MoreOnOrder extends AppCompatActivity implements OrderCallback {

    Toolbar toolbar;
    String orderId;
    OrderManager orderManager;
    Order order;
    CustomDialogs customDialogs;
    Context context;

    //customer
    TextView name, address, email, phone, uid;

    //order
    TextView orderIDView, hidden, placedDate, description;
    TextInputLayout deliveredDate, deliveredEstimatedDate, orderStatus;

    //item
    TextView itemID, title, catName, price;
    ImageView imageView;

    //payment
    TextView total, paymentStatus, paymentType;

    boolean isHidden;

    private static final String TAG = "MoreOnOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manuka_admin_activity_more_on_order);

        toolbar = findViewById(R.id.toolbar);

        //customer
        name = findViewById(R.id.orderName);
        address = findViewById(R.id.orderName);
        email = findViewById(R.id.orderName);
        phone = findViewById(R.id.orderName);
        uid = findViewById(R.id.orderName);

        //order
        orderIDView = findViewById(R.id.orderName);
        hidden = findViewById(R.id.orderName);
        placedDate = findViewById(R.id.orderName);
        description = findViewById(R.id.orderName);
        deliveredDate = findViewById(R.id.password);
        deliveredEstimatedDate = findViewById(R.id.password);
        orderStatus = findViewById(R.id.password);

        //item
        itemID = findViewById(R.id.orderName);
        title = findViewById(R.id.orderName);
        catName = findViewById(R.id.orderName);
        price = findViewById(R.id.orderName);
        imageView = findViewById(R.id.imageCamera);

        //payment
        total = findViewById(R.id.orderName);
        paymentStatus = findViewById(R.id.password);
        paymentType = findViewById(R.id.password);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.dashboardVersion));
        getSupportActionBar().setSubtitle("Order Details");

        try {
            orderId = getIntent().getStringExtra("orderID");
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }

        context = this;

        orderManager = new OrderManager(this, context);
        customDialogs = new CustomDialogs(context);

        orderManager.getOrderFromByID(orderId);
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
        if(order == null) {
            //customer
            name.setText(order.getCustomer().getName());
            address.setText(order.getCustomer().getName());
            email.setText(order.getCustomer().getName());
            phone.setText(order.getCustomer().getName());
            uid.setText(order.getCustomer().getName());

            //order
            orderIDView.setText(order.getCustomer().getName());
            hidden.setText(order.getCustomer().getName());
            placedDate.setText(order.getCustomer().getName());
            description.setText(order.getCustomer().getName());

            deliveredDate.getEditText().setText(order.getDeliveredDate().toString());
            deliveredEstimatedDate.getEditText().setText(order.getDeliveredDate().toString());
            orderStatus.getEditText().setText(order.getDeliveredDate().toString());


            uid.setText(order.getCustomer().getName());

            //item
            TextView itemID, title, catName, price;
            ImageView imageView;

            //payment
            TextView total, paymentStatus, paymentType;

            isHidden = true;
        }
    }

}