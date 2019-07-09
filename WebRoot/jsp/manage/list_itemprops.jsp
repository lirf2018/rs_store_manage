<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>属性管理</title>
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
    属性名称： <input id="itemprops_name" type="text" name="itemprops_name" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    一级分类： <s:select list="#{0:'--选择分类--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="level"
                    onchange="changeCatogry()"/>&nbsp;
    类目： <s:select list="#{0:'--选择类目--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="catogry"/>&nbsp;
    <br>
    属性编码： <input id="itemprops_code" type="text" name="itemprops_code" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" onclick="toAddOrUpdateItempropsPage(0,0)">新增</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>属性编号</th>
            <th>属性名称</th>
            <th>外部标识</th>
            <th>是否销售属性</th>
            <th>显示类型</th>
            <th>属性编码</th>
            <th>是否前端显示</th>
            <th>排序</th>
            <th>所属类目</th>
            <th>一级分类</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
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

    //加载
    function listdata(currentPage) {
        dataList = new Array();
        var itemprops_name = $("#itemprops_name").val();
        var itemprops_code = $("#itemprops_code").val();
        var category_id = $("#catogry").val();
        var status = $("#status").val();
        var leveId = $("#level").val();
        $.ajax({
            type: "post",
            data: {
                category_id: category_id, status: status, leveId: leveId,
                itemprops_code: itemprops_code, itemprops_name: itemprops_name, currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadItempropsListPage.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
// 							var str = new StrUtil();
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    if (obj[4] == "0") {
                        strs = strs + "<td>不是销售属性</td>";
                    } else {
                        strs = strs + "<td>是销售属性</td>";
                    }
                    strs = strs + "<td>" + obj[5] + "</td>";
                    strs = strs + "<td>" + obj[6] + "</td>";
                    if (obj[7] == "1") {
                        strs = strs + "<td>前端显示</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>前端不显示</td>";
                    }
                    strs = strs + "<td>" + obj[8] + "</td>";
                    strs = strs + "<td>" + obj[11] + "</td>";
                    strs = strs + "<td>" + obj[12] + "</td>";
                    if (obj[9] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[10] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delorupdateitemprops(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdateItempropsPage(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[10] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delorupdateitemprops(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdateItempropsPage(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
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


    //删除或者启用类目属性
    function delorupdateitemprops(status, itemprops_id) {
        $.ajax({
            type: "post",
            data: {itemprops_id: itemprops_id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delorupdateitemprops.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata();
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    listCatogry();

    //加载列表
    function listCatogry() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/manage/listClassfyMap.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value=''>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].level_id + "'> " + data.msg[i].level_name + " </option>";
                    }
                }
                $("#level").html(str);
            }
        });
    }

    //修改类目属性
    function changeCatogry() {
        var leveId = $("#level").val();
        if ("0" == leveId || leveId == "") {
            $("#catogry").html("<option value='0'>--选择类目--</option>");
            return;
        }
        $.ajax({
            type: "post",
            data: {leveId: leveId, status: "1"},
            async: false,
            cache: false,
            url: "${path}/manage/loadlistCategory.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                if ("" == msg) {
                    $("#catogry").html("<option value='0'>--选择类目--</option>");
                    return;
                }
                var str = "<option value='0'>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + msg[i].category_id + "'> " + msg[i].category_name_code + " </option>";
                    }
                } else {
                    $("#catogry").html("<option value='0'>--选择类目--</option>");
                }
                $("#catogry").html(str);
            }
        });
    }

    function toAddOrUpdateItempropsPage(itemprops_id, type) {
        if ("1" == type) {
            window.location.href = "${path}/manage/toAddOrUpdateItempropsPage.action?haddle=update&itemprops_id=" + itemprops_id;
        } else {
            window.location.href = "${path}/manage/toAddOrUpdateItempropsPage.action?haddle=add";
        }
    }
</script>
