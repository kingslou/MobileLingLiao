package com.cyt.ieasy.mobilelingliao;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.adapter.HistoryDetialAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.db.WuZiTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.tools.MyLogger;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.ieasy.dao.WuZi_Table;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jin on 2015.11.13.
 */
public class HistoryContent extends BaseActivity {

    @Bind(R.id.hiscontentlist)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    private List<LING_WUZIDETIAL> ling_wuzidetialList;
    private HistoryDetialAdapter historyDetialAdapter;
    private String LL_CODE;//单据号
    private int    LL_STATUS;//单据状态
    private String DEPT_NAME;
    private String STOCK_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_his_content);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        initdata();
        MyLogger.showLogWithLineNum(5, "测试呵呵" + LL_CODE);
    }

    void initView(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LING_WUZI ling_wuzi = new LING_WUZI();
                ling_wuzi.setLL_CODE(LL_CODE);
                //调用更新方法
                LingLiaoTableUtil.getLiaoTableUtil().updateWuZi(ling_wuzi, historyDetialAdapter.getLingWuZiDetial());
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                historyDetialAdapter.clearEdFouce();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    class LoadTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ling_wuzidetialList = LingLiaoTableUtil.getLiaoTableUtil().getLingWuZiDetial(LL_CODE);
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
            if(ling_wuzidetialList!=null&&ling_wuzidetialList.size()!=0){
                EventBus.getDefault().post(new MessageEvent(Const.Success));
            }else{
                EventBus.getDefault().post(new MessageEvent(Const.Failue));
            }
        }
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if(event.message.equals(Const.Success)){
            initAdapter(ling_wuzidetialList);
        }else if(event.message.equals(Const.Failue)){
            //// TODO: 2015.11.13 显示自定义错误界面
            //setContentView(EmportyUtils.netFailView(HistoryContent.this));
        }
    }

    void initdata(){
        LL_CODE = getIntent().getExtras().getString("LL_CODE");
        LL_STATUS =Integer.parseInt(getIntent().getExtras().getString("LL_STATUS"));
        setTitle(LL_CODE);
        initsearch();
        new LoadTask().execute();
    }

    void initAdapter(List<LING_WUZIDETIAL> wuZi_tableList){
        historyDetialAdapter = new HistoryDetialAdapter(HistoryContent.this,ling_wuzidetialList);
        listView.setAdapter(historyDetialAdapter);
        initView();
        historyDetialAdapter.setLL_Status(LL_STATUS);
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
                //// TODO: 2015.11.13  这个地方需要再后台写一个新的查询方法，LingWuZiDetial
                List<WuZi_Table> wuZi_tableList = WuZiTableUtil.getWuZiTableUtil().queryBystr(newText,10);
                MyLogger.showLogWithLineNum(5, "返回" + wuZi_tableList.size() + "个");
                for(WuZi_Table wuZiTable : wuZi_tableList){
                    MyLogger.showLogWithLineNum(5,"结果"+wuZiTable.getWZ_QUICK_CODE()+"名称"+wuZiTable.getWZ_NAME());
                }
                historyDetialAdapter.updateListView(ling_wuzidetialList);
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
                initAdapter(ling_wuzidetialList);
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
                .content("确定退出吗?")
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            new MaterialDialog.Builder(context)
                    .title("退出")
                    .content("确定退出吗?")
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
        EventBus.getDefault().register(HistoryContent.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(HistoryContent.this);
    }
}
