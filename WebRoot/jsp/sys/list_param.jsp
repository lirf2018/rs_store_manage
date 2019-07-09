<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>参数管理</title>
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
    参数名称： <input id="param_name" type="text" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    参数类型： <s:select list="#{-1:'--选择参数类型--'}" listKey="key" cssStyle="width:130px" listValue="value" value=""
                    id="paramType"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="state"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增参数</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>参数名称</th>
            <th>参数编码</th>
            <th>参数键</th>
            <th>参数值</th>
            <th>是否已确认</th>
            <th>状态</th>
            <th>备注</th>
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

    //加载渠道
    function listdata(currentPage) {
        var param_name = $("#param_name").val();//
        var param_code = $("#paramType").val();//
        if (param_code == -1) {
            param_code = "";
        }
        var state = $("#state").val();//状态
        index = 0;
        $.ajax({
            type: "post",
            data: {param_name: param_name, state: state, param_code: param_code, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/param/loadParamList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    //0param_id,1param_name,2param_type,3param_code,4param_value,5state,6remark,7createtime
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    strs = strs + "<td>" + obj[4] + "</td>";
                    strs = strs + "<td>" + obj[5] + "</td>";
                    if (obj[12]) {
                        strs = strs + "<td>已确认</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>未确认</td>";
                    }
                    if (obj[8] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[9] + "</td>";
                        strs = strs + "<td>" + obj[10] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[9] + "</td>";
                        strs = strs + "<td>" + obj[10] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
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
            data: {id: id, state: status},
            async: false,
            cache: false,
            url: "${path}/param/delParamState.action",
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
            var handleType = "add";
            window.location.href = "${path}/param/toAddOrUpdateParamPage.action?handleType=" + handleType;
        } else {//修改
            var handleType = "update";
            window.location.href = "${path}/param/toAddOrUpdateParamPage.action?handleType=" + handleType + "&id=" + id;
        }
    }

    listParamType();

    function listParamType() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/param/selectParamType.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value='-1' selected='selected'>--选择参数类型--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].param_code + "'> " + data.msg[i].param_name + " </option>";
                    }
                }
                $("#paramType").html(str);
            }
        });
    }
</script>
