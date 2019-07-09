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
    <script type="text/javascript">
        $(document).ready(function () {
            var id = '${id}';
            if (null == id || id == '') {
                id = 3;
            }
            $("#tab" + id).attr("checked", "checked");
            $(".panel").hide();
            $("#panel" + id).show();
        });

        function showPanel(id) {
            $(".panel").hide();
            $("#panel" + id).show();
        }

    </script>
</head>
<body>
<div style="text-align:center;clear:both;">
</div>
<article class="tabs">

    <input checked id="tab3" name="tabs" type="radio" value="1" class="radio" onclick="showPanel(3)">
    <label for="tab3">卡券规则</label>

    <input id="tab2" name="tabs" type="radio" value="2" class="radio" onclick="showPanel(2)">
    <label for="tab2">商品规则</label>

    <input id="tab1" name="tabs" type="radio" value="3" class="radio" onclick="showPanel(1)">
    <label for="tab1">商家规则</label>

    <input id="tab4" name="tabs" type="radio" value="4" class="radio" onclick="showPanel(4)">
    <label for="tab4">所有规则</label>


    <div class="panels">
        <div class="panel" id="panel3">
            <div>
                <form class="form-inline definewidth m20" action="" method="get">
                    卡券名称： <input id="ticketName" type="text" name="ticketName" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则名称： <input id="ruleName1" type="text" name="ruleName1" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则时效： <s:select id="timeLimit1" list="#{1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'}"
                                    listKey="key" listValue="value" headerKey="-1" headerValue="--选择时效--" value=""
                                    cssStyle="width:140px"/>
                    <button type="button" class="btn btn-primary" onclick="listdata(1,3)">查询</button> &nbsp;
                    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(3)">
                        新增卡券规则
                    </button> &nbsp;
                </form>
                <table class="table table-bordered table-hover definewidth m10">
                    <thead>
                    <tr>
                        <th>规则编号</th>
                        <th>卡券名称</th>
                        <th>规则名称</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>是否已确认</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="tb1">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="panel" id="panel2">
            <div>
                <form class="form-inline definewidth m20" action="" method="get">
                    商品名称： <input id="goodsName" type="text" name="goodsName" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则名称： <input id="ruleName2" type="text" name="ruleName2" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则时效： <s:select id="timeLimit2" list="#{1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'}"
                                    listKey="key" listValue="value" headerKey="-1" headerValue="--选择时效--" value="-1"
                                    cssStyle="width:140px"/>
                    <button type="button" class="btn btn-primary" onclick="listdata(1,2)">查询</button> &nbsp;
                    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(2)">
                        新增商品规则
                    </button> &nbsp;
                </form>
                <table class="table table-bordered table-hover definewidth m10">
                    <thead>
                    <tr>
                        <th>规则编号</th>
                        <th>商品名称</th>
                        <th>规则名称</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>是否已确认</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="tb2">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="panel" id="panel1">
            <div>
                <form class="form-inline definewidth m20" action="" method="get">
                    商家名称： <input id="shopName" type="text" name="shopName" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则名称： <input id="ruleName3" type="text" name="ruleName3" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    规则时效： <s:select id="timeLimit3" list="#{1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'}"
                                    listKey="key" listValue="value" headerKey="-1" headerValue="--选择时效--" value=""
                                    cssStyle="width:140px"/>
                    <button type="button" class="btn btn-primary" onclick="listdata(1,1)">查询</button> &nbsp;
                    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(1)">
                        新增商家规则
                    </button> &nbsp;
                </form>
                <table class="table table-bordered table-hover definewidth m10">
                    <thead>
                    <tr>
                        <th>规则编号</th>
                        <th>商家名称</th>
                        <th>规则名称</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>是否已确认</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="tb3">

                    </tbody>
                </table>
            </div>
        </div>
        <div class="panel" id="panel4">
            <div>
                <form class="form-inline definewidth m20" action="" method="get">
                    规则名称： <input id="ruleName4" type="text" name="ruleName4" class="abc input-default" placeholder=""
                                 value="">&nbsp;
                    名称： <input id="commonName" type="text" name="commonName" class="abc input-default" placeholder=""
                               value="">&nbsp;
                    属性： <s:select list="#{3:'卡券',2:'商品',1:'商家'}" listKey="key" cssStyle="width:110px" listValue="value"
                                  headerKey="-1" headerValue="--选择类别--" value="-1" id="typeId"/>&nbsp;
                    规则时效： <s:select id="timeLimit4" list="#{1:'长期有效',2:'未开始',3:'已开始(未结束)',4:'已结束',5:'限时有效'}"
                                    listKey="key" listValue="value" headerKey="" headerValue="--选择时效--" value=""
                                    cssStyle="width:140px"/>
                    <button type="button" class="btn btn-primary" onclick="listdata(1,4)">查询</button> &nbsp;
                </form>
                <table class="table table-bordered table-hover definewidth m10">
                    <thead>
                    <tr>
                        <th>规则编号</th>
                        <th>关联名称</th>
                        <th>规则名称</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>所属类型</th>
                        <th>是否已确认</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="tb4">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</article>
