<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>一级分类</title>
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
    分类名称： <input id="leveName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    分类编码： <input id="leveCode" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    类目名称： <s:select list="#{0:'--类目名称--'}" listKey="key" cssStyle="width:110px" listValue="value" value=""
                    id="catogryId"/>&nbsp;
    <p></p>
    类目： <input id="categoryCode" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button>
    &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增分类</button>
    &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>标识</th>
            <th>分类名称</th>
            <th>分类编码</th>
            <th>图片</th>
            <th>权重</th>
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
        var leveName = $("#leveName").val();//
        var leveCode = $("#leveCode").val();//
        var catogryId = $("#catogryId").val();//类目标识
        var categoryCode = $("#categoryCode").val();//类目
        var status = $("#status").val();//状态
        $.ajax({
            type: "post",
            data: {
                leveName: leveName,
                levelCode: leveCode,
                category_id: catogryId,
                status: status,
                categoryCode: categoryCode,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadCatogryLeve1ListPage2.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    //标识0 名称1 编码2		权重3	创建时间4	状态5 图片6
                    strs = strs + "<tr style='cursor:pointer;' onclick='showDetail(" + obj[0] + ")'><td>" + obj[0] + "</td>";//编号
                    strs = strs + "<td>" + obj[1] + "</td>";//名称
                    strs = strs + "<td>" + obj[2] + "</td>";//编码
                    strs = strs + "<td><img width='35px' height='35px' style='border: solid 1px;border-color: black;' src='" + obj[6] + "'></td>";
                    strs = strs + "<td>" + obj[3] + "</td>";//权重
                    if (obj[5] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[4] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[4] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    }
                    strs = strs + "<tr id='detail" + obj[0] + "' style='display: none'><td colspan='30'>";
                    strs = strs + "<table class='table table-bordered table-hover definewidth m10' style='font-size: 14px'>";
                    strs = strs + "<thead>";
                    strs = strs + "<tr>";
                    strs = strs + "<th>标识</th>";
                    strs = strs + "<th>类目名称</th>";
                    strs = strs + "<th>类目编码</th>";
                    strs = strs + "<th>排序</th>";
                    strs = strs + "</tr>";
                    strs = strs + "</thead>";
                    strs = strs + "<tbody id='tbd" + obj[0] + "'>";

                    strs = strs + "</tbody>";
                    strs = strs + "</table>";
                    strs = strs + "</td></tr>";
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
            data: {leveId: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/updateCatogryLeve1Status.action",
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
        if ("0" == id) {//新增
            var op = "add";
            window.location.href = "${path}/manage/toAddorUpdateCatogryLeve1Page.action?haddle=" + op;
        } else {//修改
            var op = "update";
            window.location.href = "${path}/manage/toAddorUpdateCatogryLeve1Page.action?haddle=" + op + "&leveId=" + id;
        }
    }

    listCatogry();

    //加载列表
    function listCatogry() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/manage/loadlistCategory.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value='' selected='selected'>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].category_id + "'> " + data.msg[i].category_name + "(" + data.msg[i].category_code + ")" + " </option>";
                    }
                }
                $("#catogryId").html(str);
            }
        });
    }

    function showDetail(id) {
        $.ajax({
            type: "post",
            data: {categoryId: id},
            async: false,
            cache: false,
            url: "${path}/manage/loadClassfyCatogryByClassfyId",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                $("#tbd" + id).empty();
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        var str = "<tr>";
                        str = str + "<td>" + data.msg[i].category_id + "</td>";
                        str = str + "<td>" + data.msg[i].category_name + "</td>";
                        str = str + "<td>" + data.msg[i].category_code + "</td>";
                        str = str + "<td>" + data.msg[i].short + "</td>";
                        str = str + "</tr>";
                        $("#tbd" + id).append(str.toString());
                    }
                } else {
                    str = str + "<tr><td colspan='20' style='font-size: 11px;color: red'>暂时没有数据</td></tr>";
                    $("#tbd" + id).append(str.toString());
                }
            }
        });
        $("#detail" + id).toggle();
    }
</script>
