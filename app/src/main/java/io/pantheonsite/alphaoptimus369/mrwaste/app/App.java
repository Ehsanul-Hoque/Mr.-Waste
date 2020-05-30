package io.pantheonsite.alphaoptimus369.mrwaste.app;

import androidx.multidex.MultiDexApplication;

import com.androidnetworking.AndroidNetworking;


public class App extends MultiDexApplication
{

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
    }
}
