<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>菜单列表</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        #left {
            width: 35%;
            height: 100%;
            float: left;
        }

        #right {
            width: 65%;
            height: 100%;
            float: right;
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
    子菜单名称： <input id="searchName" type="text" name="menuname" class="abc input-default" placeholder="" value=""
                  onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    子菜单编码： <input style="width: 150px" id="functionCode" type="text" name="functionCode" class="abc input-default"
                  placeholder="" value="" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    子菜单状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--"
                     value="" cssStyle="width:110px" id="state"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="addOrUpdateParentMenu(0)">新增主菜单</button>
    &nbsp;
    <button type="button" class="btn btn-success" id="addSubMenu" onclick="addOrUpdateSubMenu(0)">新增子菜单</button>
</form>
<input type="hidden" id="pmenuid" value="${pmenuid }">
<div id="left">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>功能名称</th>
            <th>编码</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb_parent">

        </tbody>
    </table>
</div>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>功能名称</th>
            <!-- 					<th style="text-align:center;vertical-align:middle;">编码</th> -->
            <th>功能动作</th>
            <th>排序</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb_sub">

        </tbody>
    </table>
</div>
</body>
</html>
<script>
    listParentMenu();
    var menuId;
    var currentPage;//当前页
    var index;//下标
    var totalPages;//总共页数
    var str_page;//当前页html
    //加载父级菜单
    function listParentMenu() {
        var parentMenu = "parentMenu";
        $.ajax({
            type: "post",
            data: {menuType: parentMenu},
            async: false,
            cache: false,
            url: "${path}/menu/loadParentMenusOrSubMenus.action",
            dataType: "json",
            success: function (data) {
// 						var page = eval("(" + data + ")");
// 						alert(data.msg.list.length);
                $("#tb_parent").empty();
                var k = 0;
                for (var i = 0; i < data.msg.list.length; i++) {
                    k = k + 1;
                    var strs = "";
                    var obj = data.msg.list[i];
// 							var str = new StrUtil();
                    if (i == 0) {
                        menuId = obj[0];
                        $("#pmenuid").val(menuId);
                    }
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td><a id='color" + k + "' style='cursor:pointer;' onclick='selectMenuByParentId(" + obj[0] + "," + currentPage + "," + k + ")'>" + obj[2] + "</a></td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    if (obj[5] == "1") {
                        strs = strs + "<td>有效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='deleteOrUpdateMenu(0," + obj[0] + ",0)'>删除</a>|<a style='cursor:pointer;' onclick='addOrUpdateParentMenu(" + obj[0] + ")'>查看/编辑</a></td>";
                    } else {
                        strs = strs + "<td  style='color: red;'>无效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='deleteOrUpdateMenu(1," + obj[0] + ",0)'>启用</a>|<a style='cursor:pointer;' onclick='addOrUpdateParentMenu(" + obj[0] + ")'>查看/编辑</a></td>";
                    }
                    strs = strs + "</tr>";
                    $("#tb_parent").append(strs.toString());
                }
                if (data.msg.list.length > 0) {
// 							$("#color1").css("color","000000");
                    $("#color1").css("text-decoration", "underline");//$("#color"+id).css("text-decoration","underline");
                    listdata(1);
                } else {
                    $("#tb_parent").append("<tr><td colspan='4' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                    $("#tb_sub").append("<tr><td colspan='7' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //加载子级菜单
    function listdata(currentPage) {
        //alert("currentPage="+currentPage+" menuId="+menuId);
        var menuType = "subMenu";
        var searchName = $("#searchName").val();
        var functionCode = $("#functionCode").val();
        var state = $("#state").val();

        menuId = $("#pmenuid").val();

        $.ajax({
            type: "post",
            data: {
                searchName: searchName,
                menuType: menuType,
                menuId: menuId,
                currentPage: currentPage,
                functionCode: functionCode,
                state: state
            },
            async: false,
            cache: false,
            url: "${path}/menu/loadParentMenusOrSubMenus.action",
            dataType: "json",
            success: function (data) {
                $("#tb_sub").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
// 							var str = new StrUtil();
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
// 							strs=strs+"<td>"+obj[1]+"</td>";
                    strs = strs + "<td>" + obj[4] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    if (obj[5] == "1") {
                        strs = strs + "<td>有效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='deleteOrUpdateMenu(0," + obj[0] + ",1)'>删除</a>|<a style='cursor:pointer;' onclick='addOrUpdateSubMenu(" + obj[0] + ")'>查看/编辑</a></td>";
                    } else {
                        strs = strs + "<td  style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[6] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='deleteOrUpdateMenu(1," + obj[0] + ",1)'>启用</a>|<a style='cursor:pointer;' onclick='addOrUpdateSubMenu(" + obj[0] + ")'>查看/编辑</a></td>";
                    }
                    strs = strs + "</tr>";
                    $("#tb_sub").append(strs.toString());
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
                    $("#tb_sub").append(strs.toString());
                } else {
                    $("#tb_sub").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //点击菜单查询下面子菜单
    function selectMenuByParentId(menuParentId, nowPage, k) {
        $("#pmenuid").val(menuParentId);
        menuId = menuParentId;
        currentPage = nowPage;
        listdata(1);
        formColor(k);
    }

    //删除或者启用菜单
    function deleteOrUpdateMenu(status, menuId, function_type) {
        $.ajax({
            type: "post",
            data: {status: status, menuId: menuId, function_type: function_type},
            async: false,
            cache: false,
            url: "${path}/menu/deleteOrUpdateMenu.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    if (function_type == 0) {
                        listParentMenu();
                    } else {
                        listdata();
                    }
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //跳转到增加或者修改主菜单页面
    function addOrUpdateParentMenu(menuId) {
        if (menuId == 0) {
            handdleType = "create";
            window.location.href = "${path}/jsp/sys/addoredit-parentmenu.jsp";
        } else {
            handdleType = "update";
            window.location.href = "${path}/menu/toaddorupdateparentMenupage.action?handdleType=" + handdleType + "&menuId=" + menuId;
        }
    }

    //跳转到增加或者修改子菜单页面
    function addOrUpdateSubMenu(menuId) {
        if (menuId == 0) {
            handdleType = "create";
            window.location.href = "${path}/menu/toaddorupdatesubMenupage.action?handdleType=" + handdleType + "&menuId=" + menuId;
        } else {
            handdleType = "update";
            window.location.href = "${path}/menu/toaddorupdatesubMenupage.action?handdleType=" + handdleType + "&menuId=" + menuId;
        }

    }

    function formColor(id) {
        for (var i = 0; i < 100; i++) {
            $("#color" + i).css("color", "#0088cc");
            $("#color" + i).css("text-decoration", "none");
        }
// 			$("#color"+id).css("color","000000");//text-decoration:underline
        $("#color" + id).css("text-decoration", "underline");

    }
</script>
