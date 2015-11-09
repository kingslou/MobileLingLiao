package com.cyt.ieasy.fragments;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.cyt.ieasy.db.StockTableUtil;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.FlowLayout;
import com.ieasy.dao.WUZI_STOCK;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by jin on 2015.11.06.
 */
public class StockFragment extends BaseFragment {

    private String TAG = "fragment_dept";
    private View rootview;
    private List<WUZI_STOCK> stock_tableList;
    private StockAdapter stockAdapter;
    private List<String> stringList = new ArrayList<>();
    @Bind(R.id.edit_tag)
    ScrollView mScrollViewFilter;
    @Bind(R.id.flowLayout)
    FlowLayout mFlowLayoutFilter ;
    @Bind(R.id.listViewStock)
    ListView listView;
    private boolean isPrepared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootview==null){
            rootview = inflater.inflate(R.layout.fragment_stock, container, false);
            ButterKnife.bind(this,rootview);
            mDrawableBuilder = TextDrawable.builder().round();
            isPrepared = true;
            lazyLoad();
        }
        Log.i(TAG, "DeptonCreateView");
        ViewGroup parent = (ViewGroup)rootview.getParent();
        if(parent!=null){
            parent.removeView(rootview);
        }
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initAdapter(){
        stringList = readStockSelectList();
        stock_tableList = StockTableUtil.getStockTableUtil().getAlldata();
        stockAdapter = new StockAdapter(getActivity(),stock_tableList);
        listView.setAdapter(stockAdapter);
    }

    @Override
    protected void lazyLoad() {
        if(!isPrepared||isVisible){
            initAdapter();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class StockAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;
        private List<WUZI_STOCK> stock_tableList = new ArrayList<>();
        public StockAdapter(Context context, List<WUZI_STOCK> dept_tableList){
            this.context = context;
            this.stock_tableList = dept_tableList;
            inflater = LayoutInflater.from(context);
        }

        public void updateListView(List<WUZI_STOCK> dept_tableList){
            this.stock_tableList = dept_tableList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return stock_tableList.size();
        }

        @Override
        public Object getItem(int position) {
            return stock_tableList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.stock_layout_item,null);
                viewHolder = new ViewHolder();
                viewHolder.mDeptName =(TextView)convertView.findViewById(R.id.stockName);
                viewHolder.mDeptSelected = (TextView)convertView.findViewById(R.id.stockSelected);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final WUZI_STOCK stock_table = stock_tableList.get(position);
            viewHolder.mDeptName.setText(stock_table.getCK_NAME());
            if(stringList.contains(stock_table.getCK_ID())){
                viewHolder.mDeptSelected.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mDeptSelected.setVisibility(View.GONE);
            }
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(stock_table.getCK_NAME().charAt(0)), mColorGenerator.getColor(position));
            viewHolder.imageView.setImageDrawable(drawable);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stringList.contains(stock_table.getCK_ID())){
                        stringList.remove(stock_table.getCK_ID());
                    }else{
                        stringList.add(stock_table.getCK_ID());
                    }
                    notifyDataSetChanged();
                    saveSelectList(stringList,stockType);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            TextView mDeptName, mDeptSelected;
            ImageView imageView;
        }
    }
}
