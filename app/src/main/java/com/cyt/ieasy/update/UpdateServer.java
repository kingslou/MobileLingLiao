package com.cyt.ieasy.update;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.db.DeptTableUtil;
import com.cyt.ieasy.db.LingLiaoTableUtil;
import com.cyt.ieasy.db.StockTableUtil;
import com.cyt.ieasy.event.UpdateEvent;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.SystemUtils;
import com.cyt.ieasy.tools.WebServiceTool;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * 同步服务端工具类
 * Created by jin on 2015.11.20.
 */
public class UpdateServer {

    private String  Base_Service;
    private String  Scm_Service;
    private String  IP_Config;
    private String  ErrorMessage;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;
    public static final Executor THREAD_POOL_EXECUTOR = Executors
            .newFixedThreadPool(SystemUtils.DEFAULT_THREAD_POOL_SIZE);

    private LING_WUZI ling_wuzi;
    private List<LING_WUZIDETIAL> ling_wuzidetialList;
    private String LL_CODE;
    private MaterialDialog dialog;
    private Context context;

    private static UpdateServer updateServer;
    private UpdateServer(){

    }

    public static UpdateServer getUpdateServer(){
        if(updateServer==null){
            synchronized (UpdateServer.class){
                updateServer = new UpdateServer();
            }
        }
        return updateServer;
    }

    private void loadIpConfig(){
        IP_Config = "http://"+ CommonTool.getGlobalSetting(MyApplication.getContext(), Const.ipconfig)
                +":"+CommonTool.getGlobalSetting(MyApplication.getContext(),Const.portconfig);
        Base_Service = IP_Config + "/webserver/WebService/BaseService.asmx";
        Scm_Service  = IP_Config+"/webserver/WebService/ScmService.asmx";
        powerManager = (PowerManager)MyApplication.getContext().getSystemService(MyApplication.getContext().POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK , "My Lock");
    }

