package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;

public class BuynowViewModel extends ViewModel {
    private MutableLiveData<Order_Customer> customer = new MutableLiveData<>();
    private MutableLiveData<Order_Item> item = new MutableLiveData<>();
    private MutableLiveData<Boolean> validateCustomerDetails = new MutableLiveData<>();
    private MutableLiveData<Boolean> startVerifyMobileNumber = new MutableLiveData<>();
    private MutableLiveData<Boolean> mobileNumberVerifyStatus = new MutableLiveData<>();
    private MutableLiveData<Boolean> paymentSelectCOD = new MutableLiveData<>();

    public LiveData<Order_Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(Order_Customer order_customer) {
        customer.setValue(order_customer);
    }

    public LiveData<Order_Item> getItem() {
        return item;
    }

    public void setItem(Order_Item item) {
        this.item.setValue(item);
    }

    public LiveData<Boolean> getValidateCustomerDetails() {
        return validateCustomerDetails;
    }

    public void setValidateCustomerDetails(Boolean validateCustomerDetails) {
        this.validateCustomerDetails.setValue(validateCustomerDetails);
    }

    public LiveData<Boolean> getStartVerifyMobileNumber() {
        return startVerifyMobileNumber;
    }

    public void setStartVerifyMobileNumber(Boolean startVerifyMobileNumber) {
        this.startVerifyMobileNumber.setValue(startVerifyMobileNumber);
    }

    public LiveData<Boolean> getMobileNumberVerifyStatus() {
        return mobileNumberVerifyStatus;
    }

    public void setMobileNumberVerifyStatus(Boolean mobileNumberVerifyStatus) {
        this.mobileNumberVerifyStatus.setValue(mobileNumberVerifyStatus);
    }

    public LiveData<Boolean> getPaymentSelectCOD() {
        return paymentSelectCOD;
    }

    public void setPaymentSelectCOD(Boolean paymentSelectCOD) {
        this.paymentSelectCOD.setValue(paymentSelectCOD);
    }
}
