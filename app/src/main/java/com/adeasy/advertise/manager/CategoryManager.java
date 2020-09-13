package com.adeasy.advertise.manager;

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.callback.CategoryCallback;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class CategoryManager {

    private static final String TAG = "CategoryManager";
    private static final String childName = "Category";
    private StorageTask storageTask;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private StorageReference storageReference;
    private CategoryCallback categoryCallback;

    public CategoryManager() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        documentReference = firebaseFirestore.collection(childName).document();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
    }

    public CategoryManager(CategoryCallback callBacks) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        documentReference = firebaseFirestore.collection(childName).document();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
        this.categoryCallback = callBacks;
    }

    public void insertCategory(Category category) {
        DocumentReference refStore;

        if (category.getId() == null) {
            String myId = documentReference.getId();
            category.setId(myId);
            refStore = documentReference;
        } else
            refStore = firebaseFirestore.collection(childName).document(category.getId());

        refStore.set(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //categoryCallback.onSuccessInsertCategory();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //categoryCallback.onFailureInsertCategory();
                    }
                });
    }

    public void uploadImage(final Advertisement advertisement, byte[] data) {
        if (storageTask != null && storageTask.isInProgress()){
            //categoryCallback.onTaskFull(true);
        }

        else{
            //categoryCallback.onTaskFull(false);
        }

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
                    //categoryCallback.onUploadImage(task);
                }
            });
    }

    public void deleteCategory(String id) {
        firebaseFirestore.collection(childName).document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //categoryCallback.onSuccessDeleteCategory();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //categoryCallback.onFailureDeleteCategory();
                    }
                });
    }

    public Query viewCategoryAll() {
        return firebaseFirestore.collection(childName);
    }

    public void getCategorybyID(String id) {

        firebaseFirestore.collection(childName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                categoryCallback.getCategoryByID(task);
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
        this.categoryCallback = null;
    }

}
