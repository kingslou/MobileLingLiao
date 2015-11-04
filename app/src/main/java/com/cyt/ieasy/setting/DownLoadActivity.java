package com.cyt.ieasy.setting;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.entity.UpdateStatus;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.interfaces.Callback;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.SystemUtils;
import com.cyt.ieasy.tools.WebServiceTool;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * 基础资料下载
 * Created by jin on 2015.11.02.
 */
public class DownLoadActivity {

    private static DownLoadActivity instance;
    private Context context;
    private String  Base_Service;
    private String  Scm_Service;
    private String  IP_Config;
    private String  ErrorMessage;
    public static final Executor THREAD_POOL_EXECUTOR = Executors
            .newFixedThreadPool(SystemUtils.DEFAULT_THREAD_POOL_SIZE);

    private DownLoadActivity(Callback<UpdateStatus> callback){
        IP_Config = "http://"+ CommonTool.getGlobalSetting(MyApplication.getContext(), Const.ipconfig)
                +":"+CommonTool.getGlobalSetting(MyApplication.getContext(),Const.portconfig);
        Base_Service = IP_Config + "/webserver/WebService/BaseService.asmx";
        Scm_Service  = IP_Config+"/webserver/WebService/ScmService.asmx";
    }
    public static DownLoadActivity getInstance(Callback<UpdateStatus> callback){
        if(null==instance){
            instance = new DownLoadActivity(callback);
        }
        return instance;
    }

    public void cancleTask(){

    }

    public void loadData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new SynServerTime().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_WuZi().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Dept().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_WuZi_Catoge().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Stock().executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else{
            new SynServerTime().execute();
            new Load_WuZi().execute();
            new Load_Dept().execute();
            new Load_WuZi_Catoge().execute();
            new Load_Stock().execute();
        }
    }

    class SynServerTime extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                MyLogger.showLogWithLineNum(5,"开始获取服务器时间");
                Object serverTime =
                        WebServiceTool.callWebservice(Base_Service, "GetServerTimeForPad", new String[] {},
                                new Object[] {});
                if(null!=serverTime){
                    MyLogger.showLogWithLineNum(5,serverTime.toString());
                    CommonTool.saveGlobalSetting(MyApplication.getContext(),Const.updatetime,serverTime.toString().trim());
                }else{
                    ErrorMessage = "获取服务器时间失败";
                    MyLogger.showLogWithLineNum(5,ErrorMessage);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new MessageEvent("时间更新完毕",1));
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }

    class Load_WuZi extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool
                                .callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {"typeName",
                                        "condition", "columns"}, new Object[] {"SCM_WUZI", " WZ_STATUS=1 ", ""});
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,op.toString());
                }else{
                    ErrorMessage+="获取物质失败";
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().postSticky(new MessageEvent("物料更新完毕", 1));
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class Load_WuZi_Catoge extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"SCM_WUZI_CATALOG", "WC_STATUS=1", ""});
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,op.toString());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().postSticky(new MessageEvent("物料类别更新完毕",1));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class Load_Dept extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"HR_B_DEPT", "", ""});
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,op.toString());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().postSticky(new MessageEvent("部门更新完毕",1));
        }
    }

    class Load_Stock extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"WL_STOCK", "CK_STATSU=1", ""});
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,op.toString());
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().postSticky(new MessageEvent("仓库更新完毕",1));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
