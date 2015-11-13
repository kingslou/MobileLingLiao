package com.cyt.ieasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cyt.ieasy.mobilelingliao.HistoryContent;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.TimeUtils;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_his_item,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final LING_WUZI ling_wuzi = ling_wuziList.get(position);
        viewHolder.histitle.setText(ling_wuzi.getLL_NAME());
        viewHolder.histime.setText(TimeUtils.getDateStr(ling_wuzi.getADDTIME()) + "");
        viewHolder.operatordept.setText(ling_wuzi.getLL_DEPT());
        viewHolder.operator.setText(ling_wuzi.getLL_OPERATOR());
        if(null==ling_wuzi.getLL_RETURNCODE()){
            viewHolder.status.setText("未同步");
            viewHolder.btn_send.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btn_send.setVisibility(View.GONE);
            viewHolder.status.setText("已同步");
        }
        viewHolder.clickitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(StringUtils.isBlank(ling_wuzi.getLL_RETURNCODE())){
//                    EventBus.getDefault().post(new LL_Event(ling_wuzi.getLL_CODE(), Const.UpdateFailue));
//                }else{
//                    EventBus.getDefault().post(new LL_Event(ling_wuzi.getLL_CODE(),Const.UpdateSuccess));
//                }
                Intent intent = new Intent();
                intent.putExtra("LL_CODE",ling_wuzi.getLL_CODE());
                intent.putExtra("LL_STATUS","0");
                intent.setClass(context, HistoryContent.class);
                context.startActivity(intent);
            }
        });
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
        @Bind(R.id.ll_status)
        TextView status;
        @Bind(R.id.btn_send)
        BootstrapButton btn_send;
        @Bind(R.id.clickitem)
        RelativeLayout clickitem;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
