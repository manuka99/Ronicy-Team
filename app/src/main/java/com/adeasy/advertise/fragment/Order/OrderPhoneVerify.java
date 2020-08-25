package com.adeasy.advertise.fragment.Order;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.activity.advertisement.BuyNow;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderPhoneVerify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderPhoneVerify extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OrderPhoneVerify";

    private boolean mVerificationInProgress = false;
    private String verificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private FirebaseAuth mAuth;
    private BuynowViewModel buynowViewModel;

    String phoneNum = "+16505554567";
    //String phoneNum = "+94721146092";
    //String phoneNum = "+94714163881";
    //String phoneNum = "+94775259715";
    //String phoneNum = "+94788445729";
    String verificationCodeInput = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText codeInput;
    TextView cancel, newcode;

    public OrderPhoneVerify() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderPhoneVerify.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderPhoneVerify newInstance(String param1, String param2) {
        OrderPhoneVerify fragment = new OrderPhoneVerify();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.manuka_fragment_order_phone_verify, container, false);

        codeInput = view.findViewById(R.id.orderCodeVerify);
        newcode = view.findViewById(R.id.resendPinCodeOrder);
        cancel = view.findViewById(R.id.pinVerifyCancel);

        newcode.setOnClickListener(this);
        cancel.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        buynowViewModel = ViewModelProviders.of(getActivity()).get(BuynowViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buynowViewModel.getStartVerifyMobileNumber().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    if (aBoolean)
                        validatePhonenumber();
                }
            }
        });

        sendMobileVerifycode();
    }

    private void sendMobileVerifycode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum, 30L /*timeout*/, TimeUnit.SECONDS,
                getActivity(), new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        verificationID = verificationId;
                        Toast.makeText(getContext(), "sent", Toast.LENGTH_LONG).show();
                        // The corresponding whitelisted code above should be used to complete sign-in.
                        //MainActivity.this.enableUserManuallyInputCode();
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Sign in with the credential
                        // ...
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // ...
                        // Save the verification id somewhere
                        // ...
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            // [START_EXCLUDE]
                            Toast.makeText(getContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                            // [END_EXCLUDE]
                        }

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                });
    }

    private void validatePhonenumber() {

        verificationCodeInput = codeInput.getText().toString();

        if (verificationCodeInput.isEmpty())
            codeInput.setError("Please enter the verification code");

        else
            verifyPhoneNumberWithCode(verificationID, verificationCodeInput);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        try {
            // [START verify_with_code]
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            if (mAuth.getCurrentUser() != null)
                linkMobileWithCurrent(credential);

            else
                signInWithPhoneAuthCredential(credential);
            //signInWithPhoneAuthCredential(credential);
            //updateMobileWithCurrent(credential);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), "Verification Code is wrong " + e.getMessage(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            e.printStackTrace();
        }

    }

    private void linkMobileWithCurrent(PhoneAuthCredential credential) {

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential: success");
                            //FirebaseUser user = task.getResult().getUser();
                            buynowViewModel.setMobileNumberVerifyStatus(true);
                            unlinkPhoneAuth();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mBinding.fieldVerificationCode.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            Log.w(TAG, "linkWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });

    }

    private void updateMobileWithCurrent(PhoneAuthCredential credential) {

        mAuth.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "linkWithCredential: success");
                    unlinkPhoneAuth();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
                        //mBinding.fieldVerificationCode.setError("Invalid code.");
                        // [END_EXCLUDE]
                    }
                    Log.w(TAG, "linkWithCredential:failure", task.getException());

                }
            }

        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        try {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                buynowViewModel.setMobileNumberVerifyStatus(true);
                                deletePhoneAuthAccout(task.getResult().getUser());

                                // [START_EXCLUDE]
                                //updateUI(STATE_SIGNIN_SUCCESS, user);
                                // [END_EXCLUDE]

                            } else {

                                // Sign in failed, display a message and update the UI
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                    // [START_EXCLUDE silent]
                                    //mBinding.fieldVerificationCode.setError("Invalid code.");
                                    // [END_EXCLUDE]
                                }
                                // [START_EXCLUDE silent]
                                // Update UI
                                //updateUI(STATE_SIGNIN_FAILED);
                                // [END_EXCLUDE]

                            }
                        } catch (Exception e) {
                            Toast toast = Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
                        }


                    }
                });
    }
    // [END sign_in_with_phone]

    public void unlinkPhoneAuth() {

        mAuth.getCurrentUser().unlink(PhoneAuthProvider.PROVIDER_ID)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Auth provider unlinked from account
                            // ...
                        }
                    }
                });

    }

    private void deletePhoneAuthAccout(FirebaseUser user) {
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }
}