package com.cyt.ieasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cyt.ieasy.mobilelingliao.R;
import com.ieasy.dao.LING_MB;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 领料模板
 * Created by jin on 2015.11.18.
 */
public class MuBanAdapter extends BaseAdapter {
    private Context context;
    public List<LING_MB> ling_mbList;
    private LayoutInflater layoutInflater;
    private TextDrawable.IBuilder mDrawableBuilder;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    public MuBanAdapter(Context context , List<LING_MB> ling_mbList){
        this.context = context;
        this.ling_mbList = ling_mbList;
        layoutInflater = LayoutInflater.from(context);
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void updateListView(List<LING_MB> ling_mbList){
        this.ling_mbList = ling_mbList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ling_mbList.size();
    }

    @Override
    public Object getItem(int position) {
        return ling_mbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = layoutInflater.inflate(R.layout.mb_item_layout,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final LING_MB ling_mb = ling_mbList.get(position);
        viewHolder.mbName.setText(ling_mb.getDJ_NAME());
        String charat;
        try{
            charat = ling_mb.getDJ_NAME().substring(0,1);
        }catch(Exception e){
            e.printStackTrace();
            charat = "#";
        }
        TextDrawable drawable = mDrawableBuilder.build(charat,mColorGenerator.getColor(charat));
        viewHolder.imageView.setImageDrawable(drawable);
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.mb_icon)
        ImageView imageView;
        @Bind(R.id.mbName)
        TextView mbName;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
