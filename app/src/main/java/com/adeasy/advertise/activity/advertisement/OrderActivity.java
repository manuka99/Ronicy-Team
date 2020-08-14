package com.adeasy.advertise.activity.advertisement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.adeasy.advertise.R;
import com.adeasy.advertise.firebase.OrderFirebase;
import com.adeasy.advertise.firebase.OrderFirebaseImpl;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;
import com.adeasy.advertise.model.Order_Payment;

public class OrderActivity extends AppCompatActivity {

    OrderFirebase orderFirebase = new OrderFirebaseImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Order order = new Order();
        Order_Payment payment = new Order_Payment();
        Order_Item item = new Order_Item();
        Order_Customer customer = new Order_Customer();

        order.setOrderDescription("dsdsdsd");

        payment.setType("Payhere");
        payment.setStatus("Approved");
        payment.setAmount(4565.00);
        order.setPayment(payment);

        item.setItemName("dsdsd");
        item.setImageUrl("https://firebasestorage.googleapis.com/v0/b/ad-easy.appspot.com/o/Advertisement%2FImages%2F-MEdJO291Ik6JtP1UJTt?alt=media&token=e5204b6b-b423-4e2e-b959-e64db679d99b");
        order.setItem(item);

        customer.setName("asasasa");
        customer.setEmail("sasasa");
        customer.setPhone("sasasas");
        order.setCustomer(customer);

        orderFirebase.insertOrder(order, getApplicationContext());

        order.setId("dLlFY7y8b2wlyKla42KN");
        orderFirebase.updateOrderImage(order);
    }
}