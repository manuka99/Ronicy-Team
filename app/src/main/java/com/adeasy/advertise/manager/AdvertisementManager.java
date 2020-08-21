package com.adeasy.advertise.manager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.adeasy.advertise.activity.advertisement.BuyNow;
import com.adeasy.advertise.callback.AdvertisementCallback;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AdvertisementManager {

    private static final String TAG = "AdvertisementManager";
    private static final String childName = "Advertisement";
    private StorageTask storageTask;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private StorageReference storageReference;
    private AdvertisementCallback advertisementCallback;

    public AdvertisementManager(AdvertisementCallback callBacks) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        documentReference = firebaseFirestore.collection(childName).document();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
        this.advertisementCallback = callBacks;
    }

    public void insertAdvertisement(Advertisement advertisement) {

        DocumentReference refStore;

        if (advertisement.getId() == null) {
            String myId = documentReference.getId();
            advertisement.setId(myId);
            refStore = documentReference;
        } else
            refStore = firebaseFirestore.collection(childName).document(advertisement.getId());

        refStore.set(advertisement)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        advertisementCallback.onSuccessInsertAd();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        advertisementCallback.onFailureInsertAd();
                    }
                });
    }

    public void uploadImage(final Advertisement advertisement, byte[] data) {

        if (storageTask != null && storageTask.isInProgress())
            advertisementCallback.onTaskFull(true);

        else {
            advertisementCallback.onTaskFull(false);

            String imageID = documentReference.getId();
            final StorageReference ref = storageReference.child(imageID);
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
                    advertisementCallback.onUploadImage(task);
                }
            });
        }
    }

    public void deleteAdd(String id) {

        firebaseFirestore.collection(childName).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        advertisementCallback.onSuccessDeleteAd();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        advertisementCallback.onFailureDeleteAd();
                    }
                });

    }

    public void hideAdd(String id, boolean visibility) {

        // Update one field
        Map<String, Object> data = new HashMap<>();
        data.put("availability", visibility);
        firebaseFirestore.collection(childName).document(id)
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                advertisementCallback.onSuccessUpdatetAd();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        advertisementCallback.onFailureUpdateAd();
                    }
                });

    }

    public Query viewAdds() {
        return firebaseFirestore.collection(childName).whereEqualTo("availability", true).whereEqualTo("approved", true).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    public Query viewAddsAll() {
        return firebaseFirestore.collection(childName).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    public void getAddbyID(String id) {
        firebaseFirestore.collection(childName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                advertisementCallback.getAdbyID(task);
            }
        });
    }

    public void deletePreviousImage(String url) {

        final StorageReference storageref = firebaseStorage.getReferenceFromUrl(url);
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

    public void destroy() {
        firebaseFirestore = null;
        firebaseStorage = null;
        documentReference = null;
        storageReference = null;
        advertisementCallback = null;
    }

}