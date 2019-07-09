<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>商品管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
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
<form class="form-inline definewidth m20 " action="index.html" method="get">
    商品名称： <input id="goodsName" type="text" class="abc input-default  " placeholder="" value="">&nbsp;
    商家： <input id="partnersName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    状态： <s:select list="#{-1:'--全部--',0:'无效'}" listKey="key" listValue="value" headerKey="1" headerValue="有效" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
</form>
<div>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td><span style="color: red;">main页默认查询30个抢购,抢购结束时间在参数管理中设置</span></td>
        </tr>
    </table>
</div>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>商品名称</th>
            <th>图片</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>抢购价</th>
            <th>库存</th>
            <th>限购数</th>
            <th>限购方式</th>
            <th>排序</th>
            <th>是否确认</th>
            <th>所属商家</th>
            <th>所属店铺</th>
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
    var currentPage;//当前页
    var index;//下标
    var totalPages;//总共页数
    var str_page;//当前页html

    //加载渠道
    function listdata(currentPage) {
        var goodsName = $("#goodsName").val();
        var partnersName = $("#partnersName").val();
        var status = $("#status").val();
        $.ajax({
            type: "post",
            data: {goodsName: goodsName, partnersName: partnersName, status: status, currentPage: currentPage},
            async: false,
            cache: false,
            url: "${path}/manage/loadTimeGoodsPage.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var obj = data.msg.list[i];
                    var strs = "";
                    strs = strs + "<tr><td>" + obj[0] + "</td>";//
                    strs = strs + "<td>" + obj[11] + "</td>";
                    strs = strs + "<td><img width='50px' height='50px' style='border: solid 1px;border-color: black;' src='" + obj[12] + "'></td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    strs = strs + "<td>" + obj[4] + "</td>";
                    strs = strs + "<td>" + obj[5] + "</td>";
//                    strs=strs+"<td>"+obj[6]+"</td>";//限购方式 1.每天一次2每月一次3.每年一次4不限购5只允许购买一次
                    if (obj[6] == 1) {
                        strs = strs + "<td>每天一次</td>";
                    } else if (obj[6] == 2) {
                        strs = strs + "<td>每月一次</td>";
                    } else if (obj[6] == 3) {
                        strs = strs + "<td>每年一次</td>";
                    } else if (obj[6] == 4) {
                        strs = strs + "<td>不限购</td>";
                    } else if (obj[6] == 5) {
                        strs = strs + "<td>只允许购买一次</td>";
                    } else {
                        strs = strs + "<td style='color: red'>未知</td>";
                    }
                    strs = strs + "<td>" + obj[7] + "</td>";
                    //是否确认
                    if (obj[8] == 1) {
                        strs = strs + "<td >已确认</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>未确认</td>";
                    }
                    strs = strs + "<td>" + obj[13] + "</td>";//所属商家
                    strs = strs + "<td>" + obj[14] + "</td>";//所属店铺

                    if (obj[10] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[9] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[15] + ")'>设置</a>|<a style='cursor:pointer;' onclick='cancelTimegoods(" + obj[15] + ")'>取消</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[9] + "</td>";
                        strs = strs + "<td></td></tr>";
                    }
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
                    var strs = "<tr><td colspan='28'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='80' style='font-size: 20px;font-weight: bold;color: red;text-align: center'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //取消抢购商品
    function cancelTimegoods(goodsId) {
        if (!confirm("取消后将无法使用,只能重新增加,是否继续删除？")) {
            return;
        }
        $.ajax({
            type: "post",
            data: {goodsId: goodsId},
            async: false,
            cache: false,
            url: "${path}/manage/timeGoodsCancel.action",
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

    //跳转到新增或者修改抢购商品页面
    function toAddOrUpdatePage(goodsId) {
        window.location.href = "${path}/manage/toAddTimeGoodsPage.action?goodsId=" + goodsId + "&pageFrom=timegoods";
    }

</script>
