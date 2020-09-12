package com.adeasy.advertise.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.ProfileManagerCallback;
import com.adeasy.advertise.manager.ProfileManager;
import com.adeasy.advertise.model.User;
import com.adeasy.advertise.util.CustomDialogs;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import static com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class UserDetails extends Fragment implements ProfileManagerCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private FirebaseUser firebaseUser;

    TextView name1, phone;
    TextInputLayout name2, phone2, email, gender, dateOfBirth, address;
    ImageView proficePic;
    FrameLayout snackbarLayout;

    LinearLayout userDetailsLayout;

    FirebaseAuth firebaseAuth;
    ProfileManager profileManager;
    CustomDialogs customErrorDialogs;

    public UserDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDetails newInstance(String param1, String param2) {
        UserDetails fragment = new UserDetails();
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
        View view = inflater.inflate(R.layout.manuka_fragment_user_details, container, false);

        name1 = view.findViewById(R.id.name);
        name2 = view.findViewById(R.id.name2);
        phone = view.findViewById(R.id.phone);
        phone2 = view.findViewById(R.id.phone2);
        email = view.findViewById(R.id.email);
        gender = view.findViewById(R.id.gender);
        dateOfBirth = view.findViewById(R.id.dateOfBirth);
        address = view.findViewById(R.id.address);
        proficePic = view.findViewById(R.id.profilePhoto);
        snackbarLayout = view.findViewById(R.id.snackView);

        userDetailsLayout = view.findViewById(R.id.userDetailsLayout);

        firebaseAuth = FirebaseAuth.getInstance();

        profileManager = new ProfileManager(this);
        customErrorDialogs = new CustomDialogs(getActivity());
        profileManager.getUser();

        userDetailsLayout.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onSuccessUpdateProfile(Void aVoid) {

    }

    @Override
    public void onFailureUpdateProfile(Exception e) {

    }

    @Override
    public void onCompleteUpdatePassword(Task<Void> task) {

    }

    @Override
    public void onCompleteUpdateEmail(Task<Void> task) {

    }

    @Override
    public void onTaskFull(boolean status) {

    }

    @Override
    public void onCompleteGetUser(Task<DocumentSnapshot> task) {
        if (task != null && task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
            user = task.getResult().toObject(User.class);
            updateUiOnDataRecieve();
        } else if (task != null) {
            if (task.getException() instanceof FirebaseFirestoreException) {
                ((FirebaseFirestoreException) task.getException()).getCode().equals(PERMISSION_DENIED);
                customErrorDialogs.showPermissionDeniedStorage();
            };
        }
    }

    public void updateUiOnDataRecieve() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (user != null) {

            if (user.getName() != null && user.getName().length() > 0) {
                name1.setText(user.getName());
                name2.getEditText().setText(user.getName());
            } else {
                name1.setVisibility(View.GONE);
                name2.setVisibility(View.GONE);
            }

            if (user.getPhone() != null && user.getPhone().length() > 0) {
                phone.setText(user.getPhone());
                phone2.getEditText().setText(user.getPhone());
            } else {
                phone.setVisibility(View.GONE);
                phone2.setVisibility(View.GONE);
            }

            if (user.getEmail() != null && user.getEmail().length() > 0)
                email.getEditText().setText(user.getEmail());
            else
                email.setVisibility(View.GONE);

            if (user.getGender() != null && user.getGender().length() > 0)
                gender.getEditText().setText(user.getGender());
            else
                gender.setVisibility(View.GONE);

            if (user.getDateOfBirth() != null && user.getDateOfBirth().length() > 0)
                dateOfBirth.getEditText().setText(user.getDateOfBirth());
            else
                dateOfBirth.setVisibility(View.GONE);

            if (user.getAddress() != null && user.getAddress().length() > 0)
                address.getEditText().setText(user.getAddress());
            else
                address.setVisibility(View.GONE);

        } else {
            name1.setText(firebaseUser.getDisplayName());
            name2.getEditText().setText(firebaseUser.getDisplayName());
            email.getEditText().setText(firebaseUser.getEmail());

            if (firebaseUser.getPhoneNumber() != null && firebaseUser.getPhoneNumber().length() > 0) {
                phone.setText(firebaseUser.getPhoneNumber());
                phone2.getEditText().setText(firebaseUser.getPhoneNumber());
            } else {
                phone.setVisibility(View.GONE);
                phone2.setVisibility(View.GONE);
            }

            gender.setVisibility(View.GONE);
            dateOfBirth.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
        }

        if (firebaseUser.getPhotoUrl() != null)
            Picasso.get().load(firebaseUser.getPhotoUrl()).fit().centerInside().into(proficePic);

        userDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void showErrorSnackbar(String error) {
        Snackbar snackbar = Snackbar.make(snackbarLayout, error, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
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

}