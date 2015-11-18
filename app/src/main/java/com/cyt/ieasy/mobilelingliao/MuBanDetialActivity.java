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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cyt.ieasy.adapter.MuBanDetialAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.db.MuBanTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.interfaces.OnErrorViewListener;
import com.cyt.ieasy.switcher.Switcher;
import com.cyt.ieasy.tools.CommonTool;
import com.ieasy.dao.LING_MB_DETIAL;
import com.ieasy.dao.LING_WUZI;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 模板详细界面
 * Created by jin on 2015.11.18.
 */
public class MuBanDetialActivity extends BaseActivity implements OnErrorViewListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.listViewuzi)
    ListView listView;
    private MuBanDetialAdapter muBanDetialAdapter;
    private EditText currentFouce;
    private List<LING_MB_DETIAL> ling_mb_detialList;
    private String LL_CODE;
    private String DEPT_NAME = "测试";
    private String STOCK_NAME = "测试";
    private String MB_ID = "";//模板ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_wuzi);
        ButterKnife.bind(this);
        switcher = new Switcher.Builder().withContentView(findViewById(R.id.main))
                .withErrorView(findViewById(R.id.error_view))
                .withErrorLabel((TextView)findViewById(R.id.error_label))
                .withNetErrorView(findViewById(R.id.neterrorview))
                .build();
        initToolbar(toolbar);
        setTitle("编辑领取物料");
        initsearch();
        LL_CODE = CommonTool.NewGuid();
        if (!CommonTool.isWifiOK(MuBanDetialActivity.this)) {
            switcher.showNetErrorView(MuBanDetialActivity.this);
        } else {
            new LoadTask().execute();
        }
    }

    void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle = intent.getExtras();
        DEPT_NAME = bundle.getString(Const.intent_deptname);
        STOCK_NAME = bundle.getString(Const.intent_ckname);
        MB_ID = bundle.getString(Const.intent_mbid);
    }

    void initView() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muBanDetialAdapter.readMaps();
                LING_WUZI ling_wuzi = new LING_WUZI();
                ling_wuzi.setLL_CODE(LL_CODE);
                ling_wuzi.setLL_DEPT(DEPT_NAME);
                ling_wuzi.setLL_STOCK(STOCK_NAME);
                LingLiaoTableUtil.getLiaoTableUtil().insertWuZi(ling_wuzi, muBanDetialAdapter.getLingWuZiDetial());
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                muBanDetialAdapter.clearEdFouce();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            getIntentData();
            ling_mb_detialList = MuBanTableUtil.getMuBanTableUtil().getLing_MB_Detial(MB_ID);
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
            if (ling_mb_detialList.size()>0) {
                EventBus.getDefault().post(new MessageEvent(Const.Success));
            } else {
                EventBus.getDefault().post(new MessageEvent(Const.Failue));
            }
        }
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if (event.message.equals(Const.Success)) {
            initAdapter(ling_mb_detialList);
        } else if (event.message.equals(Const.SaveSuccess)) {
            startActivity(HistoryActivity.class, false);
            finish();
        } else if (event.message.equals(Const.SaveFailue)) {
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
        } else {
            //// TODO: 2015.11.12 弹出框保存同步失败，重试

        }
    }

    void initAdapter(List<LING_MB_DETIAL> wuZi_tableList) {
        muBanDetialAdapter = new MuBanDetialAdapter(MuBanDetialActivity.this, wuZi_tableList);
        listView.setAdapter(muBanDetialAdapter);
        initView();
    }

    void initsearch() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                List<LING_MB_DETIAL> filteList = new ArrayList<LING_MB_DETIAL>();
                for(LING_MB_DETIAL ling_mb_detial : ling_mb_detialList){
                    Pattern pattern = Pattern.compile(newText,Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(ling_mb_detial.getDJX_WZ_NAME()+ling_mb_detial.getDJX_WZ_QUICK_CODE());
                    if(matcher.find()){
                        filteList.add(ling_mb_detial);
                        if(filteList.size()>20){
                            break;
                        }
                    }
                }
                muBanDetialAdapter.updateListView(filteList);
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
                muBanDetialAdapter.updateListView(ling_mb_detialList);
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
        EventBus.getDefault().register(MuBanDetialActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(MuBanDetialActivity.this);
    }

    @Override
    public void onErrorViewClicked() {
        new LoadTask().execute();
    }
}