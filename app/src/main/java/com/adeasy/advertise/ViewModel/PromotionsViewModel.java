package com.adeasy.advertise.ViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.Map;

/**
 * Created by Manuka yasas,
 * University Sliit
 * Email manukayasas99@gmail.com
 **/
public class PromotionsViewModel extends ViewModel {
    private MutableLiveData<Map<Integer, Integer>> selectedPromo = new MutableLiveData<>();
    private MutableLiveData<Fragment> onFragmentSelected = new MutableLiveData<>();
    ;

    public LiveData<Map<Integer, Integer>> getSelectedPromo() {
        return selectedPromo;
    }

    public void setSelectedPromo(Map<Integer, Integer> selectedPromo) {
        this.selectedPromo.setValue(selectedPromo);
    }

    public LiveData<Fragment> getOnFragmentSelected() {
        return onFragmentSelected;
    }

    public void setOnFragmentSelected(Fragment onFragmentSelected) {
        this.onFragmentSelected.setValue(onFragmentSelected);
    }
}
