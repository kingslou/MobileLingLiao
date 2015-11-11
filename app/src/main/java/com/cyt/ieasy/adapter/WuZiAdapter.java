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
import com.cyt.ieasy.tools.MyLogger;
import com.ieasy.dao.WuZi_Table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jin on 2015.11.10.
 */
public class WuZiAdapter extends BaseAdapter {

    private Context context;
    private List<WuZi_Table> wuZi_tableList = new ArrayList<>();
    private List<WuZi_Table> wuZi_tables = new ArrayList<>();
    private HashMap<String,String> numberMap;
    private HashMap<String,String> tzsMap;
    private String[] numberArray;
    private String[] tzsArray;
    private LayoutInflater layoutInflater;
    private EditText currentFouce;
    public WuZiAdapter(Context context,List<WuZi_Table> wuZi_tableList){
        this.context = context;
        currentFouce = new EditText(context);
        this.wuZi_tableList = wuZi_tableList;
        numberMap = new HashMap<String,String>();
        tzsMap = new HashMap<String,String>();
        layoutInflater = LayoutInflater.from(context);
    }

    public void clearEdFouce(){
        if(currentFouce!=null){
            if(currentFouce.hasFocus()){
                currentFouce.clearFocus();
            }
        }
    }

    public void readMaps(){
        readMap();
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
        MyWatcher myWatcher = new MyWatcher(viewHolder,wuZi_table);
        viewHolder.wzName.setText(wuZi_table.getWZ_NAME());
        String numbertext = numberMap.get(wuZi_table.getWZ_ID());
        String tzstext = tzsMap.get(wuZi_table.getWZ_ID());
        viewHolder.editNum.setText(numbertext==null?"0":numbertext);
        viewHolder.editTzs.setText(tzstext==null?"0":tzstext);
        viewHolder.editNum.setOnFocusChangeListener(new MyFouceChange(viewHolder, myWatcher));
        viewHolder.editTzs.setOnFocusChangeListener(new MyFouceChange(viewHolder,myWatcher));
        return convertView;
    }

    class MyFouceChange implements View.OnFocusChangeListener{
        ViewHolder viewHolder;
        MyWatcher  myWatcher;
        public MyFouceChange(ViewHolder viewHolder,MyWatcher myWatcher){
            this.viewHolder = viewHolder;
            this.myWatcher = myWatcher;
        }
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(viewHolder.editNum.hasFocus()){
                currentFouce = viewHolder.editNum;
                viewHolder.editNum.addTextChangedListener(myWatcher);
                viewHolder.editNum.setSelectAllOnFocus(true);
            }else{
                viewHolder.editNum.clearFocus();
                viewHolder.editNum.removeTextChangedListener(myWatcher);
            }

            if(viewHolder.editTzs.hasFocus()){
                currentFouce = viewHolder.editTzs;
                viewHolder.editTzs.addTextChangedListener(myWatcher);
                viewHolder.editNum.setSelectAllOnFocus(true);
                viewHolder.editTzs.setSelection(0,viewHolder.editTzs.getText().toString().trim().length());
            }else{
                viewHolder.editTzs.clearFocus();
                viewHolder.editTzs.removeTextChangedListener(myWatcher);
            }
        }
    }

    class MyWatcher implements TextWatcher{
        ViewHolder viewHolder;
        WuZi_Table wuZi_table;
        EditText editText;
        public MyWatcher(ViewHolder viewHolder,WuZi_Table wuZi_table){
            this.viewHolder = viewHolder;
            this.wuZi_table = wuZi_table;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            MyLogger.showLogWithLineNum(5,"滑动");
            if(viewHolder.editNum.hasFocus()){
                MyLogger.showLogWithLineNum(5,"滑动监听");
                numberMap.put(wuZi_table.getWZ_ID(),s.toString());
            }
            if(viewHolder.editTzs.hasFocus()){
                tzsMap.put(wuZi_table.getWZ_ID(),s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    void readMap(){
        boolean result =false;
        Iterator iter = numberMap.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();
            String mapkey = entry.getKey().toString();
            String mapvalue = entry.getValue().toString();
            MyLogger.showLogWithLineNum(5,"key"+mapkey+"值"+mapvalue);
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
        int ref;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
