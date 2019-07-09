<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>投诉建议管理</title>
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
    联系方式： <input id="information" type="text" name="information" class="abc input-default" placeholder="" value="">&nbsp;
    处理情况： <s:select list="#{1:'已处理',0:'未处理'}" listKey="key" listValue="value" headerKey="" headerValue="--处理情况--"
                    value="" cssStyle="width:110px" id="status"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="isRead"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>标识</th>
            <th>用户标识</th>
            <th>联系方式</th>
            <th>内容</th>
            <th>是否已处理</th>
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

    //加载渠道
    function listdata(currentPage) {
        var information = $("#information").val();//
        var status = $("#status").val();//状态
        var isRead = $("#isRead").val();//
        $.ajax({
            type: "post",
            data: {information: information, status: status, isRead: isRead, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/manage/loadComplainPage",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.resultListMap.length; i++) {
                    var obj = data.msg.resultListMap[i];
                    var strs = "";
                    strs = strs + "<tr><td>" + obj.id + "</td>";
                    strs = strs + "<td>" + obj.user_id + "</td>";
                    strs = strs + "<td>" + obj.information + "</td>";
                    strs = strs + "<td>" + obj.contents + "</td>";
                    var isRead = obj.is_read;
                    var doReadHtml = "";
                    if (isRead == '1') {
                        strs = strs + "<td>已处理</td>";
                    } else {
                        strs = strs + "<td><span style='color: red;'>未处理</span></td>";
                        doReadHtml = "|<a style='cursor:pointer;'onclick='updateReadMark(" + obj.id + ")'>设置为已处理</a>";
                    }
                    var status = obj.status;
                    if (status == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj.createtime + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdateStatus(0," + obj.id + ")'>删除</a>" + doReadHtml + "</td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj.createtime + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdateStatus(1," + obj.id + ")'>启用</a>" + doReadHtml + "</td></tr>";
                    }
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.resultListMap.length > 0) {
                    var strs = "<tr><td colspan='30'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='30' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    function delOrUpdateStatus(status, id) {
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/updateStatus",
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

    function updateReadMark(id) {
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/updateReadMark",
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

</script>
