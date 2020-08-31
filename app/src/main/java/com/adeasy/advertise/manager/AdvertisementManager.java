package com.adeasy.advertise.manager;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.AdvertisementCallback;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.util.ImageQualityReducer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(final Advertisement advertisement, byte[] data) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadImageMultiple(final Advertisement advertisement, Context context) {
        try {
            if (storageTask != null && storageTask.isInProgress())
                advertisementCallback.onTaskFull(true);

            else {
                advertisementCallback.onTaskFull(false);

                for (int i = 0; i <= advertisement.getImageUris().size(); ++i) {
                    Uri imageUri = advertisement.getImageUris().get(i);
                    byte[] data = ImageQualityReducer.reduceQualityFromBitmap(imageUri, context);
                    String imageID = documentReference.getId();
                    final StorageReference ref = storageReference.child(imageID);
                    storageTask = ref.putBytes(data);

                    final int counter = i;

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
                                advertisement.getImageUrls().add(downloadUri.toString());
                            } else {
                                // Handle failures
                                // ...
                            }

                            try{
                               advertisement.getImageUris().get(counter + 1);
                            }catch (Exception e){
                                insertAdvertisement(advertisement);
                            }

                        }
                    });

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAdd(String id) {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideAdd(String id, boolean visibility) {
        try {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Query viewAdds() {
        return firebaseFirestore.collection(childName).whereEqualTo("availability", true).whereEqualTo("approved", true).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    public Query viewAddsAll() {
        return firebaseFirestore.collection(childName).orderBy("placedDate", Query.Direction.DESCENDING);
    }

    public void getAddbyID(String id) {
        try {
            firebaseFirestore.collection(childName).document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    advertisementCallback.getAdbyID(task);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void getCount() {
        try {
            firebaseFirestore.collection(childName).whereEqualTo("availability", true).whereEqualTo("approved", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (advertisementCallback != null)
                        advertisementCallback.onAdCount(task);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        firebaseFirestore = null;
        firebaseStorage = null;
        documentReference = null;
        storageReference = null;
        advertisementCallback = null;
    }

}