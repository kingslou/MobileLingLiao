package com.cyt.ieasy.mobilelingliao;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.cyt.ieasy.adapter.HistoryAdapter;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.event.MessageEvent;
import com.ieasy.dao.LING_WUZI;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by jin on 2015.11.02.
 */
public class HistoryActivity extends BaseActivity {

    @Bind(R.id.listhistory)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private HistoryAdapter historyAdapter;
    private List<LING_WUZI> ling_wuziList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        ButterKnife.bind(this);
        initToolbar(toolbar);
        setTitle("历史记录");
        new LoadHistory().execute();
    }

    void initAdapter(){
        historyAdapter = new HistoryAdapter(HistoryActivity.this,ling_wuziList);
        listView.setAdapter(historyAdapter);
    }

    class LoadHistory extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                ling_wuziList = LingLiaoTableUtil.getLiaoTableUtil().getLingWuzi(20);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndeterminateProgressDialog(false,"获取历史数据中···");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(ling_wuziList!=null&&ling_wuziList.size()!=0){
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
            //// TODO: 2015.11.13  显示错误界面
            //setContentView(EmportyUtils.netFailView(HistoryActivity.this));
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(HistoryActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(HistoryActivity.this);
    }
}
