package xyz.devdiscovery.odoocourier.ui.salles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SallesViewModel extends ViewModel{

    private MutableLiveData<String> mText;

    public SallesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is salles fragment activity!");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
