package xyz.devdiscovery.odoocourier.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MySettingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MySettingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
