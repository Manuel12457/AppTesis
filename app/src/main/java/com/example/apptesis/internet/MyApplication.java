package com.example.apptesis.internet;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MyApplication extends Application {

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        // Registra el BroadcastReceiver sin pasar un listener todavía
        networkChangeReceiver = new NetworkChangeReceiver(null);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }
}
