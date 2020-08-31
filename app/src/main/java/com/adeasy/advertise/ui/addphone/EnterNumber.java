package com.adeasy.advertise.ui.addphone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.adeasy.advertise.R;
import com.adeasy.advertise.ViewModel.AddNewPhoneViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EnterNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnterNumber extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button submitNumber;
    TextInputLayout inputNumber;
    AddNewPhoneViewModel addNewPhoneViewModel;

    public EnterNumber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnterNumber.
     */
    // TODO: Rename and change types and number of parameters
    public static EnterNumber newInstance(String param1, String param2) {
        EnterNumber fragment = new EnterNumber();
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
        View view = inflater.inflate(R.layout.manuka_fragment_enter_number, container, false);

        submitNumber = view.findViewById(R.id.numberEnteredBtn);
        submitNumber.setOnClickListener(this);
        inputNumber = view.findViewById(R.id.inputNumber);

        addNewPhoneViewModel = ViewModelProviders.of(getActivity()).get(AddNewPhoneViewModel.class);

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == submitNumber && inputNumber.getEditText().length() == 9) {
            inputNumber.setError(null);
            addNewPhoneViewModel.setPhoneNumber(inputNumber.getEditText().getText().toString());
        } else if (view == submitNumber && inputNumber.getEditText().length() != 9)
            inputNumber.setError("Please enter a valid 9 digit phone number starting without 0.");
    }

}