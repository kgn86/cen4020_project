package edu.fsu.cs.fsutranz.ui.bus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusViewModel extends ViewModel {

    //This fragment will hold the busing information once linked to TransLocRider API
    private MutableLiveData<String> mText;

    public BusViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the bus fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}