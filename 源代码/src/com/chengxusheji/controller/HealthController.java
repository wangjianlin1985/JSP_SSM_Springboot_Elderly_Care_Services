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
import com.chengxusheji.service.HealthService;
import com.chengxusheji.po.Health;
import com.chengxusheji.service.UserInfoService;
import com.chengxusheji.po.UserInfo;

//Health管理控制层
@Controller
@RequestMapping("/Health")
public class HealthController extends BaseController {

    /*业务层对象*/
    @Resource HealthService healthService;

    @Resource UserInfoService userInfoService;
	@InitBinder("userObj")
	public void initBinderuserObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("userObj.");
	}
	@InitBinder("health")
	public void initBinderHealth(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("health.");
	}
	/*跳转到添加Health视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Health());
		/*查询所有的UserInfo信息*/
		List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
		request.setAttribute("userInfoList", userInfoList);
		return "Health_add";
	}

	/*客户端ajax方式提交添加健康信息信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Health health, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        healthService.addHealth(health);
        message = "健康信息添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询健康信息信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("userObj") UserInfo userObj,String testDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (testDate == null) testDate = "";
		if(rows != 0)healthService.setRows(rows);
		List<Health> healthList = healthService.queryHealth(userObj, testDate, page);
	    /*计算总的页数和总的记录数*/
	    healthService.queryTotalPageAndRecordNumber(userObj, testDate);
	    /*获取到总的页码数目*/
	    int totalPage = healthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = healthService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Health health:healthList) {
			JSONObject jsonHealth = health.getJsonObject();
			jsonArray.put(jsonHealth);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询健康信息信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Health> healthList = healthService.queryAllHealth();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Health health:healthList) {
			JSONObject jsonHealth = new JSONObject();
			jsonHealth.accumulate("healthId", health.getHealthId());
			jsonArray.put(jsonHealth);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询健康信息信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("userObj") UserInfo userObj,String testDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (testDate == null) testDate = "";
		List<Health> healthList = healthService.queryHealth(userObj, testDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    healthService.queryTotalPageAndRecordNumber(userObj, testDate);
	    /*获取到总的页码数目*/
	    int totalPage = healthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = healthService.getRecordNumber();
	    request.setAttribute("healthList",  healthList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("testDate", testDate);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Health/health_frontquery_result"; 
	}
	
	
	/*前台按照查询条件分页查询健康信息信息*/
	@RequestMapping(value = { "/userFrontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String userFrontlist(@ModelAttribute("userObj") UserInfo userObj,String testDate,Integer currentPage, Model model, HttpServletRequest request,HttpSession session) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (testDate == null) testDate = "";
		userObj = new UserInfo();
		userObj.setUser_name(session.getAttribute("user_name").toString());
		
		List<Health> healthList = healthService.queryHealth(userObj, testDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    healthService.queryTotalPageAndRecordNumber(userObj, testDate);
	    /*获取到总的页码数目*/
	    int totalPage = healthService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = healthService.getRecordNumber();
	    request.setAttribute("healthList",  healthList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("userObj", userObj);
	    request.setAttribute("testDate", testDate);
	    List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
	    request.setAttribute("userInfoList", userInfoList);
		return "Health/health_userFrontquery_result"; 
	}

	
	

     /*前台查询Health信息*/
	@RequestMapping(value="/{healthId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer healthId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键healthId获取Health对象*/
        Health health = healthService.getHealth(healthId);

        List<UserInfo> userInfoList = userInfoService.queryAllUserInfo();
        request.setAttribute("userInfoList", userInfoList);
        request.setAttribute("health",  health);
        return "Health/health_frontshow";
	}

	/*ajax方式显示健康信息修改jsp视图页*/
	@RequestMapping(value="/{healthId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer healthId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键healthId获取Health对象*/
        Health health = healthService.getHealth(healthId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonHealth = health.getJsonObject();
		out.println(jsonHealth.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新健康信息信息*/
	@RequestMapping(value = "/{healthId}/update", method = RequestMethod.POST)
	public void update(@Validated Health health, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			healthService.updateHealth(health);
			message = "健康信息更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "健康信息更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除健康信息信息*/
	@RequestMapping(value="/{healthId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer healthId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  healthService.deleteHealth(healthId);
	            request.setAttribute("message", "健康信息删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "健康信息删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条健康信息记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String healthIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = healthService.deleteHealths(healthIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出健康信息信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("userObj") UserInfo userObj,String testDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(testDate == null) testDate = "";
        List<Health> healthList = healthService.queryHealth(userObj,testDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Health信息记录"; 
        String[] headers = { "健康id","老人","心率","血压","呼吸频率","体温","体重","健康详述","测试日期"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<healthList.size();i++) {
        	Health health = healthList.get(i); 
        	dataset.add(new String[]{health.getHealthId() + "",health.getUserObj().getName(),health.getXinlv(),health.getXueya(),health.getHxpl(),health.getTiwen(),health.getTizhong(),health.getHealthDesc(),health.getTestDate()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Health.xls");//filename是下载的xls的名，建议最好用英文 
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
