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
    商品id： <input id="goodsId" type="text" class="abc input-default" style="width: 50px" placeholder="" value="">&nbsp;
    商品名称： <input id="goods_name" type="text" class="abc input-default  " placeholder="" value="">&nbsp;
    商家： <input id="pName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    类型： <s:select list="#{-1:'--选择类型--'}" listKey="key" cssStyle="width:110px" listValue="value" value=""
                  id="goods_type"/>&nbsp;
    属性： <s:select list="#{1:'实体商品',0:'虚拟商品'}" listKey="key" cssStyle="width:110px" listValue="value" headerKey=""
                  headerValue="--选择属性--" value="" id="goods_property"/>&nbsp;
    上架状态： <s:select list="#{2:'销售中',0:'已下架',1:'等待上架确认'}" listKey="key" cssStyle="width:130px" listValue="value"
                    headerKey="" headerValue="--选择上架状态--" value="" id="is_putaway"/>&nbsp;
    <p></p>
    一级分类： <s:select list="#{0:'--选择分类--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="level"
                    onchange="changeCatogry()"/>&nbsp;
    类目： <s:select list="#{0:'--选择类目--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="catogry"/>&nbsp;
    商品状态： <s:select list="#{1:'长期有效商品',2:'未开始商品',3:'已开始商品(未结束)',4:'已结束商品',5:'限时有效商品'}" listKey="key" listValue="value"
                    headerKey="" headerValue="--选择商品状态--" value="" cssStyle="width:150px" id="goods_status"/>&nbsp;
    是否单品： <s:select list="#{1:'单品',0:'不是单品'}" listKey="key" listValue="value" headerKey="-1" headerValue="--选择是否单品--"
                    value="" cssStyle="width:150px" id="is_single"/>&nbsp;
    是否抢购商品： <s:select list="#{1:'是抢购商品',0:'不是抢购商品'}" listKey="key" listValue="value" headerKey="-1"
                      headerValue="--是否抢购商品--" value="" cssStyle="width:150px" id="isTimeGoods"/>&nbsp;
    <p></p>
    取货方式： <s:select list="#{1:'邮寄',4:'自取',5:'配送'}" listKey="key" listValue="value" headerKey="" headerValue="--选择取货方式--"
                    value=""
                    cssStyle="width:130px" id="getWay"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增商品</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>商品id</th>
            <th>商品名称</th>
            <th>图片</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>成本价</th>
            <th>原价</th>
            <th>现价</th>
            <th>押金</th>
            <th>销售状态</th>
            <th>库存</th>
            <th>排序</th>
            <th>商品属性</th>
            <th>商品类型</th>
            <th>是否单品</th>
            <th>所属店铺</th>
            <th>所属商家</th>
            <th>取货方式</th>
            <th>类目</th>
            <th>一级分类</th>
            <th>抢购商品</th>
            <th>状态</th>
            <th>创建时间</th>
            <th>操作</th>
            <th>设置抢购</th>
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
        var goods_name = $("#goods_name").val();
        var goodsId = $("#goodsId").val();
        var goods_type = $("#goods_type").val();
        var goods_property = $("#goods_property").val();
        var is_putaway = $("#is_putaway").val();
        var goods_status = $("#goods_status").val();
        var status = $("#status").val();
        var pName = $("#pName").val();
        var leveId = $("#level").val();
        var catogryId = $("#catogry").val();
        var isSingle = $("#is_single").val();
        var isTimeGoods = $("#isTimeGoods").val();
        var getWay = $("#getWay").val();
        $.ajax({
            type: "post",
            data: {
                goodsId: goodsId,
                goods_name: goods_name,
                goods_type: goods_type,
                goods_property: goods_property,
                goods_status: goods_status,
                isSingle: isSingle,
                isTimeGoods: isTimeGoods,
                pName: pName,
                leveId: leveId,
                catogryId: catogryId,
                status: status,
                is_putaway: is_putaway,
                getWay: getWay,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadGoodsDataList2.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                var resultListMap = data.msg.resultListMap;
                for (var i = 0; i < resultListMap.length; i++) {
                    var strs = "";
                    var obj = resultListMap[i];
                    if (obj.is_single == "0") {
                        strs = strs + "<tr style='cursor:pointer;' onclick='showDetailSku(" + obj.goods_id + ")'><td>" + obj.goods_id + "</td>";
                    } else {
                        strs = strs + "<tr><td>" + obj.goods_id + "</td>";
                    }

                    strs = strs + "<td>" + obj.goods_name + "</td>";
                    strs = strs + "<td><img width='150px' height='150px' style='border: solid 1px;border-color: black;' src='" + obj.goods_img + "'></td>";

                    if (obj.is_begin == "0") {
                        strs = strs + "<td>未开始</td>";//开始时间
                    } else {
                        strs = strs + "<td>" + obj.start_time + "</td>";//开始时间
                    }

                    if (obj.valid_date == '1') {
                        strs = strs + "<td>长期有效</td>";//结束时间
                    } else {
                        if (obj.is_end == "0") {
                            strs = strs + "<td style='color: red;'>已结束</td>";//
                        } else {
                            strs = strs + "<td>" + obj.end_time + "</td>";//结束时间
                        }

                    }
                    strs = strs + "<td>" + obj.purchase_price + "</td>";//成本价
                    strs = strs + "<td>" + obj.true_money + "</td>";//原价
                    strs = strs + "<td>" + obj.now_money + "</td>";//现价
                    strs = strs + "<td>" + obj.deposit_money + "</td>";//押金
                    if (obj.is_putaway == '0') {
                        strs = strs + "<td><a style='cursor:pointer;color: red;' onclick='updateGoodsIsPutaway(0,1," + obj.goods_id + ")'>已下架</a></td>";//销售状态
                    } else if (obj.is_putaway == '1') {
                        strs = strs + "<td><a style='cursor:pointer;color: red;' onclick='updateGoodsIsPutaway(1,0," + obj.goods_id + ")'>等待上架确认</a></td>";//销售状态
                    } else if (obj.is_putaway == '2') {
                        strs = strs + "<td><a style='cursor:pointer;' onclick='updateGoodsIsPutaway(2,0," + obj.goods_id + ")'>销售中</a></td>";//销售状态
                    } else {
                        strs = strs + "<td style='color: red;'>未知</td>";
                    }

                    strs = strs + "<td>" + obj.goods_num + "</td>";//库存
                    strs = strs + "<td>" + obj.weight + "</td>";//排序
                    strs = strs + "<td>" + obj.goods_type_name + "</td>";//属性 0虚拟商品1实体商品

                    if (obj.goods_type == 0) {
                        strs = strs + "<td>实体商品</td>";//商品类型:0:实体商品1商品券2话费商品
                    } else if (obj.goods_type == 1) {
                        strs = strs + "<td>商品券</td>";//商品类型:0:实体商品1商品券2话费商品
                    } else {
                        strs = strs + "<td>话费商品</td>";//商品类型:0:实体商品1商品券2话费商品
                    }
                    if (obj.is_single == '0') {
                        strs = strs + "<td>非单品</td>";//是否单品
                    } else {
                        strs = strs + "<td>单品</td>";//是否单品
                    }

                    strs = strs + "<td>" + obj.shop_name + "</td>";//所属于店铺
                    strs = strs + "<td>" + obj.partners_name + "</td>";//商家
                    if (obj.get_way == '1') {
                        strs = strs + "<td>邮寄</td>";//取货方式 1.邮寄4.自取5配送
                    } else if (obj.get_way == '4') {
                        strs = strs + "<td>自取</td>";//取货方式 1.邮寄4.自取5配送
                    } else {
                        strs = strs + "<td>配送</td>";//取货方式 1.邮寄4.自取5配送
                    }
                    strs = strs + "<td>" + obj.category_name + "</td>";//类目
                    strs = strs + "<td>" + obj.level_name + "</td>";//一级分类
                    if (obj.is_time_goods == 1) {
                        strs = strs + "<td>是</td>";//抢购商品
                    } else {
                        strs = strs + "<td>不是</td>";//抢购商品
                    }
                    if (obj.status == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td>" + obj.createtime + "</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj.goods_id + "," + data.msg.extendsObj + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj.goods_id + ")'>编辑商品</a></td>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td>" + obj.createtime + "</td>";
                        if (data.msg.extendsObj == null || data.msg.extendsObj == '0' || data.msg.extendsObj == '') {
                            strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(1," + obj.goods_id + "," + data.msg.extendsObj + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj.goods_id + ")'>编辑商品</a></td>";

                        } else {
                            strs = strs + "<td style='color: red;'>请联系管理员恢复商品</td>";
                        }
                    }
                    if (obj.is_single == 1) {
                        if (obj.is_time_goods == 1) {
                            strs = strs + "<td><a style='cursor:pointer;' onclick='toAddOrUpdateTimeGoodsPage(" + obj.goods_id + ")'>设置</a>|<a style='cursor:pointer;' onclick='cancelTimegoods(" + obj.goods_id + ")'>取消</a></td></tr>"
                        } else {
                            strs = strs + "<td><a style='cursor:pointer;' onclick='toAddOrUpdateTimeGoodsPage(" + obj.goods_id + ")'>设置</a></td></tr>"
                        }
                    } else {
                        strs = strs + "<td></td></tr>"
                    }
                    strs = strs + "<tr id='detail" + obj.goods_id + "' style='display: none'><td colspan='30'>";
                    strs = strs + "<table class='table table-bordered table-hover definewidth m10' style='font-size: 14px'>";
                    strs = strs + "<thead>";
                    strs = strs + "<tr>";
                    strs = strs + "<th>sku标识</th>";
                    strs = strs + "<th>sku编码</th>";
                    strs = strs + "<th>sku名称</th>";
                    strs = strs + "<th>sku商品编码</th>";
                    strs = strs + "<th>图片</th>";
                    strs = strs + "<th>成本价</th>";
                    strs = strs + "<th>原价</th>";
                    strs = strs + "<th>现价</th>";
                    strs = strs + "<th>库存</th>";
                    strs = strs + "</tr>";
                    strs = strs + "</thead>";
                    strs = strs + "<tbody id='tbd" + obj.goods_id + "'>";

                    strs = strs + "</tbody>";
                    strs = strs + "</table>";
                    strs = strs + "</td></tr>";
                    strs = strs + "<td><td colspan='30'></td></tr>";
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

    //删除或者启用
    function delOrUpdate(status, id, extendsObj) {
        if (extendsObj != null && extendsObj != 0 && !confirm("删除后将无法使用,只能联系管理管理员处理,是否继续删除？")) {
            return;
        }
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/deleteGoodsStatus.action",
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
            var op = "add";
            window.location.href = "${path}/manage/toAddgoodsPage.action?op=" + op;
        } else {//修改
            var op = "update";
            window.location.href = "${path}/manage/toAddgoodsPage.action?op=" + op + "&id=" + id;
        }
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

    //跳转到新增或者修改页面
    function toAddOrUpdateSKUPage(id) {
        window.location.href = "${path}/manage/toAddorUpdateGoodsSKUPage.action?op=from-goodspage&skuId=" + id;
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

    //更新商品上架状态
    function updateGoodsIsPutaway(nowIsPutaway, isPutaway, goodsId) {
        if (nowIsPutaway == 2 && !confirm("下架后需要重新确认上架,是否继续下架？")) {
            return;
        }
        $.ajax({
            type: "post",
            data: {id: goodsId, isPutaway: isPutaway},
            async: false,
            cache: false,
            url: "${path}/manage/updateGoodsIsPutaway.action",
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
    function toAddOrUpdateTimeGoodsPage(goodsId) {
        window.location.href = "${path}/manage/toAddTimeGoodsPage.action?goodsId=" + goodsId;
    }

    function showDetailSku(id) {
        $.ajax({
            type: "post",
            data: {goods_id: id},
            async: false,
            cache: false,
            url: "${path}/manage/loadGoodsSkuList",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                $("#tbd" + id).empty();
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        var str = "<tr>";
                        str = str + "<td>" + data.msg[i].sku_id + "</td>";
                        str = str + "<td>" + data.msg[i].prop_code + "</td>";
                        str = str + "<td>" + data.msg[i].sku_name + "</td>";
                        str = str + "<td>" + data.msg[i].sku_code + "</td>";
                        str = str + "<td><img src='" + data.msg[i].sku_img + "' width='50px' height='50px'></td>";
                        str = str + "<td>" + data.msg[i].purchase_price + "</td>";
                        str = str + "<td>" + data.msg[i].true_money + "</td>";
                        str = str + "<td>" + data.msg[i].now_money + "</td>";
                        str = str + "<td>" + data.msg[i].sku_num + "</td>";
                        str = str + "</tr>";
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
</script>
