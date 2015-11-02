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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
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

public class SettingActivity extends MaterialSettingsActivity implements SampleDialog.OnDialogOkClick {
    public static final String editBm = "editbm";
    public static final String ipconfig = "ipconfig";
    public static final String portconfig = "portconfig";
    private EditText IpStr=null;
    private EditText PortStr=null;
    private CheckboxItem itemDept;
    private TextItem ipConfigItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("系统设置");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        ipConfigItem =new TextItem(getFragment(), ipconfig);
        addItem(new HeaderItem(getFragment()).setTitle("服务器地址配置"));
        addItem(ipConfigItem.setTitle("IP地址").setSubtitle("未设置IP地址").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem item) {
                showCustomView();
            }
        }));
        if(!StringUtils.isBlank(getStorageInterface().load(ipconfig,""))){
            if(getStorageInterface().load(ipconfig,"").equals("true")){

            }
        }
        itemDept = new CheckboxItem(getFragment(), editBm);
        addItem(new HeaderItem(getFragment()).setTitle("基础设置"));
        addItem(itemDept.setTitle("是否允许选择部门仓库").setSubtitle("否").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                getStorageInterface().save(editBm, isChecked + "");
                if(isChecked){
                    itemDept.updateSubTitle("是");
                }else{
                    itemDept.updateSubTitle("否");
                }
            }
        }));
        if(null!=getStorageInterface().load(editBm,"")){
            MyLogger.showLogWithLineNum(5, "测试" + getStorageInterface().load(editBm, ""));
            if(getStorageInterface().load(editBm,"").toString().equals("true")){
                itemDept.updateChecked(true);
                itemDept.updateSubTitle("是");
            }else{
                itemDept.updateChecked(false);
                itemDept.updateSubTitle("否");
            }
        }
        addItem(new DividerItem(getFragment()));
        addItem(new TextItem(getFragment(), "key3").setTitle("更新基础数据").setSubtitle("上次更新时间"+ TimeUtils.getCurrentTimeInString()).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SettingActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        }));
        addItem(new HeaderItem(getFragment()).setTitle("其他设置"));
        addItem(new TextItem(getFragment(), "key4").setTitle("Simple text item 2").setSubtitle("Subtitle of simple text item 2").setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem v) {
                Toast.makeText(SettingActivity.this, "Clicked 2", Toast.LENGTH_SHORT).show();
            }
        }));
        addItem(new DividerItem(getFragment()));
        addItem(new TextItem(getFragment(), "key5").setTitle("Simple text item 3 - no subtitle"));

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
        ((TextItem)getItem("key6")).updateSubTitle("Updated");
        getStorageInterface().save("key6", true);
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
                            getStorageInterface().save(ipconfig,Ip);
                            getStorageInterface().save(portconfig,Port);
                            ((TextItem) getItem("ipconfig")).updateSubTitle(Ip + ":" + Port);
                        }
                    }
                }).build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        IpStr = (EditText) dialog.getCustomView().findViewById(R.id.ip);
        PortStr = (EditText) dialog.getCustomView().findViewById(R.id.port);
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
}
