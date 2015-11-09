package com.cyt.ieasy.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jin on 2015.11.09.
 */
public abstract class BaseFragment extends Fragment {

    private static final int corePoolSize = 15;
    private static final int maximumPoolSize = 30;
    private static final int keepAliveTime = 10;
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    private static final Executor threadPoolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
    public TextDrawable.IBuilder mDrawableBuilder;
    public ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    public int deptType = 0;
    public int stockType = 1;
    protected boolean isVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //使用AsynTask自定义线程池
    public void addTaskInPool(AsyncTask asyncTask) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            asyncTask.executeOnExecutor(threadPoolExecutor);
        }else{
            asyncTask.execute();
        }
    }

    public List<String> readDeptSelectList(){
        List<String> resultlist = new ArrayList<>();
        String deptstr = CommonTool.getGlobalSetting(MyApplication.getContext(),Const.dept_filter,"");
        if(!StringUtils.isBlank(deptstr)){
            String[] depts = deptstr.split(",");
            for(String s :depts){
                resultlist.add(s);
            }
        }
        MyLogger.showLogWithLineNum(5, resultlist.size() + "dept");
        return resultlist;
    }

    public List<String> readStockSelectList(){
        List<String> resultlist = new ArrayList<>();
        String stockstr = CommonTool.getGlobalSetting(MyApplication.getContext(),Const.stock_filter,"");
        if(!StringUtils.isBlank(stockstr)){
            String[] stocks = stockstr.split(",");
            for(String s:stocks){
                resultlist.add(s);
            }
        }
        MyLogger.showLogWithLineNum(5,resultlist.size()+"stock");
        return resultlist;
    }

    public void saveSelectList(List<String> stringList,int type){
        if(stringList.size()!=0){
            String result = "";
            for(String s : stringList){
                result += s+",";
            }
            if(type==deptType){
                CommonTool.saveGlobalSetting(MyApplication.getContext(), Const.dept_filter,result);
            }else if(type==stockType){
                CommonTool.saveGlobalSetting(MyApplication.getContext(), Const.stock_filter,result);
            }
        }else{
            if(type==deptType){
                CommonTool.saveGlobalSetting(MyApplication.getContext(), Const.dept_filter,"");
            }else{
                CommonTool.saveGlobalSetting(MyApplication.getContext(),Const.stock_filter,"");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible  = true;
            onVisible();
        }else{
            isVisible  = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();

    protected void onInvisible(){}
}
