<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>卡券管理</title>
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
    卡券名称： <input id="tikcetName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    商家： <input id="pName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    类型： <s:select list="#{1:'实体卡券',0:'虚拟卡券'}" listKey="key" cssStyle="width:110px" listValue="value" headerKey=""
                  headerValue="--选择类型--" value="" id="ticketType"/>&nbsp;
    <p></p>
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    是否显示： <s:select list="#{0:'隐藏',1:'显示'}" listKey="key" cssStyle="width:140px" listValue="value" headerKey=""
                    headerValue="--选择显示状态--" value="" id="isShow"/>&nbsp;
    卡券状态： <s:select list="#{1:'长期有效卡券',2:'未开始卡券',3:'已开始卡券(未结束)',4:'已结束卡券',5:'限时有效卡券',6:'兑换已截至'}" listKey="key"
                    listValue="value" headerKey="" headerValue="--选择卡券状态--" value="" cssStyle="width:140px"
                    id="ticketStatus"/>&nbsp;
    <p></p>
    一级分类： <s:select list="#{0:'--选择分类--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="level"
                    onchange="changeCatogry()"/>&nbsp;
    类目： <s:select list="#{0:'--选择类目--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="catogry"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    密钥： <input id="param_key" type="password" class="abc input-default" placeholder="密钥" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="paramConfirm()">卡券上架确认</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号<input type="checkbox" name="checkAll" onclick="checkAll()"></th>
            <th>名称</th>
            <th>图片</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>数量</th>
            <th>类型</th>
            <th>卡券面值</th>
            <th>是否显示</th>
            <th>上架状态</th>
            <th>兑换过期时间</th>
            <th>排序</th>
            <th>所属于店铺</th>
            <th>商家</th>
            <th>类目</th>
            <th>分类</th>
            <th>状态</th>
            <th>创建时间</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
    <!-- 		<img width="100px" height="100px" style="border: solid 6px;border-color: black;"  src="http://pic1a.nipic.com/2008-12-04/2008124215522671_2.jpg"> -->
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
        var isShow = $("#isShow").val();
        var ticketType = $("#ticketType").val();
        var tikcetName = $("#tikcetName").val();
        var ticketStatus = $("#ticketStatus").val();
        var pName = $("#pName").val();
        var status = $("#status").val();
        var leveId = $("#level").val();
        var catogryId = $("#catogry").val();
        var isPutaway = 1;

        $.ajax({
            type: "post",
            data: {
                isShow: isShow,
                ticketType: ticketType,
                tikcetName: tikcetName,
                pName: pName,
                ticketStatus: ticketStatus,
                leveId: leveId,
                catogryId: catogryId,
                isPutaway: isPutaway,
                status: status,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadTicketDataList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    //编号-名称-图片-开始时间-结束时间-现价-销售状态-库存-排序-属性-类型-是否单品-状态-创建时间-操作
                    var obj = data.msg.list[i];
                    strs = strs + "<tr><td><input type='checkbox' name='checkSub' onclick='checkSub()' value='" + obj[0] + "'>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td><img width='150px' height='150px' style='border: solid 1px;border-color: black;' src='" + obj[2] + "'></td>";
                    strs = strs + "<td>" + obj[3] + "</td>";//开始时间
                    if (obj[13] == "1") {
                        strs = strs + "<td>长期有效</td>";//结束时间
                    } else {
                        strs = strs + "<td>" + obj[4] + "</td>";//结束时间
                    }

                    strs = strs + "<td>" + obj[5] + "</td>";//
                    if (obj[7] == '1') {//1:代金券2优惠券',
                        strs = strs + "<td>代金券</td>";
                        strs = strs + "<td>" + obj[8] + "</td>";//
                    } else {
                        strs = strs + "<td>优惠券</td>";
                        strs = strs + "<td>--</td>";//
                    }

                    if (obj[9] == '0') {
                        strs = strs + "<td style='color: red;'>隐藏</td>";
                    } else {
                        strs = strs + "<td>显示</td>";
                    }
                    if (obj[18] == '0') {
                        strs = strs + "<td style='color: red;'>已下架</td>";//销售状态
                    } else if (obj[18] == '1') {
                        strs = strs + "<td style='color: red;'>等待上架确认</td>";//销售状态
                    } else if (obj[18] == '2') {
                        strs = strs + "<td>已上线</td>";//销售状态
                    } else {
                        strs = strs + "<td style='color: red;'>未知</td>";
                    }

                    if (obj[13] == "1") {
                        strs = strs + "<td>长期有效</td>";//兑换过期时间
                    } else {
                        strs = strs + "<td>" + obj[10] + "</td>";//
                    }
                    strs = strs + "<td>" + obj[6] + "</td>";//排序
                    strs = strs + "<td>" + obj[15] + "</td>";//所属店铺
                    strs = strs + "<td>" + obj[14] + "</td>";//商家
                    strs = strs + "<td>" + obj[16] + "</td>";//类目
                    strs = strs + "<td>" + obj[17] + "</td>";//分类
                    if (obj[11] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj[12] + "</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj[12] + "</td>";

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

    listChannel();

    //加载渠道列表
    function listChannel() {
        $.ajax({
            type: "post",
            data: {param_code: 'goods_type'},
            async: false,
            cache: false,
            url: "${path}/param/listParamMap.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value='' selected='selected'>--选择类型--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].param_key + "'> " + data.msg[i].param_value + " </option>"
                    }
                }
                $("#goods_type").html(str);
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
            alert("请选中要确认的卡券");
            return;
        }

        $.ajax({
            type: "post",
            data: {ids: ids, paramKey: paramKey},
            async: false,
            cache: false,
            url: "${path}/manage/ticketConfirm.action",
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
