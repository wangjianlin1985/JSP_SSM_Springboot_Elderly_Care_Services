﻿package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Worker;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.po.Zhenduan;

import com.chengxusheji.mapper.ZhenduanMapper;
@Service
public class ZhenduanService {

	@Resource ZhenduanMapper zhenduanMapper;
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

    /*添加上门诊断记录*/
    public void addZhenduan(Zhenduan zhenduan) throws Exception {
    	zhenduanMapper.addZhenduan(zhenduan);
    }

    /*按照查询条件分页查询上门诊断记录*/
    public ArrayList<Zhenduan> queryZhenduan(Worker workerObj,String zhenduanDate,UserInfo userObj,String zhenduanState,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null  && !workerObj.getWorkUserName().equals(""))  where += " and t_zhenduan.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhenduanDate.equals("")) where = where + " and t_zhenduan.zhenduanDate like '%" + zhenduanDate + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null  && !userObj.getUser_name().equals(""))  where += " and t_zhenduan.userObj='" + userObj.getUser_name() + "'";
    	if(!zhenduanState.equals("")) where = where + " and t_zhenduan.zhenduanState like '%" + zhenduanState + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return zhenduanMapper.queryZhenduan(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Zhenduan> queryZhenduan(Worker workerObj,String zhenduanDate,UserInfo userObj,String zhenduanState) throws Exception  { 
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null && !workerObj.getWorkUserName().equals(""))  where += " and t_zhenduan.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhenduanDate.equals("")) where = where + " and t_zhenduan.zhenduanDate like '%" + zhenduanDate + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_zhenduan.userObj='" + userObj.getUser_name() + "'";
    	if(!zhenduanState.equals("")) where = where + " and t_zhenduan.zhenduanState like '%" + zhenduanState + "%'";
    	return zhenduanMapper.queryZhenduanList(where);
    }

    /*查询所有上门诊断记录*/
    public ArrayList<Zhenduan> queryAllZhenduan()  throws Exception {
        return zhenduanMapper.queryZhenduanList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Worker workerObj,String zhenduanDate,UserInfo userObj,String zhenduanState) throws Exception {
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null && !workerObj.getWorkUserName().equals(""))  where += " and t_zhenduan.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhenduanDate.equals("")) where = where + " and t_zhenduan.zhenduanDate like '%" + zhenduanDate + "%'";
    	if(null != userObj &&  userObj.getUser_name() != null && !userObj.getUser_name().equals(""))  where += " and t_zhenduan.userObj='" + userObj.getUser_name() + "'";
    	if(!zhenduanState.equals("")) where = where + " and t_zhenduan.zhenduanState like '%" + zhenduanState + "%'";
        recordNumber = zhenduanMapper.queryZhenduanCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取上门诊断记录*/
    public Zhenduan getZhenduan(int zhenduanId) throws Exception  {
        Zhenduan zhenduan = zhenduanMapper.getZhenduan(zhenduanId);
        return zhenduan;
    }

    /*更新上门诊断记录*/
    public void updateZhenduan(Zhenduan zhenduan) throws Exception {
        zhenduanMapper.updateZhenduan(zhenduan);
    }

    /*删除一条上门诊断记录*/
    public void deleteZhenduan (int zhenduanId) throws Exception {
        zhenduanMapper.deleteZhenduan(zhenduanId);
    }

    /*删除多条上门诊断信息*/
    public int deleteZhenduans (String zhenduanIds) throws Exception {
    	String _zhenduanIds[] = zhenduanIds.split(",");
    	for(String _zhenduanId: _zhenduanIds) {
    		zhenduanMapper.deleteZhenduan(Integer.parseInt(_zhenduanId));
    	}
    	return _zhenduanIds.length;
    }
}
