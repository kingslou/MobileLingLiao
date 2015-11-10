package com.cyt.ieasy.mobilelingliao;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.adapter.WuZiAdapter;
import com.cyt.ieasy.db.WuZiTableUtil;
import com.cyt.ieasy.tools.MyLogger;
import com.ieasy.dao.WuZi_Table;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.09.
 */
public class AddWuZiActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.listViewuzi)
    ListView listView;
    private WuZiAdapter wuZiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_wuzi);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        setTitle("添加物料");
        initsearch();
        new LoadTask().execute();
    }

    class LoadTask extends AsyncTask<Void,Void,Void>{
        List<WuZi_Table> wuZiTableList;
        @Override
        protected Void doInBackground(Void... params) {
            wuZiTableList = WuZiTableUtil.getWuZiTableUtil().getAlldata();
            for(WuZi_Table wuZi_table : wuZiTableList){
                MyLogger.showLogWithLineNum(5,"速查码"+wuZi_table.getWZ_QUICK_CODE()+""+wuZi_table.getWZ_NAME());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initAdapter(wuZiTableList);
        }
    }

    void initAdapter(List<WuZi_Table> wuZi_tableList){
        wuZiAdapter = new WuZiAdapter(AddWuZiActivity.this,wuZi_tableList);
        listView.setAdapter(wuZiAdapter);
    }

    void initsearch(){
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                List<WuZi_Table> wuZi_tableList = WuZiTableUtil.getWuZiTableUtil().queryBystr(newText,10);
                MyLogger.showLogWithLineNum(5,"返回"+wuZi_tableList.size()+"个");
                for(WuZi_Table wuZiTable : wuZi_tableList){
                    MyLogger.showLogWithLineNum(5,"结果"+wuZiTable.getWZ_QUICK_CODE()+"名称"+wuZiTable.getWZ_NAME());
                }
                initAdapter(wuZi_tableList);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_addwuzi, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            new MaterialDialog.Builder(context)
                    .title("退出")
                    .content("确定退出添加吗?")
                    .positiveText(R.string.agree)
                    .negativeText(R.string.disagree)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            materialDialog.dismiss();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            materialDialog.dismiss();
                            finish();
                        }
                    })
                    .show();
        }
    }
}
