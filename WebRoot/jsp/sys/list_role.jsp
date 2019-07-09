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
<form class="form-inline definewidth m20" action="index.html" method="get">
    角色名称： <input id="searchName" type="text" name="menuname" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="state_"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="addOrEditDialog(0,0)">新增角色</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>角色名称</th>
            <th>编码</th>
            <th>排序</th>
            <th>状态</th>
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
        <input type="hidden" value="${role.roleId}" id="roleid" name="role.roleId">
        <input type="hidden" value="add" id="handleType" name="handleType">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td><span style="color: red;">* </span>角色名称:</td>
                <td><input type="text" value="${role.roleName}" id="roleName" name="role.roleName"></td>
            </tr>
            <tr>
                <td><span style="color: red;">* </span>编码:</td>
                <td><input type="text" value="${role.roleCode}" id="roleCode" name="role.roleCode"></td>
            </tr>
            <tr>
                <td><span style="color: red;">* </span>排序:</td>
                <td><input type="text" value="${role.sort}" onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                           id="sort" style="width:70px;" name="role.sort"><span
                        style="color: red;">只能输入数字,数值越大越靠前</span></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<script>
    listdata(1);
    var dataList;

    //加载角色
    function listdata(currentPage) {
        dataList = new Array();
        var searchName = $("#searchName").val();
        var state = $("#state_").val();
        $.ajax({
            type: "post",
            data: {roleName: searchName, state: state, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/role/loadrolelist.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                dataList.length = 0;
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
// 							var str = new StrUtil();
                    //role_id,role_code,role_name,sort,role_parentid,state,creattime
                    var dataList_value = obj[0] + "," + obj[1] + "," + obj[2] + "," + obj[3] + "," + obj[4] + "," + obj[5] + "," + obj[6];
                    dataList.push(dataList_value);
                    //-- 编号0	角色名称2	编码1	排序3	状态5
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    if (obj[5] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdateRole(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='addOrEditDialog(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdateRole(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='addOrEditDialog(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
                    }
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
    function delOrUpdateRole(status, roleid) {
        $.ajax({
            type: "post",
            data: {roleid: roleid, status: status},
            async: false,
            cache: false,
            url: "${path}/role/delOrUpdateRole.action",
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
            height: 320,
            width: 600,
            title: "查看/编辑",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    var roleName = $("#roleName").val() + "";
                    if (roleName.trim() == "") {
                        alert("输入角色名称");
                        return;
                    }
                    ;
                    var roleCode = $("#roleCode").val() + "";
                    if (roleCode.trim() == "") {
                        alert("输入编码");
                        return;
                    }
                    ;
                    var sort = $("#sort").val();
                    if (sort == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
                        alert("排序为正整数");
                        return;
                    }
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
            data: $("#submit_form").serialize(),
            async: false,
            cache: false,
            url: "${path}/role/addOrUpdateRole.action",
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
            //0role_id,1role_code,2role_name,3sort,4role_parentid,5state,6creattime
            for (var i = 0; i < dataList.length; i++) {
                var str = dataList[i].split(",");
                var roleid = str[0];
                var code = str[1];
                var name = str[2];
                var sort = str[3];
                if (roleid == id_) {
                    $("#roleid").val(roleid);
                    $("#roleName").val(name);
                    $("#roleCode").val(code);
                    $("#sort").val(sort);
                    $("#handleType").val("update");
                }
            }
        } else {//新增
            $("#roleid").val("");
            $("#roleName").val("");
            $("#roleCode").val("");
            $("#sort").val("0");
            $("#state").val("1");
            $("#handleType").val("add");
        }
    }

    //查询参数列表
    function queryParam(type) {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/role/qureyParam_role.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                $("#div_member_type").empty();
                var str = "<select id='roleType' name='role.roleType' >";
                str = str + "<option  value='' >--选择会员类型--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {

                        if (type == data.msg[i].param_key) {
                            str = str + "<option selected='selected' value='" + data.msg[i].param_key + "'> " + data.msg[i].param_value + " </option>";
                        } else {
                            str = str + "<option value='" + data.msg[i].param_key + "'> " + data.msg[i].param_value + " </option>";
                        }
                    }
                }
                str = str + "</select>";
                $("#div_member_type").html(str);
            }
        });
    }
</script>
