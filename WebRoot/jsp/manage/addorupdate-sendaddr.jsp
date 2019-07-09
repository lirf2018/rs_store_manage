<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改配送地址</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>

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
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${id }" name="distribution.id">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>地址前缀
            </td>
            <td><input type="text" name="distribution.addrPrefix" id="addrPrefix" value="${distribution.addrPrefix}"/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>负责人
            </td>
            <td><input type="text" name="distribution.responsibleMan" id="responsibleMan"
                       value="${distribution.responsibleMan}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>运费
            </td>
            <td><input type="text" name="distribution.freight" id="freight" value="${distribution.freight}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>负责人电话
            </td>
            <td><input type="text" name="distribution.responsiblePhone" id="responsiblePhone"
                       value="${distribution.responsiblePhone}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序字母
            </td>
            <td><input type="text" name="distribution.sortChar" id="sortChar" value="${distribution.sortChar}"/> 字母排序优先
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>状态
            </td>
            <td>
                <c:choose>
                    <c:when test="${distribution.status=='0'}">
                        <input type="radio" name="distribution.status" value="1"/> 启用
                        <input type="radio" name="distribution.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="distribution.status" value="1" checked/> 启用
                        <input type="radio" name="distribution.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>地址排序
            </td>
            <td><input type="text" name="distribution.addrShort" id="addrShort" value="${distribution.addrShort}"/>
                数值越大越靠前
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>地址类型
            </td>
            <td>
                <c:choose>
                    <c:when test="${op == 'add' }">
                        <input name="addrTypes" type="checkbox" value="4">自取 <input name="addrTypes" type="checkbox"
                                                                                    value="5">配送 <input name="addrTypes"
                                                                                                        type="checkbox"
                                                                                                        value="6">还货
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{4:'自取地址',6:'还货地址'}" listKey="key" listValue="value" headerKey="5"
                                  headerValue="配送地址" name="distribution.addrType" value="distribution.addrType"
                                  cssStyle="width:110px" id="addrType"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>详细说明
            </td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="2" cols="30" name="distribution.addrDesc"
                                      id="addrDesc">${distribution.addrDesc }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>地址名称
            </td>
            <td colspan="3"><input type="text" name="distribution.addrName" id="addrName"
                                   value="${distribution.addrName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>详细地址
            </td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30"
                                      name="distribution.detailAddr"
                                      id="detailAddr">${distribution.detailAddr }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="distribution.remark"
                                      id="remark">${distribution.remark }</textarea></td>
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
        //地址前缀
        var addrPrefix = $("#addrPrefix").val() + "";
        if (addrPrefix.trim() == '') {
            alert("地址前缀不能为空,如:广西柳州");
            return;
        }
        //负责人
        var responsibleMan = $("#responsibleMan").val() + "";
        if (responsibleMan.trim() == '') {
            alert("负责人不能为空");
            return;
        }
        //地址名称
        var addrName = $("#addrName").val() + "";
        if (addrName.trim() == '') {
            alert("地址名称不能为空");
            return;
        }
        //运费
        var freight = $("#freight").val() + "";
        if (freight.trim() == '' || !checkMoney(freight)) {
            alert("运费非法");
            return;
        }
        //负责人电话
        var responsiblePhone = $("#responsiblePhone").val() + "";
        if (responsiblePhone.trim() == '') {
            alert("负责人电话不能为空");
            return;
        }
        //排序字母
        var sortChar = $("#sortChar").val() + "";
        if (sortChar.trim() == '' || sortChar.length != 1) {
            alert("排序字母不能为空且排序字母为大写的26个英文字母");
            return;
        } else {
            $("#sortChar").val(sortChar.toUpperCase());
//			var preFix = [ "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
//					"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//					"W", "X", "Y", "Z" ];
//			var flag = false;
//			for(var i=0;i<preFix.length;i++){
//				if(sortChar==preFix[i]){
//					flag = true;
//					break;
//				}
//			}
//			if(!flag){
//				alert("排序字母为大写的26个英文字母");
//				return;
//			}
        }
        //详细地址
        var detailAddr = $("#detailAddr").val() + "";
        if (detailAddr.trim() == '') {
            alert("详细地址不能为空");
            return;
        }


        var op = '${op}';
        //地址类型
        var addrTypesArr = $("input[name='addrTypes']:checked");
        if (op == 'add' && addrTypesArr.length == 0) {
            alert("选择地址类型");
            return;
        }
        // var addrTypeStr = "";
        // addrTypesArr.each(function () {
        //     addrTypeStr = addrTypeStr + $(this).val() + ",";
        // })

        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/manage/addOrUpdateDistributionAddr.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_sendaddr.jsp";
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_sendaddr.jsp";
    }

    //金钱校验
    function checkMoney(value) {
        var re = /^(\d*)?(\.)?\d+$/;
        if (!re.test($.trim(value))) {
            return false;
        }
        return true;
    }
</script>