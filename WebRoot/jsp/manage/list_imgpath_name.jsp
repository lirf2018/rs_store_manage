<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>图片地址管理</title>
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
    图片名称： <input style="width: 260px" id="imgPathName" type="text" name="imgPathName" class="abc input-default"
                 placeholder="" value="">&nbsp;
    状态： <s:select list="#{1:'使用中',0:'已创建',2:'已移除'}" listKey="key" listValue="value" headerKey="-1"
                  headerValue="--选择状态--" value="" cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" onclick="refushUseStatus(0,2)">批量刷新使用状态</button> &nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-success" onclick="removeImg(0,2)">批量移除</button> &nbsp;&nbsp;&nbsp;
    <button type="button" class="btn btn-success" onclick="delImg(0,2)">批量删除</button> &nbsp;<br/><br/>
    指定删除图片地址名称： <input style="width: 260px" id="typeDeleImg" type="text" name="typeDeleImg" class="abc input-default"
                       placeholder="" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="delImg(0,1)">指定删除</button> &nbsp;
    指定移除图片地址名称： <input style="width: 260px" id="typeRemoveImg" type="text" name="typeRemoveImg"
                       class="abc input-default" placeholder="" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="removeImg(0,1)">指定移除</button> &nbsp;
    &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th><input type="checkbox" onclick='choseCheckParent(this)' name="selectAll">全选</th>
            <th>标识</th>
            <th>图片地址</th>
            <th>所在表名称</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>大小(≈KB)</th>
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
        $("input[type='checkbox']").attr("checked", false);
        var imgPathName = $("#imgPathName").val() + "";//图片地址
        var status = $("#status").val();//状态
        $.ajax({
            type: "post",
            data: {imgPathName: imgPathName.trim(), status: status, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/manage/loadImgRecordList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var obj = data.msg.list[i];
                    var strs = "";
                    strs = strs + "<tr><td><input onclick='choseCheckSub(this)' type='checkbox' name='selectSub' value='" + obj[0] + "'></td>";
                    strs = strs + "<td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    var s = obj[3];
                    if (s == '0') {
                        strs = strs + "<td>已创建</td>";//状态
                    } else if (s == '1') {
                        strs = strs + "<td>使用中</td>";//状态
                    } else if (s == '2') {
                        strs = strs + "<td>已移除</td>";//状态
                    } else {
                        strs = strs + "<td>未知</td>";//状态
                    }
                    strs = strs + "<td>" + obj[4] + "</td>";
                    strs = strs + "<td>" + obj[5] + "</td>";
                    strs = strs + "<td>" + obj[6] + "</td>";
                    strs = strs + "<td><a style='cursor:pointer;' onclick='delImg(" + obj[0] + ",0)'>删除图片</a>|<a style='cursor:pointer;' onclick='removeImg(" + obj[0] + ",0)'>移除图片</a>|<a style='cursor:pointer;'onclick='refushUseStatus(" + obj[0] + ",0)'>刷新使用状态</a></td></tr>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
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

    //删除 type:0 单条 1指定 2 批量
    function delImg(id, type) {
        var ids = id;
        if (!confirm("图片会彻底删除,确认删除吗")) {
            return;
        }
        var typeDeleImg = "";
        if (type == 1) {
            typeDeleImg = $("#typeDeleImg").val() + "";
            if (typeDeleImg.trim() == '') {
                alert("指定删除图片地址名称不能为空")
                return;
            }
        } else if (type == 2) {
            ids = getSelectData();
            if (ids == ',' || ids == '') {
                alert("未选择要操作的数据")
                return;
            }
        }

        $.ajax({
            type: "post",
            data: {ids: ids, typeDeleImg: typeDeleImg, type: type},
            async: false,
            cache: false,
            url: "${path}/manage/delImg",
            dataType: "json",
            success: function (data) {
                if (data.flag == 1) {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //移除,不会删除 type:0 单条 1指定 2 批量
    function removeImg(id, type) {
        var ids = id;
        var typeRemoveImg = "";
        if (type == 1) {
            typeRemoveImg = $("#typeRemoveImg").val() + ""
            if (typeRemoveImg.trim() == '') {
                alert("指定移除图片地址名称不能为空")
                return;
            }
        } else if (type == 2) {
            ids = getSelectData();
            if (ids == ',' || ids == '') {
                alert("未选择要操作的数据")
                return;
            }
        }

        $.ajax({
            type: "post",
            data: {ids: ids, typeRemoveImg: typeRemoveImg, type: type},
            async: false,
            cache: false,
            url: "${path}/manage/removeImg",
            dataType: "json",
            success: function (data) {
                if (data.flag == 1) {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //type:0 单条  2 批量
    function refushUseStatus(id, type) {
        var ids = id;
        if (type == 2) {
            ids = getSelectData();
            if (ids == ',' || ids == '') {
                alert("未选择要操作的数据")
                return;
            }
        }
        $.ajax({
            type: "post",
            data: {ids: ids},
            async: false,
            cache: false,
            url: "${path}/manage/refushUseStatus",
            dataType: "json",
            success: function (data) {
                if (data.flag == 1) {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //selectAll
    //selectSub
    function choseCheckParent(obj) {
        if ($(obj).is(':checked')) {
            $("input[name='selectSub']").attr("checked", true);
        } else {
            $("input[name='selectSub']").attr("checked", false);
        }
    }

    //selectAll
    //selectSub
    function choseCheckSub(obj) {
        var subObj = $("input[name='selectSub']");
        var count = 0;
        subObj.each(function () {
            if ($(this).is(':checked')) {
                count = count + 1;
            }
        });
        $("input[name='selectAll']").attr("checked", true);
        if (count == 0) {
            $("input[name='selectAll']").attr("checked", false);
        }
    }

    function getSelectData() {
        var ids = "";
        var subObj = $("input[name='selectSub']");
        subObj.each(function () {
            if ($(this).is(':checked')) {
                ids = ids + $(this).val() + ",";
            }
        });
        return ids;
    }
</script>
