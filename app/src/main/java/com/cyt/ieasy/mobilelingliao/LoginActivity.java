package com.cyt.ieasy.mobilelingliao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.greenrobot.event.EventBus;

/**
 * Created by jin on 2015.10.10.
 */
public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        initToobar();
    }

    void initToobar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(LoginActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(LoginActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
