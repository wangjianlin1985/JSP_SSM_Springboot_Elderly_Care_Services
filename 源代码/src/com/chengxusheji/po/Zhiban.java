package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Zhiban {
    /*值班id*/
    private Integer zhibanId;
    public Integer getZhibanId(){
        return zhibanId;
    }
    public void setZhibanId(Integer zhibanId){
        this.zhibanId = zhibanId;
    }

    /*值班人*/
    private Worker workerObj;
    public Worker getWorkerObj() {
        return workerObj;
    }
    public void setWorkerObj(Worker workerObj) {
        this.workerObj = workerObj;
    }

    /*值班日期*/
    @NotEmpty(message="值班日期不能为空")
    private String zhibanDate;
    public String getZhibanDate() {
        return zhibanDate;
    }
    public void setZhibanDate(String zhibanDate) {
        this.zhibanDate = zhibanDate;
    }

    /*值班时间*/
    @NotEmpty(message="值班时间不能为空")
    private String zhibanTime;
    public String getZhibanTime() {
        return zhibanTime;
    }
    public void setZhibanTime(String zhibanTime) {
        this.zhibanTime = zhibanTime;
    }

    /*值班备注*/
    @NotEmpty(message="值班备注不能为空")
    private String zhibanMemo;
    public String getZhibanMemo() {
        return zhibanMemo;
    }
    public void setZhibanMemo(String zhibanMemo) {
        this.zhibanMemo = zhibanMemo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonZhiban=new JSONObject(); 
		jsonZhiban.accumulate("zhibanId", this.getZhibanId());
		jsonZhiban.accumulate("workerObj", this.getWorkerObj().getName());
		jsonZhiban.accumulate("workerObjPri", this.getWorkerObj().getWorkUserName());
		jsonZhiban.accumulate("zhibanDate", this.getZhibanDate().length()>19?this.getZhibanDate().substring(0,19):this.getZhibanDate());
		jsonZhiban.accumulate("zhibanTime", this.getZhibanTime());
		jsonZhiban.accumulate("zhibanMemo", this.getZhibanMemo());
		return jsonZhiban;
    }}