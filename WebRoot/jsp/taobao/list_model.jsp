<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>taobao页面管理</title>
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
    名称： <input id="name" type="text" class="abc input-default" placeholder="" value="">&nbsp;&nbsp;
    图片数量： <input id="picCount" type="text" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/[^\d]/g,'')">&nbsp;&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;&nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增模板</button> &nbsp;&nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>名称</th>
            <th>模板预览图片</th>
            <th>包含图片数量</th>
            <th>修改模板</th>
            <th>配置数据</th>
            <th>配置结果</th>
            <th>跳转路径</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
    <!-- 		<img width="100px" height="100px" style="border: solid 6px;border-color: black;"  src="http://pic1a.nipic.com/2008-12-04/2008124215522671_2.jpg"> -->
</div>
<!-- dialog style="display: none;"-->
<div id="result" style="display: none;">
    <input type="hidden" value="add" id="handleType" name="handleType">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <!-- 					<td><label id="result_label"></label></td> -->
            <td><textarea style="height: 420px;width: 600px;max-width:600px;max-height: 420px " readonly="readonly"
                          id="result_code"></textarea></td>
        </tr>
    </table>
</div>
<!-- dialog img style="display: none;"-->
<div id="resultImg" style="display: none;">
    <input type="hidden" value="add" id="handleType" name="handleType">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td><img id="img" src="" width="500px" height="400px"></td>
        </tr>
    </table>
</div>
</body>
</html>
<script>
    listdata(1);

    //加载渠道
    function listdata(currentPage) {
        var name = $("#name").val();//
        var picCount = $("#picCount").val();
        $.ajax({
            type: "post",
            data: {name: name, currentPage: currentPage, picCount: picCount, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/taobao/loadTaobaoList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                var k = 1;
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr><td>" + k + "</td>";
                    k = k + 1;
                    strs = strs + "<td>" + obj[1] + "<input type='hidden' value='" + obj[1] + "' id='name_" + obj[0] + "'/></td>";
                    strs = strs + "<td><img  width='100px' onclick='resultImg(" + obj[0] + ")' height='100px' style='cursor:pointer;border: solid 1px;border-color: black;' src='" + obj[4] + "'><input id='img" + obj[0] + "' type='hidden' value='" + obj[4] + "'></td>";
                    strs = strs + "<td>" + (obj[8] == "" ? "0" : +obj[8]) + "张</td>";
                    strs = strs + "<td><a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td>";
                    strs = strs + "<td><a style='cursor:pointer;' onclick='toSubUpdatePage(" + obj[0] + ")'>配置数据</a><input id='" + obj[0] + "' type='hidden' value='" + obj[5] + "'></td>";
                    strs = strs + "<td><a style='cursor:pointer;'onclick='result(" + obj[0] + ")'>查看配置结果代码</a><input id='result" + obj[0] + "' type='hidden' value='" + obj[3] + "'></td>";
                    strs = strs + "<td>" + obj[5] + "</td>";
                    strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a></td>";
                    strs = strs + "</tr>";
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
            url: "${path}/taobao/delModel.action",
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
            window.location.href = "${path}/taobao/toAddOrUpdatePage.action?handleType=" + handleType;
        } else {//修改
            var handleType = "update";
            window.location.href = "${path}/taobao/toAddOrUpdatePage.action?handleType=" + handleType + "&id=" + id;
        }
    }

    //跳转到具体模板修改页面
    function toSubUpdatePage(id) {
        window.location.href = "${path}/" + $("#" + id).val() + "&id=" + id;
    }

    //查看配置结果代码
    function result(id) {
        $("#result_code").val("");
        var result = $("#result" + id).val();
        result = result.replace(/_##/g, "&");
        result = result.replace(/_#/g, ";");
        $("#result_code").val(result);
        $("#result").dialog({
            autoOpen: true,
            height: 550,
            width: 700,
            title: "查看配置结果",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "取消": function () {
                    $("#result").dialog("close");
                }
            }
        });
    }

    //查看图片
    function resultImg(id) {
        $("#img").attr("src", $("#img" + id).val());
        $("#resultImg").dialog({
            autoOpen: true,
            height: 500,
            width: 750,
            title: $("#name_" + id).val() + "图片示例",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "取消": function () {
                    $("#resultImg").dialog("close");
                }
            }
        });
    }


</script>
