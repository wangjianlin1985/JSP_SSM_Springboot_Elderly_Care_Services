package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Worker;

public interface WorkerMapper {
	/*添加医疗工作者信息*/
	public void addWorker(Worker worker) throws Exception;

	/*按照查询条件分页查询医疗工作者记录*/
	public ArrayList<Worker> queryWorker(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有医疗工作者记录*/
	public ArrayList<Worker> queryWorkerList(@Param("where") String where) throws Exception;

	/*按照查询条件的医疗工作者记录数*/
	public int queryWorkerCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条医疗工作者记录*/
	public Worker getWorker(String workUserName) throws Exception;

	/*更新医疗工作者记录*/
	public void updateWorker(Worker worker) throws Exception;

	/*删除医疗工作者记录*/
	public void deleteWorker(String workUserName) throws Exception;

}
