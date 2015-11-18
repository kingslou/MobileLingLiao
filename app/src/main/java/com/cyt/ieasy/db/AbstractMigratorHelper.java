package com.cyt.ieasy.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库更新帮助类
 * Created by jin on 2015.11.16.
 */
public abstract class AbstractMigratorHelper {

    public abstract void onUpgrade(SQLiteDatabase db);

}
