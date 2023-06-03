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
import com.chengxusheji.service.WorkerService;
import com.chengxusheji.po.Worker;

//Worker管理控制层
@Controller
@RequestMapping("/Worker")
public class WorkerController extends BaseController {

    /*业务层对象*/
    @Resource WorkerService workerService;

	@InitBinder("worker")
	public void initBinderWorker(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("worker.");
	}
	/*跳转到添加Worker视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Worker());
		return "Worker_add";
	}

	/*客户端ajax方式提交添加医疗工作者信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Worker worker, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(workerService.getWorker(worker.getWorkUserName()) != null) {
			message = "工作者用户名已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
        workerService.addWorker(worker);
        message = "医疗工作者添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	
	
	 
	
	
	/*ajax方式按照查询条件分页查询医疗工作者信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String workUserName,String name,String birthDate,String telephone,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (workUserName == null) workUserName = "";
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		if(rows != 0)workerService.setRows(rows);
		List<Worker> workerList = workerService.queryWorker(workUserName, name, birthDate, telephone, page);
	    /*计算总的页数和总的记录数*/
	    workerService.queryTotalPageAndRecordNumber(workUserName, name, birthDate, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = workerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = workerService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Worker worker:workerList) {
			JSONObject jsonWorker = worker.getJsonObject();
			jsonArray.put(jsonWorker);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询医疗工作者信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Worker> workerList = workerService.queryAllWorker();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Worker worker:workerList) {
			JSONObject jsonWorker = new JSONObject();
			jsonWorker.accumulate("workUserName", worker.getWorkUserName());
			jsonWorker.accumulate("name", worker.getName());
			jsonArray.put(jsonWorker);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询医疗工作者信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String workUserName,String name,String birthDate,String telephone,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (workUserName == null) workUserName = "";
		if (name == null) name = "";
		if (birthDate == null) birthDate = "";
		if (telephone == null) telephone = "";
		List<Worker> workerList = workerService.queryWorker(workUserName, name, birthDate, telephone, currentPage);
	    /*计算总的页数和总的记录数*/
	    workerService.queryTotalPageAndRecordNumber(workUserName, name, birthDate, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = workerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = workerService.getRecordNumber();
	    request.setAttribute("workerList",  workerList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("workUserName", workUserName);
	    request.setAttribute("name", name);
	    request.setAttribute("birthDate", birthDate);
	    request.setAttribute("telephone", telephone);
		return "Worker/worker_frontquery_result"; 
	}

     /*前台查询Worker信息*/
	@RequestMapping(value="/{workUserName}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String workUserName,Model model,HttpServletRequest request) throws Exception {
		/*根据主键workUserName获取Worker对象*/
        Worker worker = workerService.getWorker(workUserName);

        request.setAttribute("worker",  worker);
        return "Worker/worker_frontshow";
	}

	/*ajax方式显示医疗工作者修改jsp视图页*/
	@RequestMapping(value="/{workUserName}/update",method=RequestMethod.GET)
	public void update(@PathVariable String workUserName,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键workUserName获取Worker对象*/
        Worker worker = workerService.getWorker(workUserName);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonWorker = worker.getJsonObject();
		out.println(jsonWorker.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新医疗工作者信息*/
	@RequestMapping(value = "/{workUserName}/update", method = RequestMethod.POST)
	public void update(@Validated Worker worker, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			workerService.updateWorker(worker);
			message = "医疗工作者更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "医疗工作者更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除医疗工作者信息*/
	@RequestMapping(value="/{workUserName}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String workUserName,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  workerService.deleteWorker(workUserName);
	            request.setAttribute("message", "医疗工作者删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "医疗工作者删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条医疗工作者记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String workUserNames,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = workerService.deleteWorkers(workUserNames);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出医疗工作者信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String workUserName,String name,String birthDate,String telephone, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(workUserName == null) workUserName = "";
        if(name == null) name = "";
        if(birthDate == null) birthDate = "";
        if(telephone == null) telephone = "";
        List<Worker> workerList = workerService.queryWorker(workUserName,name,birthDate,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Worker信息记录"; 
        String[] headers = { "工作者用户名","姓名","性别","出生日期","资质信息","联系电话","联系邮箱"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<workerList.size();i++) {
        	Worker worker = workerList.get(i); 
        	dataset.add(new String[]{worker.getWorkUserName(),worker.getName(),worker.getGender(),worker.getBirthDate(),worker.getZzxx(),worker.getTelephone(),worker.getEmail()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Worker.xls");//filename是下载的xls的名，建议最好用英文 
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
