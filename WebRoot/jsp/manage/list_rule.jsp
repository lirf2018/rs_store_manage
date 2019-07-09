<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>商品规则管理</title>
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

        form {
            margin-bottom: 50px;
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
    规则编码： <input id="ruleCode" type="text" name="ruleCode" class="abc input-default" placeholder="" value=""
                 style="width: 110px;">&nbsp;
    规则名称： <input id="ruleName" type="text" name="ruleName" class="abc input-default" placeholder="" value=""
                 style="width: 130px;">&nbsp;
    规则类型： <s:select id="ruleType" list="#{0:'商家规则',1:'商品规则',2:'卡券规则'}" listKey="key" listValue="value" headerKey=""
                    headerValue="--选择规则类型--" value="" cssStyle="width:130px"/>&nbsp;
    商家名称： <input id="shopName" type="text" name="shopName" class="abc input-default" placeholder="" value="">&nbsp;
    状态： <s:select id="status" list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey=""
                  headerValue="--选择状态--" value="" cssStyle="width:110px"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增规则</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>规则编号</th>
            <th>规则名称</th>
            <th>规则编码</th>
            <th>规则类型</th>
            <th>规则所属</th>
            <th>是否已确认</th>
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
        var ruleName = $("#ruleName").val();//详细地址
        var ruleCode = $("#ruleCode").val();//状态
        var ruleType = $("#ruleType").val();//
        var shopName = $("#shopName").val();//前缀
        var status = $("#status").val();//
        $.ajax({
            type: "post",
            data: {
                ruleName: ruleName,
                ruleCode: ruleCode,
                ruleType: ruleType,
                shopName: shopName,
                status: status,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadRuleListPage.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr>";
                    strs = strs + "<td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[8] + "</td>";
                    strs = strs + "<td>" + obj[9] + "</td>";
                    if (obj[11]) {
                        strs = strs + "<td>已确认</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>未确认</td>";
                    }
                    if (obj[10] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[7] + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[7] + "</td>";
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
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delRulestatus.action",
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
            window.location.href = "${path}/manage/toaddorupdaterulepage.action?op=add";
        } else {//修改
            window.location.href = "${path}/manage/toaddorupdaterulepage.action?op=update&id=" + id;
        }
    }
</script>
