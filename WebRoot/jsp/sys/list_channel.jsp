<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>渠道管理</title>
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
    渠道名称： <input id="searchName" type="text" name="menuname" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="state"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="addOrEditDialog(0,0)">新增渠道</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>渠道名称</th>
            <th>编码</th>
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
        <input type="hidden" value="${channel.channelId}" id="channelId" name="channel.channelId">
        <input type="hidden" value="add" id="handleType" name="handleType">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>渠道名称:</td>
                <td><input type="text" value="${channel.channelName}"
                           onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')" id="name"
                           name="channel.channelName"></td>
            </tr>
            <tr>
                <td>编码:</td>
                <td><input type="text" value="${channel.channelCode}"
                           onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')" id="code"
                           name="channel.channelCode"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<script>
    listdata(1);
    var dataList;

    //加载渠道
    function listdata(currentPage) {
        dataList = new Array();
        var searchName = $("#searchName").val();
        var state = $("#state").val();
        $.ajax({
            type: "post",
            data: {searchName: searchName, state: state, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/channel/loadlistChannel.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                dataList.length = 0;
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    var dataList_value = obj[0] + "," + obj[1] + "," + obj[2];
                    dataList.push(dataList_value);
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    if (obj[4] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[3] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdatechannel(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='addOrEditDialog(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[3] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdatechannel(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='addOrEditDialog(" + obj[0] + ",1)'>查看/编辑</a></td></tr>";
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
    function delOrUpdatechannel(status, channelid) {
        $.ajax({
            type: "post",
            data: {channelid: channelid, status: status},
            async: false,
            cache: false,
            url: "${path}/channel/delOrUpdateChannel.action",
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
            height: 230,
            width: 600,
            title: "查看/编辑",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    if ($("#name").val() == "") {
                        alert("输入渠道名称!");
                        return;
                    }
                    ;
                    if ($("#code").val() == "") {
                        alert("输入编码!");
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
            data: $("#submit_form").serialize(),
            async: false,
            cache: false,
            url: "${path}/channel/addOrUpdateChannel.action",
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
            //channel,chaneel_name,chaneel_code,state,creattime
            for (var i = 0; i < dataList.length; i++) {
                var str = dataList[i].split(",");
                var id = str[0];
                var name = str[1];
                var code = str[2];
                if (id == id_) {
                    $("#channelId").val(id);
                    $("#name").val(name);
                    $("#code").val(code);
                    $("#handleType").val("update");
                    return;
                }
            }
        } else {//新增
            $("#channelId").val("");
            $("#name").val("");
            $("#code").val("");
            $("#handleType").val("add");
        }
    }
</script>
