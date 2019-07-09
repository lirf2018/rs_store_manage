<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改商家</title>
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
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${partners.id }" name="partners.id">
    <input type="hidden" value="${partners.id }" name="id">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>商家名称
            </td>
            <td><input type="text" name="partners.partnersName" id="partnersName" value="${partners.partnersName}"/>
            </td>
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
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>商家编码
            </td>
            <td><input type="text" name="partners.partnersCode" id="partnersCode" value="${partners.partnersCode}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${partners.status=='0'}">
                        <input type="radio" name="partners.status" value="1"/> 启用
                        <input type="radio" name="partners.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="partners.status" value="1" checked/> 启用
                        <input type="radio" name="partners.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>账号
            </td>
            <td><input type="text" name="partners.account" id="account" value="${partners.account}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>商家识别字母
            </td>
            <td><input type="text" style="width: 150px" name="partners.firstNameCode" id="firstNameCode"
                       value="${partners.firstNameCode}"/><span style="color: red;">商家名称首字拼音第一个大写字母</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>密码
            </td>
            <td><input type="password" name="partners.passwd" id="passwd" value="${partners.passwd}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td><input type="text" name="partners.partnersSort" id="partnersSort" value="${partners.partnersSort}"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/><span
                    style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">所属店铺</td>
            <td colspan="3"><s:select list="listShop" listKey="shop_id" listValue="shop_name" headerKey="0"
                                      headerValue="--选择关联店铺--" name="partners.shopId" value="partners.shopId"
                                      cssStyle="width:180px;"/>
                <span style="color: red;">  </span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>商家密钥
            </td>
            <td colspan="3"><input style="width: 300px" readonly="readonly" type="password" name="partners.secretKey"
                                   id="secretKey" value="${partners.secretKey}"/>
                <span style="color: red;">  如果没绑定账号,则由管理员生成,绑定后,可以自己后端生成</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="partners.remark"
                                      id="remark">${partners.remark }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">已关联规则</td>
        </tr>
        <tr>
            <td colspan="4">
                <c:forEach items="${listRelRule}" var="items">
                    <input disabled="disabled" type="checkbox" checked="checked"> ${items.rule_name}&nbsp;
                </c:forEach>
            </td>
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
        //商家名称
        var partnersName = $("#partnersName").val() + "";
        if (partnersName.trim() == '') {
            alert("商家名称不能为空");
            return;
        }
        //商家编码
        var partnersCode = $("#partnersCode").val() + "";
        if (partnersCode.trim() == '') {
            alert("商家编码不能为空");
            return;
        }
        //商家账号
        var account = $("#account").val() + "";
        if (account.trim() == '') {
            alert("商家账号不能为空");
            return;
        }
        //商家密码
        var passwd = $("#passwd").val() + "";
        if (passwd.trim() == '') {
            alert("商家密码不能为空");
            return;
        }

        //排序字母
        var firstNameCode = $("#firstNameCode").val() + "";
        if (firstNameCode.trim() == '' || firstNameCode.length != 1) {
            alert("排序字母不能为空且排序字母为大写的26个英文字母");
            return;
        } else {
            $("#firstNameCode").val(firstNameCode.toUpperCase());
//			var preFix = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
//					"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//					"W", "X", "Y", "Z" ];
//			var flag = false;
//			for(var i=0;i<preFix.length;i++){
//				if(firstNameCode==preFix[i]){
//					flag = true;
//					break;
//				}
//			}
//			if(!flag){
//				alert("排序字母为大写的26个英文字母");
//				return;
//			}
        }

        var partnersSort = $("#partnersSort").val();
        //排序
        if (partnersSort == '' || (partnersSort.length > 1 && partnersSort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return true;
        }

        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/manage/addOrUpdatePartners.action",
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
                window.location.href = "${path}/jsp/manage/list_partners.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_partners.jsp";
    }

</script>