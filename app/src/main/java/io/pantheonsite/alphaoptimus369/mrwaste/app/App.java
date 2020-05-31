package io.pantheonsite.alphaoptimus369.mrwaste.app;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;


public class App extends MultiDexApplication
{

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AndroidNetworking.initialize(getApplicationContext());
    }

    public static App getInstance() {
        return instance;
    }

}
