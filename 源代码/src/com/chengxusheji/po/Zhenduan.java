package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Zhenduan {
    /*诊断id*/
    private Integer zhenduanId;
    public Integer getZhenduanId(){
        return zhenduanId;
    }
    public void setZhenduanId(Integer zhenduanId){
        this.zhenduanId = zhenduanId;
    }

    /*医疗者*/
    private Worker workerObj;
    public Worker getWorkerObj() {
        return workerObj;
    }
    public void setWorkerObj(Worker workerObj) {
        this.workerObj = workerObj;
    }

    /*诊断日期*/
    @NotEmpty(message="诊断日期不能为空")
    private String zhenduanDate;
    public String getZhenduanDate() {
        return zhenduanDate;
    }
    public void setZhenduanDate(String zhenduanDate) {
        this.zhenduanDate = zhenduanDate;
    }

    /*上门时间*/
    @NotEmpty(message="上门时间不能为空")
    private String zhenduanTime;
    public String getZhenduanTime() {
        return zhenduanTime;
    }
    public void setZhenduanTime(String zhenduanTime) {
        this.zhenduanTime = zhenduanTime;
    }

    /*诊断老人*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*诊断状态*/
    @NotEmpty(message="诊断状态不能为空")
    private String zhenduanState;
    public String getZhenduanState() {
        return zhenduanState;
    }
    public void setZhenduanState(String zhenduanState) {
        this.zhenduanState = zhenduanState;
    }

    /*诊断结果*/
    @NotEmpty(message="诊断结果不能为空")
    private String zhenduanResult;
    public String getZhenduanResult() {
        return zhenduanResult;
    }
    public void setZhenduanResult(String zhenduanResult) {
        this.zhenduanResult = zhenduanResult;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonZhenduan=new JSONObject(); 
		jsonZhenduan.accumulate("zhenduanId", this.getZhenduanId());
		jsonZhenduan.accumulate("workerObj", this.getWorkerObj().getName());
		jsonZhenduan.accumulate("workerObjPri", this.getWorkerObj().getWorkUserName());
		jsonZhenduan.accumulate("zhenduanDate", this.getZhenduanDate().length()>19?this.getZhenduanDate().substring(0,19):this.getZhenduanDate());
		jsonZhenduan.accumulate("zhenduanTime", this.getZhenduanTime());
		jsonZhenduan.accumulate("userObj", this.getUserObj().getName());
		jsonZhenduan.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonZhenduan.accumulate("zhenduanState", this.getZhenduanState());
		jsonZhenduan.accumulate("zhenduanResult", this.getZhenduanResult());
		return jsonZhenduan;
    }}