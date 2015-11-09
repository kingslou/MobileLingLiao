package com.cyt.ieasy.fragments;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.cyt.ieasy.db.DeptTableUtil;
import com.cyt.ieasy.mobilelingliao.R;
import com.cyt.ieasy.tools.FlowLayout;
import com.ieasy.dao.Dept_Table;
import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;

/**
 *
 * Created by jin on 2015.11.06.
 */
public class DeptFragment extends BaseFragment {

    private String TAG = "fragment_dept";
    private View rootview;
    private List<Dept_Table> dept_tableList;
    private DeptAdapter deptAdapter;
    private List<String> stringList = new ArrayList<>();
    private ScrollView mScrollViewFilter;
    private FlowLayout mFlowLayoutFilter ;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootview==null){
            rootview = inflater.inflate(R.layout.fragment_dept, container, false);
            ButterKnife.bind(getActivity());
            listView = (ListView)rootview.findViewById(R.id.listViewDept);
            mScrollViewFilter = (ScrollView)rootview.findViewById(R.id.edit_tag);
            mFlowLayoutFilter = (FlowLayout)rootview.findViewById(R.id.flowLayout);
            mDrawableBuilder = TextDrawable.builder().round();
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
        initAdapter();
    }

    private void initAdapter(){
        stringList = readDeptSelectList();
        dept_tableList = DeptTableUtil.getDeptTableUtil().getAlldata();
        deptAdapter = new DeptAdapter(getActivity(),dept_tableList);
        listView.setAdapter(deptAdapter);
    }

    public void addFilterTag() {
        final ArrayList<Dept_Table> arrFilterSelected = new ArrayList<>();
        mFlowLayoutFilter.removeAllViews();
        int length = dept_tableList.size();
        for (int i = 0; i < length; i++) {
            Dept_Table dept_table = dept_tableList.get(i);
            if(stringList.contains(dept_table.getINNERID())){
                arrFilterSelected.add(dept_table);
            }
        }
        if (stringList.size()!=0) {
            mScrollViewFilter.setVisibility(View.VISIBLE);
        } else {
            mScrollViewFilter.setVisibility(View.GONE);
        }
        int size = arrFilterSelected.size();
        LayoutInflater layoutInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < size; i++) {
            View view = layoutInflater.inflate(R.layout.dept_layout_tag, null);
            TextView tv = (TextView) view.findViewById(R.id.tvTag);
            LinearLayout linClose = (LinearLayout) view.findViewById(R.id.linClose);
            final Dept_Table select_dept = arrFilterSelected.get(i);
            linClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //showToast(filter_object.name);
                    int innerSize = dept_tableList.size();
                    for (int j = 0; j < innerSize; j++) {
                        Dept_Table dept_table1 = dept_tableList.get(j);
                        if (dept_table1.getDEPTNAME().equalsIgnoreCase(select_dept.getDEPTNAME())) {
                            stringList.remove(dept_table1.getINNERID());
                        }
                    }
                    addFilterTag();
                    deptAdapter.updateListView(arrFilterSelected);
                }
            });

            tv.setText(select_dept.getDEPTNAME());
            int color = getResources().getColor(R.color.accent);
            View newView = view;
            newView.setBackgroundColor(color);
            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            params.topMargin = 5;
            params.leftMargin = 10;
            params.bottomMargin = 5;
            newView.setLayoutParams(params);
            mFlowLayoutFilter.addView(newView);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    class DeptAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private Context context;
        private List<Dept_Table> dept_tableList = new ArrayList<>();
        public DeptAdapter(Context context,List<Dept_Table> dept_tableList){
            this.context = context;
            this.dept_tableList = dept_tableList;
            inflater = LayoutInflater.from(context);
        }

        public void updateListView(List<Dept_Table> dept_tableList){
            this.dept_tableList = dept_tableList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return dept_tableList.size();
        }

        @Override
        public Object getItem(int position) {
            return dept_tableList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView==null){
                convertView = inflater.inflate(R.layout.dept_layout_item,null);
                viewHolder = new ViewHolder();
                viewHolder.mDeptName =(TextView)convertView.findViewById(R.id.deptName);
                viewHolder.mDeptSelected = (TextView)convertView.findViewById(R.id.deptSelected);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.iv);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            final Dept_Table dept_table = dept_tableList.get(position);
            viewHolder.mDeptName.setText(dept_table.getDEPTNAME());
            if(stringList.contains(dept_table.getINNERID())){
                viewHolder.mDeptSelected.setVisibility(View.VISIBLE);
            }else{
                viewHolder.mDeptSelected.setVisibility(View.GONE);
            }
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(dept_table.getDEPTNAME().charAt(0)),mColorGenerator.getColor(position));
            viewHolder.imageView.setImageDrawable(drawable);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stringList.contains(dept_table.getINNERID())){
                        stringList.remove(dept_table.getINNERID());
                    }else{
                        stringList.add(dept_table.getINNERID());
                    }
//                    mScrollViewFilter.setVisibility(View.VISIBLE);
//                    addFilterTag();
                    notifyDataSetChanged();
                    saveSelectList(stringList,deptType);
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
