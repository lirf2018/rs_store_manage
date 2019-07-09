<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改参数</title>
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
    <input type="hidden" value="${handleType }" name="handleType">
    <input type="hidden" name="param_.paramId" value="${param_.paramId}"/>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>参数名称
            </td>
            <td><input type="text" name="param_.paramName" id="paramName" value="${param_.paramName}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>参数编码
            </td>
            <td><input type="text" name="param_.paramCode" id="paramCode" value="${param_.paramCode}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>参数键
            </td>
            <td><input type="text" name="param_.paramKey" id="paramKey" value="${param_.paramKey}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>参数值
            </td>
            <td><input type="text" name="param_.paramValue" id="paramValue" value="${param_.paramValue}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">参数值1</td>
            <td><input type="text" name="param_.paramValue1" id="paramValue1" value="${param_.paramValue1}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">参数值2</td>
            <td><input type="text" name="param_.paramValue2" id="paramValue2" value="${param_.paramValue2}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${param_.status=='0' }">
                        <input type="radio" name="param_.status" value="1"/> 启用
                        <input type="radio" name="param_.status" value="0" checked/> 禁用
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="param_.status" value="1" checked/> 启用
                        <input type="radio" name="param_.status" value="0"/> 禁用
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">是否要确认</td>
            <td>
                <input type="radio" name="param_.isMakeSure" value="0" checked="checked"/> 需要确认<span
                    style="color: red;"></span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="param_.remark"
                                      id="remark">${param_.remark }</textarea></td>
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
        //名称
        var paramName = $("#paramName").val() + "";
        if (paramName.trim() == '') {
            alert("参数名称不能为空");
            return;
        }
        //编码
        var paramCode = $("#paramCode").val() + "";
        if (paramCode.trim() == '') {
            alert("参数编码不能为空");
            return;
        }
        //key
        var paramKey = $("#paramKey").val() + "";
        if (paramKey.trim() == '') {
            alert("参数键不能为空");
            return;
        }
        //值
        var paramValue = $("#paramValue").val() + "";
        if (paramValue.trim() == '') {
            alert("参数值不能为空");
            return;
        }

        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/param/addOrUpdateParam.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/sys/list_param.jsp";
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
        window.location.href = "${path}/jsp/sys/list_param.jsp";
    }
</script>