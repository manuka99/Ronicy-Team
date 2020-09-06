package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas94@gmail.com
 **/
public class ProfileManagerViewModel extends ViewModel {
    private MutableLiveData<Boolean> updateProfile = new MutableLiveData<>();

    public LiveData<Boolean> getUpdateProfile() {
        return updateProfile;
    }

    public void setUpdateProfile(Boolean updateProfile) {
        this.updateProfile.setValue(updateProfile);
    }
}