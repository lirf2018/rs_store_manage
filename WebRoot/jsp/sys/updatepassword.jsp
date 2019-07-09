<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改密码</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/jquery.md5.js"></script>

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
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>当前登录名称
            </td>
            <td><input type="text" disabled="disabled" value="" id="loginName"/><input type="hidden" disabled="disabled"
                                                                                       value="" id="opa"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>原密码
            </td>
            <td><input type="password" name="opassword" id="opassword" value="${opassword}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>新密码
            </td>
            <td><input type="password" name="npassword" id="npassword" value="${npassword}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>重复新密码
            </td>
            <td><input type="password" name="npassword2" id="npassword2" value="${npassword2}"/></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button onclick="submitForm()" class="btn btn-primary" type="button">保存</button> &nbsp;<button
                    type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack()">返回列表
            </button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<script>

    $(function () {
        loadAdminData();
    });

    function loadAdminData() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/login/getLoginInfo.action",
            dataType: "json",
            success: function (data) {
                $("#opa").val(data.msg.loginPassword);
                $("#loginName").val(data.msg.loginName);
            }
        });
    }


    //提交表单
    function submitForm() {
        //名称
        var loginName = $("#loginName").val() + "";
        if (loginName.trim() == '') {
            alert("修改失败");
            return;
        }

        var opassword = $("#opassword").val() + "";
        if (opassword.trim() == '') {
            alert("原密码不能为空");
            return;
        }
        var npassword = $("#npassword").val() + "";
        if (npassword.trim() == '') {
            alert("新密码不能为空");
            return;
        }
        var npassword2 = $("#npassword2").val() + "";
        if (npassword != npassword2) {
            alert("两次密码不相等");
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/user/updatePassword.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert("修改成功,请重新登录系统");
                    parent.window.location.href = '${path}/login/exitweb.action';
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }
</script>