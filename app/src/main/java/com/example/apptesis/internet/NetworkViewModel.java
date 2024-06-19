package com.example.apptesis.internet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class NetworkViewModel extends AndroidViewModel {
    private final MutableLiveData<Boolean> isConnected = new MutableLiveData<>();

    public NetworkViewModel(Application application) {
        super(application);
    }

    public void setNetworkState(boolean isConnected) {
        this.isConnected.setValue(isConnected);
    }

    public LiveData<Boolean> getNetworkState() {
        return isConnected;
    }
}
