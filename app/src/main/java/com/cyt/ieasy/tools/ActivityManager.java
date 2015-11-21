package com.cyt.ieasy.tools;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by jin on 2015.11.21.
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;

    private static ActivityManager instance;

    private ActivityManager(){

    }

    public static ActivityManager getActivityManager(){

        if(instance == null){

            instance = new ActivityManager();

        }
        return instance;
    }

    public void  popActivityStack(Activity activity){

        if(null != activity){

            activity.finish();

            activityStack.remove(activity);

            activity= null;

        }
    }

    public void pushActivity2Stack(Activity activity){

        if(activityStack== null){

            activityStack= new Stack<Activity>();

        }

        activityStack.add(activity);

    }

    public Activity getCurrentActivity(){

        Activity activity = null;

        if(!activityStack.isEmpty()){

            activity= activityStack.lastElement();

        }

        return activity;

    }

    public void popAllActivityFromStack(){

        while(true){
            Activity activity = getCurrentActivity();
            if(activity == null){
                break;
            }

            popActivityStack(activity);
        }

    }
}
