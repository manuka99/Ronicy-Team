package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Category;
import com.adeasy.advertise.model.Order_Customer;
import com.adeasy.advertise.model.Order_Item;

public class NewPostViewModel  extends ViewModel {
    private MutableLiveData<String> locationSelected = new MutableLiveData<>();
    private MutableLiveData<Category> categorySelected = new MutableLiveData<>();
    private MutableLiveData<Boolean> showAllCategories = new MutableLiveData<>();

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
}
