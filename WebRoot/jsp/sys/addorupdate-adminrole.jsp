<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>角色管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <!-- <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script> -->
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>

    <link rel="stylesheet" type="text/css" href="${path}/jqueryutil/jquery-ui-1.8.21.custom.css"/>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-ui-1.8.21.custom.min.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        .table td, .table th {
            padding-top: 8px;
            padding-bottom: 4px;
            line-height: 20 ppx;
            text-align: center;
            vertical-align: middle;
        }

        @media ( max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }
    </style>
</head>
<body>
<form class="form-inline definewidth m20" action="index.html" method="post">
    <p>
    <div class="form-inline definewidth m20">
        <span style="color: red;">* </span>选择要设置的角色: <s:select list="listRole" listKey="role_id" listValue="role_name"
                                                               id="list_role" headerKey="0" headerValue="--选择角色--"/>&nbsp;&nbsp;
        <button type="button" class="btn btn-success" onclick="addAdminsRole()">给选中的用户设置角色</button> &nbsp;&nbsp;
    </div>
    <hr style="color: black;"/>
    登录名称： <input id="login_name" type="text" name="login_name" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;
    用户名称： <input id="user_name" type="text" name="user_name" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;
    查询角色: <s:select list="listRole" listKey="role_id" listValue="role_name" id="t_role_id" headerKey="0"
                    headerValue="--选择角色--"/>
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;&nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>全选<input type="checkbox" name="selectAll" onclick="selectAll()"></th>
            <th>登录名称</th>
            <th>用户名称</th>
            <th>当前所属角色</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
</div>
</body>
</html>
<script>
    listdata(1);

    //加载用户
    function listdata(currentPage) {
        dataList = new Array();
        var user_name = $("#user_name").val();
        var login_name = $("#login_name").val();
        var role_id = $("#t_role_id").val();
        $.ajax({
            type: "post",
            data: {user_name: user_name, login_name: login_name, currentPage: currentPage, role_id: role_id},
            async: false,
            cache: false,
            url: "${path}/adminrole/addadminRole_adminlist.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                dataList.length = 0;
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr style='cursor:pointer;' onclick='checkBoxTrue(" + obj[0] + ")'><td><input id='checkBox" + obj[0] + "' type='checkbox' name='subcheck' value='" + obj[0] + "'></td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
                    var strs = "<tr><td colspan='20'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //复选框事件
    //全选、取消全选的事件
    function selectAll() {
// 			$("input[name='subcheck']").attr("checked"); //读取所有name为'chk_list'对象的状态（是否选中）

        if ($("input[name='selectAll']").attr("checked") == "checked") {
            $("input[name='subcheck']").attr("checked", true);
        } else {
            $("input[name='subcheck']").attr("checked", false);      //设置所有name为'chk_list'对象的checked为true
        }
    }

    //给选中的用户设置角色
    function addAdminsRole() {
        if ($("#list_role").val() == "" || $("#list_role").val() == "0") {
            alert("请选择角色");
            return;
        }
        var admin_ids = "";
        //获取到所有name为'chk_list'并选中的checkbox(集合)
        var arrChk = $("input[name='subcheck']:checked");
        //遍历得到每个checkbox的value值
        for (var i = 0; i < arrChk.length; i++) {
            if (i == arrChk.length - 1) {
                admin_ids = admin_ids + arrChk[i].value;
            } else {
                admin_ids = admin_ids + arrChk[i].value + ",";
            }
        }
        if (admin_ids == "" || admin_ids == ',') {
            alert("请选择用户");
            return;
        }
        $.ajax({
            type: "post",
            data: {role_id: $("#list_role").val(), admin_ids: admin_ids, handleType: 'add'},
            async: false,
            cache: false,
            url: "${path}/adminrole/addOrUpdateAdminRole.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
// 						$("input[name='selectAll']").attr("checked",false);
// 						listRole(1);
                    alert("操作成功!");
                    window.location.href = "${path}/jsp/sys/list_admin_role.jsp";
                } else if (data.flag == "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败!");
                }
            }
        });
    }

    function checkBoxTrue(id) {
        if ($("#checkBox" + id).attr("checked") == "checked") {
            $("#checkBox" + id).attr("checked", false);
        } else {
            $("#checkBox" + id).attr("checked", true);      //设置所有name为'chk_list'对象的checked为true
        }

    }
</script>
