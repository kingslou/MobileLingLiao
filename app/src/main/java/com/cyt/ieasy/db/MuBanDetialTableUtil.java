package com.cyt.ieasy.db;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.ieasy.dao.LING_MB_DETIAL;
import com.ieasy.dao.LING_MB_DETIALDao;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by jin on 2015.11.19.
 */
public class MuBanDetialTableUtil extends BaseTableUtil {
    private volatile static MuBanDetialTableUtil muBanDetialTableUtil;
    private volatile static LING_MB_DETIALDao ling_mb_detialDao;
    private MuBanDetialTableUtil(){

    }

    public static MuBanDetialTableUtil getMuBanDetialTableUtil(){
        if(muBanDetialTableUtil==null){
            synchronized (MuBanDetialTableUtil.class){
                if(muBanDetialTableUtil==null){
                    muBanDetialTableUtil = new MuBanDetialTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    ling_mb_detialDao = daoSession.getLING_MB_DETIALDao();
                }
            }
        }
        return muBanDetialTableUtil;
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
                String WZ_ID = item.getString("DJX_WZ_ID");
                ling_mb_detial.setDJX_WZ_ID(WZ_ID);
                ling_mb_detial.setDJX_WZ_NAME(item.getString("DJX_WZ_NAME"));
                String DJX_WZ_QUICK_CODE = WuZiTableUtil.getWuZiTableUtil().getEntity(WZ_ID).getWZ_QUICK_CODE();
                ling_mb_detial.setDJX_WZ_QUICK_CODE(DJX_WZ_QUICK_CODE);
                MyLogger.showLogWithLineNum(5,"速查码"+DJX_WZ_QUICK_CODE);
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
        ling_mb_detialDao.deleteAll();
    }

    @Override
    public Object getEntity(String value) {
        return null;
    }
}
