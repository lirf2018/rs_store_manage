<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>增加sku</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script>
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
    <script type="text/javascript">
        var ifchangeImg = "0";

        function previewImage(file) {
            var MAXWIDTH = 100;
            var MAXHEIGHT = 100;
            var div = document.getElementById('preview');
            if (file.files && file.files[0]) {
                div.innerHTML = '<img id=imghead>';
                var img = document.getElementById('imghead');
                img.onload = function () {
                    //var imgrealywidth=img.offsetWidth;//图片实际宽
                    //var imgrealyheigth=img.offsetHeight;//图片实际高
                    var imgrealywidth = 100;
                    var imgrealyheigth = 100;

                    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, imgrealywidth, imgrealyheigth);
                    img.width = rect.width;
                    img.height = rect.height;
                    img.style.marginLeft = rect.left + 'px';
                    img.style.marginTop = rect.top + 'px';
                }
                var reader = new FileReader();
                reader.onload = function (evt) {
                    img.src = evt.target.result;
                }
                reader.readAsDataURL(file.files[0]);
                ifchangeImg = "1";
            } else {
                var sFilter = 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
                file.select();
                var src = document.selection.createRange().text;
                div.innerHTML = '<img id=imghead>';
                var img = document.getElementById('imghead');
                img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                status = ('rect:' + rect.top + ',' + rect.left + ',' + rect.width + ',' + rect.height);
                div.innerHTML = "<div id=divhead style='width:" + rect.width + "px;height:" + rect.height + "px;margin-top:" + rect.top + "px;margin-left:" + rect.left + "px;" + sFilter + src + "\"'></div>";
                ifchangeImg = "1";
            }
        }

        function clacImgZoomParam(maxWidth, maxHeight, width, height) {
            var param = {top: 0, left: 0, width: width, height: height};
            if (width > maxWidth || height > maxHeight) {
                rateWidth = width / maxWidth;
                rateHeight = height / maxHeight;
                if (rateWidth > rateHeight) {
                    param.width = maxWidth;
                    param.height = Math.round(height / rateWidth);
                } else {
                    param.width = Math.round(width / rateHeight);
                    param.height = maxHeight;
                }
            }
            param.left = Math.round((maxWidth - param.width) / 2);
            param.top = Math.round((maxHeight - param.height) / 2);
            return param;
        }
    </script>
</head>
<body>
<form action="" method="post" class="definewidth m20" id="form_submit">
    <input type="hidden" value="${sku.skuId }" name="sku.skuId">
    <input type="hidden" value="${op }" name="op" id="op">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>商品名称
            </td>
            <td><input type="text" readonly="readonly" value="${goods.goodsName}"/></td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;"><span
                    style="color: red;">* </span>图片<br><span style="color: red;">建议尺寸:960*480</span></td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>sku名称
            </td>
            <td>
                <input type="text" readonly="readonly" value="${sku.skuName}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>sku编码
            </td>
            <td>
                <input type="text" name="sku.skuCode" value="${sku.skuCode}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>原价
            </td>
            <td><input type="text" name="sku.trueMoney" value="${sku.trueMoney}" id="trueMoney"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>库存
            </td>
            <td><input type="text" name="sku.skuNum" value="${sku.skuNum}" id="skuNum"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>现价
            </td>
            <td><input type="text" name="sku.nowMoney" value="${sku.nowMoney}" id="nowMoney"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>属性编码
            </td>
            <td><input type="text" readonly="readonly" value="${sku.propCode}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">成本价</td>
            <td colspan="3"><input type="text" name="sku.purchasePrice" id="purchasePrice"
                                   value="${sku.purchasePrice}"/></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button onclick="submitForm()" class="btn btn-primary" type="button">保存</button> &nbsp;&nbsp;
                <c:choose>
                    <c:when test="${op=='from-goodspage' }">
                        <button type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack(1)">
                            返回商品列表
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack(0)">
                            返回sku列表
                        </button>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
    </table>
</form>
</body>
</html>
<script>

    //提交表单
    function submitForm() {

        if (!confirm("修改商品sku后,商品需要重新确认上架,是否继续修改？")) {
            return;
        }


        if (checkData()) {
            return;
        }
        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/manage/addOrUpdateGoodsSKU.action",
            type: 'POST',
            data: data,
            dataType: 'JSON',
            cache: false,
            processData: false,
            contentType: false
        }).done(function (ret) {
            var flag = ret.flag;
            if (flag === "ok") {
                alert(ret.msg);
                var op = $("#op").val();
                if (op == "from-goodspage") {
                    window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
                } else {
                    window.location.href = "${path}/jsp/manage/list_goods_sku.jsp";
                }
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    function checkData() {
        //原价
        var trueMoney = $("#trueMoney").val() + "";
        if (trueMoney.trim() == '') {
            alert("原价不能为空");
            return true;
        }
        //现价
        var nowMoney = $("#nowMoney").val() + "";
        if (nowMoney.trim() == '') {
            alert("现价不能为空");
            return true;
        }
        if (Number(nowMoney) > Number(trueMoney)) {
            alert("原价不能小于现价");
            return true;
        }
        //库存
        var skuNum = $("#skuNum").val() + "";
        if (skuNum.trim() == '') {
            alert("库存不能为空");
            return true;
        }
        return false;
    }

    //返回列表
    function returnBack(from) {
        if (from == "1") {
            window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
        } else {
            window.location.href = "${path}/jsp/manage/list_goods_sku.jsp";
        }

    }
</script>