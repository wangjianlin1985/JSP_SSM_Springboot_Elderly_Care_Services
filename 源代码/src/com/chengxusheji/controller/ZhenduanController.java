package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.ZhenduanService;
import com.chengxusheji.po.Zhenduan;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;
import com.chengxusheji.service.WorkerService;
import com.chengxusheji.po.Worker;

//Zhenduan管理控制层
@Controller
@RequestMapping("/Zhenduan")
public class ZhenduanController extends BaseController {

    /*业务层对象*/
    @Resource ZhenduanService zhenduanService;

    @Resource UserInfoService userInfoService;
    @Resource WorkerService workerService;
	@InitBinder("workerObj")
	public void initBinderworkerObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("workerObj.");
	}
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("zhenduan")
	public void initBinderZhenduan(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("zhenduan.");
	}
	/*跳转到添加Zhenduan视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Zhenduan());
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		/*查询所有的Worker信息*/
		List<Worker> workerList = workerService.queryAllWorker();
		request.setAttribute("workerList", workerList);
		return "Zhenduan_add";
	}

	/*客户端ajax方式提交添加上门诊断信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Zhenduan zhenduan, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        zhenduanService.addZhenduan(zhenduan);
        message = "上门诊断添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	/*客户端ajax方式提交添加上门诊断信息*/
	@RequestMapping(value = "/workerAdd", method = RequestMethod.POST)
	public void workderAdd(@Validated Zhenduan zhenduan, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		
		Worker workerObj = new Worker();
		workerObj.setWorkUserName(session.getAttribute("worker").toString());
		
		zhenduan.setWorkerObj(workerObj);
		
        zhenduanService.addZhenduan(zhenduan);
        message = "上门诊断添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	
	/*ajax方式按照查询条件分页查询上门诊断信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("workerObj") Worker workerObj,String zhenduanDate,@ModelAttribute("userObj") UserInfo userObj,String zhenduanState,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (zhenduanDate == null) zhenduanDate = "";
		if (zhenduanState == null) zhenduanState = "";
		if(rows != 0)zhenduanService.setRows(rows);
		List<Zhenduan> zhenduanList = zhenduanService.queryZhenduan(workerObj, zhenduanDate, userObj, zhenduanState, page);
	    /*计算总的页数和总的记录数*/
	    zhenduanService.queryTotalPageAndRecordNumber(workerObj, zhenduanDate, userObj, zhenduanState);
	    /*获取到总的页码数目*/
	    int totalPage = zhenduanService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zhenduanService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Zhenduan zhenduan:zhenduanList) {
			JSONObject jsonZhenduan = zhenduan.getJsonObject();
			jsonArray.put(jsonZhenduan);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询上门诊断信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Zhenduan> zhenduanList = zhenduanService.queryAllZhenduan();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Zhenduan zhenduan:zhenduanList) {
			JSONObject jsonZhenduan = new JSONObject();
			jsonZhenduan.accumulate("zhenduanId", zhenduan.getZhenduanId());
			jsonArray.put(jsonZhenduan);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询上门诊断信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("workerObj") Worker workerObj,String zhenduanDate,@ModelAttribute("userObj") UserInfo userObj,String zhenduanState,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (zhenduanDate == null) zhenduanDate = "";
		if (zhenduanState == null) zhenduanState = "";
		List<Zhenduan> zhenduanList = zhenduanService.queryZhenduan(workerObj, zhenduanDate, userObj, zhenduanState, currentPage);
	    /*计算总的页数和总的记录数*/
	    zhenduanService.queryTotalPageAndRecordNumber(workerObj, zhenduanDate, userObj, zhenduanState);
	    /*获取到总的页码数目*/
	    int totalPage = zhenduanService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zhenduanService.getRecordNumber();
	    request.setAttribute("zhenduanList",  zhenduanList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("workerObj", workerObj);
	    request.setAttribute("zhenduanDate", zhenduanDate);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("zhenduanState", zhenduanState);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
	    List<Worker> workerList = workerService.queryAllWorker();
	    request.setAttribute("workerList", workerList);
		return "Zhenduan/zhenduan_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询上门诊断信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("workerObj") Worker workerObj,String zhenduanDate,@ModelAttribute("userObj") UserInfo userObj,String zhenduanState,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (zhenduanDate == null) zhenduanDate = "";
		if (zhenduanState == null) zhenduanState = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<Zhenduan> zhenduanList = zhenduanService.queryZhenduan(workerObj, zhenduanDate, userObj, zhenduanState, currentPage);
	    /*计算总的页数和总的记录数*/
	    zhenduanService.queryTotalPageAndRecordNumber(workerObj, zhenduanDate, userObj, zhenduanState);
	    /*获取到总的页码数目*/
	    int totalPage = zhenduanService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zhenduanService.getRecordNumber();
	    request.setAttribute("zhenduanList",  zhenduanList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("workerObj", workerObj);
	    request.setAttribute("zhenduanDate", zhenduanDate);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("zhenduanState", zhenduanState);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
	    List<Worker> workerList = workerService.queryAllWorker();
	    request.setAttribute("workerList", workerList);
		return "Zhenduan/zhenduan_userFrontquery_result"; 
	}
	
	

     /*前台查询Zhenduan信息*/
	@RequestMapping(value="/{zhenduanId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer zhenduanId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键zhenduanId获取Zhenduan对象*/
        Zhenduan zhenduan = zhenduanService.getZhenduan(zhenduanId);

        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        List<Worker> workerList = workerService.queryAllWorker();
        request.setAttribute("workerList", workerList);
        request.setAttribute("zhenduan",  zhenduan);
        return "Zhenduan/zhenduan_frontshow";
	}

	/*ajax方式显示上门诊断修改jsp视图页*/
	@RequestMapping(value="/{zhenduanId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer zhenduanId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键zhenduanId获取Zhenduan对象*/
        Zhenduan zhenduan = zhenduanService.getZhenduan(zhenduanId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonZhenduan = zhenduan.getJsonObject();
		out.println(jsonZhenduan.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新上门诊断信息*/
	@RequestMapping(value = "/{zhenduanId}/update", method = RequestMethod.POST)
	public void update(@Validated Zhenduan zhenduan, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			zhenduanService.updateZhenduan(zhenduan);
			message = "上门诊断更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "上门诊断更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除上门诊断信息*/
	@RequestMapping(value="/{zhenduanId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer zhenduanId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  zhenduanService.deleteZhenduan(zhenduanId);
	            request.setAttribute("message", "上门诊断删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "上门诊断删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条上门诊断记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String zhenduanIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = zhenduanService.deleteZhenduans(zhenduanIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出上门诊断信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("workerObj") Worker workerObj,String zhenduanDate,@ModelAttribute("userObj") UserInfo userObj,String zhenduanState, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(zhenduanDate == null) zhenduanDate = "";
        if(zhenduanState == null) zhenduanState = "";
        List<Zhenduan> zhenduanList = zhenduanService.queryZhenduan(workerObj,zhenduanDate,userObj,zhenduanState);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Zhenduan信息记录"; 
        String[] headers = { "诊断id","医疗者","诊断日期","上门时间","诊断老人","诊断状态"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<zhenduanList.size();i++) {
        	Zhenduan zhenduan = zhenduanList.get(i); 
        	dataset.add(new String[]{zhenduan.getZhenduanId() + "",zhenduan.getWorkerObj().getName(),zhenduan.getZhenduanDate(),zhenduan.getZhenduanTime(),zhenduan.getUserObj().getName(),zhenduan.getZhenduanState()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Zhenduan.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,_title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
