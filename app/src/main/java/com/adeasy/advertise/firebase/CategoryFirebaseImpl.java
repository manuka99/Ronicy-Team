package com.adeasy.advertise.firebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class CategoryFirebaseImpl implements CategoryFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ContentResolver cr;
    StorageTask storageTask;
    final int requestCodeImage = 10541;
    Uri imageUri;
    ProgressDialog progressDialog;
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Category");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Category").child("Images");

    private static final String TAG = "CategoryFirebaseImpl";

    @Override
    public void uploadImage(final Category category, byte[] data, final Context context) {
        String imageID = FirebaseDatabase.getInstance().getReference().push().getKey();

        final StorageReference ref = storageReference.child(imageID);

        if (storageTask != null && storageTask.isInProgress())
            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show();

        else {

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading Image...");
            progressDialog.show();


            storageTask = ref.putBytes(data);

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
                        Log.i(TAG, downloadUri.toString());
                        progressDialog.dismiss();
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

                        category.setImageUrl(downloadUri.toString());
                        insertCat(category, context);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }

    @Override
    public void insertCat(Category category, final Context context) {
        DocumentReference refStore = db.collection("Category").document();
        String myId = refStore.getId();
        category.setId(myId);
        refStore.set(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void updateCat() {

    }

    @Override
    public void deleteCat() {

    }

    @Override
    public Query viewCats() {
        return FirebaseFirestore.getInstance().collection("Category");
    }

    @Override
    public DocumentReference getCatbyID(String id) {
        return db.collection("Category").document(id);
    }
}
