package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class AddNewPhoneViewModel extends ViewModel {
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<Integer> verifiedNumber = new MutableLiveData<>();

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public LiveData<Integer> getVerifiedNumber() {
        return verifiedNumber;
    }

    public void setVerifiedNumber(Integer verifiedNumber) {
        this.verifiedNumber.setValue(verifiedNumber);
    }
}
