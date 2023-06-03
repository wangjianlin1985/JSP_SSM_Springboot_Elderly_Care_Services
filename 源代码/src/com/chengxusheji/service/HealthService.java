package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.Health;

import com.chengxusheji.mapper.HealthMapper;
@Service
public class HealthService {

	@Resource HealthMapper healthMapper;
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

    /*添加健康信息记录*/
    public void addHealth(Health health) throws Exception {
    	healthMapper.addHealth(health);
    }

    /*按照查询条件分页查询健康信息记录*/
    public ArrayList<Health> queryHealth(UserInfo userObj,String testDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_health.userObj='" + userObj.getUser_name() + "'";
    	if(!testDate.equals("")) where = where + " and t_health.testDate like '%" + testDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return healthMapper.queryHealth(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Health> queryHealth(UserInfo userObj,String testDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_health.userObj='" + userObj.getUser_name() + "'";
    	if(!testDate.equals("")) where = where + " and t_health.testDate like '%" + testDate + "%'";
    	return healthMapper.queryHealthList(where);
    }

    /*查询所有健康信息记录*/
    public ArrayList<Health> queryAllHealth()  throws Exception {
        return healthMapper.queryHealthList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(UserInfo userObj,String testDate) throws Exception {
     	String where = "where 1=1";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_health.userObj='" + userObj.getUser_name() + "'";
    	if(!testDate.equals("")) where = where + " and t_health.testDate like '%" + testDate + "%'";
        recordNumber = healthMapper.queryHealthCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取健康信息记录*/
    public Health getHealth(int healthId) throws Exception  {
        Health health = healthMapper.getHealth(healthId);
        return health;
    }

    /*更新健康信息记录*/
    public void updateHealth(Health health) throws Exception {
        healthMapper.updateHealth(health);
    }

    /*删除一条健康信息记录*/
    public void deleteHealth (int healthId) throws Exception {
        healthMapper.deleteHealth(healthId);
    }

    /*删除多条健康信息信息*/
    public int deleteHealths (String healthIds) throws Exception {
    	String _healthIds[] = healthIds.split(",");
    	for(String _healthId: _healthIds) {
    		healthMapper.deleteHealth(Integer.parseInt(_healthId));
    	}
    	return _healthIds.length;
    }
}
