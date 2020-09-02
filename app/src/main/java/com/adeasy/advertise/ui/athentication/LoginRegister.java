package com.adeasy.advertise.ui.athentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.FirebaseAuthenticationCallback;
import com.adeasy.advertise.manager.FirebaseAuthentication;
import com.adeasy.advertise.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginRegister extends Fragment implements View.OnClickListener, FirebaseAuthenticationCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "LoginRegister";
    private static final String Post = "Please sign up to post your ad:";
    private static final String Chat = "Log in to chat with buyers and sellers on ronicy.lk";
    private static final String Account = "Log in to manage your account:";

    LinearLayout loginLayout, signUpLayout;

    //topHeader
    TextView fragmentHeader;
    String header;

    //login layout
    TextInputLayout login_email, login_password;
    TextView forgotPassword, signUpNow;

    //signup layout
    TextInputLayout signUp_name, signUp_email, signUp_password;
    TextView loginNow;

    //social login
    Button facebookLogin;

    //snackbar
    FrameLayout snackbar_text;

    //auth
    FirebaseAuth firebaseAuth;
    FirebaseAuthentication firebaseAuthentication;

    ProgressBar loginProgress, signUpProgress;
    LinearLayout loginBtnLayout, signupBtnLayout;
    TextView login, signUp;

    String name;

    public LoginRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginRegister newInstance(String param1, String param2) {
        LoginRegister fragment = new LoginRegister();
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
        View view = inflater.inflate(R.layout.manuka_fragment_login_register, container, false);

        //auth
        firebaseAuthentication = new FirebaseAuthentication(this, getActivity());
        firebaseAuth = FirebaseAuth.getInstance();

        //progress
        loginProgress = view.findViewById(R.id.progressBarLogin);
        signUpProgress = view.findViewById(R.id.progressBarsignup);

        //linear
        loginBtnLayout = view.findViewById(R.id.loginBtnLayout);
        signupBtnLayout = view.findViewById(R.id.signupBtnLayout);

        //textview
        login = view.findViewById(R.id.login);
        signUp = view.findViewById(R.id.signUp);

        //snackbar
        snackbar_text = view.findViewById(R.id.snackbar_text);

        //social login
        facebookLogin = view.findViewById(R.id.facebookLogin);

        //header
        fragmentHeader = view.findViewById(R.id.fragmentHeader);

        //loginLayout
        loginLayout = view.findViewById(R.id.loginLayout);

        login_email = view.findViewById(R.id.email);
        login_password = view.findViewById(R.id.password);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        signUpNow = view.findViewById(R.id.signUpNow);

        //SignUpLayout
        signUpLayout = view.findViewById(R.id.signUpLayout);

        signUp_name = view.findViewById(R.id.name_signUp);
        signUp_email = view.findViewById(R.id.email_signUp);
        signUp_password = view.findViewById(R.id.password_signUp);
        loginNow = view.findViewById(R.id.loginNow);

        //lissterners
        signUpNow.setOnClickListener(this);
        loginNow.setOnClickListener(this);
        signUp.setOnClickListener(this);
        login.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);

        try {
            if (getArguments().get("frame") != null) {
                header = getArguments().get("frame").toString();
                changeHeader();
            }
        } catch (NullPointerException e) {
            Log.i(TAG, "No arguments sent");
        }

        //display Login layout
        showLogin();

        return view;
    }

    private void changeHeader() {
        if (header.equals("account"))
            fragmentHeader.setText(Account);
        else if (header.equals("chat"))
            fragmentHeader.setText(Chat);
        else if (header.equals("post"))
            fragmentHeader.setText(Post);
    }

    @Override
    public void onClick(View view) {
        if (view == signUpNow)
            showSignup();
        else if (view == loginNow)
            showLogin();
        else if (view == login)
            validateLogin();
        else if (view == signUp)
            validateSignUp();
        else if (view == forgotPassword)
            forgotPassword();
        else if (view == facebookLogin)
            startSocialLogin();
    }

    private void showLogin() {
        loginLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.GONE);
    }

    private void showSignup() {
        loginLayout.setVisibility(View.GONE);
        signUpLayout.setVisibility(View.VISIBLE);
    }


    private void validateLogin() {
        if (login_email.getEditText().getText().length() == 0)
            showErrorSnackbar(R.string.invalidEmail);
        else if (login_password.getEditText().getText().length() == 0)
            showErrorSnackbar(R.string.invalidPasswordLogin);
        else {
            showSigninUi();
            firebaseAuthentication.signInWithEmailAndPassword(login_email.getEditText().getText().toString(), login_password.getEditText().getText().toString());
        }
    }

    private void validateSignUp() {
        if (signUp_name.getEditText().getText().length() == 0)
            showErrorSnackbar(R.string.invalidEmail);
        else if (signUp_email.getEditText().getText().length() == 0)
            showErrorSnackbar(R.string.invalidEmail);
        else if (signUp_password.getEditText().getText().length() == 0)
            showErrorSnackbar(R.string.invalidPasswordLogin);
        else {
            showSignupUi();
            name = signUp_name.getEditText().getText().toString();
            firebaseAuthentication.createAccount(signUp_email.getEditText().getText().toString(), signUp_password.getEditText().getText().toString());
        }
    }

    private void forgotPassword() {

    }

    private void startSocialLogin() {

    }

    private void showErrorSnackbar(int error) {
        Snackbar snackbar = Snackbar.make(snackbar_text, error, Snackbar.LENGTH_LONG).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorError2));
        snackbar.show();
    }

    @Override
    public void onCompleteSignIn(@NonNull Task<AuthResult> task) {
        endSigninUi();
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                openNewActivity();
            } else
                showErrorSnackbar(R.string.invalidLogin);
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "signInWithEmail:failure", task.getException());
            showErrorSnackbar(R.string.invalidLogin);
            // ...
        }

    }

    @Override
    public void onCompleteCreateAccount(@NonNull Task<AuthResult> task) {
        endSignupUi();
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "createUserWithEmail:success");
            FirebaseUser user = firebaseAuth.getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                            }
                        }
                    });
            openNewActivity();
        } else {
            // If sign in fails, display a message to the user.
            Log.w(TAG, "createUserWithEmail:failure", task.getException());
            showErrorSnackbar(R.string.invalidRegister);
        }
    }

    private void openNewActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finishAffinity();
        getActivity().finish();
    }

    private void showSigninUi() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        loginBtnLayout.setBackgroundResource(R.color.colorGreyBtn);
        login.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        loginProgress.startAnimation(aniSlide);
    }

    private void endSigninUi() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        loginBtnLayout.setBackgroundResource(R.color.colorGreen);
        login.setVisibility(View.VISIBLE);
        loginProgress.setVisibility(View.GONE);
        login.startAnimation(aniSlide);
    }

    private void showSignupUi() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        signupBtnLayout.setBackgroundResource(R.color.colorGreyBtn);
        signUp.setVisibility(View.GONE);
        signUpProgress.setVisibility(View.VISIBLE);
        signUpProgress.startAnimation(aniSlide);
    }

    private void endSignupUi() {
        Animation aniSlide = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        signupBtnLayout.setBackgroundResource(R.color.colorGreen);
        signUp.setVisibility(View.VISIBLE);
        signUpProgress.setVisibility(View.GONE);
        signUpProgress.startAnimation(aniSlide);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //firebaseAuthentication.destroy();
    }

}