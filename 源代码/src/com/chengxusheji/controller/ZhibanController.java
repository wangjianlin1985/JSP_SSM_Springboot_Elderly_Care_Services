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
import com.chengxusheji.service.ZhibanService;
import com.chengxusheji.po.Zhiban;
import com.chengxusheji.service.WorkerService;
import com.chengxusheji.po.Worker;

//Zhiban管理控制层
@Controller
@RequestMapping("/Zhiban")
public class ZhibanController extends BaseController {

    /*业务层对象*/
    @Resource ZhibanService zhibanService;

    @Resource WorkerService workerService;
	@InitBinder("workerObj")
	public void initBinderworkerObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("workerObj.");
	}
	@InitBinder("zhiban")
	public void initBinderZhiban(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("zhiban.");
	}
	/*跳转到添加Zhiban视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Zhiban());
		/*查询所有的Worker信息*/
		List<Worker> workerList = workerService.queryAllWorker();
		request.setAttribute("workerList", workerList);
		return "Zhiban_add";
	}

	/*客户端ajax方式提交添加值班信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Zhiban zhiban, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        zhibanService.addZhiban(zhiban);
        message = "值班添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询值班信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(@ModelAttribute("workerObj") Worker workerObj,String zhibanDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (zhibanDate == null) zhibanDate = "";
		if(rows != 0)zhibanService.setRows(rows);
		List<Zhiban> zhibanList = zhibanService.queryZhiban(workerObj, zhibanDate, page);
	    /*计算总的页数和总的记录数*/
	    zhibanService.queryTotalPageAndRecordNumber(workerObj, zhibanDate);
	    /*获取到总的页码数目*/
	    int totalPage = zhibanService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zhibanService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Zhiban zhiban:zhibanList) {
			JSONObject jsonZhiban = zhiban.getJsonObject();
			jsonArray.put(jsonZhiban);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询值班信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Zhiban> zhibanList = zhibanService.queryAllZhiban();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Zhiban zhiban:zhibanList) {
			JSONObject jsonZhiban = new JSONObject();
			jsonZhiban.accumulate("zhibanId", zhiban.getZhibanId());
			jsonArray.put(jsonZhiban);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询值班信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(@ModelAttribute("workerObj") Worker workerObj,String zhibanDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (zhibanDate == null) zhibanDate = "";
		List<Zhiban> zhibanList = zhibanService.queryZhiban(workerObj, zhibanDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    zhibanService.queryTotalPageAndRecordNumber(workerObj, zhibanDate);
	    /*获取到总的页码数目*/
	    int totalPage = zhibanService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = zhibanService.getRecordNumber();
	    request.setAttribute("zhibanList",  zhibanList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("workerObj", workerObj);
	    request.setAttribute("zhibanDate", zhibanDate);
	    List<Worker> workerList = workerService.queryAllWorker();
	    request.setAttribute("workerList", workerList);
		return "Zhiban/zhiban_frontquery_result"; 
	}

     /*前台查询Zhiban信息*/
	@RequestMapping(value="/{zhibanId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer zhibanId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键zhibanId获取Zhiban对象*/
        Zhiban zhiban = zhibanService.getZhiban(zhibanId);

        List<Worker> workerList = workerService.queryAllWorker();
        request.setAttribute("workerList", workerList);
        request.setAttribute("zhiban",  zhiban);
        return "Zhiban/zhiban_frontshow";
	}

	/*ajax方式显示值班修改jsp视图页*/
	@RequestMapping(value="/{zhibanId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer zhibanId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键zhibanId获取Zhiban对象*/
        Zhiban zhiban = zhibanService.getZhiban(zhibanId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonZhiban = zhiban.getJsonObject();
		out.println(jsonZhiban.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新值班信息*/
	@RequestMapping(value = "/{zhibanId}/update", method = RequestMethod.POST)
	public void update(@Validated Zhiban zhiban, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			zhibanService.updateZhiban(zhiban);
			message = "值班更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "值班更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除值班信息*/
	@RequestMapping(value="/{zhibanId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer zhibanId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  zhibanService.deleteZhiban(zhibanId);
	            request.setAttribute("message", "值班删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "值班删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条值班记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String zhibanIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = zhibanService.deleteZhibans(zhibanIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出值班信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(@ModelAttribute("workerObj") Worker workerObj,String zhibanDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(zhibanDate == null) zhibanDate = "";
        List<Zhiban> zhibanList = zhibanService.queryZhiban(workerObj,zhibanDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String _title = "Zhiban信息记录"; 
        String[] headers = { "值班id","值班人","值班日期","值班时间","值班备注"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<zhibanList.size();i++) {
        	Zhiban zhiban = zhibanList.get(i); 
        	dataset.add(new String[]{zhiban.getZhibanId() + "",zhiban.getWorkerObj().getName(),zhiban.getZhibanDate(),zhiban.getZhibanTime(),zhiban.getZhibanMemo()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Zhiban.xls");//filename是下载的xls的名，建议最好用英文 
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
