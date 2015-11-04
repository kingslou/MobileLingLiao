package com.ieasy;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    //文件生成的相对路径
    public static final String DAO_PATH = "../app/src/main/java-gen";
    //包名
    public static final String PACKAGE_NAME="com.ieasy.dao";
    //数据库版本号
    public static final int  VERSION_CODE = 1;
    //部门表
    public static final String TAB_DEPT = "Dept_Table";
    //物料表
    public static final String TAB_WUZI = "WuZi_Table";
    //物料类别表
    public static final String TAB_WUZI_CATALOG = "WUZI_CATALOG";
    //仓库表
    public static final String TAB_WUZI_STOCK = "WUZI_STOCK";

    public static void main(String[] args){

        Schema schema = new Schema(VERSION_CODE,PACKAGE_NAME);
        creatDeptTable(schema);
        creatWuZiTable(schema);
        creatWuZiCatalog(schema);
        creatWuZiStock(schema);
    }

    public static void creatWuZiStock(Schema schema){
        Entity entity = schema.addEntity(TAB_WUZI_STOCK);
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("CK_ID");
        entity.addStringProperty("CK_CODE");
        entity.addStringProperty("CK_NAME");
        entity.addStringProperty("CK_MANAGER");
        entity.addStringProperty("CK_REMARK");
        entity.addStringProperty("CK_DISP_ORDER");
        entity.addIntProperty("CK_STATUS");
        entity.addIntProperty("CK_IF_ZONGCANG");
        entity.addIntProperty("CK_DEL_FLAG");
        entity.addIntProperty("CK_VERSION");
        entity.addStringProperty("CK_MD_ID");
        entity.addDateProperty("CK_CRE_TIME");
        entity.addStringProperty("CK_CRE_ID");
        entity.addDateProperty("CK_MOD_TIME");
        entity.addStringProperty("CK_BAK1");
        entity.addStringProperty("CK_BAK2");
        entity.addStringProperty("CK_BAK3");
        entity.addStringProperty("CK_BAK4");
        entity.addStringProperty("CK_BAK5");
        try{
            new DaoGenerator().generateAll(schema,DAO_PATH);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void creatWuZiCatalog(Schema schema){
        Entity entity = schema.addEntity(TAB_WUZI_CATALOG);
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("WC_ID");
        entity.addStringProperty("CLOUD_ID");
        entity.addStringProperty("WC_ZB_ID");
        entity.addStringProperty("WC_NAME");
        entity.addStringProperty("WC_CODE");
        entity.addStringProperty("WC_PARENT_ID");
        entity.addStringProperty("WC_IDS");
        entity.addStringProperty("WC_REMARK");
        entity.addStringProperty("WC_DISP_ORDER");
        entity.addDateProperty("WC_CRE_TIME");
        entity.addDateProperty("WC_MOD_TIME");
        entity.addStringProperty("WC_DEL_FLAG");
        entity.addStringProperty("WC_VERSION");
        entity.addStringProperty("WC_STATUS");
        entity.addStringProperty("WC_CRE_ID");
        entity.addStringProperty("WC_MOD_ID");
        entity.addStringProperty("WC_BAK1");
        entity.addStringProperty("WC_BAK2");
        entity.addStringProperty("WC_BAK3");
        entity.addStringProperty("WC_BAK4");
        entity.addStringProperty("WC_BAK5");
        try{
            new DaoGenerator().generateAll(schema,DAO_PATH);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void creatWuZiTable(Schema schema){
        Entity entity = schema.addEntity(TAB_WUZI);
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("WZ_ID");
        entity.addStringProperty("CLOUD_ID");
        entity.addStringProperty("WC_ZB_ID");
        entity.addStringProperty("WZ_SZ_ID");
        entity.addStringProperty("WZ_SZ_NAME");
        entity.addStringProperty("WZ_NAME");
        entity.addStringProperty("WZ_CODE");
        entity.addStringProperty("WZ_QUICK_CODE");
        entity.addStringProperty("WZ_UNIT_ID");
        entity.addDateProperty("WZ_CRE_TIME");
        entity.addDateProperty("WZ_MOD_TIME");
        entity.addStringProperty("WZ_UNIT_NAME");
        entity.addStringProperty("WZ_COUNT_FLAG");
        entity.addStringProperty("CW_COUNT_MAX");
        entity.addStringProperty("CW_COUNT_MIN");
        entity.addStringProperty("CW_COUNT_APPLY");
        entity.addStringProperty("WZ_KIND");
        entity.addStringProperty("WZ_SPECIFICATION");
        entity.addStringProperty("WZ_PIECES");
        entity.addStringProperty("WZ_PRICE");
        entity.addStringProperty("WZ_STORE_CONDITION");
        entity.addStringProperty("WZ_CATEGORY");
        entity.addStringProperty("WZ_NET_RATE");
        entity.addStringProperty("WZ_IF_PANDIAN");
        entity.addStringProperty("WZ_REMARK");
        entity.addStringProperty("WZ_DISP_ORDER");
        entity.addStringProperty("WZ_IF_BOM");
        entity.addStringProperty("WZ_STATUS");
        entity.addStringProperty("WZ_DEL_FLAG");
        entity.addStringProperty("WZ_VERSION");
        entity.addStringProperty("WZ_CRE_ID");
        entity.addStringProperty("WZ_MOD_ID");
        entity.addStringProperty("WZ_BAK1");
        entity.addStringProperty("WZ_BAK2");
        entity.addStringProperty("WZ_BAK3");
        entity.addStringProperty("WZ_BAK4");
        entity.addStringProperty("WZ_BAK5");
        entity.addStringProperty("WZ_CG_UNIT_NAME");
        entity.addStringProperty("WZ_CG_UNIT_ID");
        try{
            new DaoGenerator().generateAll(schema,DAO_PATH);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public static void creatDeptTable(Schema schema){
        Entity entity = schema.addEntity(TAB_DEPT);
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("INNERID");
        entity.addStringProperty("DEPTCODE");
        entity.addStringProperty("DEPTNAME");
        entity.addStringProperty("PARENTID");
        entity.addStringProperty("PROPERTY");
        entity.addStringProperty("PREFIX");
        entity.addStringProperty("LEVELCODE");
        entity.addStringProperty("DESCRIPTION");
        entity.addStringProperty("NOTES");
        entity.addDateProperty("ADDTIME");
        entity.addStringProperty("ADDUSERID");
        entity.addStringProperty("ADDIP");
        entity.addDateProperty("UPDATETIME");
        entity.addStringProperty("UPDATEUSERID");
        entity.addStringProperty("UPDATEIP");
        entity.addStringProperty("DEPT_IF_CUNCHUN");
        try{
            new DaoGenerator().generateAll(schema,DAO_PATH);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
