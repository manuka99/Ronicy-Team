package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class OrderDashboardViewModel extends ViewModel {
    private MutableLiveData<Integer> selectedCategory = new MutableLiveData<>();;

    public LiveData<Integer> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Integer selectedCategory) {
        this.selectedCategory.setValue(selectedCategory);
    }
}
