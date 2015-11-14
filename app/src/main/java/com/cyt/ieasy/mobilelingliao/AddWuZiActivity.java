package com.cyt.ieasy.mobilelingliao;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.adapter.WuZiAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.db.WuZiTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.widget.EmportyUtils;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.WuZi_Table;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

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
    private EditText currentFouce;
    private List<WuZi_Table> wuZiTableList;
    private String LL_CODE;
    private String DEPT_NAME="测试";
    private String STOCK_NAME="测试";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_wuzi);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        setTitle("添加物料");
        initsearch();
        LL_CODE = CommonTool.NewGuid();
        if(!CommonTool.isWifiOK(AddWuZiActivity.this)){
            LayoutInflater inflater = getLayoutInflater();
            EmportyUtils.netFailView(AddWuZiActivity.this);
        }else{
            new LoadTask().execute();
        }
    }

    void initView(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wuZiAdapter.readMaps();
                LING_WUZI ling_wuzi = new LING_WUZI();
                ling_wuzi.setLL_CODE(LL_CODE);
                ling_wuzi.setLL_DEPT(DEPT_NAME);
                ling_wuzi.setLL_STOCK(STOCK_NAME);
                LingLiaoTableUtil.getLiaoTableUtil().insertWuZi(ling_wuzi, wuZiAdapter.getLingWuZiDetial());
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                wuZiAdapter.clearEdFouce();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    class LoadTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            wuZiTableList = WuZiTableUtil.getWuZiTableUtil().getAlldata();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndeterminateProgressDialog(false, "加载物料中····");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!wuZiTableList.isEmpty()){
                EventBus.getDefault().post(new MessageEvent(Const.Success));
            }else{
                EventBus.getDefault().post(new MessageEvent(Const.Failue));
            }
        }
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if(event.message.equals(Const.Success)){
            initAdapter(wuZiTableList);
        }else if(event.message.equals(Const.SaveSuccess)){
            startActivity(HistoryActivity.class,false);
            finish();
        }else if(event.message.equals(Const.SaveFailue)){
            new MaterialDialog.Builder(context)
                    .title("保存")
                    .content("保存失败")
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
        else{
            //// TODO: 2015.11.12  显示自定义加载失败view,可以点击加载重试
            EmportyUtils.createFailView(AddWuZiActivity.this);
        }
    }

    void initAdapter(List<WuZi_Table> wuZi_tableList){
        wuZiAdapter = new WuZiAdapter(AddWuZiActivity.this,wuZi_tableList);
        listView.setAdapter(wuZiAdapter);
        initView();
        wuZiAdapter.setLL_Code(LL_CODE);
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
                wuZiAdapter.updateListView(wuZi_tableList);
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
//                initAdapter(wuZiTableList);
                wuZiAdapter.updateListView(wuZiTableList);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(AddWuZiActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(AddWuZiActivity.this);
    }
}
