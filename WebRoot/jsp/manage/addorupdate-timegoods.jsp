<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改规则</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/DatePicker/WdatePicker.js"></script>

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
    <input type="hidden" value="${goods.goodsId}" name="goodsId">
    <input type="hidden" value="${timeGoods.id}" name="timeGoods.id">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td style="text-align: center">商品编号: ${goods.goodsId}</td>
            <td rowspan="5" style="vertical-align: middle">商品图片: <img src="${perfixUrl}${goods.goodsImg}" width="100px"
                                                                      height="100px"
                                                                      style="border: solid 1px;border-color: black;">
            </td>
        </tr>
        <tr>
            <td style="text-align: center">商品名称: ${goods.goodsName}</td>
        </tr>
        <tr>
            <td style="text-align: center">商品进货价: ${goods.purchasePrice}</td>
        </tr>
        <tr>
            <td style="text-align: center">商品原价: ${goods.trueMoney}</td>
        </tr>
        <tr>
            <td style="text-align: center">商品现价: ${goods.nowMoney}</td>
        </tr>
    </table>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>开始时间
            </td>
            <td><input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text"
                       name="timeGoods.beginTime" id="beginTime" placeholder="开始时间"
                       value="<fmt:formatDate value="${timeGoods.beginTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>抢购价
            </td>
            <td><input type="text" name="timeGoods.timePrice" id="timePrice" value="${timeGoods.timePrice}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>结束时间
            </td>
            <td><input readonly="readonly"
                       onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                       type="text" name="timeGoods.endTime" id="endTime"
                       value="<fmt:formatDate value="${timeGoods.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "
                       placeholder="结束时间"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>限购方式
            </td>
            <td><s:select list="#{1:'每天一次',2:'每月一次',3:'每年一次',4:'不限购',5:'只允许购买一次'}" listKey="key" listValue="value"
                          headerKey="0" headerValue="--限购方式--" id="timeWay" name="timeGoods.timeWay"
                          value="timeGoods.timeWay" cssStyle="width:180px;"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>库存
            </td>
            <td><input type="text" name="timeGoods.goodsStore" id="goodsStore" value="${timeGoods.goodsStore}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>限购数
            </td>
            <td><input type="text" name="timeGoods.limitNum" id="limitNum" value="${timeGoods.limitNum}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>限购统计开始时间
            </td>
            <td colspan="3">
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="timeGoods.limitBeginTime" id="limitBeginTime"
                       value="<fmt:formatDate value="${timeGoods.limitBeginTime}" pattern="yyyy-MM-dd"/> "
                       placeholder="限购开始时间"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="timeGoods.remark"
                                      id="remark">${timeGoods.remark }</textarea></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button onclick="submitForm()" class="btn btn-primary" type="button">保存</button> &nbsp;&nbsp;<button
                    type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack()">返回列表
            </button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<script>
    //提交表单
    function submitForm() {
        if (!checkdata()) {
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/manage/addTimeGoods.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    var pageFrom = '${pageFrom}';
                    if (pageFrom == 'timegoods') {
                        window.location.href = "${path}/jsp/manage/list_timegoods.jsp";
                    } else {
                        window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
                    }
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    function checkdata() {
        var beginTime = $("#beginTime").val() + "";
        if (beginTime.trim() == '') {
            alert("开始时间不能为空");
            return false;
        }
        var timePrice = $("#timePrice").val() + "";
        if (!numAndpoint(timePrice)) {
            alert("抢购价非法");
            return;
        }
        //现价
        var nowMoney = '${goods.nowMoney}';
        if (Number(nowMoney) < Number(timePrice)) {
            alert("抢购价不能大于现价");
            return false;
        }


        var endTime = $("#endTime").val() + "";
        if (endTime.trim() == '') {
            alert("结束时间不能为空");
            return false;
        }
        if (beginTime > endTime) {
            alert("开始时间不能大于结束时间");
            return false;
        }
        var timeWay = $("#timeWay").val();
        if (timeWay == 0) {
            alert("限购方式不能为空");
            return false;
        }
        var limitBeginTime = $("#limitBeginTime").val() + "";
        if (limitBeginTime == '') {
            alert("限购开始时间不能为空");
            return false;
        }
        var goodsStore = $("#goodsStore").val();
        if (!checknumber(goodsStore)) {
            alert("库存非法");
            return false;
        }
        var limitNum = $("#limitNum").val();
        if (!checknumber(limitNum) || Number(limitNum) == 0) {
            alert("限购数非法");
            return false;
        }
        return true;
    }

    //只能输入数字和小数
    function numAndpoint(value) {
        if (value.length > 1) {
            if (value.charAt(0) == '0' && value.charAt(1) != '.') {
                return false;
            }
        }
        var reg = /^\d+(\.\d+)?$/;
        if (!reg.test($.trim(value))) {
            return false;
        }
        return true;
    }

    //数字校验
    function checknumber(value) {
        if (value.length > 1) {
            if (value.charAt(0) == '0') {
                return false;
            }
        }
        var reg = /^[0-9]*$/;
        if (!reg.test($.trim(value))) {
            return false;
        }
        return true;
    }

    //返回列表
    function returnBack() {
        var pageFrom = '${pageFrom}';
        if (pageFrom == 'timegoods') {
            window.location.href = "${path}/jsp/manage/list_timegoods.jsp";
        } else {
            window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
        }

    }

</script>