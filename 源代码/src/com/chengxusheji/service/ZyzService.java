package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Zyz;

import com.chengxusheji.mapper.ZyzMapper;
@Service
public class ZyzService {

	@Resource ZyzMapper zyzMapper;
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

    /*添加志愿者记录*/
    public void addZyz(Zyz zyz) throws Exception {
    	zyzMapper.addZyz(zyz);
    }

    /*按照查询条件分页查询志愿者记录*/
    public ArrayList<Zyz> queryZyz(String name,String birthDate,String telephone,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_zyz.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_zyz.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_zyz.telephone like '%" + telephone + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return zyzMapper.queryZyz(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Zyz> queryZyz(String name,String birthDate,String telephone) throws Exception  { 
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_zyz.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_zyz.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_zyz.telephone like '%" + telephone + "%'";
    	return zyzMapper.queryZyzList(where);
    }

    /*查询所有志愿者记录*/
    public ArrayList<Zyz> queryAllZyz()  throws Exception {
        return zyzMapper.queryZyzList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String name,String birthDate,String telephone) throws Exception {
     	String where = "where 1=1";
    	if(!name.equals("")) where = where + " and t_zyz.name like '%" + name + "%'";
    	if(!birthDate.equals("")) where = where + " and t_zyz.birthDate like '%" + birthDate + "%'";
    	if(!telephone.equals("")) where = where + " and t_zyz.telephone like '%" + telephone + "%'";
        recordNumber = zyzMapper.queryZyzCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取志愿者记录*/
    public Zyz getZyz(int zyzId) throws Exception  {
        Zyz zyz = zyzMapper.getZyz(zyzId);
        return zyz;
    }

    /*更新志愿者记录*/
    public void updateZyz(Zyz zyz) throws Exception {
        zyzMapper.updateZyz(zyz);
    }

    /*删除一条志愿者记录*/
    public void deleteZyz (int zyzId) throws Exception {
        zyzMapper.deleteZyz(zyzId);
    }

    /*删除多条志愿者信息*/
    public int deleteZyzs (String zyzIds) throws Exception {
    	String _zyzIds[] = zyzIds.split(",");
    	for(String _zyzId: _zyzIds) {
    		zyzMapper.deleteZyz(Integer.parseInt(_zyzId));
    	}
    	return _zyzIds.length;
    }
}
