package com.adeasy.advertise.manager;

import androidx.annotation.NonNull;

import com.adeasy.advertise.callback.VerifiedNumbersCallback;
import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.VerifiedNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class VerifiedNumbersManager {

    private static final String TAG = "VerifiedNumbersManager";
    private static final String childName = "Users";
    private static final String childDoc = "verified_numbers";
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private VerifiedNumbersCallback verifiedNumbersCallback;

    public VerifiedNumbersManager(VerifiedNumbersCallback callBacks) {
        this.verifiedNumbersCallback = callBacks;
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.documentReference = firebaseFirestore.collection(childName).document(childDoc);
    }

    public VerifiedNumbersManager() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.documentReference = firebaseFirestore.collection(childName).document(childDoc);
    }

    public void insertVerifiedNumber(VerifiedNumber verifiedNumber, FirebaseUser firebaseUser) {
        DocumentReference refStore;
        refStore = documentReference.collection(firebaseUser.getUid()).document();
        refStore.set(verifiedNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void validateNumber(String number, FirebaseUser firebaseUser){
        try{
            documentReference.collection(firebaseUser.getUid()).whereEqualTo("number", number).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    verifiedNumbersCallback.onCompleteSearchNumberInUser(queryDocumentSnapshots);
                }
            });
        }catch (NullPointerException e){
            verifiedNumbersCallback.onCompleteSearchNumberInUser(null);
        }
    }

    public void destroy() {
        firebaseFirestore = null;
        documentReference = null;
    }

}
