package com.cyt.ieasy.db;

import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.MyLogger;
import com.cyt.ieasy.tools.TimeUtils;
import com.ieasy.dao.Dept_Table;
import com.ieasy.dao.Dept_TableDao;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 部门表处理工具类
 * Created by jin on 2015.11.05.
 */
public class DeptTableUtil extends BaseTableUtil {

    private volatile static DeptTableUtil deptTableUtil;
    private volatile static Dept_TableDao deptTableDao;

    private DeptTableUtil(){

    }

    public static DeptTableUtil getDeptTableUtil(){
        if(deptTableUtil==null){
            synchronized (DeptTableUtil.class){
                if(deptTableUtil==null){
                    deptTableUtil = new DeptTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    deptTableDao = daoSession.getDept_TableDao();
                }
            }
        }
        return deptTableUtil;
    }

    @Override
    public Dept_Table getEntity(String value) {
        List<Dept_Table> list = new ArrayList<>();
        QueryBuilder queryBuilder = deptTableDao.queryBuilder();
        queryBuilder.where(Dept_TableDao.Properties.DEPTNAME.eq(value));
        list = queryBuilder.list();
        if(null!=list&&list.size()!=0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void clearTable() {
        deptTableDao.deleteAll();
    }

    @Override
    public void addData(Object object) {
        try{
            JSONArray items = new JSONArray(object.toString());
            MyLogger.showLogWithLineNum(5,"执行时间"+TimeUtils.getCurrentTimeInString());
            for(int i=0;i<items.length();i++){
                JSONObject item = items.getJSONObject(i);
                Dept_Table dept_table = new Dept_Table();
                dept_table.setINNERID(item.getString("INNERID"));
                dept_table.setDEPTCODE(item.getString("DEPTCODE"));
                dept_table.setDEPT_IF_CUNCHUN(item.getString("DEPT_IF_CUNCHUN"));
                dept_table.setADDIP(item.getString("ADDIP"));
                dept_table.setADDTIME(TimeUtils.getDate(TimeUtils.convertDateTime(item.getString("ADDTIME")), ""));
                dept_table.setUPDATETIME(TimeUtils.getDate(TimeUtils.convertDateTime(item.getString("UPDATETIME")), ""));
                dept_table.setADDUSERID(item.getString("ADDUSERID"));
                dept_table.setDEPTNAME(item.getString("DEPTNAME"));
                dept_table.setDESCRIPTION(item.getString("DESCRIPTION"));
                dept_table.setLEVELCODE(item.getString("LEVELCODE"));
                dept_table.setUPDATEUSERID(item.getString("UPDATEUSERID"));
                dept_table.setNOTES(item.getString("NOTES"));
                dept_table.setPARENTID(item.getString("PARENTID"));
                dept_table.setPREFIX(item.getString("PREFIX"));
                deptTableDao.insert(dept_table);
            }
            MyLogger.showLogWithLineNum(5,"结束时间"+TimeUtils.getCurrentTimeInString());

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Dept_Table> getAlldata() {
        List<Dept_Table> list = new ArrayList<>();
        list = deptTableDao.queryBuilder().list();
        return (ArrayList<Dept_Table>) list;
    }
}
