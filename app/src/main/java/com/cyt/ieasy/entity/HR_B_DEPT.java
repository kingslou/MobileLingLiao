package com.cyt.ieasy.entity;

import java.util.List;

public class HR_B_DEPT {
  
  private String INNERID ;

  private String DEPTCODE ;

  private String DEPTNAME ;

  private String PARENTID ;

  private int PROPERTY ;

  private String PREFIX ;

  private String LEVELCODE ;

  private String DESCRIPTION ;

  private String NOTES ;

  private String ADDTIME ;

  private String ADDUSERID ;

  private String ADDIP ;

  private String UPDATETIME ;

  private String UPDATEUSERID ;

  private String UPDATEIP ;

  private String PDEPTNAME ;

  private List<HR_B_DEPT> PDEPTLIST;
 
  /// 存储部门
  private int DEPT_IF_CUNCHUN ;

  /// 出品部门
  private int Dept_If_Production_Department ;
 
  /// 销售部门
  private int Dept_If_Marketing_Department ;

  public String getINNERID() {
    return INNERID;
  }

  public void setINNERID(String INNERID) {
    this.INNERID = INNERID;
  }

  public String getDEPTCODE() {
    return DEPTCODE;
  }

  public void setDEPTCODE(String DEPTCODE) {
    this.DEPTCODE = DEPTCODE;
  }

  public String getDEPTNAME() {
    return DEPTNAME;
  }

  public void setDEPTNAME(String DEPTNAME) {
    this.DEPTNAME = DEPTNAME;
  }

  public String getPARENTID() {
    return PARENTID;
  }

  public void setPARENTID(String PARENTID) {
    this.PARENTID = PARENTID;
  }

  public int getPROPERTY() {
    return PROPERTY;
  }

  public void setPROPERTY(int PROPERTY) {
    this.PROPERTY = PROPERTY;
  }

  public String getPREFIX() {
    return PREFIX;
  }

  public void setPREFIX(String PREFIX) {
    this.PREFIX = PREFIX;
  }

  public String getLEVELCODE() {
    return LEVELCODE;
  }

  public void setLEVELCODE(String LEVELCODE) {
    this.LEVELCODE = LEVELCODE;
  }

  public String getDESCRIPTION() {
    return DESCRIPTION;
  }

  public void setDESCRIPTION(String DESCRIPTION) {
    this.DESCRIPTION = DESCRIPTION;
  }

  public String getNOTES() {
    return NOTES;
  }

  public void setNOTES(String NOTES) {
    this.NOTES = NOTES;
  }

  public String getADDTIME() {
    return ADDTIME;
  }

  public void setADDTIME(String ADDTIME) {
    this.ADDTIME = ADDTIME;
  }

  public String getADDUSERID() {
    return ADDUSERID;
  }

  public void setADDUSERID(String ADDUSERID) {
    this.ADDUSERID = ADDUSERID;
  }

  public String getADDIP() {
    return ADDIP;
  }

  public void setADDIP(String ADDIP) {
    this.ADDIP = ADDIP;
  }

  public String getUPDATETIME() {
    return UPDATETIME;
  }

  public void setUPDATETIME(String UPDATETIME) {
    this.UPDATETIME = UPDATETIME;
  }

  public String getUPDATEUSERID() {
    return UPDATEUSERID;
  }

  public void setUPDATEUSERID(String UPDATEUSERID) {
    this.UPDATEUSERID = UPDATEUSERID;
  }

  public String getUPDATEIP() {
    return UPDATEIP;
  }

  public void setUPDATEIP(String UPDATEIP) {
    this.UPDATEIP = UPDATEIP;
  }

  public String getPDEPTNAME() {
    return PDEPTNAME;
  }

  public void setPDEPTNAME(String PDEPTNAME) {
    this.PDEPTNAME = PDEPTNAME;
  }

  public List<HR_B_DEPT> getPDEPTLIST() {
    return PDEPTLIST;
  }

  public void setPDEPTLIST(List<HR_B_DEPT> PDEPTLIST) {
    this.PDEPTLIST = PDEPTLIST;
  }

  public int getDEPT_IF_CUNCHUN() {
    return DEPT_IF_CUNCHUN;
  }

  public void setDEPT_IF_CUNCHUN(int DEPT_IF_CUNCHUN) {
    this.DEPT_IF_CUNCHUN = DEPT_IF_CUNCHUN;
  }

  public int getDept_If_Production_Department() {
    return Dept_If_Production_Department;
  }

  public void setDept_If_Production_Department(int dept_If_Production_Department) {
    Dept_If_Production_Department = dept_If_Production_Department;
  }

  public int getDept_If_Marketing_Department() {
    return Dept_If_Marketing_Department;
  }

  public void setDept_If_Marketing_Department(int dept_If_Marketing_Department) {
    Dept_If_Marketing_Department = dept_If_Marketing_Department;
  }
}


