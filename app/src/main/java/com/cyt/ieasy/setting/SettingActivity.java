package com.cyt.ieasy.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.model.UpdateStatus;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.interfaces.Callback;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.TimeUtils;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

import de.greenrobot.event.EventBus;

public class SettingActivity extends MaterialSettingsActivity implements SampleDialog.OnDialogOkClick {

    private EditText IpStr=null;
    private EditText PortStr=null;
    private CheckboxItem itemDept;
    private TextItem ipConfigItem;
    private TextItem updataItem;
    private MaterialDialog dialog;
    private String Message;
    private String error;
    private int step=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("系统设置");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        ipConfigItem =new TextItem(getFragment(), Const.ipconfig);
        addItem(new HeaderItem(getFragment()).setTitle("服务器地址配置"));
        addItem(ipConfigItem.setTitle("IP地址").setSubtitle("未设置IP地址").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem item) {
                showCustomView();
            }
        }));
        if(!StringUtils.isBlank(getStorageInterface().load(Const.ipconfig,""))){
            ipConfigItem.updateSubTitle(getStorageInterface().load(Const.ipconfig, "") + ":" + getStorageInterface().load(Const.portconfig, ""));
        }
        itemDept = new CheckboxItem(getFragment(), Const.editBm);
        addItem(new HeaderItem(getFragment()).setTitle("基础设置"));
        addItem(itemDept.setTitle("是否允许选择部门").setSubtitle("否").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                getStorageInterface().save(Const.editBm, isChecked + "");
                if(isChecked){
                    itemDept.updateSubTitle("是");
                }else{
                    itemDept.updateSubTitle("否");
                }
            }
        }));
        if(null!=getStorageInterface().load(Const.editBm,"")){
            MyLogger.showLogWithLineNum(5, "测试" + getStorageInterface().load(Const.editBm, ""));
            if(getStorageInterface().load(Const.editBm,"").toString().equals("true")){
                itemDept.updateChecked(true);
                itemDept.updateSubTitle("是");
            }else{
                itemDept.updateChecked(false);
                itemDept.updateSubTitle("否");
            }
        }
        addItem(new DividerItem(getFragment()));
        updataItem = new TextItem(getFragment(), Const.updatedata);
        addItem(updataItem.setTitle("更新基础数据").setSubtitle("上次更新时间"+ TimeUtils.getCurrentTimeInString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                updata();
            }
        }));
        if(null!=getStorageInterface().load(Const.updatedata,"")){
            updataItem.updateSubTitle("上次更新时间"+getStorageInterface().load(Const.updatedata,""));
        }else{
            updataItem.updateSubTitle("未更新基础数据");
        }
        addItem(new HeaderItem(getFragment()).setTitle("其他"));
        addItem(new DividerItem(getFragment()));
        addItem(new TextItem(getFragment(), "finish").setTitle("退出设置").setOnclick(new TextItem.OnClickListener(){
            @Override
            public void onClick(TextItem item) {
                finish();
            }
        }));
        addItem(new DividerItem(getFragment()));

    }

    public void showProgress(){
        dialog =  new MaterialDialog.Builder(this)
                .title("加载中····")
                .content("更新基础数据")
                .progress(false, 100)
                .cancelable(false)
                //.progressIndeterminateStyle(true)
                .show();
    }
    public void dismissProgress(){
        if(null!=dialog){
            dialog.dismiss();
        }
    }

    public void updata(){
        if (!CommonTool.isWifiOK(SettingActivity.this)) {
            new MaterialDialog.Builder(SettingActivity.this)
                    .title("网络异常")
                    .content("请检查网络")
                    .positiveText(R.string.agree)
                    .theme(Theme.LIGHT)
                    .show();
            return;
        }
        Message = "";
        step = 0;
        error = "";
        showProgress();
        //常规做法
        DownLoadActivity.getInstance(new Callback<UpdateStatus>() {
            @Override
            public void invoke() {
            }
            @Override
            public void invole(UpdateStatus arg) {
                dismissProgress();
                if (arg.getSuccess()) {
                    //更新成功
                } else {
                    //更新失败
                    MyLogger.showLogWithLineNum(5, "失败原因" + arg.getMessage());
                }
            }
        }).loadData();
    }

    @Override
    public StorageInterface initStorageInterface() {
        return new PreferencesStorageInterface(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOkClick() {

    }

    void loadSetting(){
        if(null!=getStorageInterface().load("key1","")){
        }
    }

    public void showCustomView() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.ipsetting)
                .customView(R.layout.layout_ip, true)
                .positiveText(R.string.save)
                .negativeText(android.R.string.cancel)
                .theme(Theme.LIGHT)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //验证输入的合法性，并保存在本地
                        String Ip = IpStr.getText().toString();
                        String Port = PortStr.getText().toString();
                        if(CommonTool.isIpv4(Ip)){
                            getStorageInterface().save(Const.ipconfig,Ip);
                            getStorageInterface().save(Const.portconfig,Port);
                            ((TextItem) getItem(Const.ipconfig)).updateSubTitle(Ip + ":" + Port);
                        }
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        IpStr = (EditText) dialog.getCustomView().findViewById(R.id.ip);
        PortStr = (EditText) dialog.getCustomView().findViewById(R.id.port);
        if(!StringUtils.isBlank(getStorageInterface().load(Const.ipconfig,""))){
            IpStr.setText(getStorageInterface().load(Const.ipconfig,""));
        }
        if(!StringUtils.isBlank(getStorageInterface().load(Const.portconfig,""))){
            PortStr.setText(getStorageInterface().load(Const.portconfig,""));
        }
        IpStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(CommonTool.isIpv4(s.toString().trim())){
                    positiveAction.setEnabled(true);
                }else{
                    if(positiveAction.isEnabled()){
                        positiveAction.setEnabled(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
        positiveAction.setEnabled(false); // disabled by default
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(SettingActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(SettingActivity.this);
    }

    public void onEvent(MessageEvent event){
        Message +=event.message+"\n";
        error +=event.error+"\n";
        step+=event.step;
        if(StringUtils.isBlank(event.error)){
            if(step==5){
                Message+="数据更新完毕"+"\n";
                dialog.setContent(Message);
                dialog.setProgress(100);
                dialog.setActionButton(DialogAction.POSITIVE, "关闭");
                getStorageInterface().save(Const.updatedata, TimeUtils.getCurrentTimeInString());
                updataItem.updateSubTitle("上次更新时间"+TimeUtils.getCurrentTimeInString());
            }else{
                setproValue(0, 20);
                dialog.setContent(Message+step);
            }
        }else{
            Message+="数据更新失败"+"\n";
            dialog.setContent(error);
            dialog.setProgress(100);
            dialog.setActionButton(DialogAction.NEUTRAL, "重试");
            dialog.setActionButton(DialogAction.POSITIVE, "关闭");
        }
    }
    void setproValue(int min,int max){
        for(int i=min;i<max;i++){
            dialog.incrementProgress(1);
        }
    }
}
