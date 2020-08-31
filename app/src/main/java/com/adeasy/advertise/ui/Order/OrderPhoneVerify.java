package com.adeasy.advertise.ui.Order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.BuynowViewModel;
import com.adeasy.advertise.callback.PhoneAuthenticationCallback;
import com.adeasy.advertise.manager.FirebasePhoneAuthentication;
import com.adeasy.advertise.manager.VerifiedNumbersManager;
import com.adeasy.advertise.model.VerifiedNumber;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderPhoneVerify#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderPhoneVerify extends Fragment implements View.OnClickListener, PhoneAuthenticationCallback, TextWatcher {

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

    String phoneNum = "+16505554567";
    //String phoneNum = "+940721146092";
    //String phoneNum = "+94714163881";
    //String phoneNum = "+94775259715";
    //String phoneNum = "+94788445729";
    //String phoneNum = "";

    String verificationCodeInput = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebasePhoneAuthentication firebasePhoneAuthentication;
    EditText codeInput;
    TextView codeMessage, newcode, order_phone_number, verifyTextView;
    LinearLayout verifyBtn;
    LinearLayout linearLayout;
    ProgressBar progressBarVerifyBtn;
    VerifiedNumbersManager verifiedNumbersManager;

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
        phoneNum = getArguments().getString("phone");

        verifiedNumbersManager = new VerifiedNumbersManager();

        order_phone_number = view.findViewById(R.id.order_phone_number);
        codeInput = view.findViewById(R.id.orderCodeVerify);
        newcode = view.findViewById(R.id.resendPinCodeOrder);
        codeMessage = view.findViewById(R.id.mobileCodeMessage);
        linearLayout = view.findViewById(R.id.phoneVerifyLinearLayout);
        verifyBtn = view.findViewById(R.id.phoneVerifyBtn);
        verifyTextView = view.findViewById(R.id.PhoneVerifyTextView);
        progressBarVerifyBtn = view.findViewById(R.id.progressBarCodeVerify);

        newcode.setOnClickListener(this);
        verifyTextView.setOnClickListener(this);
        codeInput.addTextChangedListener(this);

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
        firebasePhoneAuthentication.sendMobileVerifycode("+94" + phoneNum, getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();
        buynowViewModel.setVisibilityContinue(false);
        verifyBtn.setBackgroundResource(R.color.colorGreyBtn);
        codeInput.setText(null);
    }

    @Override
    public void onClick(View view) {
        if (view == newcode && mResendToken != null) {
            showSuccessSnackbar(String.valueOf(R.string.sending_code));
            firebasePhoneAuthentication.resendVerificationCode("+94" + phoneNum, getActivity(), mResendToken);
        } else if (view == verifyTextView) {
            validatePhonenumber();
        }
    }


    private void validatePhonenumber() {

        verificationCodeInput = codeInput.getText().toString();

        if (codeInput.getText().length() != 6)
            showErrorSnackbar("Please enter the verification code");
        else if (verificationID == null)
            showErrorSnackbar("Verification code was not sent. Please check your credentials");
        else {
            startVerificationDisplay();
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
    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        verificationID = verificationId;
        mResendToken = forceResendingToken;
        showSuccessSnackbar("Verification code sent sucessfully");
        // The corresponding whitelisted code above should be used to complete sign-in.
        //MainActivity.this.enableUserManuallyInputCode();
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        startVerificationDisplay();
        codeInput.setText(phoneAuthCredential.getSmsCode());
        verifyPhoneNumberWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {

        if (e instanceof FirebaseAuthInvalidCredentialsException)
            showErrorSnackbar(getString(R.string.invalid_mobile));
        else if (e instanceof FirebaseTooManyRequestsException)
            showErrorSnackbar(getString(R.string.quota_exceeded));
        else
            showErrorSnackbar(getString(R.string.virtual_env_exception));


    }

    @Override
    public void onCompleteLinkingMobileWithUser(@NonNull Task<AuthResult> task) {

        endVerificationDisplay();

        if (task.isSuccessful()) {
            Log.d(TAG, "linkWithCredential: success");
            //FirebaseUser user = task.getResult().getUser();
            buynowViewModel.setMobileNumberVerifyStatus(true);
            firebasePhoneAuthentication.unlinkPhoneAuth(task.getResult().getUser());
            verifiedNumbersManager.insertVerifiedNumber(new VerifiedNumber(phoneNum), task.getResult().getUser());
        } else {
            showErrorSnackbar(String.valueOf(R.string.invalid_mobile_code));
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
                // [START_EXCLUDE silent]
                //mBinding.fieldVerificationCode.setError("Invalid code.");
                // [END_EXCLUDE]
            }
            Log.w(TAG, "linkWithCredential:failure", task.getException());

        }
    }

    @Override
    public void onCompleteUpdateMobileWithUser(@NonNull Task<Void> task) {

        endVerificationDisplay();

        if (task.isSuccessful()) {
            Log.d(TAG, "linkWithCredential: success");
            firebasePhoneAuthentication.unlinkPhoneAuth(mAuth.getCurrentUser());
        } else {
            showErrorSnackbar(String.valueOf(R.string.invalid_mobile_code));
            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                // The verification code entered was invalid
                // [START_EXCLUDE silent]
                //mBinding.fieldVerificationCode.setError("Invalid code.");
                // [END_EXCLUDE]
            }
            Log.w(TAG, "linkWithCredential:failure", task.getException());

        }
    }

    @Override
    public void onCompleteSignInWithPhoneAuthCredential(@NonNull Task<AuthResult> task) {

        endVerificationDisplay();

        if (task.isSuccessful()) {

            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithCredential:success");
            buynowViewModel.setMobileNumberVerifyStatus(true);
            firebasePhoneAuthentication.deletePhoneAuthAccout(task.getResult().getUser());

        } else {
            showErrorSnackbar(getString(R.string.invalid_mobile_code));
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

    private void showErrorSnackbar(String error) {
        Snackbar snackbar = Snackbar.make(linearLayout, error, Snackbar.LENGTH_LONG).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    private void showSuccessSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_LONG).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorBlue));
        snackbar.show();
    }


    private void startVerificationDisplay() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        verifyTextView.setVisibility(View.GONE);
        verifyBtn.setBackgroundColor(getResources().getColor(R.color.colorGreyBtn));
        progressBarVerifyBtn.setVisibility(View.VISIBLE);
        progressBarVerifyBtn.startAnimation(aniSlide);
    }

    private void endVerificationDisplay() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        progressBarVerifyBtn.setVisibility(View.GONE);
        verifyBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        verifyTextView.setVisibility(View.VISIBLE);
        verifyTextView.startAnimation(aniSlide);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (codeInput == null)
            verifyBtn.setBackgroundResource(R.color.colorGreyBtn);

        else if (codeInput.getText().length() != 6)
            verifyBtn.setBackgroundResource(R.color.colorGreyBtn);

        else
            verifyBtn.setBackgroundResource(R.color.colorGreen);

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}