<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Worker" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Worker> workerList = (List<Worker>)request.getAttribute("workerList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String workUserName = (String)request.getAttribute("workUserName"); //工作者用户名查询关键字
    String name = (String)request.getAttribute("name"); //姓名查询关键字
    String birthDate = (String)request.getAttribute("birthDate"); //出生日期查询关键字
    String telephone = (String)request.getAttribute("telephone"); //联系电话查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>医疗工作者查询</title>
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
			    	<li role="presentation" class="active"><a href="#workerListPanel" aria-controls="workerListPanel" role="tab" data-toggle="tab">医疗工作者列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Worker/worker_frontAdd.jsp" style="display:none;">添加医疗工作者</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="workerListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>工作者用户名</td><td>姓名</td><td>性别</td><td>出生日期</td><td>资质信息</td><td>联系电话</td><td>联系邮箱</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<workerList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Worker worker = workerList.get(i); //获取到医疗工作者对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=worker.getWorkUserName() %></td>
 											<td><%=worker.getName() %></td>
 											<td><%=worker.getGender() %></td>
 											<td><%=worker.getBirthDate() %></td>
 											<td><%=worker.getZzxx() %></td>
 											<td><%=worker.getTelephone() %></td>
 											<td><%=worker.getEmail() %></td>
 											<td>
 												<a href="<%=basePath  %>Worker/<%=worker.getWorkUserName() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="workerEdit('<%=worker.getWorkUserName() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="workerDelete('<%=worker.getWorkUserName() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>医疗工作者查询</h1>
		</div>
		<form name="workerQueryForm" id="workerQueryForm" action="<%=basePath %>Worker/frontlist" class="mar_t15" method="post">
			<div class="form-group">
				<label for="workUserName">工作者用户名:</label>
				<input type="text" id="workUserName" name="workUserName" value="<%=workUserName %>" class="form-control" placeholder="请输入工作者用户名">
			</div>






			<div class="form-group">
				<label for="name">姓名:</label>
				<input type="text" id="name" name="name" value="<%=name %>" class="form-control" placeholder="请输入姓名">
			</div>






			<div class="form-group">
				<label for="birthDate">出生日期:</label>
				<input type="text" id="birthDate" name="birthDate" class="form-control"  placeholder="请选择出生日期" value="<%=birthDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="telephone">联系电话:</label>
				<input type="text" id="telephone" name="telephone" value="<%=telephone %>" class="form-control" placeholder="请输入联系电话">
			</div>






            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="workerEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;医疗工作者信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="workerEditForm" id="workerEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="worker_workUserName_edit" class="col-md-3 text-right">工作者用户名:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="worker_workUserName_edit" name="worker.workUserName" class="form-control" placeholder="请输入工作者用户名" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="worker_password_edit" class="col-md-3 text-right">登录密码:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_password_edit" name="worker.password" class="form-control" placeholder="请输入登录密码">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_name_edit" class="col-md-3 text-right">姓名:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_name_edit" name="worker.name" class="form-control" placeholder="请输入姓名">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_gender_edit" class="col-md-3 text-right">性别:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_gender_edit" name="worker.gender" class="form-control" placeholder="请输入性别">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_birthDate_edit" class="col-md-3 text-right">出生日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date worker_birthDate_edit col-md-12" data-link-field="worker_birthDate_edit"  data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="worker_birthDate_edit" name="worker.birthDate" size="16" type="text" value="" placeholder="请选择出生日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_zzxx_edit" class="col-md-3 text-right">资质信息:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_zzxx_edit" name="worker.zzxx" class="form-control" placeholder="请输入资质信息">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_telephone_edit" class="col-md-3 text-right">联系电话:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_telephone_edit" name="worker.telephone" class="form-control" placeholder="请输入联系电话">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_email_edit" class="col-md-3 text-right">联系邮箱:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_email_edit" name="worker.email" class="form-control" placeholder="请输入联系邮箱">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_address_edit" class="col-md-3 text-right">家庭地址:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="worker_address_edit" name="worker.address" class="form-control" placeholder="请输入家庭地址">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="worker_workerDesc_edit" class="col-md-3 text-right">医疗者简介:</label>
		  	 <div class="col-md-9">
			 	<textarea name="worker.workerDesc" id="worker_workerDesc_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#workerEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxWorkerModify();">提交</button>
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
var worker_workerDesc_edit = UE.getEditor('worker_workerDesc_edit'); //医疗者简介编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.workerQueryForm.currentPage.value = currentPage;
    document.workerQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.workerQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.workerQueryForm.currentPage.value = pageValue;
    documentworkerQueryForm.submit();
}

/*弹出修改医疗工作者界面并初始化数据*/
function workerEdit(workUserName) {
	$.ajax({
		url :  basePath + "Worker/" + workUserName + "/update",
		type : "get",
		dataType: "json",
		success : function (worker, response, status) {
			if (worker) {
				$("#worker_workUserName_edit").val(worker.workUserName);
				$("#worker_password_edit").val(worker.password);
				$("#worker_name_edit").val(worker.name);
				$("#worker_gender_edit").val(worker.gender);
				$("#worker_birthDate_edit").val(worker.birthDate);
				$("#worker_zzxx_edit").val(worker.zzxx);
				$("#worker_telephone_edit").val(worker.telephone);
				$("#worker_email_edit").val(worker.email);
				$("#worker_address_edit").val(worker.address);
				worker_workerDesc_edit.setContent(worker.workerDesc, false);
				$('#workerEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除医疗工作者信息*/
function workerDelete(workUserName) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Worker/deletes",
			data : {
				workUserNames : workUserName,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#workerQueryForm").submit();
					//location.href= basePath + "Worker/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交医疗工作者信息表单给服务器端修改*/
function ajaxWorkerModify() {
	$.ajax({
		url :  basePath + "Worker/" + $("#worker_workUserName_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#workerEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#workerQueryForm").submit();
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

    /*出生日期组件*/
    $('.worker_birthDate_edit').datetimepicker({
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

