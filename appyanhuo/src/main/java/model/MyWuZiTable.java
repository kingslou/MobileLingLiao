package model;


import com.yh.dao.WuZi_Table;

/**
 * 增加若干字段
 * Created by jin on 2015.12.24.
 */
public class MyWuZiTable extends WuZi_Table {

    private String Gys_Id;
    private String Gys_Name;

    private String Bm_Id;
    private String Bm_Name;

    private Double Totle;
    private Double Num;
    private Integer Tzs;

    public String getGys_Id() {
        return Gys_Id;
    }

    public void setGys_Id(String gys_Id) {
        Gys_Id = gys_Id;
    }

    public String getBm_Id() {
        return Bm_Id;
    }

    public void setBm_Id(String bm_Id) {
        Bm_Id = bm_Id;
    }

    public String getBm_Name() {
        return Bm_Name;
    }

    public void setBm_Name(String bm_Name) {
        Bm_Name = bm_Name;
    }

    public Double getTotle() {
        return Totle;
    }

    public void setTotle(Double totle) {
        Totle = totle;
    }

    public Integer getTzs() {
        return Tzs;
    }

    public void setTzs(Integer tzs) {
        Tzs = tzs;
    }
}
