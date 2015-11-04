package com.cyt.ieasy.mobilelingliao;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.tools.MyLogger;
import com.kenumir.materialsettings.storage.StorageInterface;

/**
 * Created by jin on 2015.10.10.
 */
public class BaseActivity extends AppCompatActivity {
    MaterialDialog dialog =  null;
    MaterialDialog.Builder materialbuilder;
    Context context;
    protected final int REQUEST_CODE_DEFAULT = 1234;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private StorageInterface mStorageInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MyLogger.DEBUG = true;
        powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "My Lock");
    }

    public void initToolbar(Toolbar toolbar){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        setTitle("");
    }
    public void setTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void showIndeterminateProgressDialog(boolean horizontal,String content) {
         dialog =  new MaterialDialog.Builder(this)
                .title("加载中····")
                .content(content)
                .progress(true, 0)
                 .cancelable(false)
                .progressIndeterminateStyle(horizontal)
                .show();
    }
    public void dismiss(){
        if(null!=dialog){
            dialog.dismiss();
        }
    }

    public void startActivityByName(String actName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ComponentName cn = new ComponentName(this.getClass().getPackage().getName(), actName);
        intent.setComponent(cn);
        startActivity(intent);
    }

    protected void startActivityNoHistory(Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    protected void startActivityWithoutTrace(Class clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    protected void startActivity(Class clazz, boolean forResult) {
        if (forResult) {
            startActivityForResult(new Intent(context, clazz), REQUEST_CODE_DEFAULT);
        } else {
            startActivity(new Intent(context, clazz));
        }
    }

    public StorageInterface initStorageInterface(){
       return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }
}
