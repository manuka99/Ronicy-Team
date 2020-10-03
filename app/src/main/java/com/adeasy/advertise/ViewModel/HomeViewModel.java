package com.adeasy.advertise.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.adeasy.advertise.model.Category;
/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class HomeViewModel extends ViewModel {
    private MutableLiveData<Category> categoryMutableLiveData = new MutableLiveData<>();

    public LiveData<Category> getCategoryMutableLiveData() {
        return categoryMutableLiveData;
    }

    public void setCategoryMutableLiveData(Category categoryMutableLiveData) {
        this.categoryMutableLiveData.setValue(categoryMutableLiveData);
    }
}
