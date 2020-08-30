package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;

public class AddNewPhoneViewModel extends ViewModel {
    private MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    private MutableLiveData<Boolean> verifyStatus = new MutableLiveData<>();

    public LiveData<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.setValue(phoneNumber);
    }

    public LiveData<Boolean> getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Boolean verifyStatus) {
        this.verifyStatus.setValue(verifyStatus);
    }
}
