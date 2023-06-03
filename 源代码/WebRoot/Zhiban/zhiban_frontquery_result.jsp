<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Zhiban" %>
<%@ page import="com.chengxusheji.po.Worker" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Zhiban> zhibanList = (List<Zhiban>)request.getAttribute("zhibanList");
    //获取所有的workerObj信息
    List<Worker> workerList = (List<Worker>)request.getAttribute("workerList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    Worker workerObj = (Worker)request.getAttribute("workerObj");
    String zhibanDate = (String)request.getAttribute("zhibanDate"); //值班日期查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>值班查询</title>
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
			    	<li role="presentation" class="active"><a href="#zhibanListPanel" aria-controls="zhibanListPanel" role="tab" data-toggle="tab">值班列表</a></li>
			    	<li role="presentation" ><a href="<%=basePath %>Zhiban/zhiban_frontAdd.jsp" style="display:none;">添加值班</a></li>
				</ul>
			  	<!-- Tab panes -->
			  	<div class="tab-content">
				    <div role="tabpanel" class="tab-pane active" id="zhibanListPanel">
				    		<div class="row">
				    			<div class="col-md-12 top5">
				    				<div class="table-responsive">
				    				<table class="table table-condensed table-hover">
				    					<tr class="success bold"><td>序号</td><td>值班id</td><td>值班人</td><td>值班日期</td><td>值班时间</td><td>值班备注</td><td>操作</td></tr>
				    					<% 
				    						/*计算起始序号*/
				    	            		int startIndex = (currentPage -1) * 5;
				    	            		/*遍历记录*/
				    	            		for(int i=0;i<zhibanList.size();i++) {
					    	            		int currentIndex = startIndex + i + 1; //当前记录的序号
					    	            		Zhiban zhiban = zhibanList.get(i); //获取到值班对象
 										%>
 										<tr>
 											<td><%=currentIndex %></td>
 											<td><%=zhiban.getZhibanId() %></td>
 											<td><%=zhiban.getWorkerObj().getName() %></td>
 											<td><%=zhiban.getZhibanDate() %></td>
 											<td><%=zhiban.getZhibanTime() %></td>
 											<td><%=zhiban.getZhibanMemo() %></td>
 											<td>
 												<a href="<%=basePath  %>Zhiban/<%=zhiban.getZhibanId() %>/frontshow"><i class="fa fa-info"></i>&nbsp;查看</a>&nbsp;
 												<a href="#" onclick="zhibanEdit('<%=zhiban.getZhibanId() %>');" style="display:none;"><i class="fa fa-pencil fa-fw"></i>编辑</a>&nbsp;
 												<a href="#" onclick="zhibanDelete('<%=zhiban.getZhibanId() %>');" style="display:none;"><i class="fa fa-trash-o fa-fw"></i>删除</a>
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
    		<h1>值班查询</h1>
		</div>
		<form name="zhibanQueryForm" id="zhibanQueryForm" action="<%=basePath %>Zhiban/frontlist" class="mar_t15" method="post">
            <div class="form-group">
            	<label for="workerObj_workUserName">值班人：</label>
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
				<label for="zhibanDate">值班日期:</label>
				<input type="text" id="zhibanDate" name="zhibanDate" class="form-control"  placeholder="请选择值班日期" value="<%=zhibanDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
	</div> 
<div id="zhibanEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;值班信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="zhibanEditForm" id="zhibanEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="zhiban_zhibanId_edit" class="col-md-3 text-right">值班id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="zhiban_zhibanId_edit" name="zhiban.zhibanId" class="form-control" placeholder="请输入值班id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="zhiban_workerObj_workUserName_edit" class="col-md-3 text-right">值班人:</label>
		  	 <div class="col-md-9">
			    <select id="zhiban_workerObj_workUserName_edit" name="zhiban.workerObj.workUserName" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhiban_zhibanDate_edit" class="col-md-3 text-right">值班日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date zhiban_zhibanDate_edit col-md-12" data-link-field="zhiban_zhibanDate_edit"  data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="zhiban_zhibanDate_edit" name="zhiban.zhibanDate" size="16" type="text" value="" placeholder="请选择值班日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhiban_zhibanTime_edit" class="col-md-3 text-right">值班时间:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zhiban_zhibanTime_edit" name="zhiban.zhibanTime" class="form-control" placeholder="请输入值班时间">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zhiban_zhibanMemo_edit" class="col-md-3 text-right">值班备注:</label>
		  	 <div class="col-md-9">
			    <textarea id="zhiban_zhibanMemo_edit" name="zhiban.zhibanMemo" rows="8" class="form-control" placeholder="请输入值班备注"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#zhibanEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxZhibanModify();">提交</button>
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
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.zhibanQueryForm.currentPage.value = currentPage;
    document.zhibanQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.zhibanQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.zhibanQueryForm.currentPage.value = pageValue;
    documentzhibanQueryForm.submit();
}

/*弹出修改值班界面并初始化数据*/
function zhibanEdit(zhibanId) {
	$.ajax({
		url :  basePath + "Zhiban/" + zhibanId + "/update",
		type : "get",
		dataType: "json",
		success : function (zhiban, response, status) {
			if (zhiban) {
				$("#zhiban_zhibanId_edit").val(zhiban.zhibanId);
				$.ajax({
					url: basePath + "Worker/listAll",
					type: "get",
					success: function(workers,response,status) { 
						$("#zhiban_workerObj_workUserName_edit").empty();
						var html="";
		        		$(workers).each(function(i,worker){
		        			html += "<option value='" + worker.workUserName + "'>" + worker.name + "</option>";
		        		});
		        		$("#zhiban_workerObj_workUserName_edit").html(html);
		        		$("#zhiban_workerObj_workUserName_edit").val(zhiban.workerObjPri);
					}
				});
				$("#zhiban_zhibanDate_edit").val(zhiban.zhibanDate);
				$("#zhiban_zhibanTime_edit").val(zhiban.zhibanTime);
				$("#zhiban_zhibanMemo_edit").val(zhiban.zhibanMemo);
				$('#zhibanEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除值班信息*/
function zhibanDelete(zhibanId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Zhiban/deletes",
			data : {
				zhibanIds : zhibanId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#zhibanQueryForm").submit();
					//location.href= basePath + "Zhiban/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交值班信息表单给服务器端修改*/
function ajaxZhibanModify() {
	$.ajax({
		url :  basePath + "Zhiban/" + $("#zhiban_zhibanId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#zhibanEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#zhibanQueryForm").submit();
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

    /*值班日期组件*/
    $('.zhiban_zhibanDate_edit').datetimepicker({
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

