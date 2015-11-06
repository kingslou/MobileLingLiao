package com.cyt.ieasy.db;
import com.cyt.ieasy.mobilelingliao.MyApplication;
import com.ieasy.dao.WUZI_STOCK;
import com.ieasy.dao.WUZI_STOCKDao;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jin on 2015.11.06.
 */
public class StockTableUtil extends BaseTableUtil {

    private volatile static StockTableUtil stockTableUtil;
    private volatile static WUZI_STOCKDao wuzi_stockDao;

    private StockTableUtil(){

    }

    public static StockTableUtil getStockTableUtil(){
        if(stockTableUtil==null){
            synchronized (StockTableUtil.class){
                if(stockTableUtil==null){
                    stockTableUtil = new StockTableUtil();
                    daoSession = MyApplication.getDaoSession();
                    wuzi_stockDao = daoSession.getWUZI_STOCKDao();
                }
            }
        }
        return stockTableUtil;
    }
    @Override
    public void clearTable() {
        wuzi_stockDao.deleteAll();
    }

    @Override
    public ArrayList<WUZI_STOCK> getAlldata() {
        List<WUZI_STOCK> wuzi_stockList = new ArrayList<>();
        wuzi_stockList = wuzi_stockDao.queryBuilder().list();
        return (ArrayList<WUZI_STOCK>) wuzi_stockList;
    }

    @Override
    public void addData(Object object) {
        try{
            JSONArray Items = new JSONArray(object.toString());
            WUZI_STOCK[] wuzi_stocks = new WUZI_STOCK[Items.length()];
            for(int i=0;i<Items.length();i++){
                WUZI_STOCK wuzi_stock = new WUZI_STOCK();
                JSONObject item = Items.getJSONObject(i);
                wuzi_stock.setCK_ID(item.getString("CK_ID"));
                wuzi_stock.setCK_NAME(item.getString("CK_NAME"));
                wuzi_stock.setCK_CODE(item.getString("CK_CODE"));
                wuzi_stock.setCK_CRE_ID(item.getString("CK_CRE_ID"));
                wuzi_stock.setCK_DISP_ORDER(item.getString("CK_DISP_ORDER"));
                wuzi_stock.setCK_DEL_FLAG(item.getInt("CK_DEL_FLAG"));
                wuzi_stock.setCK_MANAGER(item.getString("CK_MANAGER"));
                wuzi_stock.setCK_IF_ZONGCANG(item.getInt("CK_IF_ZONGCANG"));
                wuzi_stock.setCK_MD_ID(item.getString("CK_MD_ID"));
                wuzi_stock.setCK_REMARK(item.getString("CK_REMARK"));
                wuzi_stock.setCK_STATUS(item.getInt("CK_STATUS"));
                wuzi_stock.setCK_VERSION(item.getInt("CK_VERSION"));
                wuzi_stock.setCK_BAK1(item.getString("CK_BAK1"));
                wuzi_stock.setCK_BAK2(item.getString("CK_BAK2"));
                wuzi_stock.setCK_BAK3(item.getString("CK_BAK3"));
                wuzi_stock.setCK_BAK4(item.getString("CK_BAK4"));
                wuzi_stock.setCK_BAK5(item.getString("CK_BAK5"));
                wuzi_stocks[i] = wuzi_stock;
            }
            wuzi_stockDao.insertInTx(wuzi_stocks);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
