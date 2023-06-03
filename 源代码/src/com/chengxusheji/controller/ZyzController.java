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
import com.chengxusheji.service.ZyzService;
import com.chengxusheji.po.Zyz;

//Zyz管理控制层
@Controller
@RequestMapping("/Zyz")
public class ZyzController extends BaseController {

    /*业务层对象*/
    @Resource ZyzService zyzService;

	@InitBinder("zyz")
	public void initBinderZyz(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("zyz.");
	}
	/*跳转到添加Zyz视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Zyz());
		return "Zyz_add";
	}

	/*客户端ajax方式提交添加志愿者信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Zyz zyz, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			zyz.setUserPhoto(this.handlePhotoUpload(request, "userPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        zyzService.addZyz(zyz);
        message = "志愿者添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询志愿者信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String name,String birthDate,String telephone,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		if(rows != 0)zyzService.setRows(rows);
		List<Zyz> zyzList = zyzService.queryZyz(name, birthDate, telephone, page);
	    /*计算总的页数和总的记录数*/
	    zyzService.queryTotalPageAndRecordNumber(name, birthDate, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = zyzService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zyzService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Zyz zyz:zyzList) {
			JSONObject jsonZyz = zyz.getJsonObject();
			jsonArray.put(jsonZyz);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询志愿者信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Zyz> zyzList = zyzService.queryAllZyz();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Zyz zyz:zyzList) {
			JSONObject jsonZyz = new JSONObject();
			jsonZyz.accumulate("zyzId", zyz.getZyzId());
			jsonZyz.accumulate("name", zyz.getName());
			jsonArray.put(jsonZyz);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询志愿者信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String name,String birthDate,String telephone,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		List<Zyz> zyzList = zyzService.queryZyz(name, birthDate, telephone, currentPage);
	    /*计算总的页数和总的记录数*/
	    zyzService.queryTotalPageAndRecordNumber(name, birthDate, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = zyzService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zyzService.getRecordNumber();
	    request.setAttribute("zyzList",  zyzList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("name", name);
	    request.setAttribute("birthDate", birthDate);
	    request.setAttribute("telephone", telephone);
		return "Zyz/zyz_frontquery_result"; 
	}

     /*前台查询Zyz信息*/
	@RequestMapping(value="/{zyzId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer zyzId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键zyzId获取Zyz对象*/
        Zyz zyz = zyzService.getZyz(zyzId);

        request.setAttribute("zyz",  zyz);
        return "Zyz/zyz_frontshow";
	}

	/*ajax方式显示志愿者修改jsp视图页*/
	@RequestMapping(value="/{zyzId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer zyzId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键zyzId获取Zyz对象*/
        Zyz zyz = zyzService.getZyz(zyzId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonZyz = zyz.getJsonObject();
		out.println(jsonZyz.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新志愿者信息*/
	@RequestMapping(value = "/{zyzId}/update", method = RequestMethod.POST)
	public void update(@Validated Zyz zyz, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String userPhotoFileName = this.handlePhotoUpload(request, "userPhotoFile");
		if(!userPhotoFileName.equals("upload/NoImage.jpg"))zyz.setUserPhoto(userPhotoFileName); 


		try {
			zyzService.updateZyz(zyz);
			message = "志愿者更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "志愿者更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除志愿者信息*/
	@RequestMapping(value="/{zyzId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer zyzId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  zyzService.deleteZyz(zyzId);
	            request.setAttribute("message", "志愿者删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "志愿者删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条志愿者记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String zyzIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = zyzService.deleteZyzs(zyzIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出志愿者信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String name,String birthDate,String telephone, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(name == null) name = "";
        if(birthDate == null) birthDate = "";
        if(telephone == null) telephone = "";
        List<Zyz> zyzList = zyzService.queryZyz(name,birthDate,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Zyz信息记录"; 
        String[] headers = { "志愿者id","姓名","性别","出生日期","志愿者照片","联系电话"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<zyzList.size();i++) {
        	Zyz zyz = zyzList.get(i); 
        	dataset.add(new String[]{zyz.getZyzId() + "",zyz.getName(),zyz.getGender(),zyz.getBirthDate(),zyz.getUserPhoto(),zyz.getTelephone()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Zyz.xls");//filename是下载的xls的名，建议最好用英文 
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
