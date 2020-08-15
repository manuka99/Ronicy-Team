package com.adeasy.advertise.firebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Order;
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
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class OrderFirebaseImpl implements OrderFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ContentResolver cr;
    StorageTask storageTask;
    final int requestCodeImage = 10711;
    Advertisement advertisement;

    ///DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Orders").child("Images");

    private static final String TAG = "OrderFirebaseImpl";

    @Override
    public void uploadImage(final Order order) throws FileNotFoundException {

        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(order.getItem().getImageUrl());
        final long ONE_MEGABYTE = 1024 * 1024;
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
                            updateOrderImage(order);
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

    @Override
    public void insertOrder(final Order order, final Context context){

        DocumentReference refStore;

        if (order.getId() == null) {
            refStore =  db.collection("Order").document();
            String myId = refStore.getId();
            order.setId(myId);
        }

        else
            refStore =  db.collection("Order").document(order.getId());

        refStore.set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "Success: Your order was placed", Toast.LENGTH_LONG).show();
                        try {
                            uploadImage(order);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    @Override
    public void updateOrderImage(Order order) {

        // Update one field, creating the document if it does not already exist.
        Map<Object, Object> item = new HashMap<>();
        item.put("imageUrl", order.getItem().getImageUrl());

        Map<Object, Object> data = new HashMap<>();
        data.put("item", item);

        //data.put(order );
        db.collection("Order").document(order.getId())
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    @Override
    public void deleteOrder(String id, String url, Context context) {

    }

    @Override
    public void hideOrder(String id, boolean visibility, Context context) {

    }

    @Override
    public Query viewOrders(String uid) {
        return null;
    }

    @Override
    public Query viewOrdersAll() {
        return null;
    }

    @Override
    public DocumentReference getOrderByID(String id) {
        return null;
    }

    @Override
    public String getDocumentNextID() {
        return db.collection("Order").document().getId();
    }

    @Override
    public DocumentReference getInsertQueryOrder(String id) {

        DocumentReference refStore;

        if (id == null) {
            refStore =  db.collection("Order").document();
            id = refStore.getId();
        }

            refStore =  db.collection("Order").document(id);

        return refStore;
    }
}
