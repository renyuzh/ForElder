<%@page import="com.heart.admin.AdminService"%>
<%@page import="com.ckeditor.CKEditorConfig"%>
<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" href="../../favicon.ico">

<title>牵挂管理</title>
<link rel="stylesheet"
	href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css">

<link rel="stylesheet" href="res/css/doc.css">



<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<style>
body {
	font-family: "Helvetica Neue", "Hiragino Sans GB", "Microsoft YaHei",
		"微软雅黑", "黑体", Arial, sans-serif
}

.big-font {
	font-size: 25px;
	padding-left: 40px;
	font-weight: bolder
}

.bigfont2 {
	font-size: 20px;
}

.menu-title {
	color: #8d8d8d;
	font-size: 20px;
	letter-spacing: 2px;
	font-weight: bold;
	padding-left: 5px;
}

.a-menu {
	color: #222;
	text-overflow: ellipsis;
	white-space: nowrap;
	word-wrap: normal
}

.menu-selected {
	background-color: #7DCF80;
	color: #fff
}

.msgSender {
	margin: 0 30px;
	border: 1px solid #e7e7eb;
}

.tab-navs {
	border-top-width: 0;
	background-color: #fff;
	line-weight: 38px;
	height: 38px;
	text-align: center;
	border-bottom: 1px solid #e7e7eb;
	-webkit-box-shadow: inset 0 1px 0 0 rgba(255, 255, 255, 0.5);
	list-style-type: none;
	font-size: 20px;
}

.tab-nav {
	padding-top: 6px;
	padding-right: 20px;
	background-color: #fff;
	float: left;
	font-size: 20px;
	text-align: center;
}

.tab-nav:hover {
	filter: alpha(opacity =     50);
	-moz-opacity: 0.5;
	-khtml-opacity: 0.5;
	opacity: 0.5;
}

.tab-panel {
	background-color: #fff;
	min-height: 256px;
	margin-top: -10px;
}

.content { /* margin-top: -10px; */
	height: 280px;
	width: 100%;
	resize: none;
	outline: none;
	border: none;
}

.content:focus {
	border: 1px solid #e7e7eb;
	height: 260px;
	border-bottom: none;
}

.abstract{
    
  height:80px;
  width:100%;
  max-width:100%;
  max-height:80px;
}
.toolbar {
	padding: 0 20px;
	line-height: 36px;
	border-top: 1px solid #e7e7eb;
}

.editor-tip {
	float: right;
	color: #8d8d8d;
}
</style>
</head>

