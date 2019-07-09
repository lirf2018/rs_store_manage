<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="./util/taglibs.jsp" %>
<%@ include file="./util/meta.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>rs后台管理系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="${path}/assets/css/dpl-min.css" rel="stylesheet" type="text/css"/>
    <link href="${path}/assets/css/bui-min.css" rel="stylesheet" type="text/css"/>
    <link href="${path}/assets/css/main-min.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var path = '${path}';
    </script>
</head>
<body>

<div class="header">
    <div class="dl-log">欢迎您：<span class="dl-log-user" id="loginmsg">  </span><a onclick="exitOut()"
                                                                                style="cursor:pointer" title="退出系统"
                                                                                class="dl-log-quit">[退出]</a>&nbsp;&nbsp;
    </div>
</div>
<div class="content">
    <div class="dl-main-nav">
        <div class="dl-inform">
            <div class="dl-inform-title"><s class="dl-inform-icon dl-up"></s></div>
        </div>
        <ul id="J_Nav" class="nav-list ks-clear">
            <li class="nav-item dl-selected">
                <div class="nav-item-inner nav-home">功能页</div>
            </li>
        </ul>
    </div>
    <ul id="J_NavContent" class="dl-tab-conten">

    </ul>
</div>
<script type="text/javascript" src="${path}/assets/js/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="${path}/assets/js/bui.js"></script>
<script type="text/javascript" src="${path}/assets/js/config.js"></script>
<script>
    loadFucntion();

    function loadFucntion() {
        $.ajax({
            type: "post",
            data: $("#form_submit").serialize(),
            async: false,
            cache: false,
            url: "${path}/login/loadMenus.action",
            dataType: "json",
            success: function (data) {
                if ("false" == data.flag) {
                    window.location.href = '../';
                } else {
                    loadMenus(data.msg);
                }
            },
            error: function (data) {
                alert("会话结束");
                window.location.href = "${path}/jsp/login/login.jsp";
            }
        });
    }

    function loadMenus(msg) {
        var textMenus = new Array();
        var t_menus = new Array();
        var t_menus_ = new Array();
        for (var i = 0; i < msg.length; i++) {
            var function_parentid = msg[i].function_parentid;
            if (function_parentid == "0") {
                if (t_menus.length > 0) {
                    textMenus.push(t_menus);
                }
                t_menus = new Array();
                t_menus.push(msg[i]);
            } else {
                t_menus.push(msg[i]);
            }
            if (i == msg.length - 1) {
                textMenus.push(t_menus);
            }
        }
        var menusTextParent = new Array();
        for (var i = 0; i < textMenus.length; i++) {
            t_menus_ = textMenus[i];
            var menusTextSub = "";
            for (var j = 0; j < t_menus_.length; j++) {
                var function_parentid = t_menus_[j].function_parentid;
                var function_name = t_menus_[j].function_name;
                var function_action = t_menus_[j].function_action;
                if (function_action == "") {
                    function_action = "jsp/404.jsp";
                }
                var function_code = t_menus_[j].function_code;
                if (function_parentid == "0") {
                    menusTextSub = "{";
                    menusTextSub = menusTextSub + " text:'" + function_name + "'";
                } else {
                    if (j == 1) {
                        menusTextSub = menusTextSub + ",items:[";
                    }
                    if (j == t_menus_.length - 1) {
                        menusTextSub = menusTextSub + "{id:'" + function_code + "',text:'" + function_name + "',href:'${path}/" + function_action + "'}";
                        menusTextSub = menusTextSub + "]";
                    } else {
                        menusTextSub = menusTextSub + "{id:'" + function_code + "',text:'" + function_name + "',href:'${path}/" + function_action + "'},";
                    }
                }
            }
            menusTextSub = menusTextSub + "}";
            menusTextParent.push(eval("(" + menusTextSub + ")"));
        }

// 	    	var center="{text:'个人中心', items:[ {id:'firstPage',text:'欢迎页',href:'${path}/jsp/main.jsp',closeable : false},{id:'centerpage',text:'个人中心',href:'${path}/jsp/main.jsp'}]}";
        var center = "{text:'个人中心', items:[ {id:'firstPage',text:'欢迎页',href:'${path}/jsp/welcome.jsp',closeable : false},{id:'centerpage',text:'修改密码',href:'${path}/jsp/sys/updatepassword.jsp'}]}";
        menusTextParent.push(eval("(" + center + ")"));
        BUI.use('common/main', function () {
            //首页显示
            var homePage = 'firstPage';
            var config = [{
                id: 'menu',
                homePage: homePage,
                menu: menusTextParent
            }];

            new PageUtil.MainPage({
                modulesConfig: config
            });
        });
        //得到用户信息
        getLoginInfo();
    }

    function getLoginInfo() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/login/getLoginInfo.action",
            dataType: "json",
            success: function (data) {
                $("#loginmsg").html(data.msg.userName);
            }
        });
    }

    //退出系统
    function exitOut() {
        window.location.href = '${path}/login/exitweb.action';
    }
</script>
</body>
</html>
