package com.adeasy.advertise.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.callback.OrderCallback;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Order;
import com.adeasy.advertise.service.MailService;
import com.adeasy.advertise.service.MailServiceImpl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class OrderManager {

    private static final String TAG = "OrderManager";
    private static final String childName = "Order";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private StorageTask storageTask;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private StorageReference storageReference;
    private OrderCallback orderCallback;
    private Context context;
    private MailService mailService;

    public OrderManager() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        documentReference = firebaseFirestore.collection(childName).document();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
        mailService = new MailServiceImpl(context);
    }

    public OrderManager(OrderCallback callBacks, Context context) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        documentReference = firebaseFirestore.collection(childName).document();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
        mailService = new MailServiceImpl(context);
        this.orderCallback = callBacks;
    }

    public void insertOrder(final Order order, final boolean uploadImageFromOrderItem) {
        DocumentReference refStore;

        if (order.getId() == null) {
            String myId = documentReference.getId();
            order.setId(myId);
            refStore = documentReference;
        } else
            refStore = firebaseFirestore.collection(childName).document(order.getId());

        refStore.set(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                orderCallback.onCompleteInsertOrder(task);
                if (task.isSuccessful()) {
                    mailService.SendOrderPlacedEmail(order);
                    try {
                        if(uploadImageFromOrderItem)
                        uploadImage(order);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void uploadImage(final Order order) throws FileNotFoundException {

        StorageReference httpsReference = firebaseStorage.getReferenceFromUrl(order.getItem().getImageUrl());
        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                String imageID = FirebaseDatabase.getInstance().getReference().push().getKey();

                final StorageReference ref = storageReference.child(imageID);

                storageTask = ref.putBytes(bytes);

                Task<Uri> urlTask = storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            order.getItem().setImageUrl(downloadUri.toString());
                            updateOrderImageUrl(order);
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    public void updateOrderImageUrl(Order order) {

        // Update one field, creating the document if it does not already exist.
        Map<Object, Object> item = new HashMap<>();
        item.put("imageUrl", order.getItem().getImageUrl());

        Map<Object, Object> data = new HashMap<>();
        data.put("item", item);

        //data.put(order );
        firebaseFirestore.collection(childName).document(order.getId())
                .set(data, SetOptions.merge());
    }

    public Query recentOrders(){
        Date currentDate = new Date(System.currentTimeMillis() - 3600 * 24 * 1000);
        Log.i(TAG, "current date back 24h: " + currentDate);
        return FirebaseFirestore.getInstance().collection(childName).whereGreaterThanOrEqualTo("placedDate", currentDate).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    public void destroy() {
        orderCallback = null;
    }

}
