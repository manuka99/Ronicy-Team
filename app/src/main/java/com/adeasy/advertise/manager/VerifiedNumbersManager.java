package com.adeasy.advertise.manager;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.VerifiedNumbersCallback;
import com.adeasy.advertise.model.User;
import com.adeasy.advertise.model.UserVerifiedNumbers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class VerifiedNumbersManager {

    private static final String TAG = "VerifiedNumbersManager";
    private static final String childName = "Users";
    private static final String arrayName = "verifiedNumbers";
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private VerifiedNumbersCallback verifiedNumbersCallback;

    public VerifiedNumbersManager(VerifiedNumbersCallback callBacks) {
        this.verifiedNumbersCallback = callBacks;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public VerifiedNumbersManager() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void insertVerifiedNumber(final UserVerifiedNumbers user, FirebaseUser firebaseUser) {

        final DocumentReference refStore;
        refStore = firebaseFirestore.collection(childName).document(firebaseUser.getUid());
        refStore.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    for (Integer num : user.getVerifiedNumbers())
                        refStore.update(arrayName, FieldValue.arrayUnion(num)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                verifiedNumbersCallback.onSuccessfullNumberInserted();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    ;
                } else {
                    refStore.set(user, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    verifiedNumbersCallback.onSuccessfullNumberInserted();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                }
            }
        });

    }

    public void validateNumber(Integer number, FirebaseUser firebaseUser) {
        try {
            firebaseFirestore.collection(childName).whereArrayContainsAny(arrayName, Arrays.asList(number)).whereEqualTo("uid", firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    verifiedNumbersCallback.onCompleteSearchNumberInUser(queryDocumentSnapshots);
                }
            });
        } catch (NullPointerException e) {
            verifiedNumbersCallback.onCompleteSearchNumberInUser(null);
        }
    }

    public Query viewVerifiedNumbersByUser(FirebaseUser user) {
        Query query = null;
        try {
            query = documentReference.collection(user.getUid());
        } catch (NullPointerException e) {
            query = null;
        }
        return query;
    }

    public void getVerifiedNumbersOfUser(FirebaseUser firebaseUser) {
        try {
            firebaseFirestore.collection(childName).document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    verifiedNumbersCallback.onCompleteRecieveAllNumbersInUser(documentSnapshot);
                }
            });
        } catch (NullPointerException e) {
            verifiedNumbersCallback.onCompleteSearchNumberInUser(null);
        }
    }

    public void destroy() {
        firebaseFirestore = null;
        documentReference = null;
    }

}
