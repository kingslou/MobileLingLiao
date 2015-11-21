package com.cyt.ieasy.mobilelingliao;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cyt.ieasy.adapter.MuBanAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.MuBanTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.interfaces.OnErrorViewListener;
import com.cyt.ieasy.switcher.Switcher;
import com.cyt.ieasy.tools.ActivityManager;
import com.cyt.ieasy.tools.CommonTool;
import com.ieasy.dao.LING_MB;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 选择模板界面
 * Created by jin on 2015.11.18.
 */
public class MuBanActivity extends BaseActivity implements OnErrorViewListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_view)
    MaterialSearchView searchView;
    @Bind(R.id.listmuban)
    ListView listView;
    private Switcher switcher;
    private MuBanAdapter muBanAdapter;
    private List<LING_MB> ling_mbList;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_muban);
        ButterKnife.bind(this);
        switcher = new Switcher.Builder().withContentView(findViewById(R.id.content))
                .withErrorView(findViewById(R.id.error_view))
                .withErrorLabel((TextView)findViewById(R.id.error_label))
                .withNetErrorView(findViewById(R.id.neterrorview))
                .build();
        ActivityManager.getActivityManager().pushActivity2Stack(this);
        initToolbar(toolbar);
        setTitle("选择领料模板");
        if(CommonTool.isWifiOK(MuBanActivity.this)){
            new Load_MB().execute();
        }else{
            switcher.showNetErrorView(MuBanActivity.this);
        }
        getData();
    }

    void getData(){
        Intent intent = getIntent();
        bundle = intent.getExtras();
    }

    void initAdapter(){
        muBanAdapter = new MuBanAdapter(MuBanActivity.this,ling_mbList);
        listView.setAdapter(muBanAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LING_MB ling_mb = muBanAdapter.ling_mbList.get(position);
                Intent intent = new Intent();
                bundle.putString(Const.intent_mbid,ling_mb.getDJ_ID());
                intent.putExtras(bundle);
                intent.setClass(MuBanActivity.this,MuBanDetialActivity.class);
                startActivity(intent);
            }
        });
    }

    class Load_MB extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                ling_mbList = new ArrayList<>();
                ling_mbList = MuBanTableUtil.getMuBanTableUtil().getLing_Mb();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndeterminateProgressDialog(false, "加载模板");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ling_mbList.size()>0){
                EventBus.getDefault().post(new MessageEvent(Const.Success));
            }else{
                EventBus.getDefault().post(new MessageEvent(Const.Failue));
            }
        }
    }

    public void onEvent(MessageEvent event) {
        dismiss();
        if(event.message.equals(Const.Success)){
            initAdapter();
        }else if(event.message.equals(Const.Failue)){
            switcher.showErrorView("没有获取到数据",MuBanActivity.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onErrorViewClicked() {
        new Load_MB().execute();
    }
}
