<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单详情</title>
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

        @media (max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }
    </style>
    <style type="text/css">
        #preview {
            width: 100px;
            height: 100px;
            border: 1px solid #000;
            overflow: hidden;
        }

        #imghead {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);
        }
    </style>
</head>
<body>
<form action="" method="post" class="definewidth m20" id="form_submit">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单号</td>
            <td>${listOrder[0].order_no}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">用户标识</td>
            <td>${listOrder[0].user_id}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单标识</td>
            <td>${listOrder[0].order_id}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单支付总价</td>
            <td>${listOrder[0].order_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单预付款</td>
            <td>${listOrder[0].advance_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">下单来源</td>
            <td>${listOrder[0].order_from}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单实际价格</td>
            <td>${listOrder[0].real_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订单待付款</td>
            <td>${listOrder[0].needpay_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">商家id</td>
            <td>${listOrder[0].partners_id}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">收货人姓名</td>
            <td>${listOrder[0].user_name}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">收货人手机号</td>
            <td>${listOrder[0].user_phone}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">用户地址标识</td>
            <td>${listOrder[0].user_addr_id}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">收货人地址</td>
            <td colspan="10">${listOrder[0].user_addr}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">预付支付时间</td>
            <td>${listOrder[0].advance_pay_time}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">支付时间</td>
            <td>${listOrder[0].pay_time}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">取货方式</td>
            <td>${listOrder[0].post_way_name}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">预付支付方式</td>
            <td>${listOrder[0].advance_pay_way_name}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">支付方式</td>
            <td>${listOrder[0].pay_way_name}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">快递公司</td>
            <td>${postCompName}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">预付支付交易号</td>
            <td>${listOrder[0].advance_pay_code}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">支付交易号</td>
            <td>${listOrder[0].pay_code}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">配送人员</td>
            <td>${listOrder[0].post_man}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">快递费</td>
            <td>${listOrder[0].post_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">快递单号</td>
            <td>${listOrder[0].post_no}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">订购商品数</td>
            <td>${listOrder[0].goods_count}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">用户备注</td>
            <td colspan="10">${listOrder[0].user_remark}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">客服备注</td>
            <td colspan="10">${listOrder[0].service_remark}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">下单时间</td>
            <td>${listOrder[0].order_time}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">发货时间</td>
            <td>${listOrder[0].post_time}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">业务类型</td>
            <td>${listOrder[0].business_type_name}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">优惠券标识</td>
            <td>${listOrder[0].discounts_id}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">优惠金额</td>
            <td>${listOrder[0].discounts_price}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">退款金额</td>
            <td>${listOrder[0].refund_price}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">商家名称</td>
            <td>${listOrder[0].partners_name}</td>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">配送人员电话</td>
            <td colspan="3">${listOrder[0].post_phone}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">退款原因</td>
            <td colspan="10">${listOrder[0].refund_remark}</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:right;vertical-align:middle;">优惠说明</td>
            <td colspan="10">${listOrder[0].discounts_remark}</td>
        </tr>
        <tr>
            <td class="tableleft" colspan="6">
                <table class="table table-bordered table-hover definewidth m10">
                    <tr>
                        <td class="tableleft" style="text-align:right;vertical-align:middle;">订单状态</td>
                        <td colspan="5">${listOrder[0].order_status_name}&nbsp;&nbsp;&nbsp;&nbsp;<span
                                onclick="showStatusRecord()" style="cursor: pointer"><u>查看详细</u></span></td>
                    </tr>
                </table>
                <div id="orderStatusRecord" style="display: none">
                    <table class="table table-bordered table-hover m10">
                        <tr>
                            <th>操作时间</th>
                            <th>操作人</th>
                            <th>备注</th>
                            <th>操作状态</th>
                        </tr>
                        <tbody>
                        <c:forEach items="${listStatusRecord}" var="item">
                            <tr>
                                <td>${item.lastaltertime}</td>
                                <td>${item.lastalterman}</td>
                                <td>${item.remark}</td>
                                <td>${item.status_name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="10" style="text-align:center;vertical-align:middle;">
                <c:choose>
                    <c:when test="${listOrder[0].order_status==0&&listOrder[0].advance_pay_way==0}">
                        <input type="hidden" value="1" id="ispaytype0">
                    </c:when>
                    <c:otherwise>
                        <input type="hidden" value="0" id="ispaytype0">
                    </c:otherwise>
                </c:choose>
                <%--0	待付款--%>
                <c:if test="${listOrder[0].order_status==0}">
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},7,'设置已取消')"
                            class="btn btn-primary" type="button">设置为已取消
                    </button>
                    &nbsp;&nbsp;
                </c:if>
                <%--1 已付款  --%>
                <c:if test="${listOrder[0].order_status==1}">
                    <button onclick="openSaveRefundOrderStatus(${listOrder[0].order_id},9,'设置退款中')"
                            class="btn btn-primary" type="button">设置为退款中
                    </button>
                    &nbsp;&nbsp;
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},4,'设置为待发货')"
                            class="btn btn-primary" type="button">设置为待发货
                    </button>
                    &nbsp;&nbsp;
                </c:if>

                <%--2	平台确认  --%>
                <c:if test="${listOrder[0].order_status==2}">
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},3,'设置为下单失败')"
                            class="btn btn-primary" type="button">设置为下单失败
                    </button>
                    &nbsp;&nbsp;
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},4,'设置为待发货')"
                            class="btn btn-primary" type="button">设置为待发货
                    </button>
                    &nbsp;&nbsp;
                </c:if>
                <%--<c:if test="${listOrder[0].order_status==2&&listOrder[0].advance_pay_way!=0}">
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},3,'设置为下单失败')" class="btn btn-primary" type="button">设置为下单失败</button> &nbsp;&nbsp;
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},4,'设置为待发货')" class="btn btn-primary" type="button">设置为待发货</button> &nbsp;&nbsp;
                </c:if>--%>
                <%-- &lt;%&ndash;2	平台确认 --当为到店付款支付的时候  &ndash;%&gt;
                 <c:if test="${listOrder[0].order_status==0&&listOrder[0].advance_pay_way==0}">
                     <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},3,'设置为下单失败')" class="btn btn-primary" type="button">设置为下单失败</button> &nbsp;&nbsp;
                     <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},4,'设置为待发货')" class="btn btn-primary" type="button">设置为待发货</button> &nbsp;&nbsp;
                 </c:if>--%>
                <%--4	待发货--%>
                <c:if test="${listOrder[0].order_status==4}">
                    <button onclick="updateOrderPostStatus(${listOrder[0].order_id},5)" class="btn btn-primary"
                            type="button">发货
                    </button>
                    &nbsp;&nbsp;
                </c:if>
                <%--5	待收货--%>
                <c:if test="${listOrder[0].order_status==5}">
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},6,'设置为已完成')"
                            class="btn btn-primary" type="button">设置为已完成
                    </button>
                    &nbsp;&nbsp;
                    <button onclick="javaScript:openSaveOrderStatus(${listOrder[0].order_id},7,'设置为已取消')"
                            class="btn btn-primary" type="button">设置为已取消
                    </button>
                    &nbsp;&nbsp;
                </c:if>
                <%--9	退款中--%>
                <c:if test="${listOrder[0].order_status==9}">
                    <button onclick="openSaveRefundOrderStatus(${listOrder[0].order_id},10)" class="btn btn-primary"
                            type="button">设置已退款
                    </button>
                    &nbsp;&nbsp;
                </c:if>
                <button type="button" class="btn btn-success" id="updateOrderStatus"
                        onclick="javascript:openUpdateOrderInfo(${listOrder[0].order_id})">修改订单信息
                </button>&nbsp;&nbsp;
            </td>
        </tr>
    </table>
    <table class="table table-bordered table-hover m10">
        <tr>
            <th class="tableleft" style="text-align:center;vertical-align:middle;">订单详情</th>
        </tr>
    </table>
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <th>详情标识</th>
            <th>商品标识</th>
            <th>商品名称</th>
            <th>规格编码</th>
            <th>规格名称</th>
            <th>购买数量</th>
            <th>销售价</th>
            <th>原价</th>
            <th>成本价</th>
            <th>抢购价</th>
            <th>押金</th>
            <th>店铺名称</th>
            <th>领取地址</th>
            <th>领取时间</th>
            <th>归还地址</th>
            <th>归还时间</th>
            <th>详情状态</th>
            <th>是否卡券</th>
            <th>是否抢购</th>
            <th>领取时间</th>
            <th>抢购商品标识</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${listDetail}" var="item">
            <tr>
                <td>${item.detail_id}</td>
                <td>${item.goods_id}</td>
                <td>${item.goods_name}</td>
                <td>${item.goods_spec}</td>
                <td>${item.goods_spec_name}</td>
                <td>${item.goods_count}</td>
                <td>${item.sale_money}</td>
                <td>${item.goods_true_money}</td>
                <td>${item.goods_purchase_price}</td>
                <td>${item.time_price}</td>
                <td>${item.deposit_price}</td>
                <td>${item.shop_name}</td>
                <td>${item.get_addr_name}</td>
                <td>${item.get_time}</td>
                <td>${item.back_addr_name}</td>
                <td>${item.back_time}</td>
                <td>${item.detail_status_name}</td>
                <td>${item.is_ticket_name}</td>
                <td>${item.is_time_goods_name}</td>
                <td>${item.get_goods_date}</td>
                <td>${item.time_goods_id}</td>
                <td>
                    <!--0 未取货 -->
                    <c:if test="${item.detail_status=='0'}">
                        <button onclick="updateDetailStatus(${listOrder[0].order_id},${item.detail_id},1)"
                                class="btn btn-primary" type="button">设置已取货
                        </button>
                        <button onclick="updateDetailStatus(${listOrder[0].order_id},${item.detail_id},2)"
                                class="btn btn-primary" type="button">设置已取消
                        </button>
                    </c:if>
                    <!--1 已取货 -->
                    <c:if test="${item.detail_status=='1'}">
                        <button onclick="updateDetailStatus(${listOrder[0].order_id},${item.detail_id},3)"
                                class="btn btn-primary" type="button">设置已完成
                        </button>
                        <button onclick="updateDetailStatus(${listOrder[0].order_id},${item.detail_id},4)"
                                class="btn btn-primary" type="button">设置已退货
                        </button>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="35">
                    <c:if test="${!empty item.tp_detail_id}">
                        <table class="table table-bordered table-hover definewidth m10">
                            <th>属性值标识</th>
                            <th>属性值key</th>
                            <th>属性值</th>
                            <c:forEach items="${listDetailProp}" var="itemP">
                                <c:if test="${itemP.detail_id==item.tp_detail_id}">
                                    <tr>
                                        <td>${itemP.property_id}</td>
                                        <td>${itemP.property_key}</td>
                                        <td>${itemP.property_value}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </table>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td colspan="30"></td>
            </tr>
        </c:forEach>
    </table>
    <!-- dialog  发货 -->
    <div id="sendDialog" style="display: none;">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td style="vertical-align: middle;text-align: right;font-size: 12px">快递公司</td>
                <td>
                    <select id="postCompCode" style="width: 350px">
                        <option value="">--选择快递公司--</option>
                        <c:forEach items="${postComp}" var="list">
                            <c:if test="${list.param_key!='platform_service'}">
                                <c:choose>
                                    <c:when test="${list.param_key==listOrder[0].post_name}">
                                        <option value="${list.param_key}" selected>${list.param_value}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${list.param_key}">${list.param_value}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="vertical-align: middle;text-align: right;font-size: 12px">订单号</td>
                <td><input type="text" id="postNum" style="width: 350px" value="${listOrder[0].post_no}"></td>
            </tr>
        </table>
    </div>
    <!-- dialog  退款 -->
    <div id="refundDialog" style="display: none;">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td style="vertical-align: middle;text-align: right;font-size: 12px">客服备注</td>
                <td><textarea id="refundServiceRemark"
                              style="height: 100px;width:100%;resize:none;font-size: 12px">${listOrder[0].service_remark}</textarea>
                </td>
            </tr>
            <tr>
                <td style="vertical-align: middle;text-align: right;font-size: 12px">退款原因</td>
                <td><textarea id="refundRemark"
                              style="height: 100px;width:100%;resize:none;font-size: 12px">${listOrder[0].refund_remark}</textarea>
                </td>
            </tr>
        </table>
    </div>
    <!-- dialog  退款 -->
    <div id="orderStatusDialog" style="display: none;">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td style="vertical-align: middle;text-align: right;font-size: 12px">客服备注</td>
                <td><textarea id="serviceRemark"
                              style="height: 100px;width:100%;resize:none;font-size: 12px">${listOrder[0].service_remark}</textarea>
                </td>
            </tr>
        </table>
    </div>
    <!-- dialog  订单信息 -->
    <div id="orderDialog" style="display: none;">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td style="text-align: right;font-size: 14px">密钥</td>
                <td><input style="font-size: 14px" type="password" id="paramKey" value=""></td>
            </tr>
            <tr>
                <td style="text-align: right;font-size: 14px">订单状态</td>
                <td>
                    <select id="orderStatus" style="font-size: 14px">
                        <option value="-1">--选择订单状态--</option>
                        <c:forEach items="${listStatus}" var="item">
                            <c:choose>
                                <c:when test="${item.param_key==listOrder[0].order_status}">
                                    <option value="${item.param_key}" selected>${item.param_value}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${item.param_key}">${item.param_value}</option>
                                </c:otherwise>
                            </c:choose>

                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;font-size: 14px">退款金额</td>
                <td><input style="font-size: 14px" style="font-size: 14px" type="text" id="refundPrice"
                           value="${listOrder[0].refund_price}"></td>
            </tr>
            <tr>
                <td style="text-align: right;vertical-align: middle;text-align: right;font-size: 14px">退款原因</td>
                <td><textarea id="refundRemarkOrder"
                              style="height: 50px;width:100%;resize:none;font-size: 14px">${listOrder[0].refund_remark}</textarea>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;vertical-align: middle;text-align: right;font-size: 12px">客服备注</td>
                <td><textarea id="orderServiceRemark"
                              style="height: 50px;width:100%;resize:none;font-size: 12px">${listOrder[0].service_remark}</textarea>
                </td>
            </tr>
            <tr>
                <td style="text-align: right;font-size: 14px">快递人员姓名</td>
                <td><input style="font-size: 14px" type="text" id="postMan" value="${listOrder[0].post_man}"></td>
            </tr>
            <tr>
                <td style="text-align: right;font-size: 14px">快递人员电话</td>
                <td><input style="font-size: 14px" type="text" id="postPhone" value="${listOrder[0].post_phone}"></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
