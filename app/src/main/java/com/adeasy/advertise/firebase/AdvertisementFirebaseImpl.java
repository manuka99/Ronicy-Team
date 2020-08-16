package com.adeasy.advertise.firebase;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.adeasy.advertise.model.Advertisement;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.SnapshotMetadata;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AdvertisementFirebaseImpl implements AdvertisementFirebase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ContentResolver cr;
    StorageTask storageTask;
    final int requestCodeImage = 10541;
    ProgressDialog progressDialog;

    Advertisement advertisement;

    ///DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Advertisement");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Advertisement").child("Images");

    private static final String TAG = "AdvertisementFireImpl";

    @Override
    public void insertAdd(final Advertisement advertisement, final Context context) {

        DocumentReference refStore;

        if (advertisement.getId() == null) {
            refStore =  db.collection("Advertisement").document();
            String myId = refStore.getId();
            advertisement.setId(myId);
        }

        else
            refStore =  db.collection("Advertisement").document(advertisement.getId());

        refStore.set(advertisement)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(context, "Success: Your advertisement was submited", Toast.LENGTH_LONG).show();
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
    public void uploadImage(final Advertisement advertisement, byte[] data,
                            final Context context) {

        if (storageTask != null && storageTask.isInProgress())
            Toast.makeText(context, "Please Wait..", Toast.LENGTH_SHORT).show();

        else {
            String imageID = FirebaseDatabase.getInstance().getReference().push().getKey();

            final StorageReference ref = storageReference.child(imageID);

            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Publishing your advertisement...");
            progressDialog.setMessage("Your advertisement will be live after we approve it.");
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

                        progressDialog.dismiss();

                        if(advertisement.getImageUrl() != null)
                            deletePreviousImage(advertisement.getImageUrl());

                        advertisement.setImageUrl(downloadUri.toString());
                        insertAdd(advertisement, context);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    @Override
    public void updateAdd() {

    }

    @Override
    public void deleteAdd(String id, final String url, final Context context) {

        db.collection("Advertisement").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(context, "Success: Your advertisement was deleted.", Toast.LENGTH_LONG).show();

                        deletePreviousImage(url);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void hideAdd(String id, boolean visibility, final Context context) {

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> data = new HashMap<>();
        data.put("availability", visibility);
        db.collection("Advertisement").document(id)
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
                Toast.makeText(context, "Success: " + "Changes saved", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public Query viewAdds() {
        return db.collection("Advertisement").whereEqualTo("availability", true).whereEqualTo("approved", true).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    @Override
    public Query viewAddsAll() {
        return db.collection("Advertisement").orderBy("placedDate", Query.Direction.DESCENDING);
    }

    @Override
    public DocumentReference getAddbyID(String id) {

        return db.collection("Advertisement").document(id);

    }

    public void deletePreviousImage(String url) {

        final StorageReference storageref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });


    }

}
