package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Health {
    /*健康id*/
    private Integer healthId;
    public Integer getHealthId(){
        return healthId;
    }
    public void setHealthId(Integer healthId){
        this.healthId = healthId;
    }

    /*老人*/
    private UserInfo userObj;
    public UserInfo getUserObj() {
        return userObj;
    }
    public void setUserObj(UserInfo userObj) {
        this.userObj = userObj;
    }

    /*心率*/
    @NotEmpty(message="心率不能为空")
    private String xinlv;
    public String getXinlv() {
        return xinlv;
    }
    public void setXinlv(String xinlv) {
        this.xinlv = xinlv;
    }

    /*血压*/
    @NotEmpty(message="血压不能为空")
    private String xueya;
    public String getXueya() {
        return xueya;
    }
    public void setXueya(String xueya) {
        this.xueya = xueya;
    }

    /*呼吸频率*/
    @NotEmpty(message="呼吸频率不能为空")
    private String hxpl;
    public String getHxpl() {
        return hxpl;
    }
    public void setHxpl(String hxpl) {
        this.hxpl = hxpl;
    }

    /*体温*/
    @NotEmpty(message="体温不能为空")
    private String tiwen;
    public String getTiwen() {
        return tiwen;
    }
    public void setTiwen(String tiwen) {
        this.tiwen = tiwen;
    }

    /*体重*/
    @NotEmpty(message="体重不能为空")
    private String tizhong;
    public String getTizhong() {
        return tizhong;
    }
    public void setTizhong(String tizhong) {
        this.tizhong = tizhong;
    }

    /*健康详述*/
    @NotEmpty(message="健康详述不能为空")
    private String healthDesc;
    public String getHealthDesc() {
        return healthDesc;
    }
    public void setHealthDesc(String healthDesc) {
        this.healthDesc = healthDesc;
    }

    /*测试日期*/
    @NotEmpty(message="测试日期不能为空")
    private String testDate;
    public String getTestDate() {
        return testDate;
    }
    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonHealth=new JSONObject(); 
		jsonHealth.accumulate("healthId", this.getHealthId());
		jsonHealth.accumulate("userObj", this.getUserObj().getName());
		jsonHealth.accumulate("userObjPri", this.getUserObj().getUser_name());
		jsonHealth.accumulate("xinlv", this.getXinlv());
		jsonHealth.accumulate("xueya", this.getXueya());
		jsonHealth.accumulate("hxpl", this.getHxpl());
		jsonHealth.accumulate("tiwen", this.getTiwen());
		jsonHealth.accumulate("tizhong", this.getTizhong());
		jsonHealth.accumulate("healthDesc", this.getHealthDesc());
		jsonHealth.accumulate("testDate", this.getTestDate().length()>19?this.getTestDate().substring(0,19):this.getTestDate());
		return jsonHealth;
    }}