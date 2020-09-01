package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Advertisement;
import com.adeasy.advertise.model.Category;

import java.util.List;

public class NewPostViewModel  extends ViewModel {
    private MutableLiveData<String> locationSelected = new MutableLiveData<>();
    private MutableLiveData<Category> categorySelected = new MutableLiveData<>();
    private MutableLiveData<Boolean> showAllCategories = new MutableLiveData<>();
    private MutableLiveData<Advertisement> advertisement = new MutableLiveData<>();
    private MutableLiveData<List<Integer>> contactDetailsValidation = new MutableLiveData<>();
    private MutableLiveData<Boolean> adDetailsValidation = new MutableLiveData<>();

    public LiveData<String> getLocationSelected() {
        return locationSelected;
    }

    public void setLocationSelected(String locationSelected) {
        this.locationSelected.setValue(locationSelected);
    }

    public LiveData<Category> getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected.setValue(categorySelected);
    }

    public LiveData<Boolean> getShowAllCategories() {
        return showAllCategories;
    }

    public void setShowAllCategories(Boolean showAllCategories) {
        this.showAllCategories.setValue(showAllCategories);
    }

    public LiveData<Advertisement> getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement.setValue(advertisement);
    }

    public LiveData<List<Integer>> getContactDetailsValidation() {
        return contactDetailsValidation;
    }

    public void setContactDetailsValidation(List<Integer> numbers) {
        this.contactDetailsValidation.setValue(numbers);
    }

    public LiveData<Boolean> getAdDetailsValidation() {
        return adDetailsValidation;
    }

    public void setAdDetailsValidation(Boolean adDetailsValidation) {
        this.adDetailsValidation.setValue(adDetailsValidation);
    }
}
