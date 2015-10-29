package com.cyt.ieasy.setting;

import android.os.Bundle;

import com.kenumir.materialsettings.MaterialSettingsActivity;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.DividerItem;
import com.kenumir.materialsettings.items.HeaderItem;

/**
 * Created by jin on 2015.10.29.
 * 设置界面
 */
public class SettingActivity extends MaterialSettingsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addItem(new HeaderItem(getFragment()).setTitle("设置"));
        addItem(new CheckboxItem(getFragment(), "key1").setTitle("Checkbox item 1").setSubtitle("Subtitle text 1").setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {

            }
        }));
        addItem(new DividerItem(getFragment()));
    }
}
