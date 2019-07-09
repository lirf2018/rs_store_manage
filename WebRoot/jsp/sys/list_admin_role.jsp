<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户角色管理</title>
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
<form class="form-inline definewidth m20" action="" method="get">
    登录名称： <input id="login_name" type="text" name="login_name" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;&nbsp;
    用户名称： <input id="user_name" type="text" name="user_name" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;&nbsp;
    角色名称: <s:select list="#{0:'--选择角色--'}" listKey="key" listValue="value" name="role_id" id="list_role"/>
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;&nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddAdminRolePage()">新增或更新用户角色</button>
    &nbsp;&nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>登录名称</th>
            <th>用户姓名</th>
            <th>角色名称</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
</div>
<!-- dialog -->
<div id="addOrEditDialog" style="display: none;">
    <form action="" id="submit_form">
        <input type="hidden" name="admin_role_id" id="admin_role_id">
        <input type="hidden" name="role_id" id="role_id">
        <input type="hidden" name="admin_id" id="admin_id">
        <input type="hidden" value="" id="handleType" name="handleType">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>用户名称:</td>
                <td><input type="text" value="" disabled="disabled"
                           onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')" id="user_name_"></td>
            </tr>
            <tr>
                <td>登录名称:</td>
                <td><input type="text" disabled="disabled" value=""
                           onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')" id="login_name_"></td>
            </tr>
            <tr>
                <td><span style="color: red;">* </span>选择角色:</td>
                <td><s:select list="#{0:'--选择角色--'}" listKey="key" listValue="value" value="" id="list_role_"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<script>
    listdata(1);
    var dataList;

    //加载用户角色
    function listdata(currentPage) {
        dataList = new Array();
        var login_name = $("#login_name").val();
        var user_name = $("#user_name").val();
        var role_id = $("#list_role").val();
        $.ajax({
            type: "post",
            data: {login_name: login_name, role_id: role_id, user_name: user_name, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/adminrole/loadadminrolelist.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                dataList.length = 0;
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
// 							var str = new StrUtil();
                    var dataList_value = obj[0] + "," + obj[1] + "," + obj[2] + "," + obj[3] + "," + obj[4] + "," + obj[5] + "," + obj[6];
                    dataList.push(dataList_value);

                    strs = strs + "<tr><td>" + obj[0] + "</td>";//编号
                    strs = strs + "<td>" + obj[1] + "</td>";//登录名称
                    strs = strs + "<td>" + obj[2] + "</td>";//用户姓名
                    strs = strs + "<td>" + obj[3] + "</td>";//角色名称
                    strs = strs + "<td>" + obj[4] + "</td>";//创建时间
                    strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdatechannel(" + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='addOrEditDialog(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
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


    //删除或者启用菜单
    function delOrUpdatechannel(admin_role_id) {
        if (!confirm("确认要删除？")) {
            return;
        }
        $.ajax({
            type: "post",
            data: {admin_role_id: admin_role_id},
            async: false,
            cache: false,
            url: "${path}/adminrole/deleteAdminRole.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    function addOrEditDialog(id, handleType) {
        //设置值
        setData(id, handleType);

        $("#addOrEditDialog").dialog({
            autoOpen: true,
            height: 300,
            width: 600,
            title: "查看/编辑",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    if ($("#list_role_").val() == "" || $("#list_role_").val() == "0") {
                        alert("选择角色名称!");
                        return;
                    }
                    ;
                    addOrUpdate();
                },
                "取消": function () {
                    $("#addOrEditDialog").dialog("close");
                }
            }
        });
    }

    //
    function addOrUpdate() {
        $.ajax({
            type: "post",
            data: {admin_id: $("#admin_id").val(), role_id: $("#list_role_").val()},
            async: false,
            cache: false,
            url: "${path}/adminrole/addOrUpdateAdminRole.action",
            dataType: "json",
            success: function (data) {

                if (data.flag == "ok") {
                    alert(data.msg);
                    $("#addOrEditDialog").dialog("close");
                    listdata(1);
                } else if (data.flag == "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败!");
                }
            }
        });
    }

    //设置数据
    function setData(id_, handleType) {
        if ("1" == handleType) {//update
            for (var i = 0; i < dataList.length; i++) {
                var str = dataList[i].split(",");
                var admin_role_id = str[0];
                var login_name = str[1];
                var user_name = str[2];
                var role_id = str[5];
                var admin_id = str[6];
                if (admin_role_id == id_) {
                    $("#admin_role_id").val(admin_role_id);
                    $("#admin_id").val(admin_id);
// 						$("#role_id").val(role_id);
                    $("#handleType").val("update");
                    $("#user_name_").val(user_name);
                    $("#login_name_").val(login_name);
                    $("#list_role_").val(role_id);
                    return;
                }
            }
        } else {//新增
        }
    }

    listRole();

    //加载角色列表
    function listRole() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/adminrole/getListRoleAdminRole.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value='0' selected='selected'>--选择角色--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].role_id + "'> " + data.msg[i].role_name + " </option>"
                    }
                }
                $("#list_role").html(str);
                $("#list_role_").html(str);
            }
        });
    }

    //跳转到增加用户角色页面
    function toAddAdminRolePage() {
        window.location.href = "${path}/adminrole/toAddAdminRolePage.action";
    }
</script>
