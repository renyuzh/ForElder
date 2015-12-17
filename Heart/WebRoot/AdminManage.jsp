<%@page import="com.heart.admin.AdminService"%>
<%@ page language="java" import="java.util.*"  contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.2.0/css/bootstrap.min.css">

    <link rel="stylesheet" href="res/css/doc.css">


    <!-- 
    <script src="../../assets/js/ie-emulation-modes-warning.js"></script>

    
    <script src="../../assets/js/ie10-viewport-bug-workaround.js"></script>-->


    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <style>
        
        body{
            font-family: "Helvetica Neue","Hiragino Sans GB","Microsoft YaHei","微软雅黑","黑体",Arial,sans-serif
        }
        .big-font {
            font-size: 25px;
            padding-left: 40px;
            font-weight: bolder
        }
        .bigfont2 {
            font-size: 20px;
        }
        .info-icon1 {
            background: url("res/img/admin.png") 0 0 no-repeat;
            margin-top: 30px;
            width: 34px;
            height: 34px;
            vertical-align: middle;
            display: inline-block
        }
        .info-icon2 {
            background: url("res/img/admin.png") 0 -44px no-repeat;
            margin-top: 30px;
            width: 34px;
            height: 34px;
            vertical-align: middle;
            display: inline-block
        }
        .info-icon3 {
            background: url("res/img/admin.png") 0 -88px no-repeat;
            margin-top: 30px;
            width: 34px;
            height: 34px;
            vertical-align: middle;
            display: inline-block
        }
        
             .info-icon4 {
            background: url("res/img/data.png") no-repeat;
            margin-top: 30px;
            width: 34px;
            height: 34px;
            vertical-align: middle;
            display: inline-block
        }
        .info-item-new-msg {
            background-color: #60d295;
            border: 1px solid #57c78b;
            height: 110px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
           
        }
        
        .info-item-new-fan {
            background-color: #60d295;
            border: 1px solid #57c78b;
            height: 110px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
        }
        
        .info-item-data {
            
              background-color: #60d295;
            border: 1px solid #57c78b;
            height: 110px;
            -moz-border-radius: 5px;
            -webkit-border-radius: 5px;
        }
        .info-item:hover{
            
            background-color: #7ADD92;
            
        }
        .info-number {
            margin-left: 5px;
            font-weight: 400;
            font-style: normal;
            vertical-align: middle;
            font-size: 35px;
            color: #fff
        }
        .info-label {
            display: block;
            padding-top: 10px;
            font-weight: 400;
            font-style: normal;
            font-size: 16px;
            letter-spacing: 2px;
            margin-top: -10px;
            color: #fff
        }
        
        .menu-title{
            color: #8d8d8d;
            font-size: 20px;
            letter-spacing: 2px;
            font-weight: bold;
            padding-left: 5px;
        }
        .a-menu{
            color: #222;
            text-overflow:ellipsis;
            white-space:nowrap;
            word-wrap:normal
        }
    </style>
</head>

