package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;

import com.chengxusheji.po.Admin;
import com.chengxusheji.po.Worker;

import com.chengxusheji.mapper.WorkerMapper;
@Service
public class WorkerService {

	@Resource WorkerMapper workerMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加医疗工作者记录*/
    public void addWorker(Worker worker) throws Exception {
    	workerMapper.addWorker(worker);
    }

    /*按照查询条件分页查询医疗工作者记录*/
    public ArrayList<Worker> queryWorker(String workUserName,String name,String birthDate,String telephone,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!workUserName.equals("")) where = where + " and t_worker.workUserName like '%" + workUserName + "%'";
    	if(!name.equals("")) where = where + " and t_worker.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_worker.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_worker.telephone like '%" + telephone + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return workerMapper.queryWorker(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Worker> queryWorker(String workUserName,String name,String birthDate,String telephone) throws Exception  { 
     	String where = "where 1=1";
    	if(!workUserName.equals("")) where = where + " and t_worker.workUserName like '%" + workUserName + "%'";
    	if(!name.equals("")) where = where + " and t_worker.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_worker.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_worker.telephone like '%" + telephone + "%'";
    	return workerMapper.queryWorkerList(where);
    }

    /*查询所有医疗工作者记录*/
    public ArrayList<Worker> queryAllWorker()  throws Exception {
        return workerMapper.queryWorkerList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String workUserName,String name,String birthDate,String telephone) throws Exception {
     	String where = "where 1=1";
    	if(!workUserName.equals("")) where = where + " and t_worker.workUserName like '%" + workUserName + "%'";
    	if(!name.equals("")) where = where + " and t_worker.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_worker.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_worker.telephone like '%" + telephone + "%'";
        recordNumber = workerMapper.queryWorkerCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取医疗工作者记录*/
    public Worker getWorker(String workUserName) throws Exception  {
        Worker worker = workerMapper.getWorker(workUserName);
        return worker;
    }

    /*更新医疗工作者记录*/
    public void updateWorker(Worker worker) throws Exception {
        workerMapper.updateWorker(worker);
    }

    /*删除一条医疗工作者记录*/
    public void deleteWorker (String workUserName) throws Exception {
        workerMapper.deleteWorker(workUserName);
    }

    /*删除多条医疗工作者信息*/
    public int deleteWorkers (String workUserNames) throws Exception {
    	String _workUserNames[] = workUserNames.split(",");
    	for(String _workUserName: _workUserNames) {
    		workerMapper.deleteWorker(_workUserName);
    	}
    	return _workUserNames.length;
    }
    
    
	/*保存业务逻辑错误信息字段*/
	private String errMessage;
	public String getErrMessage() { return this.errMessage; }
	
	/*验证用户登录*/
	public boolean checkLogin(Admin admin) throws Exception { 
		Worker db_worker = (Worker) workerMapper.getWorker(admin.getUsername());
		if(db_worker == null) { 
			this.errMessage = " 账号不存在 ";
			System.out.print(this.errMessage);
			return false;
		} else if( !db_worker.getPassword().equals(admin.getPassword())) {
			this.errMessage = " 密码不正确! ";
			System.out.print(this.errMessage);
			return false;
		}
		
		return true;
	}
}
