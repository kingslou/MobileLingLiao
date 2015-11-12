package com.cyt.ieasy.db;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.ieasy.dao.WUZI_CATALOG;
import com.ieasy.dao.WUZI_CATALOGDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    public WUZI_CATALOG getEntity(String value) {

        return null;
    }

    @Override
    public void clearTable() {
        wuzi_catalogDao.deleteAll();
    }

    @Override
    public ArrayList<WUZI_CATALOG> getAlldata() {
        List<WUZI_CATALOG> wuzi_catalogs = new ArrayList<>();
        wuzi_catalogs = wuzi_catalogDao.queryBuilder().list();
        return (ArrayList<WUZI_CATALOG>) wuzi_catalogs;
    }

    @Override
    public void addData(Object object) {
        try{
            JSONArray Items = new JSONArray(object.toString());
            WUZI_CATALOG[] wuzi_catalogs = new WUZI_CATALOG[Items.length()];
            for(int i=0;i<Items.length();i++){
                JSONObject item = Items.getJSONObject(i);
                WUZI_CATALOG wuzi_catalog = new WUZI_CATALOG();
                wuzi_catalog.setWC_ID(item.getString("WC_ID"));
                wuzi_catalog.setWC_NAME(item.getString("WC_NAME"));
                wuzi_catalog.setWC_CODE(item.getString("WC_CODE"));
                wuzi_catalog.setWC_CRE_ID(item.getString("WC_CRE_ID"));
                wuzi_catalog.setWC_DEL_FLAG(item.getString("WC_DEL_FLAG"));
                wuzi_catalog.setWC_DISP_ORDER(item.getString("WC_DISP_ORDER"));
                wuzi_catalog.setWC_IDS(item.getString("WC_IDS"));
                wuzi_catalog.setWC_MOD_ID(item.getString("WC_MOD_ID"));
                wuzi_catalog.setWC_REMARK(item.getString("WC_REMARK"));
                wuzi_catalog.setWC_PARENT_ID(item.getString("WC_PARENT_ID"));
                wuzi_catalog.setWC_STATUS(item.getString("WC_STATUS"));
                wuzi_catalog.setWC_ZB_ID(item.getString("WC_ZB_ID"));
                wuzi_catalog.setWC_BAK1(item.getString("WC_BAK1"));
                wuzi_catalogs[i] = wuzi_catalog;
            }
            wuzi_catalogDao.insertInTx(wuzi_catalogs);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
