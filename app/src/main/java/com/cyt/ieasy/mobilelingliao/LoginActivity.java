package com.cyt.ieasy.mobilelingliao;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.DeptTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.event.UpdateEvent;
import com.cyt.ieasy.model.Operator;
import com.cyt.ieasy.setting.ChangeLogDialog;
import com.cyt.ieasy.setting.DownLoadActivity;
import com.cyt.ieasy.setting.SettingActivity;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.SystemUtils;
import com.cyt.ieasy.tools.WebServiceTool;
import com.cyt.ieasy.update.CheckUpdate;
import com.ieasy.dao.Dept_Table;
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
public class LoginActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rotateloading)
    RotateLoading rotateLoading;
    @Bind(R.id.btn_signup)
    BootstrapButton loginbtn;
    @Bind(R.id.input_layout_name)
    TextInputLayout inputNamelayout;
    @Bind(R.id.input_layout_password)
    TextInputLayout inputPwdlayout;
    @Bind(R.id.input_name)
    EditText inputName;
    @Bind(R.id.input_password)
    EditText inputPwd;
    @Bind(R.id.savePassword)
    CheckBox checkbox;
    @Bind(R.id.coordinatorlayout)
    CoordinatorLayout coordinatorLayout;
    private final int droidGreen = Color.parseColor("#0275d8");
    public static final Executor THREAD_POOL_EXECUTOR = Executors
            .newFixedThreadPool(SystemUtils.DEFAULT_THREAD_POOL_SIZE);
    private String  Base_Service;
    private String  Scm_Service;
    private String  IP_Config;
    private String Message;
    private String error;
    private int step=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        ButterKnife.bind(this);
        initView();
        WebServiceTool.init();
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        Toast.makeText(context,smallestScreenWidth+"高度",Toast.LENGTH_SHORT).show();
    }

    void initView(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        getSupportActionBar().setTitle("");
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPwd.addTextChangedListener(new MyTextWatcher(inputPwd));
        String signpwd = CommonTool.getGlobalSetting(context, Const.savepwd);
        if(!StringUtils.isBlank(signpwd)){
            if(Boolean.parseBoolean(signpwd)){
                checkbox.setChecked(true);
                readInputUser();
            }
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CommonTool.saveGlobalSetting(context, Const.savepwd, true);
                } else {
                    CommonTool.saveGlobalSetting(context, Const.savepwd, false);
                }
            }
        });
    }
    @OnClick(R.id.btn_signup)
    public void signOnClick(){
        if (!validateName()) {
            return;
        }
        if (!validatePassword()){
            return;
        }
        loadIpConfig();
        //首先检查更新
        showIndeterminateProgressDialog(false,"检查更新中····");
        Message = "";
        error = "";
        step = 0;
        CheckUpdate checkUpdate = new CheckUpdate();
        checkUpdate.isUpdata();
    }

    private void loadIpConfig(){
        IP_Config = "http://"+ CommonTool.getGlobalSetting(MyApplication.getContext(), Const.ipconfig)
                +":"+CommonTool.getGlobalSetting(MyApplication.getContext(),Const.portconfig);
        Base_Service = IP_Config + "/webserver/WebService/BaseService.asmx";
        Scm_Service  = IP_Config+"/webserver/WebService/ScmService.asmx";

    }

    class LoginTask extends AsyncTask<Void,Void,Void> {
        private String name;
        private String pwd;
        private Operator operator;
        private String errorMsg;
        public LoginTask(String name,String pwd){
            this.name = name;
            this.pwd = pwd;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op = WebServiceTool.callWebservice(
                                        Base_Service, "GetOperatorByPwd",
                                        new String[] {"operator_code", "operator_password"},
                                        new Object[] {name, CommonTool.MD5(pwd)});
                if(null!=op){
                    operator = (Operator)WebServiceTool.toObject(op,Operator.class);

                    CommonTool.saveGlobalSetting(context,Const.cachuser,operator.Operator_Name);
                    CommonTool.saveGlobalSetting(context,Const.cachuserid,operator.Operator_ID);
                    CommonTool.saveGlobalSetting(context,Const.cachuserdeptid,operator.CP_ID);
                    MyLogger.showLogWithLineNum(5,"部门信息"+operator.CP_ID+"部门");
                    Dept_Table dept_table = DeptTableUtil.getDeptTableUtil().getDeptByID(operator.CP_ID);
                    if(null!=dept_table){
                        CommonTool.saveGlobalSetting(context,Const.cachuserdept,dept_table.getDEPTNAME());
                    }else{
                        errorMsg = "未找到该登陆人部门信息";
                    }
                    MyLogger.showLogWithLineNum(5, "登陆信息" + operator.Operator_Name+dept_table.getDEPTNAME());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndeterminateProgressDialog(false,"登陆中····");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(null!=operator){
                EventBus.getDefault().post(new MessageEvent(Const.Success));
            }else{
                EventBus.getDefault().post(new MessageEvent(Const.Failue));
            }

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

    private void login(){
        String name = inputName.getText().toString();
        String pwd = inputPwd.getText().toString();
        //如果版本号大于10,按照多线程模式执行，由系统自动分配线程池大小
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new LoginTask(name,pwd).executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else{
            new LoginTask(name,pwd).execute();
        }
    }

    public void onEvent(MessageEvent event){
        dismiss();
        if(event.message.equals(Const.Success)){
            if(checkbox.isChecked()){
                saveInputUser();
            }else{
                clearCachUser();
            }
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else if(event.message.equals(Const.Failue)){
            new MaterialDialog.Builder(this)
                    .title("登陆信息")
                    .content("登陆失败，请重试")
                    .positiveText("确定")
                    .show();
        }else{
            Message +=event.message+"\n";
            error +=event.error+"\n";
            step+=event.step;
            if(StringUtils.isBlank(event.error)){
                if(step==7){
                    dismiss();
                    login();
                }
            }else{
                dismiss();
                Message+="数据更新失败"+"\n";
                new MaterialDialog.Builder(this)
                        .title("检查更新")
                        .content(Message+"请重试")
                        .positiveText("确定")
                        .show();
            }
        }
    }

    private void setproValue(int min,int max){
        for(int i=min;i<max;i++){
            dialog.incrementProgress(1);
        }
    }

    public void onEvent(UpdateEvent event){
        dismiss();
        MyLogger.showLogWithLineNum(5,"检查更新"+event.result);
        if(event.result.equals(Const.isUpdate)){
            showIndeterminateProgressDialog(true,"更新数据");
            DownLoadActivity.getInstance().loadWuZi();
        }else if(event.result.equals(Const.noUpdate)){
            login();
        }
    }

    public void saveInputUser(){
        CommonTool.saveGlobalSetting(context,Const.cachuser,inputName.getText().toString());
        CommonTool.saveGlobalSetting(context,Const.cachpwd,inputPwd.getText().toString());
    }

    public void readInputUser(){
        String cachuser = CommonTool.getGlobalSetting(context,Const.cachuser);
        String cachpwd  = CommonTool.getGlobalSetting(context,Const.cachpwd);
        if(!StringUtils.isBlank(cachuser)){
            inputName.setText(cachuser);
        }
        if(!StringUtils.isBlank(cachpwd)){
            inputPwd.setText(cachpwd);
        }
    }

    public void clearCachUser(){
        CommonTool.removeGlobalSetting(context,Const.cachuser);
        CommonTool.removeGlobalSetting(context,Const.cachpwd);
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

    /**
     * Opens the dialog
     *
     * @param dialogStandardFragment
     */
    private void openDialogFragment(DialogFragment dialogStandardFragment) {
        if (dialogStandardFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("changelogdemo_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            dialogStandardFragment.show(ft, "changelogdemo_dialog");
        }
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
            //// TODO: 2015.10.29 弹出框，提示请输入密码 默认为215000
            new MaterialDialog.Builder(this)
                    .title(R.string.input)
                    .content(R.string.input_content)
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                    .negativeText("取消")
                    .cancelable(false)
                    .input(R.string.input_hint, R.string.input_init, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // Do something
                            if (null != input) {
                                if (input.toString().equals(Const.SettingPwd)) {
                                    startActivity(SettingActivity.class, false);
                                    dialog.dismiss();
                                }
                            }
                        }
                    }).show();

        }else if(item.getItemId()==R.id.updatelog){
            openDialogFragment(new ChangeLogDialog());
        }
        else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
