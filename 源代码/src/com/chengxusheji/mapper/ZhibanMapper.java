package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Zhiban;

public interface ZhibanMapper {
	/*添加值班信息*/
	public void addZhiban(Zhiban zhiban) throws Exception;

	/*按照查询条件分页查询值班记录*/
	public ArrayList<Zhiban> queryZhiban(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有值班记录*/
	public ArrayList<Zhiban> queryZhibanList(@Param("where") String where) throws Exception;

	/*按照查询条件的值班记录数*/
	public int queryZhibanCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条值班记录*/
	public Zhiban getZhiban(int zhibanId) throws Exception;

	/*更新值班记录*/
	public void updateZhiban(Zhiban zhiban) throws Exception;

	/*删除值班记录*/
	public void deleteZhiban(int zhibanId) throws Exception;

}
