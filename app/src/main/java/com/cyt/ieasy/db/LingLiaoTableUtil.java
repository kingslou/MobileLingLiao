package com.cyt.ieasy.db;

import com.cyt.ieasy.constans.Const;
import com.cyt.ieasy.event.MessageEvent;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.cyt.ieasy.tools.TimeUtils;
import com.ieasy.dao.LING_WUZI;
import com.ieasy.dao.LING_WUZIDETIAL;
import com.ieasy.dao.LING_WUZIDETIALDao;
import com.ieasy.dao.LING_WUZIDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

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
        queryBuilder.orderDesc(LING_WUZIDao.Properties.ADDTIME);
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

    public void insertWuZi(LING_WUZI ling_wuzi,List<LING_WUZIDETIAL> ling_wuzidetials){
        try{
            ling_wuzi.setADDTIME(new Date());
            ling_wuzi.setLL_NAME(TimeUtils.getCurrentTimeInString()+"领料");
            LING_WUZIDETIAL[] ling_wuzidetials1 = new LING_WUZIDETIAL[ling_wuzidetials.size()];
            for(int i=0;i<ling_wuzidetials.size();i++){
                ling_wuzidetials1[i] = ling_wuzidetials.get(i);
            }
            ling_wuziDao.insert(ling_wuzi);
            ling_wuzidetialDao.insertInTx(ling_wuzidetials1);
            EventBus.getDefault().post(new MessageEvent(Const.SaveSuccess));
        }catch(Exception e){
            e.printStackTrace();
            EventBus.getDefault().post(new MessageEvent(Const.SaveFailue));
        }
    }

    /**
     * 更新历史记录详细表中的物料数据
     * @param ling_wuzi
     * @param ling_wuzidetials
     */
    public void updateWuZi(LING_WUZI ling_wuzi,List<LING_WUZIDETIAL> ling_wuzidetials){
        try{
            LING_WUZIDETIAL[] entitys = new LING_WUZIDETIAL[ling_wuzidetials.size()];
            //首先得出该记录下所有的物料详细
            List<LING_WUZIDETIAL> ling_wuzidetialList = getLingWuZiDetial(ling_wuzi.getLL_CODE());
            //然后跟传递进来的进行差异比较更新
            for(LING_WUZIDETIAL ling_wuzidetial : ling_wuzidetials){
                for(int i=0;i<ling_wuzidetialList.size();i++){
                    LING_WUZIDETIAL tempLing = ling_wuzidetials.get(i);
                    if(tempLing.getLL_WZ_ID().equals(ling_wuzidetial.getLL_WZ_ID())){
                        entitys[i] = tempLing;
                        break;
                    }
                }
            }
            ling_wuzidetialDao.updateInTx(entitys);
        }catch(Exception e){
            e.printStackTrace();
            EventBus.getDefault().post(new MessageEvent(Const.SaveFailue));
        }
    }

    @Override
    public LING_WUZI getEntity(String value) {
        return null;
    }
}
