package com.cyt.ieasy.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.cyt.ieasy.mobilelingliao.R;
import com.ieasy.dao.WuZi_Table;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.10.
 */
public class WuZiAdapter extends BaseAdapter {

    private Context context;
    private List<WuZi_Table> wuZi_tableList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    public WuZiAdapter(Context context,List<WuZi_Table> wuZi_tableList){
        this.context = context;
        this.wuZi_tableList = wuZi_tableList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wuZi_tableList.size();
    }

    @Override
    public Object getItem(int position) {
        return wuZi_tableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.layout_wz_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        WuZi_Table wuZi_table = wuZi_tableList.get(position);
        viewHolder.wzName.setText(wuZi_table.getWZ_NAME()+"坐标"+position);
        return convertView;
    }

    class MyWatcher implements TextWatcher{
        ViewHolder viewHolder;
        WuZi_Table wuZi_table;
        public MyWatcher(ViewHolder viewHolder,WuZi_Table wuZi_table){
            this.viewHolder = viewHolder;
            this.wuZi_table = wuZi_table;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(viewHolder.editNum.hasFocus()){
                notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

     class ViewHolder {

        @Bind(R.id.wzName)
        TextView wzName;
        @Bind(R.id.unitname)
        TextView unitName;
        @Bind(R.id.txtguige)
        TextView guige;
        @Bind(R.id.editnum)
        EditText editNum;
        @Bind(R.id.edittzs)
        EditText editTzs;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
