package com.cyt.ieasy.mobilelingliao;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by jin on 2015.10.10.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
