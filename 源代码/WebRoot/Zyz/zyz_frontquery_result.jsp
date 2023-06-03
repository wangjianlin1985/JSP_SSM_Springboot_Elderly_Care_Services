<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Zyz" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Zyz> zyzList = (List<Zyz>)request.getAttribute("zyzList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
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
<title>志愿者查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Zyz/frontlist">志愿者信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Zyz/zyz_frontAdd.jsp" style="display:none;">添加志愿者</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<zyzList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Zyz zyz = zyzList.get(i); //获取到志愿者对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Zyz/<%=zyz.getZyzId() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=zyz.getUserPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		志愿者id:<%=zyz.getZyzId() %>
			     	</div>
			     	<div class="field">
	            		姓名:<%=zyz.getName() %>
			     	</div>
			     	<div class="field">
	            		性别:<%=zyz.getGender() %>
			     	</div>
			     	<div class="field">
	            		出生日期:<%=zyz.getBirthDate() %>
			     	</div>
			     	<div class="field">
	            		联系电话:<%=zyz.getTelephone() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Zyz/<%=zyz.getZyzId() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="zyzEdit('<%=zyz.getZyzId() %>');" style="display:none;">修改</a>
			        <a class="btn btn-primary top5" onclick="zyzDelete('<%=zyz.getZyzId() %>');" style="display:none;">删除</a>
			     </div>
			</div>
			<%  } %>

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

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>志愿者查询</h1>
		</div>
		<form name="zyzQueryForm" id="zyzQueryForm" action="<%=basePath %>Zyz/frontlist" class="mar_t15" method="post">
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
<div id="zyzEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" style="width:900px;" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;志愿者信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="zyzEditForm" id="zyzEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="zyz_zyzId_edit" class="col-md-3 text-right">志愿者id:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="zyz_zyzId_edit" name="zyz.zyzId" class="form-control" placeholder="请输入志愿者id" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="zyz_name_edit" class="col-md-3 text-right">姓名:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zyz_name_edit" name="zyz.name" class="form-control" placeholder="请输入姓名">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_gender_edit" class="col-md-3 text-right">性别:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zyz_gender_edit" name="zyz.gender" class="form-control" placeholder="请输入性别">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_birthDate_edit" class="col-md-3 text-right">出生日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date zyz_birthDate_edit col-md-12" data-link-field="zyz_birthDate_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="zyz_birthDate_edit" name="zyz.birthDate" size="16" type="text" value="" placeholder="请选择出生日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_userPhoto_edit" class="col-md-3 text-right">志愿者照片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="zyz_userPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="zyz_userPhoto" name="zyz.userPhoto"/>
			    <input id="userPhotoFile" name="userPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_telephone_edit" class="col-md-3 text-right">联系电话:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zyz_telephone_edit" name="zyz.telephone" class="form-control" placeholder="请输入联系电话">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_address_edit" class="col-md-3 text-right">家庭地址:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="zyz_address_edit" name="zyz.address" class="form-control" placeholder="请输入家庭地址">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_grda_edit" class="col-md-3 text-right">个人档案:</label>
		  	 <div class="col-md-9">
			 	<textarea name="zyz.grda" id="zyz_grda_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="zyz_fwxm_edit" class="col-md-3 text-right">服务项目:</label>
		  	 <div class="col-md-9">
			 	<textarea name="zyz.fwxm" id="zyz_fwxm_edit" style="width:100%;height:500px;"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#zyzEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxZyzModify();">提交</button>
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
var zyz_grda_edit = UE.getEditor('zyz_grda_edit'); //个人档案编辑器
var zyz_fwxm_edit = UE.getEditor('zyz_fwxm_edit'); //服务项目编辑器
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.zyzQueryForm.currentPage.value = currentPage;
    document.zyzQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.zyzQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.zyzQueryForm.currentPage.value = pageValue;
    documentzyzQueryForm.submit();
}

/*弹出修改志愿者界面并初始化数据*/
function zyzEdit(zyzId) {
	$.ajax({
		url :  basePath + "Zyz/" + zyzId + "/update",
		type : "get",
		dataType: "json",
		success : function (zyz, response, status) {
			if (zyz) {
				$("#zyz_zyzId_edit").val(zyz.zyzId);
				$("#zyz_name_edit").val(zyz.name);
				$("#zyz_gender_edit").val(zyz.gender);
				$("#zyz_birthDate_edit").val(zyz.birthDate);
				$("#zyz_userPhoto").val(zyz.userPhoto);
				$("#zyz_userPhotoImg").attr("src", basePath +　zyz.userPhoto);
				$("#zyz_telephone_edit").val(zyz.telephone);
				$("#zyz_address_edit").val(zyz.address);
				zyz_grda_edit.setContent(zyz.grda, false);
				zyz_fwxm_edit.setContent(zyz.fwxm, false);
				$('#zyzEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除志愿者信息*/
function zyzDelete(zyzId) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Zyz/deletes",
			data : {
				zyzIds : zyzId,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#zyzQueryForm").submit();
					//location.href= basePath + "Zyz/frontlist";
				}
				else 
					alert(obj.message);
			},
		});
	}
}

/*ajax方式提交志愿者信息表单给服务器端修改*/
function ajaxZyzModify() {
	$.ajax({
		url :  basePath + "Zyz/" + $("#zyz_zyzId_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#zyzEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#zyzQueryForm").submit();
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
    $('.zyz_birthDate_edit').datetimepicker({
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

