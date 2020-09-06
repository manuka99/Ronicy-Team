package com.adeasy.advertise.manager;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.ProfileManagerCallback;
import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas94@gmail.com
 **/

public class ProfileManager {

    private static final String TAG = "ProfileManager";
    private static final String childName = "Users";
    private ProfileManagerCallback profileManagerCallback;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageTask storageTask;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public ProfileManager(ProfileManagerCallback profileManagerCallback) {
        this.profileManagerCallback = profileManagerCallback;
        firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child(childName).child("Images");
    }

    public void updateProfile(User user) {
        final DocumentReference refStore;
        refStore = firebaseFirestore.collection(childName).document(firebaseAuth.getCurrentUser().getUid());
        refStore.set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        profileManagerCallback.onSuccessUpdateProfile(aVoid);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        profileManagerCallback.onFailureUpdateProfile(e);
                    }
                });
    }

    public void updatePassword(String password) {
        firebaseAuth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                profileManagerCallback.onCompleteUpdatePassword(task);
            }
        });
    }


    private void updateFirebaseProfile(final User user, String url) {
        UserProfileChangeRequest profileUpdates;
        profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getName()).build();

        if (url != null) {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(user.getName()).setPhotoUri(Uri.parse(url)).build();
        }

        firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            updateProfile(user);
                        } else {
                            profileManagerCallback.onFailureUpdateProfile(task.getException());
                        }
                    }
                });
    }

    private void updateFirebaseEmail(String email) {
        firebaseAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                profileManagerCallback.onCompleteUpdateEmail(task);
            }
        });
    }

    public void deleteImage(String url) {
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

    public void uploadImage(final User user, byte[] data) {
        try {
            if (storageTask != null && storageTask.isInProgress())
                profileManagerCallback.onTaskFull(true);

            else {
                profileManagerCallback.onTaskFull(false);

                String imageID = UUID.randomUUID().toString().replace("-", "");
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
                        if (task.isSuccessful()) {
                            Uri uri = task.getResult();
                            updateFirebaseProfile(user, uri.toString());
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUser() {
        try {
            firebaseFirestore.collection(childName).document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    profileManagerCallback.onSuccessGetUser(documentSnapshot);
                }
            });
        } catch (NullPointerException e) {
            profileManagerCallback.onSuccessGetUser(null);
        }
    }

}
