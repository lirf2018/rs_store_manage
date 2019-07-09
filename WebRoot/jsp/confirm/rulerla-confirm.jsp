<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>规则关联管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/tabstyle.css"/>
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
<form class="form-inline definewidth m20" action="" method="get">
    规则名称： <input id="ruleName4" type="text" name="ruleName4" class="abc input-default" placeholder="" value="">&nbsp;
    名称： <input id="commonName" type="text" name="commonName" class="abc input-default" placeholder="" value="">&nbsp;
    属性： <s:select list="#{3:'卡券',2:'商品',1:'商家'}" listKey="key" cssStyle="width:110px" listValue="value" headerKey="-1"
                  headerValue="--选择类别--" value="-1" id="typeId"/>&nbsp;
    规则时效： <s:select id="timeLimit4" list="#{1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'}" listKey="key"
                    listValue="value" headerKey="" headerValue="--选择时效--" value="" cssStyle="width:140px"/>
    <button type="button" class="btn btn-primary" onclick="listdata(1,4)">查询</button> &nbsp;<p></p>
    密钥： <input id="param_key" type="password" class="abc input-default" placeholder="密钥" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="paramConfirm()">规则关联确认</button> &nbsp;
</form>
<table class="table table-bordered table-hover definewidth m10">
    <thead>
    <tr>
        <th>规则编号<input type="checkbox" name="checkAll" onclick="checkAll()"></th>
        <th>关联名称</th>
        <th>规则名称</th>
        <th>开始时间</th>
        <th>结束时间</th>
        <th>所属类型</th>
        <th>是否已确认</th>
        <th>创建时间</th>
    </tr>
    </thead>
    <tbody id="tb4">

    </tbody>
</table>
</body>
</html>
<script>
    $(document).ready(function () {
        listdata(1);//查询类型 4全部
    });

    //加载
    function listdata(currentPage) {
        var name = $("#commonName").val();
        var ruleName = $("#ruleName4").val();
        var timeLimit = $("#timeLimit4").val();
        var typeId = $("#typeId").val();

        $.ajax({
            type: "post",
            data: {ruleName: ruleName, name: name, typeId: typeId, timeLimit: timeLimit, currentPage: currentPage},
            async: true,
            cache: false,
            url: "${path}/manage/loadAllRulerel.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb4").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr>";
                    strs = strs + "<td><input type='checkbox' name='checkSub' onclick='checkSub()' value='" + obj[0] + "'>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    //是否长期有效
                    if (obj[6] == 1) {
                        strs = strs + "<td>长期有效</td>";
                    } else {
                        strs = strs + "<td>" + obj[4] + "</td>";
                    }

                    if (obj[7] == '1') {
                        strs = strs + "<td>商家</td>";
                    } else if (obj[7] == '2') {
                        strs = strs + "<td>商品</td>";
                    } else if (obj[7] == '3') {
                        strs = strs + "<td>卡券</td>";
                    } else {
                        strs = strs + "<td></td>";
                    }
// 								strs=strs+"<td>"+obj[9]+"</td>";//是否确认
                    if (obj[8] == 1) {
                        strs = strs + "<td>已确认</td>";
                    } else {
                        strs = strs + "<td style='color: red'>未确认</td>";
                    }
                    strs = strs + "<td>" + obj[5] + "</td>";
                    $("#tb4").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
                    var strs = "<tr><td colspan='20'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb4").append(strs.toString());
                } else {
                    $("#tb4").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //选择全选
    function checkAll() {
        var checkAll = $("input[name=checkAll]").is(':checked');
        if (checkAll) {
            $("input[name=checkSub]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkSub]:checkbox").prop("checked", false);
        }
    }

    function checkSub() {
        var checkSub = $("input[name=checkSub]:checkbox");
        var flag = false;
        checkSub.each(function () {
            if (false == $(this).prop("checked")) {
                flag = true;
                return false;
            }
        });
        if (!flag) {
            //如果子项全部选中,则全选选中
            $("input[name=checkAll]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkAll]:checkbox").prop("checked", false);
        }
    }

    function paramConfirm() {
        var paramKey = $("#param_key").val() + "";
        if (null == paramKey || "" == paramKey) {
            alert("密钥不能为空");
            return;
        }
        //处理选中的数据
        var ids = "";
        var checkSub = $("input[name=checkSub]:checkbox");
        checkSub.each(function () {
            if (true == $(this).prop("checked")) {
                ids = ids + this.value + ",";
            }
        });
        if ('' == ids) {
            alert("请选中要确认的关联");
            return;
        }

        $.ajax({
            type: "post",
            data: {ids: ids, paramKey: paramKey},
            async: false,
            cache: false,
            url: "${path}/manage/rulerelConfirm.action",
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
