package com.adeasy.advertise.firebase;

import android.content.Context;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import java.io.FileNotFoundException;

public interface OrderFirebase {
    public void uploadImage(Order order) throws FileNotFoundException;
    public void insertOrder(Order order, Context context);
    public void updateOrderImage(Order order);
    public void deleteOrder(String id, final String url, Context context);
    public void hideOrder(String id, boolean visibility, Context context);
    public Query viewOrders(String uid);
    public Query viewOrdersAll();
    public DocumentReference getOrderByID(String id);
    public String getDocumentNextID();
}
