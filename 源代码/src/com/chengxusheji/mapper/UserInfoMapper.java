﻿package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.UserInfo;

public interface UserInfoMapper {
	/*添加老人信息*/
	public void addUserInfo(UserInfo userInfo) throws Exception;

	/*按照查询条件分页查询老人记录*/
	public ArrayList<UserInfo> queryUserInfo(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有老人记录*/
	public ArrayList<UserInfo> queryUserInfoList(@Param("where") String where) throws Exception;

	/*按照查询条件的老人记录数*/
	public int queryUserInfoCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条老人记录*/
	public UserInfo getUserInfo(String user_name) throws Exception;

	/*更新老人记录*/
	public void updateUserInfo(UserInfo userInfo) throws Exception;

	/*删除老人记录*/
	public void deleteUserInfo(String user_name) throws Exception;

}
