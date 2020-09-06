package com.adeasy.advertise.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.ProfileManagerCallback;
import com.adeasy.advertise.manager.ProfileManager;
import com.adeasy.advertise.util.HideSoftKeyboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class ProfileSecurity extends Fragment implements TextWatcher, View.OnClickListener, ProfileManagerCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProfileSecurity";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextInputLayout oldPass, newPass, repeatPass;
    Button changePassword;
    FrameLayout snackView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String oldPassString, newPassString;
    Boolean isUpdating = false;
    ProfileManager profileManager;
    ProgressBar progressBar;

    public ProfileSecurity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileSecurity.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSecurity newInstance(String param1, String param2) {
        ProfileSecurity fragment = new ProfileSecurity();
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
        View view = inflater.inflate(R.layout.manuka_fragment_profile_security, container, false);

        oldPass = view.findViewById(R.id.currentPassword);
        newPass = view.findViewById(R.id.newPassword);
        repeatPass = view.findViewById(R.id.repeatPassword);

        progressBar = view.findViewById(R.id.progressBar);

        changePassword = view.findViewById(R.id.changePassword);

        snackView = view.findViewById(R.id.snackView);

        //auth
        firebaseAuth = FirebaseAuth.getInstance();

        //watchers
        oldPass.getEditText().addTextChangedListener(this);
        newPass.getEditText().addTextChangedListener(this);
        repeatPass.getEditText().addTextChangedListener(this);

        //listeners
        changePassword.setOnClickListener(this);

        profileManager = new ProfileManager(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() == null)
            getActivity().onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        oldPass.setError(null);
        newPass.setError(null);
        repeatPass.setError(null);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        if (view == changePassword)
            validatePassword();
    }

    public void validatePassword() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {

            HideSoftKeyboard.hideKeyboard(requireActivity());

            if (oldPass.getEditText().getText().length() == 0)
                oldPass.setError("Please enter your current password");

            else if (newPass.getEditText().getText().length() == 0)
                newPass.setError("Please enter your new password");

            else if (newPass.getEditText().getText().length() < 10)
                newPass.setError("Please make sure that your password is strong enough");

            else if (repeatPass.getEditText().getText().length() == 0)
                repeatPass.setError("Please re-enter your new password");

            else if (!repeatPass.getEditText().getText().toString().equals(newPass.getEditText().getText().toString()))
                repeatPass.setError("New password and repeat password does not match");

            else if (!isUpdating) {
                oldPassString = oldPass.getEditText().getText().toString();
                newPassString = newPass.getEditText().getText().toString();
                isUpdating = true;
                updatingUi();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(firebaseUser.getEmail(), oldPassString);
                profileManager.reathenticateWithCredentials(credential, newPassString);
            } else
                Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_LONG).show();

        }

    }

    private void showErrorSnackbar(String error) {
        Snackbar snackbar = Snackbar.make(snackView, error, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    private void showSuccessSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(snackView, message, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    private void updatingUi(){
        changePassword.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void endUpdatingUi(){
        progressBar.setVisibility(View.GONE);
        changePassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessUpdateProfile(Void aVoid) {

    }

    @Override
    public void onFailureUpdateProfile(Exception e) {

    }

    @Override
    public void onCompleteUpdatePassword(Task<Void> task) {
        HideSoftKeyboard.hideKeyboard(requireActivity());
        if (task.isSuccessful()) {
            showSuccessSnackbar("Your password was updated successfully");
            oldPass.getEditText().setText(null);
            newPass.getEditText().setText(null);
            repeatPass.getEditText().setText(null);
        } else {
            showErrorSnackbar(task.getException().getMessage());
        }

        isUpdating = false;
        endUpdatingUi();
    }

    @Override
    public void onCompleteUpdateEmail(Task<Void> task) {

    }

    @Override
    public void onTaskFull(boolean status) {

    }

    @Override
    public void onSuccessGetUser(DocumentSnapshot documentSnapshot) {

    }

}