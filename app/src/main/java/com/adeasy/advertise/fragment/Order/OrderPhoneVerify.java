package com.adeasy.advertise.fragment.Order;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.activity.advertisement.BuyNow;
import com.adeasy.advertise.callback.PhoneAuthenticationCallback;
import com.adeasy.advertise.manager.FirebasePhoneAuthentication;
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
public class OrderPhoneVerify extends Fragment implements View.OnClickListener, PhoneAuthenticationCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "OrderPhoneVerify";

    private boolean mVerificationInProgress = false;
    private String verificationID;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private BuynowViewModel buynowViewModel;

    //String phoneNum = "+16505554567";
    //String phoneNum = "+940721146092";
    //String phoneNum = "+94714163881";
    //String phoneNum = "+94775259715";
    //String phoneNum = "+94788445729";
    String phoneNum = "";
    String verificationCodeInput = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebasePhoneAuthentication firebasePhoneAuthentication;
    EditText codeInput;
    TextView codeMessage, newcode, order_phone_number;
    ProgressBar progressBar;

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
        firebasePhoneAuthentication = new FirebasePhoneAuthentication(this);
        phoneNum = "+94" + getArguments().getString("phone");

        order_phone_number = view.findViewById(R.id.order_phone_number);
        codeInput = view.findViewById(R.id.orderCodeVerify);
        newcode = view.findViewById(R.id.resendPinCodeOrder);
        codeMessage = view.findViewById(R.id.codeFailed);
        progressBar = view.findViewById(R.id.progressVerifyNumberOrder);

        newcode.setOnClickListener(this);

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
        order_phone_number.setText(phoneNum);
        buynowViewModel.getStartVerifyMobileNumber().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                    if (aBoolean) {
                        codeMessage.setTextColor(getResources().getColor(R.color.colorGreen));
                        codeMessage.setText("Validating your code...");
                        progressBar.setVisibility(View.VISIBLE);
                        validatePhonenumber();
                    }
                }
            }
        });

        firebasePhoneAuthentication.sendMobileVerifycode(phoneNum, getActivity());
    }


    private void validatePhonenumber() {

        verificationCodeInput = codeInput.getText().toString();

        if (verificationID.isEmpty())
            codeInput.setError("Verification code was not sent. Please check your credentials");

        else if (verificationCodeInput.isEmpty())
            codeInput.setError("Please enter the verification code");

        else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, verificationCodeInput);
            verifyPhoneNumberWithPhoneAuthCredential(credential);
        }

    }

    private void verifyPhoneNumberWithPhoneAuthCredential(PhoneAuthCredential credential) {

        if (mAuth.getCurrentUser() != null)
            firebasePhoneAuthentication.linkMobileWithCurrentUser(credential, mAuth.getCurrentUser());

        else
            firebasePhoneAuthentication.signInWithPhoneAuthCredential(credential, mAuth);
        //signInWithPhoneAuthCredential(credential);
        //updateMobileWithCurrent(credential);

    }

    @Override
    public void onClick(View view) {
        if (view == newcode && mResendToken != null) {
            codeMessage.setVisibility(View.VISIBLE);
            codeMessage.setTextColor(getResources().getColor(R.color.colorGreen));
            codeMessage.setText(R.string.sending_code);
            progressBar.setVisibility(View.VISIBLE);
            firebasePhoneAuthentication.resendVerificationCode(phoneNum, getActivity(), mResendToken);
        }
    }

    @Override
    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        progressBar.setVisibility(View.GONE);
        codeMessage.setVisibility(View.GONE);
        verificationID = verificationId;
        mResendToken = forceResendingToken;
        Toast.makeText(getContext(), "Verification code sent sucessfully", Toast.LENGTH_LONG).show();
        // The corresponding whitelisted code above should be used to complete sign-in.
        //MainActivity.this.enableUserManuallyInputCode();
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        progressBar.setVisibility(View.VISIBLE);
        codeMessage.setVisibility(View.VISIBLE);
        codeMessage.setTextColor(getResources().getColor(R.color.colorGreen));
        codeMessage.setText("Validating your code...");
        codeInput.setText(phoneAuthCredential.getSmsCode());
        OrderPhoneVerify.this.verifyPhoneNumberWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        progressBar.setVisibility(View.GONE);
        codeMessage.setTextColor(getResources().getColor(R.color.colorError));
        codeMessage.setVisibility(View.GONE);

        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            codeMessage.setText(R.string.invalid_mobile);
        } else if (e instanceof FirebaseTooManyRequestsException) {
            codeMessage.setText(R.string.quota_exceeded);
        } else
            codeMessage.setText(R.string.virtual_env_exception);


    }

    @Override
    public void onCompleteLinkingMobileWithUser(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "linkWithCredential: success");
            //FirebaseUser user = task.getResult().getUser();
            buynowViewModel.setMobileNumberVerifyStatus(true);
            firebasePhoneAuthentication.unlinkPhoneAuth(mAuth.getCurrentUser());
        } else {
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
                // [START_EXCLUDE silent]
                //mBinding.fieldVerificationCode.setError("Invalid code.");
                // [END_EXCLUDE]
                progressBar.setVisibility(View.GONE);
                codeMessage.setTextColor(getResources().getColor(R.color.colorError));
                codeMessage.setVisibility(View.VISIBLE);
                codeMessage.setText(R.string.invalid_mobile_code);
            }
            Log.w(TAG, "linkWithCredential:failure", task.getException());

        }
    }

    @Override
    public void onCompleteUpdateMobileWithUser(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "linkWithCredential: success");
            firebasePhoneAuthentication.unlinkPhoneAuth(mAuth.getCurrentUser());
        } else {
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
                // [START_EXCLUDE silent]
                //mBinding.fieldVerificationCode.setError("Invalid code.");
                // [END_EXCLUDE]
                progressBar.setVisibility(View.GONE);
                codeMessage.setTextColor(getResources().getColor(R.color.colorError));
                codeMessage.setVisibility(View.VISIBLE);
                codeMessage.setText(R.string.invalid_mobile_code);
            }
            Log.w(TAG, "linkWithCredential:failure", task.getException());

        }
    }

    @Override
    public void onCompleteSignInWithPhoneAuthCredential(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {

            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            buynowViewModel.setMobileNumberVerifyStatus(true);
            firebasePhoneAuthentication.deletePhoneAuthAccout(task.getResult().getUser());

        } else {

            // Sign in failed, display a message and update the UI
            Log.w(TAG, "signInWithCredential:failure", task.getException());
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
                // [START_EXCLUDE silent]
                //mBinding.fieldVerificationCode.setError("Invalid code.");
                // [END_EXCLUDE]
                progressBar.setVisibility(View.GONE);
                codeMessage.setTextColor(getResources().getColor(R.color.colorError));
                codeMessage.setVisibility(View.VISIBLE);
                codeMessage.setText(R.string.invalid_mobile_code);
            }
            // [START_EXCLUDE silent]
            // Update UI
            //updateUI(STATE_SIGNIN_FAILED);
            // [END_EXCLUDE]

        }
    }

    @Override
    public void onCompleteUnlinkPhoneAuthCredential(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            // Auth provider unlinked from account
            // ...
        }
    }

    @Override
    public void onCompleteDeletePhoneAuthCredentialUser(@NonNull Task<Void> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "User account deleted.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebasePhoneAuthentication.destroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}