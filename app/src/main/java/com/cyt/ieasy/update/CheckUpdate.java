package com.cyt.ieasy.update;
import android.os.AsyncTask;
import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.event.UpdateEvent;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.CommonTool;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.WebServiceTool;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import de.greenrobot.event.EventBus;

/**
 * 检查更新类
 * Created by jin on 2015.11.20.
 */
public class CheckUpdate {

    private SimpleDateFormat df;
    private Date lastSaveTime;
    private String  Base_Service;
    private String  IP_Config;

    public CheckUpdate(){
    }

    public void isUpdata(){
        MyLogger.showLogWithLineNum(5,"打印Context"+MyApplication.getContext());
        loadIpConfig();
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        new UpdateAsyn().execute();
    }

    private void loadIpConfig(){
        IP_Config = "http://"+ CommonTool.getGlobalSetting(MyApplication.getContext(), Const.ipconfig)
                +":"+CommonTool.getGlobalSetting(MyApplication.getContext(),Const.portconfig);
        Base_Service = IP_Config + "/webserver/WebService/BaseService.asmx";
    }

    private void initLastSaveTime() {
        String timeStr = "";
        timeStr = CommonTool.getGlobalSetting(MyApplication.getContext(), Const.updatetime);
        MyLogger.showLogWithLineNum(5, "打印时间"+timeStr);
        if (timeStr == null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                lastSaveTime = df.parse("1949-10-01");
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            try {
                lastSaveTime = df.parse(timeStr.trim());
                MyLogger.showLogWithLineNum(5,"格式转换后的时间"+lastSaveTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    class UpdateAsyn extends AsyncTask<String, String, String> {
        boolean isUpdate = false;
        Object checkupdate = null;
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                initLastSaveTime();
                String llltime = df.format(lastSaveTime);
                MyLogger.showLogWithLineNum(5,"测试时间"+llltime);
                MyLogger.showLogWithLineNum(5, "打印" + Base_Service);
                checkupdate =
                        WebServiceTool.callWebservice(Base_Service, "CheckWlNeedUpdate",
                                new String[]{"lastUpdateTime"}, new Object[]{llltime});
                MyLogger.showLogWithLineNum(5,"上次记录更新时间"+checkupdate);
                if (null != checkupdate) {
                    isUpdate = Boolean.parseBoolean(checkupdate.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(isUpdate){
                EventBus.getDefault().post(new UpdateEvent(Const.isUpdate));
            }else{
                EventBus.getDefault().post(new UpdateEvent(Const.noUpdate));
            }
        }
    }
}
