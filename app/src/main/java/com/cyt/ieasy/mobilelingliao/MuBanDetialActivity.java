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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.cyt.ieasy.adapter.MuBanDetialAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.db.MuBanDetialTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.event.UpdateEvent;
import com.cyt.ieasy.interfaces.OnErrorViewListener;
import com.cyt.ieasy.switcher.Switcher;
import com.cyt.ieasy.tools.ActivityManager;
import com.cyt.ieasy.tools.Arith;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.update.UpdateServer;
import com.ieasy.dao.LING_MB_DETIAL;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private List<LING_MB_DETIAL> ling_mb_detialList = new ArrayList<>();
    private String LL_CODE;
    private String DEPT_NAME = "";
    private String STOCK_NAME = "";
    private String MB_ID = "";//模板ID
    private String LL_SELECT_TIME;//选择的领料时间
    private HashMap<String,String[]> map = new HashMap<>();
    @Bind(R.id.fabtoolbar)
    FooterLayout mFabToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.updateserver)
    RelativeLayout updateserver;
    @Bind(R.id.savelocal)
    RelativeLayout savelocal;
    @Bind(R.id.cancle)
    RelativeLayout cancle;
    private View nowClick;
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
        mFabToolbar.setFab(mFab);
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
        LL_SELECT_TIME = bundle.getString(Const.intent_time);
        MB_ID = bundle.getString(Const.intent_mbid);
    }

    @OnClick(R.id.updateserver)
    void onupdateClick(){
        nowClick = updateserver;
        iconAnim(updateserver);
        updateServer();
    }

    @OnClick(R.id.savelocal)
    void saveClick(){
        nowClick = savelocal;
        iconAnim(savelocal);
        saveLoaclData();
    }

    @OnClick(R.id.cancle)
    void cancleClick(){
        nowClick = cancle;
        iconAnim(cancle);
        mFabToolbar.slideInFab();
    }

    void updateServer(){
        new MaterialDialog.Builder(context)
                .title("同步")
                .content("确定同步到服务端吗")
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
                        //// TODO: 2015.11.20 同步到服务端代码
                        saveData();
                    }
                })
                .show();
    }

    void saveLoaclData(){
        new MaterialDialog.Builder(context)
                .title("保存")
                .content("确定保存吗")
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
                        saveData();
                    }
                })
                .show();
    }

    /**
     * 保存
     */
    void saveData(){
        if(LingLiaoTableUtil.getLiaoTableUtil().isexist(LL_CODE)){
            UpdateServer.getUpdateServer().updateToServer(LL_CODE,context);
        }else{
            LING_WUZI ling_wuzi = new LING_WUZI();
            ling_wuzi.setLL_CODE(LL_CODE);
            ling_wuzi.setLL_DEPT(DEPT_NAME);
            ling_wuzi.setLL_STOCK(STOCK_NAME);
            ling_wuzi.setLL_OPERATOR(CommonTool.getGlobalSetting(context,Const.cachuser));
            ling_wuzi.setLL_SELECT_TIME(LL_SELECT_TIME);
            LingLiaoTableUtil.getLiaoTableUtil().insertWuZi(ling_wuzi, muBanDetialAdapter.getLingWuZiDetial());
        }
    }

    void initView() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabToolbar.expandFab();
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
            ling_mb_detialList = new ArrayList<>();
            ling_mb_detialList = MuBanDetialTableUtil.getMuBanDetialTableUtil().getLing_MB_Detial(MB_ID);
            MyLogger.showLogWithLineNum(5,"模板详细"+JSON.toJSONString(ling_mb_detialList));
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

    public void onEvent(UpdateEvent event){
        if(event.result.equals(Const.UpdateServerSuccess)){
            Intent intent = new Intent();
            intent.setClass(MuBanDetialActivity.this, HistoryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else{
            //// TODO: 2015.11.21 当同步失败的时候，弹出一个框，保存本地或者重试，不允许再操作当前界面
            new MaterialDialog.Builder(context)
                    .title("同步结果")
                    .content("同步失败，请重试或者先把订单保存至本地")
                    .positiveText(R.string.retry)
                    .negativeText(R.string.savelocal)
                    .cancelable(false)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            materialDialog.dismiss();
                            toHistory();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            saveData();
                        }
                    })
                    .show();
        }
    }

    /**
     * 跳转到历史记录
     */
    private void toHistory(){
        ActivityManager.getActivityManager().popAllActivityFromStack();
        Intent intent = new Intent();
        intent.setClass(MuBanDetialActivity.this, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if (event.message.equals(Const.Success)) {
            initAdapter(ling_mb_detialList,map);
        } else if (event.message.equals(Const.SaveSuccess)) {
            if(nowClick!=null&&nowClick==updateserver){
                UpdateServer.getUpdateServer().updateToServer(LL_CODE,context);
            }else if(nowClick!=null&&nowClick==savelocal){
                toHistory();
            }
        } else if (event.message.equals(Const.SaveFailue)) {
            new MaterialDialog.Builder(context)
                    .title("保存")
                    .content("保存失败,是否重试")
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
                            saveData();
                        }
                    })
                    .show();
        }
    }

    void initAdapter(List<LING_MB_DETIAL> wuZi_tableList,HashMap<String,String[]> map) {
        muBanDetialAdapter = new MuBanDetialAdapter(MuBanDetialActivity.this, wuZi_tableList,map);
        listView.setAdapter(muBanDetialAdapter);
        initView();
        muBanDetialAdapter.setLL_Code(LL_CODE);
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
                for (LING_MB_DETIAL ling_mb_detial : ling_mb_detialList) {
                    Pattern pattern = Pattern.compile(newText, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(ling_mb_detial.getDJX_WZ_NAME() + ling_mb_detial.getDJX_WZ_QUICK_CODE());
                    if (matcher.find()) {
                        filteList.add(ling_mb_detial);
                        if (filteList.size() > 20) {
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
                muBanDetialAdapter.updateListView(ling_mb_detialList);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==REQUEST_CODE_DEFAULT){
            //// TODO: 2015.11.19  从全部物料中添加过来的物料
            String intentwuzi = data.getExtras().getString("intentwuzi");
            List<LING_WUZIDETIAL> ling_wuzidetialList =JSON.parseArray(intentwuzi,LING_WUZIDETIAL.class);
            MyLogger.showLogWithLineNum(5,"fastJSON"+intentwuzi+"个数"+ling_wuzidetialList.size());
            new Load_IntentData(intentwuzi).execute();
        }
    }

    boolean isexist(String WZ_ID){
        boolean result = false;
        for(LING_MB_DETIAL ling_mb_detial : ling_mb_detialList){
            if(ling_mb_detial.getDJX_WZ_ID().equals(WZ_ID)){
                ling_mb_detialList.remove(ling_mb_detial);
                result = true;
                break;
            }
        }
        return result;
    }

    class Load_IntentData extends AsyncTask<Void,Void,Void>{
        private String intentdata;
        private List<LING_WUZIDETIAL> ling_wuzidetialList;
        private HashMap<String,String[]> mymap;
        public Load_IntentData(String intentdata){
            this.intentdata = intentdata;
        }
        @Override
        protected Void doInBackground(Void... params) {
            ling_wuzidetialList =JSON.parseArray(intentdata, LING_WUZIDETIAL.class);
            mymap = muBanDetialAdapter.getMap();
            for(LING_WUZIDETIAL ling_wuzidetial : ling_wuzidetialList){
                LING_MB_DETIAL ling_mb_detial = new LING_MB_DETIAL();
                ling_mb_detial.setDJX_WZ_ID(ling_wuzidetial.getLL_WZ_ID());
                ling_mb_detial.setDJX_WZ_NAME(ling_wuzidetial.getLL_WZ_NAME());
                ling_mb_detial.setDJX_WZ_SP(ling_wuzidetial.getLL_WZ_GUIGE());
                ling_mb_detial.setDJX_WC_ID(ling_wuzidetial.getLL_WZ_CATEGORY_ID());
                ling_mb_detial.setDJX_WZ_QUICK_CODE(ling_wuzidetial.getLL_WZ_QUICKCODE());
                ling_mb_detial.setDJX_UNIT_NAME(ling_wuzidetial.getLL_WZ_UNITNAME());
                ling_mb_detial.setDJX_UNIT_ID(ling_wuzidetial.getLL_WZ_UNITID());
                ling_mb_detial.setDJX_REMARK(ling_wuzidetial.getLL_WZ_REMARK());
                ling_mb_detial.setDJX_WZ_NUM(Arith.getDecimalString(ling_wuzidetial.getLL_NUM()));
                ling_mb_detial.setDJX_WZ_TZS(ling_wuzidetial.getLL_TZS() == 0 ? "" : Arith.getDecimalString(ling_wuzidetial.getLL_TZS()));
                isexist(ling_wuzidetial.getLL_WZ_ID());
                ling_mb_detialList.add(ling_mb_detial);
                String[] tempArray = new String[2];
                tempArray[0] = ling_wuzidetial.getLL_NUM()+"";
                tempArray[1] = ling_wuzidetial.getLL_TZS()+"";
                mymap.put(ling_wuzidetial.getLL_WZ_ID(),tempArray);
            }
            MyLogger.showLogWithLineNum(5, "条目数" + ling_wuzidetialList.size());
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            muBanDetialAdapter.updateListView(ling_mb_detialList);
            muBanDetialAdapter.updateMap(mymap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_mbdetial, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.menu_add){
            new MaterialDialog.Builder(context)
                    .title("添加物料")
                    .content("确定添加其他物料吗?")
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
                            startActivity(AddotherWuZiActivity.class, true);
                        }
                    })
                    .show();
        }else {
            new MaterialDialog.Builder(context)
                    .title("退出")
                    .content("确定退出模板吗?")
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
    protected void onDestroy() {
        super.onDestroy();
        muBanDetialAdapter.getMap().clear();
        muBanDetialAdapter.updateListView(new ArrayList<LING_MB_DETIAL>());
    }

    @Override
    public void onErrorViewClicked() {
        new LoadTask().execute();
    }
}