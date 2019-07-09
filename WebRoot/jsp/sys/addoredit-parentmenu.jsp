<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加主菜单</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script>
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
</head>
<body>
<form action="" method="post" class="definewidth m20" id="submit_form">
    <input type="hidden" value="parentMenu" id="menuType" name="menuType"/>
    <input type="hidden" value="${function.functionId}" id="functionId" name="function.functionId"/>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>主菜单名称
            </td>
            <td><input type="text" name="function.functionName" id="functionName" autofocus="autofocus"
                       value="${function.functionName}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>主菜单编码
            </td>
            <td><input type="text" name="function.functionCode" id="functionCode" value="${function.functionCode}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td colspan="3">
                <input type="text" name="function.sort" id="sort" value="${function.sort }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/> <span
                    style="color: red;">只能输入数字,数值越大越靠前</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="function.remark"
                                      id="remark">${function.remark }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td colspan="3">
                <c:choose>
                    <c:when test="${function.status=='0'}">
                        <input type="radio" name="function.status" value="1"/> 启用
                        <input type="radio" name="function.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="function.status" value="1" checked/> 启用
                        <input type="radio" name="function.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
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

    //保存数据
    function submitForm() {
        var name = $("#functionName").val() + "";
        if (name.trim() == "") {
            alert("请输入 名称");
            return;
        }
        var code = $("#functionCode").val() + "";
        if (code.trim() == "") {
            alert("请输入编码");
            return;
        }
        var sort = $("#sort").val() + "";
        if (sort == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return;
        }
        $.ajax({
            type: "post",
            data: $("#submit_form").serialize(),
            async: false,
            cache: false,
            url: "${path}/menu/addorupdateMenu.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert("操作成功");
                    window.location.href = "${path}/jsp/sys/list_menu.jsp";
                } else if (data.flag = "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/sys/list_menu.jsp";
    }


</script>