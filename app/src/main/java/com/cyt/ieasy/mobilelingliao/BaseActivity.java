package com.cyt.ieasy.mobilelingliao;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.tools.MyLogger;

/**
 * Created by jin on 2015.10.10.
 */
public class BaseActivity extends AppCompatActivity {
    MaterialDialog dialog =  null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLogger.DEBUG = true;
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

    public void showIndeterminateProgressDialog(boolean horizontal) {
         dialog =  new MaterialDialog.Builder(this)
                .title("加载中····")
                .content("请稍后····")
                .progress(true, 0)
                .progressIndeterminateStyle(horizontal)
                .show();
    }
    public void dismiss(){
        if(null!=dialog){
            dialog.dismiss();
        }
    }
}
