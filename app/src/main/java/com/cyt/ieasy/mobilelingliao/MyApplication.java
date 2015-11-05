package com.cyt.ieasy.mobilelingliao;

import android.app.Application;
import android.content.Context;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.cyt.ieasy.constans.Const;
import com.ieasy.dao.DaoMaster;
import com.ieasy.dao.DaoSession;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * Created by jin on 2015.10.10.
 */
public class MyApplication extends Application {
    private static Context context;
    private volatile static DaoMaster daoMaster;
    private volatile static DaoSession daoSession;

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

    public static DaoMaster getDaoMaster(){
        if(daoMaster==null){
            synchronized(MyApplication.class){
                if(daoMaster==null){
                    DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, Const.DB_NAME, null);
                    daoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(){
        if(daoSession==null){
            synchronized (MyApplication.class){
                if(daoSession==null){
                    if(daoMaster==null){
                        daoMaster=getDaoMaster();
                    }
                    daoSession=daoMaster.newSession();
                }
            }
        }
        return daoSession;
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
