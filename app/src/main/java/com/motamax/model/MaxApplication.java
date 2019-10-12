package com.motamax.model;

import android.app.Application;

public class MaxApplication extends Application {

    private static MaxApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public static synchronized MaxApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}