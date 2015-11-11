package com.cyt.ieasy.db;

import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.StringUtils;
import com.cyt.ieasy.tools.TimeUtils;
import com.ieasy.dao.WuZi_Table;
import com.ieasy.dao.WuZi_TableDao;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 物料表处理
 * Created by jin on 2015.11.05.
 */
public class WuZiTableUtil extends BaseTableUtil {

    private volatile static WuZiTableUtil wuZiTableUtil;
    private volatile static WuZi_TableDao wuZi_tableDao;

    private WuZiTableUtil(){

    }

    public static WuZiTableUtil getWuZiTableUtil(){
        if(wuZiTableUtil==null){
            synchronized (WuZiTableUtil.class){
                if(wuZiTableUtil==null){
                    wuZiTableUtil = new WuZiTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    wuZi_tableDao = daoSession.getWuZi_TableDao();
                }
            }
        }
        return wuZiTableUtil;
    }

    @Override
    public void clearTable() {
        wuZi_tableDao.deleteAll();
    }

    @Override
    public ArrayList<WuZi_Table> getAlldata() {
        List<WuZi_Table> list = new ArrayList<>();
        list = wuZi_tableDao.queryBuilder().list();
        return (ArrayList<WuZi_Table>) list;
    }

    public List<WuZi_Table> queryBystr(String text,int returnCount){
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
        List<WuZi_Table> list = new ArrayList<>();
        if(StringUtils.isBlank(text)){
            return list;
        }
        //条件查询
        QueryBuilder queryBuilder = wuZi_tableDao.queryBuilder();
        queryBuilder.where(WuZi_TableDao.Properties.WZ_NAME.like("%"+text+"%"));
        list = wuZi_tableDao.queryBuilder().whereOr(WuZi_TableDao.Properties.WZ_NAME.like("%"+text+"%")
        ,WuZi_TableDao.Properties.WZ_QUICK_CODE.like("%"+text+"%")).limit(returnCount).list();
        return list;
    }

    @Override
    public void addData(Object object) {
        try{
            JSONArray Items = new JSONArray(object.toString());
            MyLogger.showLogWithLineNum(5, "插入物料执行时间" + TimeUtils.getCurrentTimeInString());
            WuZi_Table[] wuZi_tables = new WuZi_Table[Items.length()];
            for(int i=0;i<Items.length();i++){
                JSONObject item = Items.getJSONObject(i);
                WuZi_Table wuZi_table = new WuZi_Table();
                wuZi_table.setCW_COUNT_APPLY(item.getString("CW_COUNT_APPLY"));
                wuZi_table.setWC_ZB_ID(item.getString("WC_ZB_ID"));
                wuZi_table.setCW_COUNT_MAX(item.getString("CW_COUNT_MAX"));
                wuZi_table.setCW_COUNT_MIN(item.getString("CW_COUNT_MIN"));
                wuZi_table.setWZ_ID(item.getString("WZ_ID"));
                wuZi_table.setWZ_NAME(item.getString("WZ_NAME"));
                wuZi_table.setWZ_CODE(item.getString("WZ_CODE"));
                wuZi_table.setWZ_CATEGORY(item.getString("WZ_CATEGORY"));
                wuZi_table.setWZ_CG_UNIT_ID(item.getString("WZ_CG_UNIT_ID"));
                wuZi_table.setWZ_CG_UNIT_NAME(item.getString("WZ_CG_UNIT_NAME"));
                wuZi_table.setWZ_UNIT_ID(item.getString("WZ_UNIT_ID"));
                wuZi_table.setWZ_UNIT_NAME(item.getString("WZ_UNIT_NAME"));
                wuZi_table.setWZ_COUNT_FLAG(item.getString("WZ_COUNT_FLAG"));
                wuZi_table.setWZ_DEL_FLAG(item.getString("WZ_DEL_FLAG"));
                wuZi_table.setWZ_DISP_ORDER(item.getString("WZ_DISP_ORDER"));
                wuZi_table.setWZ_PRICE(item.getString("WZ_PRICE"));
                wuZi_table.setWZ_QUICK_CODE(item.getString("WZ_QUICK_CODE"));
                wuZi_table.setWZ_SZ_ID(item.getString("WZ_SZ_ID"));
                wuZi_table.setWZ_SZ_NAME(item.getString("WZ_SZ_NAME"));
                wuZi_table.setWZ_STORE_CONDITION(item.getString("WZ_STORE_CONDITION"));
                wuZi_table.setWZ_STATUS(item.getString("WZ_STATUS"));
                wuZi_table.setWZ_VERSION(item.getString("WZ_VERSION"));
                wuZi_table.setWZ_PIECES(item.getString("WZ_PIECES"));
                wuZi_table.setWZ_CRE_ID(item.getString("WZ_CRE_ID"));
                wuZi_table.setWZ_IF_BOM(item.getString("WZ_IF_BOM"));
                wuZi_table.setWZ_IF_PANDIAN(item.getString("WZ_IF_PANDIAN"));
                wuZi_table.setWZ_BAK1(item.getString("WZ_BAK1"));
                wuZi_table.setWZ_BAK2(item.getString("WZ_BAK2"));
                wuZi_table.setWZ_BAK3(item.getString("WZ_BAK3"));
                wuZi_table.setWZ_BAK4(item.getString("WZ_BAK4"));
                wuZi_table.setWZ_BAK5(item.getString("WZ_BAK5"));
                wuZi_tables[i] = wuZi_table;
            }
             //批量插入 性能大大的提升
             wuZi_tableDao.insertInTx(wuZi_tables);
             MyLogger.showLogWithLineNum(5, "插入物料结束时间" + TimeUtils.getCurrentTimeInString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
