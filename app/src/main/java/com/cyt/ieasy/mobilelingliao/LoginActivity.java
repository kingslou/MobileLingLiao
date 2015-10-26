package com.cyt.ieasy.mobilelingliao;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.tools.Const;
import com.cyt.ieasy.tools.SystemUtils;
import com.victor.loading.rotate.RotateLoading;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jin on 2015.10.10.
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.rotateloading) RotateLoading rotateLoading;
    @Bind(R.id.btn_signup) BootstrapButton loginbtn;
    @Bind(R.id.input_layout_name) TextInputLayout inputNamelayout;
    @Bind(R.id.input_layout_password) TextInputLayout inputPwdlayout;
    @Bind(R.id.input_name) EditText inputName;
    @Bind(R.id.input_password) EditText inputPwd;
    private final int droidGreen = Color.parseColor("#0275d8");
    public static final Executor THREAD_POOL_EXECUTOR = Executors
            .newFixedThreadPool(SystemUtils.DEFAULT_THREAD_POOL_SIZE);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        ButterKnife.bind(this);
        initToobar();
    }

    void initToobar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        getSupportActionBar().setTitle("");
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPwd.addTextChangedListener(new MyTextWatcher(inputPwd));
    }
    @OnClick(R.id.btn_signup)
    public void signOnClick(){
        if (!validateName()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
        //如果版本号大于10,按照多线程模式执行，由系统自动分配线程池大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new LoginTask().executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else{
            new LoginTask().execute();
        }
    }

    class LoginTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Thread.sleep(15000);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            rotateLoading.setBackgroundColor(droidGreen);
            rotateLoading.start();
            loginbtn.setEnabled(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new MessageEvent(Const.Success));
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            EventBus.getDefault().post(new MessageEvent(Const.Cancle));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputNamelayout.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputNamelayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPwd.getText().toString().trim().isEmpty()) {
            inputPwdlayout.setError(getString(R.string.err_msg_password));
            requestFocus(inputPwd);
            return false;
        } else {
            inputPwdlayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onEvent(MessageEvent event){
        if(event.Message.equals(Const.Success)){
            rotateLoading.stop();
            loginbtn.setEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(LoginActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(LoginActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.menu_main, menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_settings){
            Toast.makeText(this,"测试",Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