<input type="hidden" id="htmlId" value="1">
</body>
</html>
<script>
    $(document).ready(function () {
        listdata(1, 3);//查询类型 3卡券
        listdata(1, 2);//查询类型 2商品
        listdata(1, 1);//查询类型 1商家
        listdata(1, 4);//查询类型 4全部
    });

    //加载
    function listdata(currentPage, fromtype) {
        var ruleName = "";//
        var timeLimit = -1;//
        var name = "";//
        var typeId = -1;//
        var htmlId = 1;//用于页面展现
        if (fromtype == 1) {// 1商家
            name = $("#shopName").val();
            ruleName = $("#ruleName3").val();
            timeLimit = $("#timeLimit3").val();
            htmlId = 3;
        } else if (fromtype == 2) {// 2商品
            name = $("#goodsName").val();
            ruleName = $("#ruleName2").val();
            timeLimit = $("#timeLimit2").val();
            htmlId = 2;
        } else if (fromtype == 4) {//4全部
            name = $("#commonName").val();
            ruleName = $("#ruleName4").val();
            timeLimit = $("#timeLimit4").val();
            typeId = $("#typeId").val();
            htmlId = 4;
        } else {// 3卡券
            name = $("#ticketName").val();
            ruleName = $("#ruleName1").val();
            timeLimit = $("#timeLimit1").val();
            htmlId = 1;
        }
        $.ajax({
            type: "post",
            data: {
                ruleName: ruleName,
                name: name,
                typeId: typeId,
                timeLimit: timeLimit,
                fromtype: fromtype,
                currentPage: currentPage
            },
            async: true,
            cache: false,
            url: "${path}/manage/loadDataRuleRle.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb" + htmlId).empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    strs = strs + "<tr>";
                    strs = strs + "<td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    //是否长期有效
                    if (obj[6] == 1) {
                        strs = strs + "<td>长期有效</td>";
                    } else {
                        strs = strs + "<td>" + obj[4] + "</td>";
                    }

                    if (fromtype == 4) {
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
                    } else {
// 								strs=strs+"<td>"+obj[7]+"</td>";//是否确认
                        if (obj[7] == 1) {
                            strs = strs + "<td>已确认</td>";
                        } else {
                            strs = strs + "<td style='color: red'>未确认</td>";
                        }
                    }
                    strs = strs + "<td>" + obj[5] + "</td>";
                    strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(" + obj[0] + "," + fromtype + ")'>删除</a></td></tr>";
                    $("#tb" + htmlId).append(strs.toString());
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
                    $("#tb" + htmlId).append(strs.toString());
                } else {
                    $("#tb" + htmlId).append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //删除或者启用
    function delOrUpdate(id, typeId) {
        if (!confirm("确认删除吗")) {
            return;
        }
        $.ajax({
            type: "post",
            data: {id: id},
            async: false,
            cache: false,
            url: "${path}/manage/delRuleRel.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata(1, typeId);
                    listdata(1, 4);
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //跳转到新增或者修改页面
    function toAddOrUpdatePage(fromtype) {
        window.location.href = "${path}/manage/toAddRuleRelPage.action?fromtype=" + fromtype;
    }

</script>
