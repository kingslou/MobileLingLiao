package com.cyt.ieasy.mobilelingliao;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.tools.MyLogger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jin on 2015.10.10.
 */
public class BaseActivity extends AppCompatActivity {
    MaterialDialog dialog =  null;
    Context context;
    protected final int REQUEST_CODE_DEFAULT = 1234;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    private static final int corePoolSize = 15;
    private static final int maximumPoolSize = 30;
    private static final int keepAliveTime = 10;
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    private static final Executor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
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
         dialog=null;
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

    protected  void showSnackBar(View view){
        final Snackbar snackbar = Snackbar.make(view,"欢迎使用",Snackbar.LENGTH_LONG).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }

    //使用AsynTask自定义线程池
    public void addTaskInPool(AsyncTask asyncTask) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            asyncTask.executeOnExecutor(threadPoolExecutor);
        }else{
            asyncTask.execute();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//      EventBus.getDefault().unregister(this);
    }

    public void showback(){
        new MaterialDialog.Builder(context)
                .title("退出")
                .content("确定退出吗?")
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        materialDialog.dismiss();
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

//    public void onEvent(MessageEvent event){
//
//    }
//
//    public void onEvent(LL_Event event){
//
//    }

    public boolean isListViewReachBottomEdge(final ListView listView) {
        boolean result=false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt(listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result= (listView.getHeight()>=bottomChildView.getBottom());
        };
        return  result;
    }

    public boolean isListViewReachTopEdge(final ListView listView) {
        boolean result=false;
        if(listView.getFirstVisiblePosition()==0){
            final View topChildView = listView.getChildAt(0);
            result=topChildView.getTop()==0;
        }
        return result ;
    }
}
