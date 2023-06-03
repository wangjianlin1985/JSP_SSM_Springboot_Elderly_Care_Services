package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Worker {
    /*工作者用户名*/
    @NotEmpty(message="工作者用户名不能为空")
    private String workUserName;
    public String getWorkUserName(){
        return workUserName;
    }
    public void setWorkUserName(String workUserName){
        this.workUserName = workUserName;
    }

    /*登录密码*/
    @NotEmpty(message="登录密码不能为空")
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*姓名*/
    @NotEmpty(message="姓名不能为空")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /*性别*/
    @NotEmpty(message="性别不能为空")
    private String gender;
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    /*出生日期*/
    @NotEmpty(message="出生日期不能为空")
    private String birthDate;
    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /*资质信息*/
    @NotEmpty(message="资质信息不能为空")
    private String zzxx;
    public String getZzxx() {
        return zzxx;
    }
    public void setZzxx(String zzxx) {
        this.zzxx = zzxx;
    }

    /*联系电话*/
    @NotEmpty(message="联系电话不能为空")
    private String telephone;
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /*联系邮箱*/
    @NotEmpty(message="联系邮箱不能为空")
    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    /*家庭地址*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*医疗者简介*/
    @NotEmpty(message="医疗者简介不能为空")
    private String workerDesc;
    public String getWorkerDesc() {
        return workerDesc;
    }
    public void setWorkerDesc(String workerDesc) {
        this.workerDesc = workerDesc;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonWorker=new JSONObject(); 
		jsonWorker.accumulate("workUserName", this.getWorkUserName());
		jsonWorker.accumulate("password", this.getPassword());
		jsonWorker.accumulate("name", this.getName());
		jsonWorker.accumulate("gender", this.getGender());
		jsonWorker.accumulate("birthDate", this.getBirthDate().length()>19?this.getBirthDate().substring(0,19):this.getBirthDate());
		jsonWorker.accumulate("zzxx", this.getZzxx());
		jsonWorker.accumulate("telephone", this.getTelephone());
		jsonWorker.accumulate("email", this.getEmail());
		jsonWorker.accumulate("address", this.getAddress());
		jsonWorker.accumulate("workerDesc", this.getWorkerDesc());
		return jsonWorker;
    }}