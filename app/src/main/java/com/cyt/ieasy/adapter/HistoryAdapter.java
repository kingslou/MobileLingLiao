package com.cyt.ieasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyt.ieasy.mobilelingliao.R;
import com.ieasy.dao.LING_WUZI;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 历史记录
 * Created by jin on 2015.11.12.
 */
public class HistoryAdapter extends BaseAdapter {
    private Context context;
    private List<LING_WUZI> ling_wuziList;
    private LayoutInflater layoutInflater;

    public HistoryAdapter(Context context ,List<LING_WUZI> ling_wuziList){
        this.context = context;
        this.ling_wuziList = ling_wuziList;
        layoutInflater = LayoutInflater.from(context);
    }

    public void updateListView(List<LING_WUZI> ling_wuziList){
        this.ling_wuziList = ling_wuziList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ling_wuziList.size();
    }

    @Override
    public Object getItem(int position) {
        return ling_wuziList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){

        }else{

        }
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.histitle)
        TextView histitle;
        @Bind(R.id.operatprdept)
        TextView operatordept;
        @Bind(R.id.operator)
        TextView operator;
        @Bind(R.id.histime)
        TextView histime;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
