package com.adeasy.advertise.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.adeasy.advertise.R;
import com.adeasy.advertise.callback.ProfileManagerCallback;
import com.adeasy.advertise.manager.ProfileManager;
import com.adeasy.advertise.model.User;
import com.adeasy.advertise.util.HideSoftKeyboard;
import com.adeasy.advertise.util.ImageQualityReducer;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class EditProfile extends Fragment implements ProfileManagerCallback, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "EditProfile";
    private static final int IMAGE_SELECTOR = 4554;

    // TODO: Rename and change types of parameters

    private String mParam1;
    private String mParam2;

    LinearLayout layoutEditDetails;
    FrameLayout snackbarLayout;

    TextInputLayout name, phone, email, address, birth;
    TextInputEditText dob;
    Button deleteImage, changePhoto, updateBtn;
    RadioButton male, female;
    ImageView profilePhoto;

    FirebaseAuth firebaseAuth;
    ProfileManager profileManager;

    User user;
    FirebaseUser firebaseUser;
    Uri imageUri;
    boolean imageSelected = false;
    boolean isUpdating = false;

    DatePickerDialog picker;
    ProgressBar progressBar;

    public EditProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfile newInstance(String param1, String param2) {
        EditProfile fragment = new EditProfile();
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
        View view = inflater.inflate(R.layout.manuka_fragment_edit_profile, container, false);

        layoutEditDetails = view.findViewById(R.id.layoutEditDetails);
        snackbarLayout = view.findViewById(R.id.snackView);

        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        address = view.findViewById(R.id.location);
        birth = view.findViewById(R.id.dateOfBirth);
        dob = view.findViewById(R.id.dob);
        progressBar = view.findViewById(R.id.progressBar);

        birth.getEditText().setInputType(InputType.TYPE_NULL);

        deleteImage = view.findViewById(R.id.deleteImage);
        changePhoto = view.findViewById(R.id.changePhoto);
        profilePhoto = view.findViewById(R.id.profilePhoto);
        updateBtn = view.findViewById(R.id.updateBtn);

        male = view.findViewById(R.id.radio_male);
        female = view.findViewById(R.id.radio_female);

        layoutEditDetails.setVisibility(View.GONE);
        deleteImage.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        profileManager = new ProfileManager(this);

        user = new User();

        //listeners
        deleteImage.setOnClickListener(this);
        changePhoto.setOnClickListener(this);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        birth.setOnClickListener(this);
        dob.setOnClickListener(this);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile_update) {
            updateProfile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        profileManager.getUser();
    }

    @Override
    public void onSuccessUpdateProfile(Void aVoid) {
        HideSoftKeyboard.hideKeyboard(requireActivity());
        showSuccessSnackbar("Your profile was updated successfully");
        isUpdating = false;
        endUpdatingUi();
    }

    @Override
    public void onFailureUpdateProfile(Exception e) {
        HideSoftKeyboard.hideKeyboard(requireActivity());
        showErrorSnackbar(e.getMessage());
        isUpdating = false;
        endUpdatingUi();
    }

    @Override
    public void onCompleteUpdatePassword(Task<Void> task) {
        if (task.isSuccessful()) {

        } else {
            HideSoftKeyboard.hideKeyboard(requireActivity());
            isUpdating = false;
            endUpdatingUi();
        }
    }

    @Override
    public void onCompleteUpdateEmail(Task<Void> task) {
        if (!task.isSuccessful()) {
            HideSoftKeyboard.hideKeyboard(requireActivity());
            showErrorSnackbar(task.getException().getMessage());
            isUpdating = false;
            endUpdatingUi();
        }
    }

    @Override
    public void onTaskFull(boolean status) {
        if (status)
            Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_LONG).show();

        isUpdating = false;
    }

    @Override
    public void onSuccessGetUser(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null && documentSnapshot.exists()) {
            user = documentSnapshot.toObject(User.class);
        }
        updateUiOnDataRecieve();
    }

    public void updateUiOnDataRecieve() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (user.getUid() != null) {

            name.getEditText().setText(user.getName());

            phone.getEditText().setText(user.getPhone());

            email.getEditText().setText(user.getEmail());

            birth.getEditText().setText(user.getDateOfBirth());

            address.getEditText().setText(user.getAddress());

            if (user.getGender() != null && user.getGender().equals("Male"))
                male.setChecked(true);

            else if (user.getGender() != null && user.getGender().equals("Female"))
                female.setChecked(true);

        } else {
            name.getEditText().setText(firebaseUser.getDisplayName());
            phone.getEditText().setText(firebaseUser.getPhoneNumber());
            email.getEditText().setText(firebaseUser.getEmail());
        }

        if (firebaseUser.getPhotoUrl() != null) {
            profilePhoto.setImageURI(null);
            profilePhoto.setImageBitmap(null);
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(profilePhoto);
        }

        layoutEditDetails.setVisibility(View.VISIBLE);
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

    private void showSuccessSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(snackbarLayout, message, Snackbar.LENGTH_INDEFINITE).setAction("x", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        }).setActionTextColor(getResources().getColor(R.color.colorWhite));

        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        snackbar.show();
    }

    private void updatingUi(){
        updateBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void endUpdatingUi(){
        progressBar.setVisibility(View.GONE);
        updateBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == male)
            user.setGender("Male");
        else if (view == female)
            user.setGender("Female");
        else if (view == changePhoto)
            profileImageSelector();
        else if (view == deleteImage)
            deleteSelectedImage();
        else if (view == updateBtn)
            updateProfile();
        else if (view == birth || view == dob || view == dob.getHint())
            showDateCalender();
    }

    private void updateProfile() {
        HideSoftKeyboard.hideKeyboard(requireActivity());
        if (name.getEditText().getText().length() == 0) {
            name.setError("User name cannot empty");
            showErrorSnackbar("Please fill the missing fields before updating.");
        } else if (email.getEditText().getText().length() == 0) {
            email.setError("User name cannot empty");
            showErrorSnackbar("Please fill the missing fields before updating.");
        } else if (!isUpdating) {
            updatingUi();
            user.setUid(firebaseAuth.getCurrentUser().getUid());
            user.setName(name.getEditText().getText().toString());
            user.setPhone(phone.getEditText().getText().toString());
            user.setEmail(email.getEditText().getText().toString());
            user.setAddress(address.getEditText().getText().toString());
            user.setDateOfBirth(birth.getEditText().getText().toString());
            isUpdating = true;
            if (imageSelected)
                profileManager.updateFirebaseEmailAndUserAndImage(email.getEditText().getText().toString(), user, ImageQualityReducer.reduceQuality(profilePhoto.getDrawable()));
            else
                profileManager.updateFirebaseEmailAndUserAndImage(email.getEditText().getText().toString(), user, null);
        } else if (isUpdating)
            Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_LONG).show();
    }

    private void showDateCalender() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        birth.getEditText().setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();
    }

    private void deleteSelectedImage() {
        imageSelected = false;
        deleteImage.setVisibility(View.GONE);
        profilePhoto.setImageURI(null);
        profilePhoto.setImageBitmap(null);
        if (firebaseUser.getPhotoUrl() != null) {
            Picasso.get().load(firebaseUser.getPhotoUrl()).into(profilePhoto);
        } else
            profilePhoto.setBackgroundResource(R.drawable.round_user);
    }

    private void profileImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent, "Select an image for profile photo"), IMAGE_SELECTOR);

        Intent pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        Intent chooser = Intent.createChooser(pictureActionIntent, "Select an image for profile photo");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});
        this.startActivityForResult(chooser, IMAGE_SELECTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_SELECTOR) {
            if (data.getData() != null) {
                imageSelected = true;
                profilePhoto.setImageURI(data.getData());
                deleteImage.setVisibility(View.VISIBLE);
            } else if (data.getExtras() != null) {
                imageSelected = true;
                Bundle bundle = data.getExtras();
                profilePhoto.setImageBitmap((Bitmap) bundle.get("data"));
                deleteImage.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileManager.destroy();
    }

}