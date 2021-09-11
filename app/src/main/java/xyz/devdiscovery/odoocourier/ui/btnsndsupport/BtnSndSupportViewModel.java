package xyz.devdiscovery.odoocourier.ui.btnsndsupport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BtnSndSupportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BtnSndSupportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}
