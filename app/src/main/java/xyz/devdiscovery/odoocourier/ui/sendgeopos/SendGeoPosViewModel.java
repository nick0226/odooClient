package xyz.devdiscovery.odoocourier.ui.sendgeopos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendGeoPosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SendGeoPosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}

