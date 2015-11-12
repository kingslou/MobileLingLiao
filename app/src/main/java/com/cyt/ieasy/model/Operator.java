package com.cyt.ieasy.model;

import java.util.Date;

/**
 * Created by jin on 2015.10.28.
 * 操作员实体
 */
public class Operator extends BaseEntity implements Comparable<Operator> {

    public String Operator_ID;

    public String Operator_Name;

    public String Operator_Code;

    public String Operator_Desc;

    public String Operator_Password;

    public String Personnel_ID;

    public byte Operator_Status;

    public int Operator_Type;

    public String CP_ID;

    public String MD_ID;

    public int Version;

    public String AddOperater_ID;

    public Date AddTime;

    public String ModOperator_ID;

    public Date ModTime;

    public String Memo_1;

    public String Memo_2;

    public String Memo_3;

    public String Operator_Pwd;

    public int Operator_IfYeWuYuan;

    public int compareTo(Operator another) {
        // TODO Auto-generated method stub
        return this.Operator_Code.compareTo(another.Operator_Code);
    }
}
