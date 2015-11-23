package com.cyt.ieasy.setting;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;

import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.DeptTableUtil;
import com.cyt.ieasy.db.MuBanDetialTableUtil;
import com.cyt.ieasy.db.MuBanTableUtil;
import com.cyt.ieasy.db.StockTableUtil;
import com.cyt.ieasy.db.WuZiCATALOG_TableUtil;
import com.cyt.ieasy.db.WuZiTableUtil;
import com.cyt.ieasy.model.UpdateStatus;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.interfaces.Callback;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.SystemUtils;
import com.cyt.ieasy.tools.TimeUtils;
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
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    public static final Executor THREAD_POOL_EXECUTOR = Executors
            .newFixedThreadPool(SystemUtils.DEFAULT_THREAD_POOL_SIZE);

    private DownLoadActivity(Callback<UpdateStatus> callback){

    }
    public static DownLoadActivity getInstance(Callback<UpdateStatus> callback){
        if(null==instance){
            instance = new DownLoadActivity(callback);
        }
        return instance;
    }

    private void loadIpConfig(){
        IP_Config = "http://"+ CommonTool.getGlobalSetting(MyApplication.getContext(), Const.ipconfig)
                +":"+CommonTool.getGlobalSetting(MyApplication.getContext(),Const.portconfig);
        Base_Service = IP_Config + "/webserver/WebService/BaseService.asmx";
        Scm_Service  = IP_Config+"/webserver/WebService/ScmService.asmx";
        powerManager = (PowerManager)MyApplication.getContext().getSystemService(MyApplication.getContext().POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "My Lock");
    }

    public void cancleTask(){

    }

    public void loadWuZi(){
        loadIpConfig();
        ErrorMessage = "";
        new Load_WuZi().execute();
    }

    public void loadData(){
        loadIpConfig();
        ErrorMessage = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new SynServerTime().executeOnExecutor(THREAD_POOL_EXECUTOR);
            //new Load_WuZi().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Dept().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_WuZi_Catoge().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Stock().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Mb().executeOnExecutor(THREAD_POOL_EXECUTOR);
            new Load_Mb_Detial().executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else{
            new SynServerTime().execute();
            //new Load_WuZi().execute();
            new Load_Dept().execute();
            new Load_WuZi_Catoge().execute();
            new Load_Stock().execute();
            new Load_Mb().execute();
            new Load_Mb_Detial().execute();
        }
    }

    class SynServerTime extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                MyLogger.showLogWithLineNum(5,"开始获取服务器时间");
                Object serverTime =
                        WebServiceTool.callWebservice(Base_Service, "GetServerTimeForPad", new String[] {},
                                new Object[] {});
                if(isCancelled()){
                    return null;
                }
                if(null!=serverTime){
                    MyLogger.showLogWithLineNum(5,serverTime.toString());
                    CommonTool.saveGlobalSetting(MyApplication.getContext(),Const.updatetime,serverTime.toString().trim());
                }else{
                    errormsg = "获取服务器时间失败";
                    ErrorMessage = ErrorMessage+errormsg;
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
            if(isCancelled()){
                return;
            }
            EventBus.getDefault().post(new MessageEvent("时间更新完毕","",1));
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            EventBus.getDefault().post(new MessageEvent("取消时间更新",ErrorMessage ,1));
        }
    }

    class Load_WuZi extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool
                                .callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {"typeName",
                                        "condition", "columns"}, new Object[] {"SCM_WUZI", " WZ_STATUS=1 ", ""});
                if(isCancelled()){
                    ErrorMessage = "取消物料更新";
                    return null;
                }
                if(null!=op){
                    //MyLogger.showLogWithLineNum(5,op.toString());
                    WuZiTableUtil.getWuZiTableUtil().clearTable();
                    MyLogger.showLogWithLineNum(5, "物料更新时间" + TimeUtils.getCurrentTimeInString());
                    WuZiTableUtil.getWuZiTableUtil().addData(op);
                    MyLogger.showLogWithLineNum(5, "物料更新结束时间" + TimeUtils.getCurrentTimeInString());
                }else{
                    errormsg = "获取物质失败";
                    ErrorMessage = ErrorMessage+errormsg;
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
            if(StringUtils.isBlank(errormsg)){
                EventBus.getDefault().postSticky(new MessageEvent("物料更新完毕","",1));
                MyLogger.showLogWithLineNum(5, "物料共" + WuZiTableUtil.getWuZiTableUtil().getAlldata().size());
                loadData();
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("物料更新",ErrorMessage,1));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class Load_WuZi_Catoge extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"SCM_WUZI_CATALOG", "WC_STATUS=1", ""});
                if(isCancelled()){
                    ErrorMessage += "取消更新物料类别";
                    return null;
                }
                if(null!=op){
                   // MyLogger.showLogWithLineNum(5,op.toString());
                    WuZiCATALOG_TableUtil.getWuZiCATALOG_tableUtil().clearTable();
                    WuZiCATALOG_TableUtil.getWuZiCATALOG_tableUtil().addData(op);
                }else{
                    errormsg = "物料类别更新失败";
                    ErrorMessage = ErrorMessage+errormsg;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(StringUtils.isBlank(ErrorMessage)){
                EventBus.getDefault().postSticky(new MessageEvent("物料类别更新完毕","",1));
                MyLogger.showLogWithLineNum(5, "物料类别数据共" + WuZiCATALOG_TableUtil.getWuZiCATALOG_tableUtil().getAlldata().size());
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("物料类别更新",ErrorMessage,0));
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class Load_Dept extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"HR_B_DEPT", "", ""});
                if(isCancelled()){
                    ErrorMessage += "部门取消更新";
                    return null;
                }
                if(null!=op){
                    //MyLogger.showLogWithLineNum(5,op.toString());
                    DeptTableUtil.getDeptTableUtil().clearTable();
                    DeptTableUtil.getDeptTableUtil().addData(op);
                }else{
                    errormsg = "部门更新失败";
                    ErrorMessage = ErrorMessage+errormsg;
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
            if(StringUtils.isBlank(errormsg)){
                EventBus.getDefault().postSticky(new MessageEvent("部门更新完毕","",1));
                MyLogger.showLogWithLineNum(5,"部门数量"+DeptTableUtil.getDeptTableUtil().getAlldata().size());
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("部门更新",ErrorMessage,1));
            }
        }
    }

    class Load_Stock extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {
                                "typeName", "condition", "columns"}, new Object[] {"WL_STOCK", "", ""});
                if(isCancelled()){
                    ErrorMessage +="仓库取消更新";
                    return null;
                }
                if(null!=op&&!op.equals("anyType{}")){
                    StockTableUtil.getStockTableUtil().clearTable();
                    StockTableUtil.getStockTableUtil().addData(op);
                }else{
                    errormsg = "仓库更新失败";
                    ErrorMessage = ErrorMessage+errormsg;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(StringUtils.isBlank(errormsg)){
                EventBus.getDefault().postSticky(new MessageEvent("仓库更新完毕","",1));
                MyLogger.showLogWithLineNum(5,"仓库共"+ StockTableUtil.getStockTableUtil().getAlldata().size());
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("仓库更新",ErrorMessage,1));
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    class Load_Mb extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool
                                .callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {"typeName",
                                        "condition", "columns"}, new Object[] {"WL_TEMP_BILL", "", ""});
                if(isCancelled()){
                    ErrorMessage += "取消模板更新";
                    return null;
                }
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,"模板"+op.toString());
                    MuBanTableUtil.getMuBanTableUtil().clearTable();
                    MuBanTableUtil.getMuBanTableUtil().addLing_MB_Data(op.toString());
                }else{
                    errormsg = "模板更新失败";
                    ErrorMessage=ErrorMessage+errormsg;
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
            if(StringUtils.isBlank(errormsg)){
                EventBus.getDefault().postSticky(new MessageEvent("模板更新完毕","",1));
                MyLogger.showLogWithLineNum(5,"模板共"+MuBanTableUtil.getMuBanTableUtil().getLing_Mb().size());
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("模板更新",ErrorMessage,1));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    class Load_Mb_Detial extends AsyncTask<Void,Void,Void>{
        private String errormsg;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                Object op =
                        WebServiceTool
                                .callWebservice(Scm_Service, "FindEntityJsonByCondition", new String[] {"typeName",
                                        "condition", "columns"}, new Object[] {"WL_TEMP_BILL_DETAIL", "", ""});
                if(isCancelled()){
                    ErrorMessage += "取消模板内容更新";
                    return null;
                }
                if(null!=op){
                    MyLogger.showLogWithLineNum(5,"模板内容"+op.toString());
                    MuBanDetialTableUtil.getMuBanDetialTableUtil().clearTable();
                    MuBanDetialTableUtil.getMuBanDetialTableUtil().addLing_MB_Detial(op);
                }else{
                    errormsg = "模板内容更新失败";
                    ErrorMessage = ErrorMessage+errormsg;
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
            if(StringUtils.isBlank(ErrorMessage)){
                EventBus.getDefault().postSticky(new MessageEvent("模板详细更新完毕","",1));
            }else{
                EventBus.getDefault().postSticky(new MessageEvent("模板详细更新",ErrorMessage,1));
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }
    }
}
