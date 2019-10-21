package edu.fsu.cs.fsutranz.ui.parking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ParkingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ParkingViewModel() {
        mText = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }
}