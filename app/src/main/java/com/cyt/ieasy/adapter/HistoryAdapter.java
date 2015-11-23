package com.cyt.ieasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.mobilelingliao.HistoryContent;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.TimeUtils;
import com.cyt.ieasy.update.UpdateServer;
import com.daimajia.swipe.SwipeLayout;
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
    private SwipeLayout myswipeLayout;

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
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewWithTag("Bottom3"));
            viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout swipeLayout) {

                }
                @Override
                public void onOpen(SwipeLayout swipeLayout) {
                    if(myswipeLayout!=swipeLayout&&myswipeLayout.isShown()){
                        myswipeLayout.close();
                    }
                    myswipeLayout = swipeLayout;
                }
                @Override
                public void onStartClose(SwipeLayout swipeLayout) {

                }
                @Override
                public void onClose(SwipeLayout swipeLayout) {

                }
                @Override
                public void onUpdate(SwipeLayout swipeLayout, int i, int i1) {

                }
                @Override
                public void onHandRelease(SwipeLayout swipeLayout, float v, float v1) {

                }
            });
            myswipeLayout = viewHolder.swipeLayout;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final LING_WUZI ling_wuzi = ling_wuziList.get(position);
        viewHolder.histitle.setText(ling_wuzi.getLL_NAME());
        viewHolder.histime.setText(TimeUtils.getDateStr(ling_wuzi.getADDTIME()) + "");
        viewHolder.operatordept.setText(ling_wuzi.getLL_DEPT());
        viewHolder.operator.setText(ling_wuzi.getLL_OPERATOR());
        viewHolder.ckname.setText(ling_wuzi.getLL_STOCK()+"");
        if(null==ling_wuzi.getLL_RETURNCODE()){
            viewHolder.status.setText("未同步");
            viewHolder.btn_send.setVisibility(View.VISIBLE);
        }else{
            viewHolder.btn_send.setVisibility(View.GONE);
            viewHolder.status.setText("已同步");
            viewHolder.histitle.setText(ling_wuzi.getLL_RETURNCODE());
        }
        viewHolder.clickitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("LL_CODE",ling_wuzi.getLL_CODE());
                if(StringUtils.isBlank(ling_wuzi.getLL_RETURNCODE())){
                    intent.putExtra("LL_STATUS","0");
                    intent.putExtra("LL_Name",ling_wuzi.getLL_NAME());
                }else{
                    intent.putExtra("LL_STATUS","1");
                    intent.putExtra("LL_Name",ling_wuzi.getLL_RETURNCODE());
                }
                intent.setClass(context, HistoryContent.class);
                context.startActivity(intent);
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ling_wuziList.remove(ling_wuzi);
                LingLiaoTableUtil.getLiaoTableUtil().deleteLingWuZi(ling_wuzi);
                viewHolder.swipeLayout.close();
                updateListView(ling_wuziList);
            }
        });

        viewHolder.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateServer.getUpdateServer().updateToServer(ling_wuzi.getLL_CODE(),context);
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
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;
        @Bind(R.id.trash)
        ImageView trash;
        @Bind(R.id.delete)
        Button delete;
        @Bind(R.id.ckname)
        TextView ckname;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
