package com.cyt.ieasy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cyt.ieasy.mobilelingliao.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 未上传的领料单
 * Created by jin on 2015.11.16.
 */
public class NoUploadFragment extends BaseFragment {

    private boolean isPrepared;
    private View rootView;
    @Bind(R.id.hiscontentlist)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.layout_his_content,container,false);
            ButterKnife.bind(this, rootView);
            isPrepared = true;
            lazyLoad();
        }
        ViewGroup parent = (ViewGroup)rootView.getParent();
        if(parent!=null){
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void initAdapter(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {

    }
}
