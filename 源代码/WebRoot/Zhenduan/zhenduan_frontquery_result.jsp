<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Zhenduan" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%@ page import="com.chengxusheji.po.Worker" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Zhenduan> zhenduanList = (List<Zhenduan>)request.getAttribute("zhenduanList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    //获取所有的workerObj信息
    List<Worker> workerList = (List<Worker>)request.getAttribute("workerList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Worker workerObj = (Worker)request.getAttribute("workerObj");
    String zhenduanDate = (String)request.getAttribute("zhenduanDate"); //诊断日期查询关键字
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String zhenduanState = (String)request.getAttribute("zhenduanState"); //诊断状态查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>上门诊断查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="row"> 
		<div class="col-md-9 wow fadeInDown" data-wow-duration="0.5s">
			<div>
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
			    	<li><a href="<%=basePath %>index.jsp">首页</a></li>
			    	<li role="presentation" class="active"><a href="#zhenduanListPanel" aria-controls="zhenduanListPanel" role="tab" data-toggle="tab">上门诊断列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Zhenduan/zhenduan_frontAdd.jsp" style="display:none;">添加上门诊断</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="zhenduanListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>诊断id</td><td>医疗者</td><td>诊断日期</td><td>上门时间</td><td>诊断老人</td><td>诊断状态</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<zhenduanList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Zhenduan zhenduan = zhenduanList.get(i); //获取到上门诊断对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=zhenduan.getZhenduanId() %></td>
 											<td><%=zhenduan.getWorkerObj().getName() %></td>
 											<td><%=zhenduan.getZhenduanDate() %></td>
 											<td><%=zhenduan.getZhenduanTime() %></td>
 											<td><%=zhenduan.getUserObj().getName() %></td>
 											<td><%=zhenduan.getZhenduanState() %></td>
 											<td>
 												<a href="<%=basePath  %>Zhenduan/<%=zhenduan.getZhenduanId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="zhenduanEdit('<%=zhenduan.getZhenduanId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="zhenduanDelete('<%=zhenduan.getZhenduanId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
 											</td> 
 										</tr>
 										<%}%>
				    				</table>
				    				</div>
				    			</div>
				    		</div>

				    		<div class="row">
					            <div class="col-md-12">
						            <nav class="pull-left">
						                <ul class="pagination">
						                    <li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
						                     <%
						                    	int startPage = currentPage - 5;
						                    	int endPage = currentPage + 5;
						                    	if(startPage < 1) startPage=1;
						                    	if(endPage > totalPage) endPage = totalPage;
						                    	for(int i=startPage;i<=endPage;i++) {
						                    %>
						                    <li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
						                    <%  } %> 
						                    <li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						                </ul>
						            </nav>
						            <div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
					            </div>
				            </div> 
				    </div>
				</div>
			</div>
		</div>
	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>上门诊断查询</h1>
		</div>
		<form name="zhenduanQueryForm" id="zhenduanQueryForm" action="<%=basePath %>Zhenduan/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="workerObj_workUserName">医疗者：</label>
                <select id="workerObj_workUserName" name="workerObj.workUserName" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(Worker workerTemp:workerList) {
	 					String selected = "";
 					if(workerObj!=null && workerObj.getWorkUserName()!=null && workerObj.getWorkUserName().equals(workerTemp.getWorkUserName()))
 						selected = "selected";
	 				%>
 				 <option value="<%=workerTemp.getWorkUserName() %>" <%=selected %>><%=workerTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="zhenduanDate">诊断日期:</label>
				<input type="text" id="zhenduanDate" name="zhenduanDate" class="form-control"  placeholder="请选择诊断日期" value="<%=zhenduanDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <div class="form-group">
            	<label for="userObj_user_name">诊断老人：</label>
                <select id="userObj_user_name" name="userObj.user_name" class="form-control">
                	<option value="">不限制</option>
	 				<%
	 				for(UserInfo userInfoTemp:userInfoList) {
	 					String selected = "";
 					if(userObj!=null && userObj.getUser_name()!=null && userObj.getUser_name().equals(userInfoTemp.getUser_name()))
 						selected = "selected";
	 				%>
 				 <option value="<%=userInfoTemp.getUser_name() %>" <%=selected %>><%=userInfoTemp.getName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="zhenduanState">诊断状态:</label>
				<input type="text" id="zhenduanState" name="zhenduanState" value="<%=zhenduanState %>" class="form-control" placeholder="请输入诊断状态">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="zhenduanEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;上门诊断信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="zhenduanEditForm" id="zhenduanEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="zhenduan_zhenduanId_edit" class="col-md-3 text-right">诊断id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="zhenduan_zhenduanId_edit" name="zhenduan.zhenduanId" class="form-control" placeholder="请输入诊断id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="zhenduan_workerObj_workUserName_edit" class="col-md-3 text-right">医疗者:</label>
		  	 <div class="col-md-9">
			    <select id="zhenduan_workerObj_workUserName_edit" name="zhenduan.workerObj.workUserName" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhenduan_zhenduanDate_edit" class="col-md-3 text-right">诊断日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date zhenduan_zhenduanDate_edit col-md-12" data-link-field="zhenduan_zhenduanDate_edit"  data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="zhenduan_zhenduanDate_edit" name="zhenduan.zhenduanDate" size="16" type="text" value="" placeholder="请选择诊断日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhenduan_zhenduanTime_edit" class="col-md-3 text-right">上门时间:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zhenduan_zhenduanTime_edit" name="zhenduan.zhenduanTime" class="form-control" placeholder="请输入上门时间">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhenduan_userObj_user_name_edit" class="col-md-3 text-right">诊断老人:</label>
		  	 <div class="col-md-9">
			    <select id="zhenduan_userObj_user_name_edit" name="zhenduan.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhenduan_zhenduanState_edit" class="col-md-3 text-right">诊断状态:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zhenduan_zhenduanState_edit" name="zhenduan.zhenduanState" class="form-control" placeholder="请输入诊断状态">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhenduan_zhenduanResult_edit" class="col-md-3 text-right">诊断结果:</label>
		  	 <div class="col-md-9">
			 	<textarea name="zhenduan.zhenduanResult" id="zhenduan_zhenduanResult_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#zhenduanEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxZhenduanModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${pageContext.request.contextPath}/ueditor1_4_3/lang/zh-cn/zh-cn.js"></script>
<script>
//实例化编辑器
var zhenduan_zhenduanResult_edit = UE.getEditor('zhenduan_zhenduanResult_edit'); //诊断结果编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.zhenduanQueryForm.currentPage.value = currentPage;
    document.zhenduanQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.zhenduanQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.zhenduanQueryForm.currentPage.value = pageValue;
    documentzhenduanQueryForm.submit();
}

/*弹出修改上门诊断界面并初始化数据*/
function zhenduanEdit(zhenduanId) {
	$.ajax({
		url :  basePath + "Zhenduan/" + zhenduanId + "/update",
		type : "get",
		dataType: "json",
		success : function (zhenduan, response, status) {
			if (zhenduan) {
				$("#zhenduan_zhenduanId_edit").val(zhenduan.zhenduanId);
				$.ajax({
					url: basePath + "Worker/listAll",
					type: "get",
					success: function(workers,response,status) { 
						$("#zhenduan_workerObj_workUserName_edit").empty();
						var html="";
		        		$(workers).each(function(i,worker){
		        			html += "<option value='" + worker.workUserName + "'>" + worker.name + "</option>";
		        		});
		        		$("#zhenduan_workerObj_workUserName_edit").html(html);
		        		$("#zhenduan_workerObj_workUserName_edit").val(zhenduan.workerObjPri);
					}
				});
				$("#zhenduan_zhenduanDate_edit").val(zhenduan.zhenduanDate);
				$("#zhenduan_zhenduanTime_edit").val(zhenduan.zhenduanTime);
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#zhenduan_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#zhenduan_userObj_user_name_edit").html(html);
		        		$("#zhenduan_userObj_user_name_edit").val(zhenduan.userObjPri);
					}
				});
				$("#zhenduan_zhenduanState_edit").val(zhenduan.zhenduanState);
				zhenduan_zhenduanResult_edit.setContent(zhenduan.zhenduanResult, false);
				$('#zhenduanEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除上门诊断信息*/
function zhenduanDelete(zhenduanId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Zhenduan/deletes",
			data : {
				zhenduanIds : zhenduanId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#zhenduanQueryForm").submit();
					//location.href= basePath + "Zhenduan/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交上门诊断信息表单给服务器端修改*/
function ajaxZhenduanModify() {
	$.ajax({
		url :  basePath + "Zhenduan/" + $("#zhenduan_zhenduanId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#zhenduanEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#zhenduanQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*诊断日期组件*/
    $('.zhenduan_zhenduanDate_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

