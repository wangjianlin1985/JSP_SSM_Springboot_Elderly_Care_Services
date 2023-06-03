package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Worker;
import com.chengxusheji.po.Zhiban;

import com.chengxusheji.mapper.ZhibanMapper;
@Service
public class ZhibanService {

	@Resource ZhibanMapper zhibanMapper;
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

    /*添加值班记录*/
    public void addZhiban(Zhiban zhiban) throws Exception {
    	zhibanMapper.addZhiban(zhiban);
    }

    /*按照查询条件分页查询值班记录*/
    public ArrayList<Zhiban> queryZhiban(Worker workerObj,String zhibanDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null  && !workerObj.getWorkUserName().equals(""))  where += " and t_zhiban.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhibanDate.equals("")) where = where + " and t_zhiban.zhibanDate like '%" + zhibanDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return zhibanMapper.queryZhiban(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Zhiban> queryZhiban(Worker workerObj,String zhibanDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null && !workerObj.getWorkUserName().equals(""))  where += " and t_zhiban.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhibanDate.equals("")) where = where + " and t_zhiban.zhibanDate like '%" + zhibanDate + "%'";
    	return zhibanMapper.queryZhibanList(where);
    }

    /*查询所有值班记录*/
    public ArrayList<Zhiban> queryAllZhiban()  throws Exception {
        return zhibanMapper.queryZhibanList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Worker workerObj,String zhibanDate) throws Exception {
     	String where = "where 1=1";
    	if(null != workerObj &&  workerObj.getWorkUserName() != null && !workerObj.getWorkUserName().equals(""))  where += " and t_zhiban.workerObj='" + workerObj.getWorkUserName() + "'";
    	if(!zhibanDate.equals("")) where = where + " and t_zhiban.zhibanDate like '%" + zhibanDate + "%'";
        recordNumber = zhibanMapper.queryZhibanCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取值班记录*/
    public Zhiban getZhiban(int zhibanId) throws Exception  {
        Zhiban zhiban = zhibanMapper.getZhiban(zhibanId);
        return zhiban;
    }

    /*更新值班记录*/
    public void updateZhiban(Zhiban zhiban) throws Exception {
        zhibanMapper.updateZhiban(zhiban);
    }

    /*删除一条值班记录*/
    public void deleteZhiban (int zhibanId) throws Exception {
        zhibanMapper.deleteZhiban(zhibanId);
    }

    /*删除多条值班信息*/
    public int deleteZhibans (String zhibanIds) throws Exception {
    	String _zhibanIds[] = zhibanIds.split(",");
    	for(String _zhibanId: _zhibanIds) {
    		zhibanMapper.deleteZhiban(Integer.parseInt(_zhibanId));
    	}
    	return _zhibanIds.length;
    }
}
