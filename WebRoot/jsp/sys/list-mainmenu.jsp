<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>wap菜单管理</title>
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
            line-height: 20px;
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
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增main菜单</button>
    &nbsp;
    状态： <s:select list="#{-1:'全部',0:'无效'}" listKey="key" listValue="value" headerKey="1" headerValue="有效" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>名称</th>
            <th>图片</th>
            <th>排序</th>
            <th>关联一级分类</th>
            <th>关联类目</th>
            <th>跳转地址</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
    <!-- 		<img width="100px" height="100px" style="border: solid 6px;border-color: black;"  src="http://pic1a.nipic.com/2008-12-04/2008124215522671_2.jpg"> -->
</div>
<!-- dialog -->
<div id="addOrEditDialog" style="display: none;">
    <form action="" id="submit_form">
        <input type="hidden" value="${role.roleId}" id="roleid" name="role.roleId">
        <input type="hidden" value="add" id="handleType" name="handleType">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td style="text-align: left"><span id="menuUrl"></span></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<script>
    listdata(1);
    var currentPage;//当前页
    var index;//下标
    var totalPages;//总共页数
    var str_page;//当前页html

    //加载渠道
    function listdata(currentPage) {
        $.ajax({
            type: "post",
            data: {currentPage: currentPage, status: $("#status").val()},
            async: false,
            cache: false,
            url: "${path}/menu/loadMainMenuDataList",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr><td>" + obj[0] + "</td>";//编号
                    strs = strs + "<td>" + obj[1] + "</td>";//名称
                    strs = strs + "<td><img width='100px' height='50px' style='border: solid 1px;border-color: black;' src='" + obj[7] + "'></td>";
                    strs = strs + "<td><span><input id='sortInput" + obj[0] + "' type='text' value=" + obj[2] + " style='width: 50px;text-align: center'></span></td>";//排序
                    strs = strs + "<td>" + obj[8] + "</td>";
                    strs = strs + "<td>" + obj[9] + "</td>";
                    strs = strs + "<td><span><input type='hidden' value='" + obj[10] + "'' id='menuUrl" + obj[0] + "'><input type='button' class='btn btn-primary' onclick='showUlr(" + obj[0] + ")'  value='查看链接'></span></td>";
                    if (obj[3] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>编辑</a>|<a style='cursor:pointer;'onclick='updateMenuSort(" + obj[0] + ")'>保存排序</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>编辑</a>|<a style='cursor:pointer;'onclick='updateMenuSort(" + obj[0] + ")'>保存排序</a></td></tr>";
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
    function delOrUpdate(status, id) {
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delStatusBannel.action",
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

    //跳转到新增或者修改页面
    function toAddOrUpdatePage(id) {
        if (Number(id) > 0) {
            window.location.href = "${path}/menu/toAddMainMenuPage?handdleType=update&id=" + id;
        } else {
            window.location.href = "${path}/menu/toAddMainMenuPage";
        }

    }

    function delOrUpdate(status, id) {
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/menu/deleteMainMenu",
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

    function updateMenuSort(id) {

        var menuSort = $("#sortInput" + id).val();
        if (!checkNumber(menuSort)) {
            alert("只能输入数字");
            return;
        }
        $.ajax({
            type: "post",
            data: {ids: id, menuSort: menuSort},
            async: false,
            cache: false,
            url: "${path}/menu/updateMainMenuSort",
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

    //数字校验
    function checkNumber(value) {
        var reg = /^[0-9]*$/;
        if (!reg.test($.trim(value))) {
            return false;
        }
        return true;
    }

    function showUlr(id) {
        $("#menuUrl").html($("#menuUrl" + id).val());
        $("#addOrEditDialog").dialog({
            autoOpen: true,
            height: 150,
            width: 700,
            title: "查看",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "取消": function () {
                    $("#addOrEditDialog").dialog("close");
                }
            }
        });
    }
</script>
