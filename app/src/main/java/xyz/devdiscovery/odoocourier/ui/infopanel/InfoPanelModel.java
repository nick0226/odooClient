package xyz.devdiscovery.odoocourier.ui.infopanel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoPanelModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoPanelModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}
