package com.cyt.ieasy.mobilelingliao;

import android.app.Application;
import android.content.Context;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by jin on 2015.10.10.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        TypefaceProvider.registerDefaultIconSets();
        CustomActivityOnCrash.setLaunchActivityEvenIfInBackground(false);
        CustomActivityOnCrash.setRestartActivityClass(MainActivity.class);
        CustomActivityOnCrash.setShowErrorDetails(true);
        CustomActivityOnCrash.install(this);
    }

    public static Context getContext(){
       return context;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
