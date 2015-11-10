package com.cyt.ieasy.db;

import com.ieasy.dao.DaoSession;
import java.util.ArrayList;

/**
 * Created by jin on 2015.11.05.
 */
public abstract class BaseTableUtil<T> {

    protected static DaoSession daoSession;

    public abstract void clearTable();

    public abstract ArrayList<T> getAlldata();

    public abstract void addData(Object object);

}