<body>
<%--      
     <c:if test="${adminLogin != true }">
       <jsp:forward page="/admin.html"></jsp:forward>
     </c:if> --%>

    <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand big-font" href="adminHome">Heart Wings</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="javasript:void(0);" class="bigfont2">管理员</a>
                    </li>
                    <li><a href="javasript:void(0);" class="bigfont2"><span class="glyphicon glyphicon-envelope"></span></a>
                    </li>
                    <li><a href="javasript:void(0);" class="bigfont2">退出</a>
                    </li>
                </ul>

            </div>
        </div>
    </div>

    <div class="container-fluid">
        <div class="row">
            <div class="col-sm-3 col-md-2 sidebar">
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="javasript:void(0);"><img src="res/img/icon_menu_function.png"><span class="menu-title">功能</span></a>
                    </li>
                    <li><a href="sendAll.jsp" class="a-menu">群发功能</a>
                    </li>
                    <li><a href="#" class="a-menu">导出功能</a>
                    </li>
                  
                </ul>
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="javasript:void(0);"><img src="res/img/icon_menu_management.png"><span class="menu-title">管理</span></a>
                    </li>
                    <li><a href="" class="a-menu">消息管理</a>
                    </li>
                    <li><a href="" class="a-menu">监护人管理</a>
                    </li>
                    <li><a href="" class="a-menu">老人管理</a>
                    </li>
                   
                </ul>
                <ul class="nav nav-sidebar">
                    <li class="active"><a href="javasript:void(0);"><img src="res/img/icon_menu_statistics.png"><span class="menu-title">统计</span></a>
                    </li>
                    <li><a href="" class="a-menu">消息分析</a>
                    </li>
                    <li><a href="" class="a-menu">用户分析</a>
                    </li>
                    
                </ul>
                       <ul class="nav nav-sidebar">
                    <li class="active"><a href="javasript:void(0);"><img src="res/img/icon_menu_setup.png"><span class="menu-title">设置</span></a>
                    </li>
                    <li><a href="" class="a-menu">安全中心</a>
                    </li>
                   
                    
                </ul>
            </div>
            <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                <h1 class="page-header"></h1>

                <div class="row placeholders">
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <div class="info-item-new-msg info-item">
                            <span>
                 <i class="info-icon1"></i>  
                <em class="info-number"><c:out value="${todayMsg }"></c:out></em>
                <strong class="info-label">今日消息</strong>
                </span>
                        </div>


                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">

                        <div style="" class="info-item-new-fan info-item">

                           
                                <span>
                 <i class="info-icon2"></i>  
                <em class="info-number"><c:out value="${todayFan }"></c:out></em>
                <strong class="info-label">新增人数</strong>
                </span>

                        </div>

                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <div style="" class="info-item-new-fan info-item">
                            <span>
                 <i class="info-icon3"></i>  
                <em class="info-number"><c:out value="${allFan }"></c:out></em>
                <strong class="info-label">总用户数</strong>
                </span>
                        </div>
                    </div>
                    <div class="col-xs-6 col-sm-3 placeholder">
                        <div style="" class="info-item-data info-item">
                            <span>
                 <i class="info-icon4"></i>  
                <em class="info-number">79.2%</em>
                <strong class="info-label">摔倒精度</strong>
                </span>
                        </div>

                    </div>
                </div>




                <h2 class="sub-header">Section title</h2>
  <!--              <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Header</th>
                                <th>Header</th>
                                <th>Header</th>
                                <th>Header</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1,001</td>
                                <td>Lorem</td>
                                <td>ipsum</td>
                                <td>dolor</td>
                                <td>sit</td>
                            </tr>
                            <tr>
                                <td>1,002</td>
                                <td>amet</td>
                                <td>consectetur</td>
                                <td>adipiscing</td>
                                <td>elit</td>
                            </tr>
                            <tr>
                                <td>1,003</td>
                                <td>Integer</td>
                                <td>nec</td>
                                <td>odio</td>
                                <td>Praesent</td>
                            </tr>
                            <tr>
                                <td>1,003</td>
                                <td>libero</td>
                                <td>Sed</td>
                                <td>cursus</td>
                                <td>ante</td>
                            </tr>
                            <tr>
                                <td>1,004</td>
                                <td>dapibus</td>
                                <td>diam</td>
                                <td>Sed</td>
                                <td>nisi</td>
                            </tr>
                            <tr>
                                <td>1,005</td>
                                <td>Nulla</td>
                                <td>quis</td>
                                <td>sem</td>
                                <td>at</td>
                            </tr>
                            <tr>
                                <td>1,006</td>
                                <td>nibh</td>
                                <td>elementum</td>
                                <td>imperdiet</td>
                                <td>Duis</td>
                            </tr>
                            <tr>
                                <td>1,007</td>
                                <td>sagittis</td>
                                <td>ipsum</td>
                                <td>Praesent</td>
                                <td>mauris</td>
                            </tr>
                            <tr>
                                <td>1,008</td>
                                <td>Fusce</td>
                                <td>nec</td>
                                <td>tellus</td>
                                <td>sed</td>
                            </tr>
                            <tr>
                                <td>1,009</td>
                                <td>augue</td>
                                <td>semper</td>
                                <td>porta</td>
                                <td>Mauris</td>
                            </tr>
                            <tr>
                                <td>1,010</td>
                                <td>massa</td>
                                <td>Vestibulum</td>
                                <td>lacinia</td>
                                <td>arcu</td>
                            </tr>
                            <tr>
                                <td>1,011</td>
                                <td>eget</td>
                                <td>nulla</td>
                                <td>Class</td>
                                <td>aptent</td>
                            </tr>
                            <tr>
                                <td>1,012</td>
                                <td>taciti</td>
                                <td>sociosqu</td>
                                <td>ad</td>
                                <td>litora</td>
                            </tr>
                            <tr>
                                <td>1,013</td>
                                <td>torquent</td>
                                <td>per</td>
                                <td>conubia</td>
                                <td>nostra</td>
                            </tr>
                            <tr>
                                <td>1,014</td>
                                <td>per</td>
                                <td>inceptos</td>
                                <td>himenaeos</td>
                                <td>Curabitur</td>
                            </tr>
                            <tr>
                                <td>1,015</td>
                                <td>sodales</td>
                                <td>ligula</td>
                                <td>in</td>
                                <td>libero</td>
                            </tr>
                        </tbody>
                    </table>
                </div>-->
            </div>
        </div>
    </div>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="http://cdn.bootcss.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</body>

</html>
