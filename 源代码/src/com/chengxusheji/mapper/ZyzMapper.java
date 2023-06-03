package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Zyz;

public interface ZyzMapper {
	/*添加志愿者信息*/
	public void addZyz(Zyz zyz) throws Exception;

	/*按照查询条件分页查询志愿者记录*/
	public ArrayList<Zyz> queryZyz(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有志愿者记录*/
	public ArrayList<Zyz> queryZyzList(@Param("where") String where) throws Exception;

	/*按照查询条件的志愿者记录数*/
	public int queryZyzCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条志愿者记录*/
	public Zyz getZyz(int zyzId) throws Exception;

	/*更新志愿者记录*/
	public void updateZyz(Zyz zyz) throws Exception;

	/*删除志愿者记录*/
	public void deleteZyz(int zyzId) throws Exception;

}
