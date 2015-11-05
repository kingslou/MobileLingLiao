package com.cyt.ieasy.db;

import com.cyt.ieasy.entity.HR_B_DEPT;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.ieasy.dao.Dept_Table;
import com.ieasy.dao.Dept_TableDao;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public void clearTable() {

    }

    @Override
    public void addData(Object object) {
        try{
            JSONObject item = new JSONObject(object.toString());
            Dept_Table dept_table = new Dept_Table();
            dept_table.setINNERID(item.getString(""));
            dept_table.setDEPTCODE(item.getString(""));
            deptTableDao.insert(dept_table);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<HR_B_DEPT> getAlldata() {
        return null;
    }
}
