package com.adeasy.advertise.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.CustomClaimsCallback;
import com.adeasy.advertise.callback.FacebookAuthCallback;
import com.adeasy.advertise.manager.CustomAuthTokenManager;
import com.adeasy.advertise.manager.FacebookAuthManager;
import com.adeasy.advertise.ui.administration.home.DashboardHome;
import com.adeasy.advertise.ui.advertisement.Donations;
import com.adeasy.advertise.ui.advertisement.Myadds;
import com.adeasy.advertise.ui.athentication.LoginRegister;
import com.adeasy.advertise.ui.favaourite.divya_MActivity;
import com.adeasy.advertise.ui.getintouch.GetInTouchActivity;
import com.adeasy.advertise.ui.profile.Profile;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class Account extends Fragment implements View.OnClickListener, FacebookAuthCallback, CustomClaimsCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Account";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    TextView username;
    Button myads, favaourite, membership, profile, faq, donateUs, logout, linkfb;
    FrameLayout noAuthFragment, snackView;
    LinearLayout authContent;
    LoginRegister loginRegister;
    CallbackManager callbackManager;
    FacebookAuthManager facebookAuthManager;
    LoginManager loginManager;
    Boolean isFbLinked = false, isAdministrator = false;
    CustomAuthTokenManager customAuthTokenManager;

    Toolbar toolbar;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.account);

        mAuth = FirebaseAuth.getInstance();

        noAuthFragment = view.findViewById(R.id.noAuthFragment);
        authContent = view.findViewById(R.id.authContent);

        username = view.findViewById(R.id.userName);
        myads = view.findViewById(R.id.myads);
        membership = view.findViewById(R.id.accountMember);
        favaourite = view.findViewById(R.id.favourite);
        profile = view.findViewById(R.id.myprofile);
        faq = view.findViewById(R.id.faq);
        donateUs = view.findViewById(R.id.donateUs);
        logout = view.findViewById(R.id.logout);
        linkfb = view.findViewById(R.id.linkfb);
        snackView = view.findViewById(R.id.snackView);

        loginRegister = new LoginRegister();

        //listeners
        myads.setOnClickListener(this);
        donateUs.setOnClickListener(this);
        logout.setOnClickListener(this);
        myads.setOnClickListener(this);
        faq.setOnClickListener(this);
        favaourite.setOnClickListener(this);
        linkfb.setOnClickListener(this);
        profile.setOnClickListener(this);

        facebookAuthManager = new FacebookAuthManager(this);
        customAuthTokenManager = new CustomAuthTokenManager(this);

        if (mAuth.getCurrentUser() != null)
            showAuthContent();
        else
            showNoAuthContent();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (isAdministrator)
            inflater.inflate(R.menu.admin, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_admin) {
            startActivity(new Intent(getActivity(), DashboardHome.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                facebookAuthManager.linkFacebookToUser(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == myads)
            startActivity(new Intent(getContext(), Myadds.class));
        else if (view == donateUs)
            startActivity(new Intent(getContext(), Donations.class));
        else if (view == logout)
            logoutDialog();
        else if (view == faq)
            startActivity(new Intent(getContext(), GetInTouchActivity.class));
        else if (view == favaourite)
            startActivity(new Intent(getContext(), divya_MActivity.class));
        else if (view == linkfb)
            linkWithFb();
        else if (view == profile)
            startActivity(new Intent(getContext(), Profile.class));
    }

    private void showNoAuthContent() {
        logout.setVisibility(View.GONE);
        authContent.setVisibility(View.GONE);
        noAuthFragment.setVisibility(View.VISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("frame", "account");
        loginRegister.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(noAuthFragment.getId(), loginRegister);
        fragmentTransaction.commit();
    }

    private void showAuthContent() {
        authContent.setVisibility(View.VISIBLE);
        noAuthFragment.setVisibility(View.GONE);
        logout.setVisibility(View.VISIBLE);
        username.setText(mAuth.getCurrentUser().getDisplayName());
        updateFacebookLinkState();
        customAuthTokenManager.getCustomClaimsInLoggedinUser();
    }

    private void linkWithFb() {
        updateFacebookLinkState();
        if (isFbLinked)
            displayDialogUnlinkFb();
        else {
            loginManager.logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    private void displayDialogUnlinkFb() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())

                .setMessage("Are you sure you want to unlink from facebook?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        facebookAuthManager.unlinkFromFBProvider();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                .show();
    }

    public void updateFacebookLinkState() {
        if (mAuth.getCurrentUser() != null) {
            isFbLinked = false;
            List<? extends UserInfo> providerData = mAuth.getCurrentUser().getProviderData();
            for (UserInfo userInfo : providerData) {
                if (userInfo.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)) {
                    linkfb.setText("Connected as " + userInfo.getDisplayName());
                    isFbLinked = true;
                }
            }
            if (isFbLinked == false)
                linkfb.setText(getString(R.string.linkFb));
        }
    }

    @Override
    public void onCompleteSignInWithFacebook(@NonNull Task<AuthResult> task) {

    }

    @Override
    public void onCompleteLinkInWithFacebook(@NonNull Task<AuthResult> task) {
        updateFacebookLinkState();
        if (task.isSuccessful()) {
            showSuccessSnackbar(getString(R.string.linkFbSuccess));
        } else
            showErrorSnackbar(task.getException().getMessage());
        //showErrorSnackbar(getString(R.string.linkFbError));
    }

    @Override
    public void onCompleteUnlinkFacebookAuthCredential(@NonNull Task<AuthResult> task) {
        updateFacebookLinkState();
        if (task.isSuccessful()) {
            showSuccessSnackbar(getString(R.string.linkFbRemoveSuccess));
        } else
            showErrorSnackbar(getString(R.string.linkFbRemoveError));
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
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void logoutDialog() {
        new AlertDialog.Builder(getActivity())

                .setMessage("Are you sure you want to log out?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mAuth.getCurrentUser() != null)
                            mAuth.signOut();
                        openNewActivity();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })

                .show();
    }

    private void openNewActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finishAffinity();
        getActivity().finish();
    }

    @Override
    public void onCompleteGetCustomClaims(@NonNull Task<GetTokenResult> task) {
        if (task.isSuccessful()) {
            try {
                if ((Boolean) task.getResult().getClaims().get("admin") || (Boolean) task.getResult().getClaims().get("advertisement_manager") || (Boolean) task.getResult().getClaims().get("favourite_manager") || (Boolean) task.getResult().getClaims().get("chat_manager") || (Boolean) task.getResult().getClaims().get("contact_manager") || (Boolean) task.getResult().getClaims().get("order_manager")) {
                    isAdministrator = true;
                    Log.i(TAG, task.getResult().getClaims().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                isAdministrator = false;
            }
        } else {
            isAdministrator = false;
        }

        getActivity().invalidateOptionsMenu();
    }

}