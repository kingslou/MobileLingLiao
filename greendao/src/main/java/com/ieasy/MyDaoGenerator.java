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

    public static void main(String[] args){

        Schema schema = new Schema(VERSION_CODE,PACKAGE_NAME);
        creatDeptTable(schema);
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
