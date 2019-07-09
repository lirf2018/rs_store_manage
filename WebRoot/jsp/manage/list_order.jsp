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
    <script type="text/javascript" src="${path}/js/DatePicker/WdatePicker.js"></script>
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
    订单id： <input id="orderId" type="text" class="abc input-default" style="width: 50px" placeholder="" value="">&nbsp;
    商品id： <input id="goodsId" type="text" class="abc input-default" style="width: 50px" placeholder="" value="">&nbsp;
    商品名称： <input id="goodsName" type="text" class="abc input-default  " placeholder="" value="">&nbsp;
    订单号： <input id="orderNo" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    用户姓名： <input id="userName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    <p></p>
    用户手机： <input id="phone" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    业务类型： <s:select list="#{-1:'--选择业务类型--'}" listKey="key" cssStyle="width:130px" listValue="value" value="-1"
                    id="businessType"/>&nbsp;
    订单状态： <s:select list="#{-1:'--选择订单状态--'}" listKey="key" cssStyle="width:130px" listValue="value" value="-1"
                    id="orderStatus"/>&nbsp;
    预付款支付方式： <s:select list="#{-1:'--选择支付方式--',0:'现金付款',1:'微信',2:'支付宝',4:'账户余额'}" listKey="key" cssStyle="width:130px"
                       listValue="value" value="-1" id="adPayType"/>&nbsp;
    <p></p>
    商家名称： <input id="partnersName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    下单时间：
    <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text" id="beginTime" placeholder=""
           pattern="yyyy-MM-dd"/> --
    <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text" id="endTime" placeholder=""
           value="" pattern="yyyy-MM-dd"/> &nbsp;
    取货方式： <s:select list="#{1:'邮寄',4:'自取',5:'配送'}" listKey="key" listValue="value" headerKey="" headerValue="--选择取货方式--"
                    value=""
                    cssStyle="width:130px" id="getWay"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button>
    &nbsp;
    &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>订单标识</th>
            <th>用户标识</th>
            <th>订单号</th>
            <th>姓名</th>
            <th>手机号</th>
            <th>订购商品数</th>
            <th>预付款</th>
            <th>待付款</th>
            <th>快递费</th>
            <th>优惠金额</th>
            <th>订单支付总价</th>
            <th>订单实际价格</th>
            <%--<th>退款金额</th>--%>
            <th>预付款支付方式</th>
            <th>业务类型</th>
            <th>订单状态</th>
            <th>商家名称</th>
            <th>取货方式</th>
            <th>下单时间</th>
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
        var orderId = $("#orderId").val();
        var goodsId = $("#goodsId").val();
        var goodsName = $("#goodsName").val();
        var orderNo = $("#orderNo").val();
        var userName = $("#userName").val();
        var phone = $("#phone").val();
        var businessType = $("#businessType").val();
        var orderStatus = $("#orderStatus").val();
        var partnersName = $("#partnersName").val();
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var adPayType = $("#adPayType").val();
        var getWay = $("#getWay").val();
        $.ajax({
            type: "post",
            data: {
                orderId: orderId,
                goodsId: goodsId,
                goodsName: goodsName,
                orderNo: orderNo,
                userName: userName,
                phone: phone,
                businessType: businessType,
                orderStatus: orderStatus,
                partnersName: partnersName,
                beginTime: beginTime,
                endTime: endTime,
                adPayType: adPayType,
                getWay: getWay,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadOrderDataList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                var resultListMap = data.msg.resultListMap;
                for (var i = 0; i < resultListMap.length; i++) {
                    var strs = "";
                    var obj = resultListMap[i];
                    strs = strs + "<tr style='cursor:pointer;' onclick='showDetail(" + obj.order_id + ")'><td>" + obj.order_id + "</td>";//订单标识
                    strs = strs + "<td>" + obj.user_id + "</td>";//用户标识
                    strs = strs + "<td>" + obj.order_no + "</td>";//订单号
                    strs = strs + "<td>" + obj.user_name + "</td>";//姓名
                    strs = strs + "<td>" + obj.user_phone + "</td>";//手机号
                    strs = strs + "<td>" + obj.goods_count + "</td>";//订购商品数
                    strs = strs + "<td>" + obj.advance_price + "</td>";//预付款
                    strs = strs + "<td>" + obj.needpay_price + "</td>";//待付款
                    strs = strs + "<td>" + obj.post_price + "</td>";//快递费
                    strs = strs + "<td>" + obj.discounts_price + "</td>";//优惠金额
                    strs = strs + "<td>" + obj.real_price + "</td>";//订单总价
                    strs = strs + "<td>" + obj.order_price + "</td>";//实际支付价格
                    strs = strs + "<td>" + obj.advance_pay_way_name + "</td>";//预付款支付方式
                    strs = strs + "<td>" + obj.business_type_name + "</td>";//业务类型
                    strs = strs + "<td>" + obj.order_status_name + "</td>";//订单状态
                    strs = strs + "<td>" + obj.partners_name + "</td>";//商家名称
                    if (obj.post_way == '1') {
                        strs = strs + "<td>邮寄</td>";//取货方式 1.邮寄4.自取5配送
                    } else if (obj.post_way == '4') {
                        strs = strs + "<td>自取</td>";//取货方式 1.邮寄4.自取5配送
                    } else {
                        strs = strs + "<td>配送</td>";//取货方式 1.邮寄4.自取5配送
                    }
                    strs = strs + "<td>" + obj.createtime + "</td>";//下单时间
                    strs = strs + "<td><a style='cursor:pointer;' onclick='toOrderDetailPage(" + obj.order_id + ")'>详情</a></td></tr>"

                    strs = strs + "<tr  id='detail" + obj.order_id + "' style='cursor:pointer;display: none'><td colspan='30'>";
                    strs = strs + "<table class='table table-bordered table-hover definewidth m10' style='font-size: 14px'>";
                    strs = strs + "<thead>";
                    strs = strs + "<tr>";
                    strs = strs + "<th>详情标识</th>";
                    strs = strs + "<th>商品标识</th>";
                    strs = strs + "<th>商品名称</th>";
                    strs = strs + "<th>规格编码</th>";
                    strs = strs + "<th>规格名称</th>";
                    strs = strs + "<th>购买数量</th>";
                    strs = strs + "<th>销售价</th>";
                    <!--现价-->
                    strs = strs + "<th>抢购价</th>";
                    strs = strs + "<th>总押金</th>";
                    strs = strs + "<th>详情状态</th>";
                    strs = strs + "<th>是否卡券</th>";
                    strs = strs + "<th>是否抢购</th>";
                    strs = strs + "<th>取货时间</th>";
                    // strs = strs + "<th>领取时间</th>";
                    strs = strs + "<th>归还时间</th>";
                    strs = strs + "</tr>";
                    strs = strs + "</thead>";
                    strs = strs + "<tbody id='tbd" + obj.order_id + "'>";

                    strs = strs + "</tbody>";
                    strs = strs + "</table>";
                    strs = strs + "</td></tr>";

                    strs = strs + "<tr><td colspan='50'></td></tr>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (resultListMap.length > 0) {
                    var strs = "<tr><td colspan='28'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='80' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //跳转到新增或者修改页面
    function toOrderDetailPage(id) {
        window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + id;
    }

    function showDetail(id) {
        $.ajax({
            type: "post",
            data: {order_id: id},
            async: false,
            cache: false,
            url: "${path}/manage/loadOrderDetailListMap",
            dataType: "json",
            success: function (data) {
                var msg = data.msg.listDetail;//详情列表
                var msgPorp = data.msg.listDetailProp;//详情属性列表

                $("#tbd" + id).empty();
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        var str = "<tr>";
                        str = str + "<td>" + msg[i].detail_id + "</td>";
                        str = str + "<td>" + msg[i].goods_id + "</td>";
                        str = str + "<td>" + msg[i].goods_name + "</td>";
                        str = str + "<td>" + msg[i].goods_spec + "</td>";
                        str = str + "<td>" + msg[i].goods_spec_name + "</td>";
                        str = str + "<td>" + msg[i].goods_count + "</td>";
                        str = str + "<td>" + msg[i].sale_money + "</td>";
                        str = str + "<td>" + msg[i].time_price + "</td>";
                        str = str + "<td>" + msg[i].deposit_price + "</td>";
                        str = str + "<td>" + msg[i].detail_status_name + "</td>";
                        str = str + "<td>" + msg[i].is_ticket_name + "</td>";
                        str = str + "<td>" + msg[i].is_time_goods_name + "</td>";
                        // str = str + "<td>" + msg[i].get_goods_date + "</td>";
                        str = str + "<td>" + msg[i].get_time + "</td>";
                        str = str + "<td>" + msg[i].back_time + "</td>";
                        str = str + "</tr>";
                        var detailId = msg[i].detail_id;
                        var index = 0;
                        for (var j = 0; j < msgPorp.length; j++) {
                            var detailId_ = msgPorp[j].detail_id;
                            if (detailId == detailId_) {
                                if (index != 1) {
                                    //详情属性
                                    str = str + "<tr><td colspan='30'>";
                                    str = str + "<table class='table table-bordered table-hover definewidth m10' style='font-size: 14px'>";
                                    str = str + "<thead>";
                                    str = str + "<tr>";
                                    str = str + "<th>详情属性标识</th>";
                                    str = str + "<th>属性key</th>";
                                    str = str + "<th>属性value</th>";
                                    str = str + "</tr>";
                                    str = str + "</thead>";
                                    str = str + "<tbody>";
                                    index = 1;
                                }
                                str = str + "<tr><td>" + msgPorp[j].property_id + "</td>";
                                str = str + "<td>" + msgPorp[j].property_key + "</td>";
                                str = str + "<td>" + msgPorp[j].property_value + "</td></tr>";
                            }
                        }
                        if (index == 1) {
                            str = str + "</tbody>";
                            str = str + "</table>";
                            str = str + "</td></tr>";
                        }
                        str = str + "<tr><td colspan='50'></td></tr>";
                        $("#tbd" + id).append(str.toString());

                    }
                } else {
                    str = str + "<tr><td colspan='20' style='font-size: 11px;color: red'>暂时没有数据</td></tr>";
                    $("#tbd" + id).append(str.toString());
                }
            }
        });
        $("#detail" + id).toggle();
    }

    listOrderBusinessTypeParam();

    //加载列表
    function listOrderBusinessTypeParam() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/manage/listOrderBusinessTypeParam.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value=''>--业务类型--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].param_key + "'> " + data.msg[i].param_value + " </option>";
                    }
                }
                $("#businessType").html(str);
            }
        });
    }

    listOrderStatusParam();

    //加载列表
    function listOrderStatusParam() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/manage/listOrderStatusParam.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value=''>--订单状态--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].param_key + "'> " + data.msg[i].param_value + " </option>";
                    }
                }
                $("#orderStatus").html(str);
            }
        });
    }


</script>
