package com.cyt.ieasy.mobilelingliao;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bowyer.app.fabtransitionlayout.FooterLayout;
import com.cyt.ieasy.adapter.HistoryDetialAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.event.UpdateEvent;
import com.cyt.ieasy.interfaces.OnErrorViewListener;
import com.cyt.ieasy.switcher.Switcher;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.update.UpdateServer;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by jin on 2015.11.13.
 */
public class HistoryContent extends BaseActivity implements OnErrorViewListener {

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
    private String LL_Name;//本地名称
    private String DEPT_NAME;
    private String STOCK_NAME;
    private Switcher switcher;
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
        setContentView(R.layout.layout_his_content);
        ButterKnife.bind(this);
        switcher = new Switcher.Builder().withContentView(findViewById(R.id.main))
                .withErrorView(findViewById(R.id.error_view))
                .withErrorLabel((TextView)findViewById(R.id.error_label))
                .withNetErrorView(findViewById(R.id.neterrorview))
                .build();
        initToolbar(toolbar);
        initdata();
        mFabToolbar.setFab(mFab);
    }

    @OnClick(R.id.updateserver)
    void onupdateClick(){
        iconAnim(updateserver);
        nowClick = updateserver;
        updateServer();
    }

    @OnClick(R.id.savelocal)
    void saveClick(){
        iconAnim(savelocal);
        nowClick = savelocal;
        new MaterialDialog.Builder(context)
                .title("保存")
                .content("确定保存修改吗?")
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
                        saveLoaclData();
                    }
                })
                .show();
    }

    @OnClick(R.id.cancle)
    void cancleClick(){
        iconAnim(cancle);
        nowClick = cancle;
        mFabToolbar.slideInFab();
    }
    @OnClick(R.id.fab)
    void fabOnClick(){
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabToolbar.expandFab();
            }
        });
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
                        saveLoaclData();
                    }
                })
                .show();
    }

    void saveLoaclData(){
        LING_WUZI ling_wuzi = new LING_WUZI();
        ling_wuzi.setLL_CODE(LL_CODE);
        //调用更新方法
        LingLiaoTableUtil.getLiaoTableUtil().updateWuZi(ling_wuzi, historyDetialAdapter.getLingWuZiDetial());
    }

    class LoadTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            ling_wuzidetialList = LingLiaoTableUtil.getLiaoTableUtil().getLingWuZiDetial(LL_CODE);
            MyLogger.showLogWithLineNum(5,"全部"+ling_wuzidetialList.size()+"条");

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

    public void onEvent(UpdateEvent event){
        if(event.result.equals(Const.UpdateServerSuccess)){
            new MaterialDialog.Builder(this)
                    .title("同步结果")
                    .content("同步成功，即将跳转")
                    .show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    EventBus.getDefault().post(new MessageEvent(Const.Cancle));
                }
            }, 800);
        }else{
            new MaterialDialog.Builder(this)
                    .title("同步结果")
                    .content("同步失败，请重试")
                    .positiveText("关闭")
                    .show();
        }
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if(event.message.equals(Const.Success)){
            initAdapter(ling_wuzidetialList);
        }else if(event.message.equals(Const.Failue)){
            //// TODO: 2015.11.13 显示自定义错误界面
            //setContentView(EmportyUtils.netFailView(HistoryContent.this));
        }else if(event.message.equals(Const.SaveSuccess)){
            if(nowClick!=null&&nowClick==updateserver){
                //继续进行同步操作
                UpdateServer.getUpdateServer().updateToServer(LL_CODE,context);
            }else {
                new MaterialDialog.Builder(this)
                        .title("修改结果")
                        .content("修改成功，即将关闭此页面")
                        .show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MessageEvent(Const.Cancle));
                    }
                }, 800);
            }

        }else if(event.message.equals(Const.SaveFailue)){
            new MaterialDialog.Builder(this)
                    .title("修改结果")
                    .content("修改保存失败，请重试")
                    .positiveText("关闭")
                    .show();
        }else if(event.message.equals(Const.Cancle)){
            dismiss();
            finish();
        }
    }

    void initView(){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                historyDetialAdapter.clearEdFouce();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount + firstVisibleItem == totalItemCount) {

                }
            }
        });
    }

    void initdata(){
        LL_CODE = getIntent().getExtras().getString("LL_CODE");
        LL_STATUS =Integer.parseInt(getIntent().getExtras().getString("LL_STATUS"));
        LL_Name = getIntent().getExtras().getString("LL_Name");
        if(LL_STATUS==1){
            mFab.setVisibility(View.INVISIBLE);
        }
        setTitle(LL_Name);
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
                //// TODO: 2015.11.16  从内存中直接查
                List<LING_WUZIDETIAL> FilterList = new ArrayList<LING_WUZIDETIAL>();
                for(LING_WUZIDETIAL lingWuzidetial : ling_wuzidetialList){
                    Pattern pattern = Pattern.compile(newText,Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(lingWuzidetial.getLL_WZ_NAME()+lingWuzidetial.getLL_WZ_QUICKCODE());
                    if(matcher.find()){
                        FilterList.add(lingWuzidetial);
                        //每次只查20个 超过
                        if(FilterList.size()>20){
                            break;
                        }
                    }
                }
                historyDetialAdapter.updateListView(FilterList);
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
        showback();
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
            showback();
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

    @Override
    public void onErrorViewClicked() {
        Toast.makeText(HistoryContent.this,"点击了",Toast.LENGTH_SHORT).show();
    }
}