<script>

    //设置已退款
    function openSaveRefundOrderStatus(orderId, status) {
        $("#refundDialog").dialog({
            autoOpen: true,
            height: 350,
            width: 600,
            title: "设置为已退款",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    saveRefundOrderStatus(orderId, status)
                },
                "取消": function () {
                    $("#refundDialog").dialog("close");
                }
            }
        });
    }

    function saveRefundOrderStatus(orderId, status) {
        var refundRemark = $("#refundRemark").val();
        var serviceRemark = $("#refundServiceRemark").val();
        $.ajax({
            url: "${path}/manage/updateOrderStatus.action",
            type: 'POST',
            data: {orderId: orderId, orderStatus: status, refundRemark: refundRemark, serviceRemark: serviceRemark},//
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + orderId;
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }


    //其它修改订单状态
    function openSaveOrderStatus(orderId, status, title) {
        var ispaytype0 = $("#ispaytype0").val();
        if (ispaytype0 == 1 && status == 4) {//到店付款支付
            if (!confirm("请确认收到到店付款全款后再发货")) {
                return;
            }
        }

        $("#orderStatusDialog").dialog({
            autoOpen: true,
            height: 220,
            width: 600,
            title: title,
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    saveOrderStatus(orderId, status)
                },
                "取消": function () {
                    $("#orderStatusDialog").dialog("close");
                }
            }
        });
    }

    function saveOrderStatus(orderId, status) {
        var serviceRemark = $("#serviceRemark").val();
        $.ajax({
            url: "${path}/manage/updateOrderStatus.action",
            type: 'POST',
            data: {orderId: orderId, orderStatus: status, serviceRemark: serviceRemark},//
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + orderId;
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //发货
    function updateOrderPostStatus(orderId, status) {
        var postCompCode = "";
        var postNum = "";
        if ('1' == '${listOrder[0].post_way}') {

            $("#sendDialog").dialog({
                autoOpen: true,
                height: 200,
                width: 800,
                title: "修改订单信息",
                hide: "slide",
                show: "slide",
                position: ['center', 'buttom'],
                modal: true,//蒙层（弹出会影响页面大小）
                buttons: {
                    "保存": function () {
                        postCompCode = $("#postCompCode").val();
                        postNum = $("#postNum").val();
                        if (postCompCode == '') {
                            alert("选择快递公司");
                            return;
                        }
                        if (postNum == '') {
                            alert("快递单号不能为空");
                            return;
                        }
                        sendOrder(orderId, status, postCompCode, postNum);
                        $("#sendDialog").dialog("close");
                    },
                    "取消": function () {
                        $("#sendDialog").dialog("close");
                    }
                }
            });
        } else {
            sendOrder(orderId, status, postCompCode, postNum);
        }
    }

    function sendOrder(orderId, status, postCompCode, postNum) {
        $.ajax({
            url: "${path}/manage/updateOrderPostStatus.action",
            type: 'POST',
            data: {orderId: orderId, orderStatus: status, postCompCode: postCompCode, postNum: postNum},//
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + orderId;
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //修改订单信息
    function openUpdateOrderInfo(orderId) {
        $("#orderDialog").dialog({
            autoOpen: true,
            height: 520,
            width: 800,
            title: "修改订单信息",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    updateOrderInfo(orderId)
                },
                "取消": function () {
                    $("#orderDialog").dialog("close");
                }
            }
        });
    }

    function updateOrderInfo(orderId) {
        var orderStatus = $("#orderStatus").val();
        var paramKey = $("#paramKey").val();
        var refundPrice = $("#refundPrice").val();
        var refundPemark = $("#refundRemarkOrder").val();
        var postPhone = $("#postPhone").val();
        var postMan = $("#postMan").val();
        var serviceRemark = $("#orderServiceRemark").val();
        $.ajax({
            url: "${path}/manage/updateOrderInfo.action",
            type: 'POST',
            data: {
                orderId: orderId,
                orderStatus: orderStatus,
                paramKey: paramKey,
                refundPrice: refundPrice,
                refundPemark: refundPemark,
                postPhone: postPhone,
                postMan: postMan,
                serviceRemark: serviceRemark
            },//
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + orderId;
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //修改详情状态
    function updateDetailStatus(orderId, detailId, status) {
        $.ajax({
            url: "${path}/manage/updateOrderDetailStatus.action",
            type: 'POST',
            data: {orderId: orderId, detailId: detailId, detailStatus: status},//
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/manage/toOrderDetailPage.action?orderId=" + orderId;
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    function showStatusRecord() {
        $("#orderStatusRecord").toggle();
    }
</script>