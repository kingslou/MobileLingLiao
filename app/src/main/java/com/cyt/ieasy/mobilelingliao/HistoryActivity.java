package com.cyt.ieasy.mobilelingliao;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cyt.ieasy.adapter.HistoryAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.02.
 */
public class HistoryActivity extends BaseActivity {

    @Bind(R.id.listhistory)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        setTitle("历史记录");
    }

    void initAdapter(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
