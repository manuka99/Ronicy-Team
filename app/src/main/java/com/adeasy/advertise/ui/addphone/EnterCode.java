package com.adeasy.advertise.ui.addphone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.AddNewPhoneViewModel;
import com.adeasy.advertise.callback.PhoneAuthenticationCallback;
import com.adeasy.advertise.callback.VerifiedNumbersCallback;
import com.adeasy.advertise.manager.FirebasePhoneAuthentication;
import com.adeasy.advertise.manager.VerifiedNumbersManager;
import com.adeasy.advertise.model.User;
import com.adeasy.advertise.model.UserVerifiedNumbers;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterCode#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class EnterCode extends Fragment implements View.OnClickListener, TextWatcher, PhoneAuthenticationCallback, VerifiedNumbersCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Integer phoneNumber;
    String verificationID;

    TextView phoneNumberView, phoneVerifyTextView, checkAgain;
    TextInputLayout codeEntered;
    LinearLayout phoneVerifyLayout, snackBarLayout;
    ProgressBar progressBarCodeVerify;
    FirebasePhoneAuthentication firebasePhoneAuthentication;
    FirebaseAuth firebaseAuth;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    AddNewPhoneViewModel addNewPhoneViewModel;
    VerifiedNumbersManager verifiedNumbersManager;

    public EnterCode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterCode.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterCode newInstance(String param1, String param2) {
        EnterCode fragment = new EnterCode();
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
        View view = inflater.inflate(R.layout.manuka_fragment_enter_code, container, false);

        try {
            phoneNumber = Integer.valueOf(getArguments().getString("phone"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        phoneNumberView = view.findViewById(R.id.phoneNumber);
        phoneVerifyTextView = view.findViewById(R.id.PhoneVerifyTextView);
        checkAgain = view.findViewById(R.id.checkAgain);

        codeEntered = view.findViewById(R.id.codeEntered);
        phoneVerifyLayout = view.findViewById(R.id.phoneVerifyLayout);
        progressBarCodeVerify = view.findViewById(R.id.progressBarCodeVerify);
        snackBarLayout = view.findViewById(R.id.snackbar_text);

        phoneVerifyTextView.setOnClickListener(this);
        checkAgain.setOnClickListener(this);

        codeEntered.getEditText().addTextChangedListener(this);

        phoneNumberView.setText(phoneNumber.toString());

        firebaseAuth = FirebaseAuth.getInstance();

        firebasePhoneAuthentication = new FirebasePhoneAuthentication(this);

        addNewPhoneViewModel = ViewModelProviders.of(getActivity()).get(AddNewPhoneViewModel.class);

        verifiedNumbersManager = new VerifiedNumbersManager(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        verifiedNumbersManager.validateNumber(phoneNumber, firebaseAuth.getCurrentUser());
    }

    @Override
    public void onClick(View view) {
        if (view == phoneVerifyTextView && codeEntered.getEditText().getText().length() == 6) {
            validateCodeInput();
        } else if (view == phoneVerifyTextView && codeEntered.getEditText().getText().length() != 6) {
            codeEntered.setError(getString(R.string.order_phone_subheading));
            showErrorSnackbar(R.string.order_phone_subheading);
        }
    }

    private void validateCodeInput() {
        if (verificationID != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, codeEntered.getEditText().getText().toString());
            verifyPhoneNumberWithPhoneAuthCredential(credential);
        } else
            showErrorSnackbar(R.string.verify_code_not_sent);

    }

    private void verifyPhoneNumberWithPhoneAuthCredential(PhoneAuthCredential credential) {

        showVerifyProgress();

        if (firebaseAuth.getCurrentUser() != null)
            firebasePhoneAuthentication.linkMobileWithCurrentUser(credential, firebaseAuth.getCurrentUser());

        else {
            showErrorSnackbar(R.string.not_loggedIn);
            codeEntered.setError(getString(R.string.not_loggedIn));
        }

        //signInWithPhoneAuthCredential(credential);
        //updateMobileWithCurrent(credential);

    }

    public void showVerifyProgress() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        phoneVerifyLayout.setBackgroundResource(R.color.colorGreyBtn);
        phoneVerifyTextView.setVisibility(View.GONE);
        progressBarCodeVerify.setVisibility(View.VISIBLE);
        progressBarCodeVerify.startAnimation(aniSlide);
    }

    public void endVerifyProgress() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        phoneVerifyLayout.setBackgroundResource(R.color.colorGreen);
        phoneVerifyTextView.setVisibility(View.VISIBLE);
        progressBarCodeVerify.setVisibility(View.GONE);
        phoneVerifyTextView.startAnimation(aniSlide);
    }

    public void verificationDisabled() {
        phoneVerifyLayout.setBackgroundResource(R.color.colorGreyBtn);
        phoneVerifyTextView.setVisibility(View.VISIBLE);
        progressBarCodeVerify.setVisibility(View.GONE);
    }

    public void verificationEnabled() {
        phoneVerifyLayout.setBackgroundResource(R.color.colorGreen);
        phoneVerifyTextView.setVisibility(View.VISIBLE);
        progressBarCodeVerify.setVisibility(View.GONE);
    }

    private void showErrorSnackbar(int error) {
        Snackbar snackbar = Snackbar.make(snackBarLayout, error, Snackbar.LENGTH_LONG).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    private void showSuccessSnackbar(int message) {
        Snackbar snackbar = Snackbar.make(snackBarLayout, message, Snackbar.LENGTH_LONG).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorBlue));
        snackbar.show();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (codeEntered.getEditText().getText().length() == 6)
            verificationEnabled();
        else
            verificationDisabled();
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        codeEntered.setError(null);
        if (codeEntered.getEditText().getText().length() == 6)
            verificationEnabled();
        else
            verificationDisabled();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        verificationID = verificationId;
        resendingToken = forceResendingToken;
        showSuccessSnackbar(R.string.verify_code_sent);
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        codeEntered.getEditText().setText(phoneAuthCredential.getSmsCode());
        verifyPhoneNumberWithPhoneAuthCredential(phoneAuthCredential);
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException)
            showErrorSnackbar(R.string.invalid_mobile);
        else if (e instanceof FirebaseTooManyRequestsException)
            showErrorSnackbar(R.string.quota_exceeded);
        else
            showErrorSnackbar(R.string.virtual_env_exception);
    }

    @Override
    public void onCompleteLinkingMobileWithUser(@NonNull Task<AuthResult> task) {
        endVerifyProgress();
        if (task.isSuccessful()) {
            firebasePhoneAuthentication.unlinkPhoneAuth(task.getResult().getUser());
            verifiedNumbersManager.insertVerifiedNumber(new UserVerifiedNumbers(firebaseAuth.getCurrentUser(), phoneNumber), firebaseAuth.getCurrentUser());
        } else {
            showErrorSnackbar(R.string.invalid_mobile_code);
            codeEntered.setError(getString(R.string.invalid_mobile_code));
        }
    }

    @Override
    public void onCompleteUpdateMobileWithUser(@NonNull Task<Void> task) {

    }

    @Override
    public void onCompleteSignInWithPhoneAuthCredential(@NonNull Task<AuthResult> task) {
    }

    @Override
    public void onCompleteUnlinkPhoneAuthCredential(@NonNull Task<AuthResult> task) {

    }

    @Override
    public void onCompleteDeletePhoneAuthCredentialUser(@NonNull Task<Void> task) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        verifiedNumbersManager.destroy();
        firebasePhoneAuthentication.destroy();
    }

    @Override
    public void onCompleteNumberInserted(Task<Void> task) {
        if (task != null && task.isSuccessful())
            addNewPhoneViewModel.setVerifiedNumber(phoneNumber);
    }

    @Override
    public void onCompleteSearchNumberInUser(Task<QuerySnapshot> task) {
        if (task != null && task.isSuccessful() && task.getResult().getDocuments().isEmpty() == false)
            addNewPhoneViewModel.setVerifiedNumber(phoneNumber);
        else
            firebasePhoneAuthentication.sendMobileVerifycode("+94" + phoneNumber, getActivity());
    }

    @Override
    public void onCompleteRecieveAllNumbersInUser(Task<DocumentSnapshot> task) {

    }

}