package com.cyt.ieasy.db;

import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.TimeUtils;
import com.ieasy.dao.WUZI_CATALOG;
import com.ieasy.dao.WUZI_CATALOGDao;

import java.util.ArrayList;

/**
 * Created by jin on 2015.11.05.
 */
public class WuZiCATALOG_TableUtil extends BaseTableUtil {

    private volatile static WuZiCATALOG_TableUtil wuZiCATALOG_tableUtil;
    private static WUZI_CATALOGDao wuzi_catalogDao;

    private WuZiCATALOG_TableUtil(){

    }

    public static WuZiCATALOG_TableUtil getWuZiCATALOG_tableUtil(){
        if(wuZiCATALOG_tableUtil==null){
            synchronized (WuZiCATALOG_TableUtil.class){
                if(wuZiCATALOG_tableUtil==null){
                    wuZiCATALOG_tableUtil = new WuZiCATALOG_TableUtil();
                    daoSession = MyApplication.getDaoSession();
                    wuzi_catalogDao = daoSession.getWUZI_CATALOGDao();
                }
            }
        }
        return wuZiCATALOG_tableUtil;
    }


    @Override
    public void clearTable() {
        wuzi_catalogDao.deleteAll();
    }

    @Override
    public ArrayList getAlldata() {
        return null;
    }

    @Override
    public void addData(Object object) {
        try{
//            JSONArray Items = new JSONArray(object.toString());
//            for(int i=0;i<Items.length();i++){
//                WUZI_CATALOG wuzi_catalog = new WUZI_CATALOG();
//                wuzi_catalogDao.insert(wuzi_catalog);
//            }
            MyLogger.showLogWithLineNum(5,"测试5000开始时间"+ TimeUtils.getCurrentTimeInString());
            WUZI_CATALOG[] wuzi_catalogs = new WUZI_CATALOG[5000];
            for(int i=0;i<5000;i++){
                WUZI_CATALOG wuzi_catalog = new WUZI_CATALOG();
                wuzi_catalog.setWC_BAK1("test");
                wuzi_catalogs[i] = wuzi_catalog;
            }
            wuzi_catalogDao.insertInTx(wuzi_catalogs);
            MyLogger.showLogWithLineNum(5,"测试5000结束时间"+ TimeUtils.getCurrentTimeInString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