    public void updateToServer(String LL_CODE,Context context){
        loadIpConfig();
        this.LL_CODE = LL_CODE;
        this.context = context;
        dialog =  new MaterialDialog.Builder(context)
                .title("同步中····")
                .content("正在同步数据")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(false)
                .show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Update().executeOnExecutor(THREAD_POOL_EXECUTOR);
        }else{
            new Update().execute();
        }
    }

    private  String createJson(List<LING_WUZIDETIAL> ling_wuzidetialList){
        String Items = null;
        List<WuZiEntity> wuZiEntityList = new ArrayList<>();
        for(LING_WUZIDETIAL ling_wuzidetial : ling_wuzidetialList){
            WuZiEntity wuZiEntity = new WuZiEntity();
            wuZiEntity.DJX_WZ_ID = ling_wuzidetial.getLL_WZ_ID();
            wuZiEntity.DJX_AMOUNT_PRICE = "";
            wuZiEntity.DJX_AMOUNT_TRUE = "";
            wuZiEntity.DJX_COUNT = ling_wuzidetial.getLL_NUM();
            wuZiEntity.DJX_COUNT_TZ = ling_wuzidetial.getLL_TZS();
            wuZiEntity.DJX_REMARK = ling_wuzidetial.getLL_WZ_REMARK();
            wuZiEntity.DJX_WZ_NAME = ling_wuzidetial.getLL_WZ_NAME();
            wuZiEntity.DJX_WZ_UNIT_ID = ling_wuzidetial.getLL_WZ_UNITID();
            wuZiEntity.DJX_WZ_UNIT_NAME = ling_wuzidetial.getLL_WZ_UNITNAME();
            wuZiEntityList.add(wuZiEntity);
        }
        Items = JSON.toJSONString(wuZiEntityList);
        return Items;
    }

    class Update extends AsyncTask<Void,Void,Void>{
        private String billjson;
        private String sjson;
        private String returnCode;
        private String returnId;
        private String errorMessage;
        @Override
        protected Void doInBackground(Void... params) {
            try{
                billjson = "";
                ling_wuzidetialList = LingLiaoTableUtil.getLiaoTableUtil().getLingWuZiDetial(LL_CODE);
                ling_wuzi = LingLiaoTableUtil.getLiaoTableUtil().getEntity(LL_CODE);
                sjson = "{"+"Items:"+createJson(ling_wuzidetialList)+"}";
                String DJ_CK_ID = "";//仓库ID
                String DJ_CK_NAME = "";
                String DJ_IN_CK_ID = "";//部门ID
                String DJ_IN_CK_NAME = "";
                String DJ_DEALER = "";//领料人
                String DJ_CRE_ID = "";//登录人ID
                String DJ_CRE_NAME = "";//登录人
                String DJ_DATE = "";//制单日期
                String DJ_REMARK = "";//备注
                String editbm = CommonTool.getGlobalSetting(context,Const.editBm);
                if(StringUtils.isBlank(editbm)){
                    DJ_IN_CK_ID = CommonTool.getGlobalSetting(context,Const.cachuserdeptid);
                    DJ_IN_CK_NAME = CommonTool.getGlobalSetting(context,Const.cachuserdept);
                }else{
                    DJ_IN_CK_NAME = ling_wuzi.getLL_DEPT();
                    MyLogger.showLogWithLineNum(5,"当前选择的部门名称"+DJ_IN_CK_NAME);
                    DJ_IN_CK_ID = DeptTableUtil.getDeptTableUtil().getEntity(DJ_IN_CK_NAME).getINNERID();
                }
                DJ_CK_NAME = ling_wuzi.getLL_STOCK();
                DJ_CK_ID = StockTableUtil.getStockTableUtil().getEntity(DJ_CK_NAME).getCK_ID();
                DJ_DEALER = CommonTool.getGlobalSetting(context, Const.cachuser);
                DJ_CRE_ID = CommonTool.getGlobalSetting(context, Const.cachuserid);
                DJ_CRE_NAME = DJ_DEALER;
                DJ_DATE = ling_wuzi.getLL_SELECT_TIME();
                DJ_REMARK = ling_wuzi.getBAK()==null?"":ling_wuzi.getBAK();
                billjson = DJ_CK_ID+"|"+DJ_CK_NAME+"|"+DJ_IN_CK_ID+"|"+DJ_IN_CK_NAME+"|"+DJ_DEALER+"|"
                        +DJ_CRE_ID+"|"+DJ_CRE_NAME+"|"+DJ_DATE+"|"+DJ_REMARK;
                MyLogger.showLogWithLineNum(5,"同步信息billJson"+billjson);
                MyLogger.showLogWithLineNum(5,"同步信息sjson"+sjson);
                Object op =
                        WebServiceTool.callWebservice(Scm_Service, "MobileLingLiao", new String[]{"billJson",
                                "wuziJson"}, new Object[]{billjson, sjson});
                MyLogger.showLogWithLineNum(5,"同步信息"+op+"");
                if(null!=op){
                    if(!op.toString().equals("anyType{}")){
                        String[] returnArray = StringUtils.fastSplit(op.toString(),',');
                        returnCode = returnArray[1];
                        returnId = returnArray[0];
                        ling_wuzi.setLL_RETURNCODE(returnCode);
                        LingLiaoTableUtil.getLiaoTableUtil().update(ling_wuzi);
                    }else{
                        errorMessage = "同步失败返回"+op.toString();
                    }
                }else{
                    errorMessage="同步失败";
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
            dialog.dismiss();
            if(StringUtils.isBlank(errorMessage)){
                //// TODO: 2015.11.20 如果同步成功，更新本地存储表字段，表示已经同步
                EventBus.getDefault().post(new UpdateEvent(Const.UpdateServerSuccess));
            }else{
                EventBus.getDefault().post(new UpdateEvent(Const.UpdateServerFailue));
            }
        }
    }

    class WuZiEntity{
        public String DJX_WZ_ID;
        public String DJX_WZ_NAME;
        public String DJX_WZ_UNIT_ID;
        public String DJX_WZ_UNIT_NAME;
        public Double DJX_COUNT;
        public Integer DJX_COUNT_TZ;
        public String DJX_AMOUNT_PRICE;
        public String DJX_AMOUNT_TRUE;
        public String DJX_REMARK;
    }
}