<body>

	<%-- 	   <c:if test="${adminLogin != true }">
       <jsp:forward page="/admin.html"></jsp:forward>
       </c:if> --%>


	<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target=".navbar-collapse">
					<span class="sr-only"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand big-font" href="adminHome">Heart Wings</a>
			</div>
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="javasript:void(0);" class="bigfont2">管理员</a></li>
					<li><a href="javasript:void(0);" class="bigfont2"><span
							class="glyphicon glyphicon-envelope"></span></a></li>
					<li><a href="javasript:void(0);" class="bigfont2">退出</a></li>
				</ul>

			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3 col-md-2 sidebar">
				<ul class="nav nav-sidebar">
					<li class="active"><a href="javasript:void(0);"><img
							src="res/img/icon_menu_function.png"><span
							class="menu-title">功能</span></a></li>
					<li class="menu-selected"><a href="javacript:void(0);"
						class="a-menu selected">群发功能</a></li>
					<li><a href="#" class="a-menu">导出功能</a></li>

				</ul>
				<ul class="nav nav-sidebar">
					<li class="active"><a href="javasript:void(0);"><img
							src="res/img/icon_menu_management.png"><span
							class="menu-title">管理</span></a></li>
					<li><a href="" class="a-menu">消息管理</a></li>
					<li><a href="" class="a-menu">监护人管理</a></li>
					<li><a href="" class="a-menu">老人管理</a></li>

				</ul>
				<ul class="nav nav-sidebar">
					<li class="active"><a href="javasript:void(0);"><img
							src="res/img/icon_menu_statistics.png"><span
							class="menu-title">统计</span></a></li>
					<li><a href="" class="a-menu">消息分析</a></li>
					<li><a href="" class="a-menu">用户分析</a></li>

				</ul>
				<ul class="nav nav-sidebar">
					<li class="active"><a href="javasript:void(0);"><img
							src="res/img/icon_menu_setup.png"><span class="menu-title">设置</span></a>
					</li>
					<li><a href="" class="a-menu">安全中心</a></li>


				</ul>
			</div>

			<form action="sendAllAction" method="post" enctype="multipart/form-data">

				<input type="hidden" value="1" name="Fantag" id="Fantag" />

				<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
					<h1 class="page-header">群发功能</h1>
					<div style="margin:5px 30px 0;padding-bottom:10px">
						<!-- Single button -->
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown">
								<span id="thisFan">全部用户 </span><span class="caret"></span>
							</button>
							<ul class="dropdown-menu" role="menu">
								<li class="Afan" id="1"><a href="javascript:void(0);">全部用户</a></li>
								<li class="Afan" id="2"><a href="javascript:void(0);">全部监护人</a></li>
								<li class="Afan" id="3"><a href="javascript:void(0);">全部老人</a></li>
							</ul>
						</div>
					</div>
					<div class="msgSender">
					    	<div>
								<div class="row">
									
										<div class="col-md-4">
									<div class="input-group">
										<input type="text" class="form-control" name='textfield'
											id='textfield' disabled="true"> <span
											class="input-group-btn">
											<button class="btn btn-primary" type="button" id="lookBtn">浏览</button>
											<input type="file" name="fileField" class="file"
											id="fileField" style="display:none"  required
											onchange="document.getElementById('textfield').value=this.value" />
										</span>
									</div>
									<!-- /input-group -->
								</div>
								</div>
								<div class="row" style="padding-top:10px">
									<div class="col-md-4">
										<input type="text" class="form-control" placeholder="输入标题" name="newstitle" id="newstitle" required>
									</div>
								</div>

							</div>
						<div class="msg-tab">
							<ul class="tab-navs">
								<li class="tab-nav"><span
									class="glyphicon glyphicon-pencil"></span></li>
								<li class="tab-nav"><span
									class="glyphicon glyphicon-picture"></span></li>
							</ul>
							
							<div class="tab-panel">
							<textarea name="abstract" id="abstract" class="abstract">新闻摘要</textarea>
							    
								<textarea name="content" id="content" class="content"
									autofocus="autofocus">新闻正文</textarea>

								<%
									CKEditorConfig settings = new CKEditorConfig();
									settings.addConfigValue(
											"toolbar",
											"[{name:'formating',items:['Bold','Italic','Strike','-','RemoveFormat']},'/',{name:'styles',items:['Styles','Format']},"
													+ "{name:'paragraph',items:['NumberedList','BulletedList']}]");
								%>
								<ckeditor:replace replace="content" basePath="ckeditor/"
									config="<%=settings%>" />
							</div>

							<div class="toolbar">
								<!-- <p class="editor-tip">
									还可以输入<em id="ramain"> 400</em> 字
								</p> -->
							</div>
						</div>

					</div>

					<div style="padding-top:20px">
						<span style="margin:5px 30px 0;">
							<button class="btn btn-info" style="height:40px;width:104px"
								type="submit">群发</button>
						</span>
					</div>



				</div>
			</form>
		</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script
		src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("li.menu-selected").hover(function() {

				$("li.menu-selected").css({
					"background-color" : "#44b549"
				});
				// console.log("d" + $(this).css("background-color"));
			});
			$("a.selected").mouseenter(function() {
				$(this).css({
					"background-color" : "#44b549",
					"color" : "#fff"
				});
			});

			$("a.selected").mouseleave(function() {
				$(this).css({
					"background-color" : "#44b549",
					"color" : "#333"
				});
			});

			$(".Afan").click(function() {

				var id = $(this).attr("id");
				$("#Fantag").val(id);

				if (id == "1") {
					$("#thisFan").html("全部用户");
				}
				if (id == "2") {
					$("#thisFan").html("全部监护人");
				}
				if (id == "3") {
					$("#thisFan").html("全部老人");
				}

				console.log($("#thisFan").val());
			});

			/* $("#content").bind("keyup change",function(){
				
				console.log("all is " + $(this).val());
			    
			    var remain = 400 - $(this).val().length;
			    
			    if(remain > 0)
			    	{
			    	   $("#ramain").html(remain);
			    	 
			    	}
			    else
			    	{
			    	$("#ramain").html(0);
			    	
			    	}
			}); */

			$("#lookBtn").click(function(){
				
				$("#fileField").click();
			});
		});
	</script>
</body>

</html>