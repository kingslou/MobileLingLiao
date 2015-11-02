package com.cyt.ieasy.setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.cyt.ieasy.mobilelingliao.R;
import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.SwitcherItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.PreferencesStorageInterface;
import com.kenumir.materialsettings.storage.StorageInterface;

public class SettingActivity extends MaterialSettingsActivity implements SampleDialog.OnDialogOkClick {
    public static final String editBm = "editbm";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("系统设置");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        addItem(new HeaderItem(getFragment()).setTitle("基础设置"));
        addItem(new CheckboxItem(getFragment(), "key1").setTitle("设置1").setSubtitle("Subtitle text 1").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                getStorageInterface().save("key1",isChecked+"");
            }
        }));
        addItem(new DividerItem(getFragment()));
        addItem(new DividerItem(getFragment()));
        addItem(new SwitcherItem(getFragment(), editBm).setTitle("是否允许选择部门仓库").setSubtitle("否"));
        addItem(new CheckboxItem(getFragment(), "key2").setTitle("Checkbox item 2").setSubtitle("Subtitle text 2 with long text and more txt and more and more ;-)").setDefaultValue(true));
        addItem(new DividerItem(getFragment()));
        addItem(new TextItem(getFragment(), "key3").setTitle("Simple text item 1").setSubtitle("Subtitle of simple text item 1").setOnclick(new TextItem.OnClickListener() {
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
        addItem(new DividerItem(getFragment()));
        addItem(new HeaderItem(getFragment()).setTitle("Same usage with dialogs"));
        addItem(new TextItem(getFragment(), "key6").setTitle("基础数据更新").setSubtitle("Clck to show message and change subtext").setOnclick(new TextItem.OnClickListener() {
                @Override
                public void onClick(TextItem item) {

                }
            }));
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
}
