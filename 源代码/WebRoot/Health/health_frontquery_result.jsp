﻿<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Health" %>
<%@ page import="com.chengxusheji.po.UserInfo" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Health> healthList = (List<Health>)request.getAttribute("healthList");
    //获取所有的userObj信息
    List<UserInfo> userInfoList = (List<UserInfo>)request.getAttribute("userInfoList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    UserInfo userObj = (UserInfo)request.getAttribute("userObj");
    String testDate = (String)request.getAttribute("testDate"); //测试日期查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>健康信息查询</title>
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
			    	<li role="presentation" class="active"><a href="#healthListPanel" aria-controls="healthListPanel" role="tab" data-toggle="tab">健康信息列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Health/health_frontAdd.jsp" style="display:none;">添加健康信息</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="healthListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>健康id</td><td>老人</td><td>心率</td><td>血压</td><td>呼吸频率</td><td>体温</td><td>体重</td><td>健康详述</td><td>测试日期</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<healthList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Health health = healthList.get(i); //获取到健康信息对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=health.getHealthId() %></td>
 											<td><%=health.getUserObj().getName() %></td>
 											<td><%=health.getXinlv() %></td>
 											<td><%=health.getXueya() %></td>
 											<td><%=health.getHxpl() %></td>
 											<td><%=health.getTiwen() %></td>
 											<td><%=health.getTizhong() %></td>
 											<td><%=health.getHealthDesc() %></td>
 											<td><%=health.getTestDate() %></td>
 											<td>
 												<a href="<%=basePath  %>Health/<%=health.getHealthId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="healthEdit('<%=health.getHealthId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="healthDelete('<%=health.getHealthId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>健康信息查询</h1>
		</div>
		<form name="healthQueryForm" id="healthQueryForm" action="<%=basePath %>Health/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="userObj_user_name">老人：</label>
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
				<label for="testDate">测试日期:</label>
				<input type="text" id="testDate" name="testDate" class="form-control"  placeholder="请选择测试日期" value="<%=testDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="healthEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;健康信息信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="healthEditForm" id="healthEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="health_healthId_edit" class="col-md-3 text-right">健康id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="health_healthId_edit" name="health.healthId" class="form-control" placeholder="请输入健康id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="health_userObj_user_name_edit" class="col-md-3 text-right">老人:</label>
		  	 <div class="col-md-9">
			    <select id="health_userObj_user_name_edit" name="health.userObj.user_name" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_xinlv_edit" class="col-md-3 text-right">心率:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="health_xinlv_edit" name="health.xinlv" class="form-control" placeholder="请输入心率">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_xueya_edit" class="col-md-3 text-right">血压:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="health_xueya_edit" name="health.xueya" class="form-control" placeholder="请输入血压">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_hxpl_edit" class="col-md-3 text-right">呼吸频率:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="health_hxpl_edit" name="health.hxpl" class="form-control" placeholder="请输入呼吸频率">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_tiwen_edit" class="col-md-3 text-right">体温:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="health_tiwen_edit" name="health.tiwen" class="form-control" placeholder="请输入体温">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_tizhong_edit" class="col-md-3 text-right">体重:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="health_tizhong_edit" name="health.tizhong" class="form-control" placeholder="请输入体重">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_healthDesc_edit" class="col-md-3 text-right">健康详述:</label>
		  	 <div class="col-md-9">
			 	<textarea name="health.healthDesc" id="health_healthDesc_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="health_testDate_edit" class="col-md-3 text-right">测试日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date health_testDate_edit col-md-12" data-link-field="health_testDate_edit"  data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="health_testDate_edit" name="health.testDate" size="16" type="text" value="" placeholder="请选择测试日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		</form> 
	    <style>#healthEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxHealthModify();">提交</button>
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
var health_healthDesc_edit = UE.getEditor('health_healthDesc_edit'); //健康详述编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.healthQueryForm.currentPage.value = currentPage;
    document.healthQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.healthQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.healthQueryForm.currentPage.value = pageValue;
    documenthealthQueryForm.submit();
}

/*弹出修改健康信息界面并初始化数据*/
function healthEdit(healthId) {
	$.ajax({
		url :  basePath + "Health/" + healthId + "/update",
		type : "get",
		dataType: "json",
		success : function (health, response, status) {
			if (health) {
				$("#health_healthId_edit").val(health.healthId);
				$.ajax({
					url: basePath + "UserInfo/listAll",
					type: "get",
					success: function(userInfos,response,status) { 
						$("#health_userObj_user_name_edit").empty();
						var html="";
		        		$(userInfos).each(function(i,userInfo){
		        			html += "<option value='" + userInfo.user_name + "'>" + userInfo.name + "</option>";
		        		});
		        		$("#health_userObj_user_name_edit").html(html);
		        		$("#health_userObj_user_name_edit").val(health.userObjPri);
					}
				});
				$("#health_xinlv_edit").val(health.xinlv);
				$("#health_xueya_edit").val(health.xueya);
				$("#health_hxpl_edit").val(health.hxpl);
				$("#health_tiwen_edit").val(health.tiwen);
				$("#health_tizhong_edit").val(health.tizhong);
				health_healthDesc_edit.setContent(health.healthDesc, false);
				$("#health_testDate_edit").val(health.testDate);
				$('#healthEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除健康信息信息*/
function healthDelete(healthId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Health/deletes",
			data : {
				healthIds : healthId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#healthQueryForm").submit();
					//location.href= basePath + "Health/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交健康信息信息表单给服务器端修改*/
function ajaxHealthModify() {
	$.ajax({
		url :  basePath + "Health/" + $("#health_healthId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#healthEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#healthQueryForm").submit();
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

    /*测试日期组件*/
    $('.health_testDate_edit').datetimepicker({
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

