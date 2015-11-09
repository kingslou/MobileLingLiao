package com.cyt.ieasy.mobilelingliao;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.09.
 */
public class AddWuZiActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_wuzi);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        setTitle("添加物料");
    }
}
