package com.cyt.ieasy.db;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.TimeUtils;
import com.ieasy.dao.LING_MB;
import com.ieasy.dao.LING_MBDao;
import com.ieasy.dao.LING_MB_DETIAL;
import com.ieasy.dao.LING_MB_DETIALDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by jin on 2015.11.18.
 */
public class MuBanTableUtil extends BaseTableUtil {

    private volatile static MuBanTableUtil muBanTableUtil;
    private volatile static LING_MBDao ling_mbDao;
    private volatile static LING_MB_DETIALDao ling_mb_detialDao;
    private MuBanTableUtil(){

    }

    public static MuBanTableUtil getMuBanTableUtil(){
        if(muBanTableUtil==null){
            synchronized (MuBanTableUtil.class){
                if(muBanTableUtil==null){
                    muBanTableUtil = new MuBanTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    ling_mbDao = daoSession.getLING_MBDao();
                    ling_mb_detialDao = daoSession.getLING_MB_DETIALDao();
                }
            }
        }
        return muBanTableUtil;
    }

    /**
     * 获取全部模板
     * @return
     */
    public List<LING_MB> getLing_Mb(){
        List<LING_MB> ling_mbList = new ArrayList<>();
        QueryBuilder queryBuilder = ling_mbDao.queryBuilder();
        ling_mbList = queryBuilder.list();
        return ling_mbList;
    }

    /**
     * 根据模板ID获取模板数据
     * @param MB_ID
     * @return
     */
    public List<LING_MB_DETIAL> getLing_MB_Detial(String MB_ID){
        List<LING_MB_DETIAL> ling_mb_detialList = new ArrayList<>();
        QueryBuilder queryBuilder = ling_mb_detialDao.queryBuilder();
        if(!StringUtils.isBlank(MB_ID)){
            queryBuilder.where(LING_MB_DETIALDao.Properties.DJX_DJ_ID.eq(MB_ID));
        }
        ling_mb_detialList = queryBuilder.list();
        return ling_mb_detialList;
    }


    @Override
    public ArrayList getAlldata() {
        return null;
    }

    @Override
    public void addData(Object object) {

    }

    public void addLing_MB_Data(Object object){
        try{
            JSONArray items = new JSONArray(object.toString());
            LING_MB[] lingMbs = new LING_MB[items.length()];
            for(int i=0;i< items.length();i++){
                JSONObject item = items.getJSONObject(i);
                LING_MB ling_mb = new LING_MB();
                ling_mb.setDJ_ID(item.getString("DJ_ID"));
                ling_mb.setDJ_CODE(item.getString("DJ_CODE"));
                ling_mb.setDJ_NAME(item.getString("DJ_NAME"));
                ling_mb.setDJ_CRE_ID(item.getString("DJ_CRE_ID"));
                ling_mb.setDJ_CRE_NAME(item.getString("DJ_CRE_NAME"));
                ling_mb.setDJ_DATE(TimeUtils.getDate(TimeUtils.convertDateTime(item.getString("DJ_DATE")), ""));
                ling_mb.setDJ_DEALER(item.getString("DJ_DEALER"));
                ling_mb.setDJ_MD_ID(item.getString("DJ_MD_ID"));
                ling_mb.setDJ_MD_NAME(item.getString("DJ_MD_NAME"));
                ling_mb.setDJ_DISP_ORDER(item.getInt("DJ_DISP_ORDER"));
                ling_mb.setDJ_REMARK(item.getString("DJ_REMARK"));
                ling_mb.setDJ_VERSION(item.getInt("DJ_VERSION"));
                lingMbs[i] = ling_mb;
            }
            ling_mbDao.insertInTx(lingMbs);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void addLing_MB_Detial(Object object){
        try{
            JSONArray items = new JSONArray(object.toString());
            LING_MB_DETIAL[] lingMbDetials = new LING_MB_DETIAL[items.length()];
            for(int i=0;i<items.length();i++){
                JSONObject item = items.getJSONObject(i);
                LING_MB_DETIAL ling_mb_detial = new LING_MB_DETIAL();
                ling_mb_detial.setDJX_ID(item.getString("DJX_ID"));
                ling_mb_detial.setDJX_DJ_ID(item.getString("DJX_DJ_ID"));
                ling_mb_detial.setDJX_DISP_ORDER(item.getString("DJX_DISP_ORDER"));
                ling_mb_detial.setDJX_DJ_CODE(item.getString("DJX_DJ_CODE"));
                ling_mb_detial.setDJX_MD_ID(item.getString("DJX_MD_ID"));
                ling_mb_detial.setDJX_REMARK(item.getString("DJX_REMARK"));
                ling_mb_detial.setDJX_UNIT_ID(item.getString("DJX_UNIT_ID"));
                ling_mb_detial.setDJX_UNIT_NAME(item.getString("DJX_UNIT_NAME"));
                ling_mb_detial.setDJX_WC_ID(item.getString("DJX_WC_ID"));
                ling_mb_detial.setDJX_WC_NAME(item.getString("DJX_WC_NAME"));
                ling_mb_detial.setDJX_WZ_ID(item.getString("DJX_WZ_ID"));
                ling_mb_detial.setDJX_WZ_NAME(item.getString("DJX_WZ_NAME"));
                //ling_mb_detial.setDJX_WZ_QUICK_CODE(item.getString("DJX_WZ_QUICK_CODE"));
                ling_mb_detial.setDJX_WZ_SP(item.getString("DJX_WZ_SP"));
                lingMbDetials[i] = ling_mb_detial;
            }
            ling_mb_detialDao.insertInTx(lingMbDetials);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void clearTable() {
        ling_mbDao.deleteAll();
    }

    public void clearTableDetial(){
        ling_mb_detialDao.deleteAll();
    }

    @Override
    public Object getEntity(String value) {
        return null;
    }
}
