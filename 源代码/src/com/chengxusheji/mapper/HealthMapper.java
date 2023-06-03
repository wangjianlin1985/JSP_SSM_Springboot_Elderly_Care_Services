package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Health;

public interface HealthMapper {
	/*添加健康信息信息*/
	public void addHealth(Health health) throws Exception;

	/*按照查询条件分页查询健康信息记录*/
	public ArrayList<Health> queryHealth(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有健康信息记录*/
	public ArrayList<Health> queryHealthList(@Param("where") String where) throws Exception;

	/*按照查询条件的健康信息记录数*/
	public int queryHealthCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条健康信息记录*/
	public Health getHealth(int healthId) throws Exception;

	/*更新健康信息记录*/
	public void updateHealth(Health health) throws Exception;

	/*删除健康信息记录*/
	public void deleteHealth(int healthId) throws Exception;

}
