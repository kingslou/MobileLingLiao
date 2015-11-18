package com.cyt.ieasy.adapter;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cyt.ieasy.db.WuZiTableUtil;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.ieasy.dao.LING_MB_DETIAL;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.ieasy.dao.WuZi_Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import butterknife.Bind;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitTextView;

/**
 * Created by jin on 2015.11.18.
 */
public class MuBanDetialAdapter extends BaseAdapter {
    private Context context;
    private List<LING_MB_DETIAL> wuZi_tableList = new ArrayList<>();
    private HashMap<String,String> numberMap;
    private HashMap<String,String> tzsMap;
    private HashMap<String,String[]> testMap;
    private LayoutInflater layoutInflater;
    private EditText currentFouce;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String LL_Code;
    public MuBanDetialAdapter(Context context,List<LING_MB_DETIAL> wuZi_tableList){
        this.context = context;
        currentFouce = new EditText(context);
        this.wuZi_tableList = wuZi_tableList;
        numberMap = new HashMap<String,String>();
        tzsMap = new HashMap<String,String>();
        testMap = new HashMap<>();
        mDrawableBuilder = TextDrawable.builder().round();
        layoutInflater = LayoutInflater.from(context);
    }

    public void setLL_Code(String LL_Code){
        this.LL_Code = LL_Code;
    }

    public void updateListView(List<LING_MB_DETIAL> wuZi_tableList){
        this.wuZi_tableList = wuZi_tableList;
        notifyDataSetChanged();
    }

    public void clearEdFouce(){
        if(currentFouce!=null){
            if(currentFouce.hasFocus()){
                currentFouce.clearFocus();
            }
        }
    }

    public void removeTextChange(){

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
//        WuZi_Table wuZi_table = wuZi_tableList.get(position);
        LING_MB_DETIAL ling_mb_detial = wuZi_tableList.get(position);
        MyWatcher myWatcher = new MyWatcher(viewHolder,ling_mb_detial);
        viewHolder.wzName.setText(ling_mb_detial.getDJX_WZ_NAME());
        String numbertext = numberMap.get(ling_mb_detial.getDJX_WZ_ID());
        String tzstext = tzsMap.get(ling_mb_detial.getDJX_WZ_ID());
        viewHolder.editNum.setText(numbertext==null?"":numbertext);
        viewHolder.editTzs.setText(tzstext==null?"":tzstext);
        viewHolder.guige.setText(ling_mb_detial.getDJX_WZ_SP());
        viewHolder.unitName.setText(ling_mb_detial.getDJX_UNIT_NAME());
        String charat;
        try{
            charat = ling_mb_detial.getDJX_WZ_QUICK_CODE().substring(0, 1);
        }catch(Exception e){
            charat="#";
            e.printStackTrace();
        }
        TextDrawable drawable = mDrawableBuilder.build(charat,mColorGenerator.getColor(charat));
        viewHolder.image.setImageDrawable(drawable);
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
                viewHolder.editNum.selectAll();
            }else{
                viewHolder.editNum.clearFocus();
                viewHolder.editNum.removeTextChangedListener(myWatcher);
            }

            if(viewHolder.editTzs.hasFocus()){
                currentFouce = viewHolder.editTzs;
                viewHolder.editTzs.addTextChangedListener(myWatcher);
                viewHolder.editTzs.setSelectAllOnFocus(true);
                viewHolder.editTzs.selectAll();
            }else{
                viewHolder.editTzs.clearFocus();
                viewHolder.editTzs.removeTextChangedListener(myWatcher);
            }
        }
    }

    class MyWatcher implements TextWatcher {
        ViewHolder viewHolder;
        LING_MB_DETIAL ling_mb_detial;
        EditText editText;
        String[] testArray = new String[2];
        public MyWatcher(ViewHolder viewHolder,LING_MB_DETIAL ling_mb_detial){
            this.viewHolder = viewHolder;
            this.ling_mb_detial = ling_mb_detial;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            MyLogger.showLogWithLineNum(5, "滑动");
            if(viewHolder.editNum.hasFocus()){
                numberMap.put(ling_mb_detial.getDJX_WZ_ID(), s.toString());
                testArray[0] = s.toString();
                testArray[1] = viewHolder.editTzs.getText().toString();
                testMap.put(ling_mb_detial.getDJX_WZ_ID(),testArray);
            }
            if(viewHolder.editTzs.hasFocus()){
                tzsMap.put(ling_mb_detial.getDJX_WZ_ID(),s.toString());
                testArray[0] = viewHolder.editNum.getText().toString();
                testArray[1]=s.toString();
                testMap.put(ling_mb_detial.getDJX_WZ_ID(),testArray);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public void saveLoacl(){
        if(numberMap.size()!=0){

        }
    }

    public List<LING_WUZIDETIAL> getLingWuZiDetial(){
        List<LING_WUZIDETIAL> ling_wuzidetials = new ArrayList<>();
        Iterator iterator = testMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            String mapkey = entry.getKey().toString();
            String[] myArray = (String[])entry.getValue();
            LING_WUZIDETIAL ling_wuzidetial = new LING_WUZIDETIAL();
            String num = myArray[0];
            String tzs = myArray[1];
            if(StringUtils.isBlank(num)&&StringUtils.isBlank(tzs)){
                //不加入
            }else{
                if(StringUtils.isBlank(tzs)){
                    ling_wuzidetial.setLL_TZS(0);
                }else{
                    ling_wuzidetial.setLL_TZS(Integer.parseInt(tzs));
                }
                if(StringUtils.isBlank(num)){
                    ling_wuzidetial.setLL_NUM(0.0);
                }else{
                    ling_wuzidetial.setLL_NUM(Double.parseDouble(num));
                }
                WuZi_Table wuZi_table = WuZiTableUtil.getWuZiTableUtil().getEntity(mapkey);
                ling_wuzidetial.setLL_CODE(LL_Code);
                ling_wuzidetial.setLL_WZ_CATEGORY(wuZi_table.getWZ_CATEGORY());
                ling_wuzidetial.setLL_WZ_ID(mapkey);
                ling_wuzidetial.setLL_WZ_QUICKCODE(wuZi_table.getWZ_QUICK_CODE());
                ling_wuzidetial.setLL_WZ_GUIGE(wuZi_table.getWZ_SPECIFICATION());
                ling_wuzidetial.setLL_WZ_NAME(wuZi_table.getWZ_NAME());
                ling_wuzidetial.setLL_WZ_CATEGORY_ID(wuZi_table.getWZ_SZ_ID());
                ling_wuzidetials.add(ling_wuzidetial);
            }
            MyLogger.showLogWithLineNum(5,"测试"+mapkey+"记录"+myArray[0]+myArray[1]);
        }
        return  ling_wuzidetials;
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
        AutofitTextView wzName;
        @Bind(R.id.unitname)
        TextView unitName;
        @Bind(R.id.txtguige)
        AutofitTextView guige;
        @Bind(R.id.editnum)
        EditText editNum;
        @Bind(R.id.edittzs)
        EditText editTzs;
        @Bind(R.id.txtsort)
        ImageView image;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
