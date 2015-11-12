package com.cyt.ieasy.db;

import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.ieasy.dao.LING_WUZIDETIALDao;
import com.ieasy.dao.LING_WUZIDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * 本地领料物质，历史记录处理工具类
 * Created by jin on 2015.11.10.
 */
public class LingLiaoTableUtil extends BaseTableUtil {

    private volatile static LingLiaoTableUtil lingLiaoTableUtil;
    private volatile static LING_WUZIDao ling_wuziDao;
    private volatile static LING_WUZIDETIALDao ling_wuzidetialDao;
    private LingLiaoTableUtil(){
    }

    public static LingLiaoTableUtil getLiaoTableUtil(){
        if(lingLiaoTableUtil==null){
            synchronized (LingLiaoTableUtil.class){
                if(lingLiaoTableUtil==null){
                    lingLiaoTableUtil = new LingLiaoTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    ling_wuziDao = daoSession.getLING_WUZIDao();
                    ling_wuzidetialDao = daoSession.getLING_WUZIDETIALDao();
                }
            }
        }
        return lingLiaoTableUtil;
    }

    @Override
    public void clearTable() {
        ling_wuziDao.deleteAll();
        ling_wuzidetialDao.deleteAll();
    }

    @Override
    public ArrayList getAlldata() {
        return null;
    }

    /**
     * 返回条目数
     * @param returnNum
     * @return
     */
    public List<LING_WUZI> getLingWuzi(int returnNum){
        List<LING_WUZI> ling_wuziList = new ArrayList<>();
        QueryBuilder queryBuilder = ling_wuziDao.queryBuilder();
        queryBuilder.orderAsc(LING_WUZIDao.Properties.ADDTIME);
        ling_wuziList = queryBuilder.list();
        return  ling_wuziList;
    }

    /**
     * 根据主表中的code去查询
     * @param lingCode
     * @return
     */
    public List<LING_WUZIDETIAL> getLingWuZiDetial(String lingCode){
        List<LING_WUZIDETIAL> ling_wuzidetials = new ArrayList<>();
        QueryBuilder queryBuilder = ling_wuzidetialDao.queryBuilder();
        queryBuilder.where(LING_WUZIDETIALDao.Properties.LL_CODE.eq(lingCode));
        return ling_wuzidetials;
    }
    @Override
    public void addData(Object object) {

    }
}
