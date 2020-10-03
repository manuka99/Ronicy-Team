package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderDashboardViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedCategory = new MutableLiveData<>();;

    public LiveData<Integer> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Integer selectedCategory) {
        this.selectedCategory.setValue(selectedCategory);
    }
}
