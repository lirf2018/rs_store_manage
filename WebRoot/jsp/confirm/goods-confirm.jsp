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
</head>
<body>
<form class="form-inline definewidth m20 " action="index.html" method="get">
    商品名称： <input id="goods_name" type="text" class="abc input-default  " placeholder="" value="">&nbsp;
    商家： <input id="pName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    类型： <s:select list="#{-1:'--选择类型--'}" listKey="key" cssStyle="width:110px" listValue="value" value=""
                  id="goods_type"/>&nbsp;
    属性： <s:select list="#{1:'实体商品',0:'虚拟商品'}" listKey="key" cssStyle="width:110px" listValue="value" headerKey=""
                  headerValue="--选择属性--" value="" id="goods_property"/>&nbsp;
    商品状态： <s:select list="#{1:'长期有效商品',2:'未开始商品',3:'已开始商品(未结束)',4:'已结束商品',5:'限时有效商品'}" listKey="key" listValue="value"
                    headerKey="" headerValue="--选择商品状态--" value="" cssStyle="width:150px" id="goods_status"/>&nbsp;
    <p></p>
    一级分类： <s:select list="#{0:'--选择分类--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="level"
                    onchange="changeCatogry()"/>&nbsp;
    类目： <s:select list="#{0:'--选择类目--'}" listKey="key" cssStyle="width:130px" listValue="value" value="0" id="catogry"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    密钥： <input id="param_key" type="password" class="abc input-default" placeholder="密钥" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="paramConfirm()">商品上架确认</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td><span style="color: red">备注：商品id相同，则为同一个商品,不管确认哪一个商品sku,该商品其它sku规则也会被确认</span></td>
        </tr>
    </table>
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>商品id<input type="checkbox" name="checkAll" onclick="checkAll()"></th>
            <th>商品名称(sku名称)</th>
            <th>图片</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>成本价</th>
            <th>原价</th>
            <th>现价</th>
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
        var goods_name = $("#goods_name").val();
        var goods_type = $("#goods_type").val();
        var goods_property = $("#goods_property").val();
        var is_putaway = 1;
        var goods_status = $("#goods_status").val();
        var status = $("#status").val();
        var pName = $("#pName").val();
        var leveId = $("#level").val();
        var catogryId = $("#catogry").val();
        $.ajax({
            type: "post",
            data: {
                goods_name: goods_name,
                goods_type: goods_type,
                goods_property: goods_property,
                goods_status: goods_status,
                pName: pName,
                leveId: leveId,
                catogryId: catogryId,
                status: status,
                is_putaway: is_putaway,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadGoodsDataList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                var resultListMap = data.msg.resultListMap;
                for (var i = 0; i < resultListMap.length; i++) {
                    var strs = "";
                    //编号-名称-图片-开始时间-结束时间-现价-销售状态-库存-排序-属性-类型-是否单品-状态-创建时间-操作
                    var obj = resultListMap[i];
                    strs = strs + "<tr><td><input type='checkbox' name='checkSub' onclick='checkSub()' value='" + obj.goods_id + "'>" + obj.goods_id + "</td>";
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

                    if (obj.is_putaway == '0') {
                        strs = strs + "<td><span style='color: red;'>已下架</span></td>";//销售状态
                    } else if (obj.is_putaway == '1') {
                        strs = strs + "<td><span style='color: red;'>等待上架确认</span></td>";//销售状态
                    } else if (obj.is_putaway == '2') {
                        strs = strs + "<td>销售中</td>";//销售状态
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
                    if (obj.status == "1") {
                        strs = strs + "<td >有效</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                    }
                    strs = strs + "<td>" + obj.createtime + "</td>";
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
                    $("#tb").append("<tr><td colspan='80' style='font-size: 20px;font-weight: bold;color: red;text-align: center'>暂时没有数据</td></tr>");
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
            alert("请选中要确认的商品");
            return;
        }

        $.ajax({
            type: "post",
            data: {ids: ids, paramKey: paramKey},
            async: false,
            cache: false,
            url: "${path}/manage/goodsConfirm.action",
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
