package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Zyz {
    /*志愿者id*/
    private Integer zyzId;
    public Integer getZyzId(){
        return zyzId;
    }
    public void setZyzId(Integer zyzId){
        this.zyzId = zyzId;
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

    /*志愿者照片*/
    private String userPhoto;
    public String getUserPhoto() {
        return userPhoto;
    }
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
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

    /*家庭地址*/
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    /*个人档案*/
    @NotEmpty(message="个人档案不能为空")
    private String grda;
    public String getGrda() {
        return grda;
    }
    public void setGrda(String grda) {
        this.grda = grda;
    }

    /*服务项目*/
    @NotEmpty(message="服务项目不能为空")
    private String fwxm;
    public String getFwxm() {
        return fwxm;
    }
    public void setFwxm(String fwxm) {
        this.fwxm = fwxm;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonZyz=new JSONObject(); 
		jsonZyz.accumulate("zyzId", this.getZyzId());
		jsonZyz.accumulate("name", this.getName());
		jsonZyz.accumulate("gender", this.getGender());
		jsonZyz.accumulate("birthDate", this.getBirthDate().length()>19?this.getBirthDate().substring(0,19):this.getBirthDate());
		jsonZyz.accumulate("userPhoto", this.getUserPhoto());
		jsonZyz.accumulate("telephone", this.getTelephone());
		jsonZyz.accumulate("address", this.getAddress());
		jsonZyz.accumulate("grda", this.getGrda());
		jsonZyz.accumulate("fwxm", this.getFwxm());
		return jsonZyz;
    }}