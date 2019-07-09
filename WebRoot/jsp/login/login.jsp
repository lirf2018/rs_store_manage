<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <TITLE>登录页面</TITLE>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="${path}/css/login.css">
    <script type="text/javascript" src="${path}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //得到焦点
            $("#password").focus(function () {
                $("#left_hand").animate({
                    left: "150",
                    top: " -38"
                }, {
                    step: function () {
                        if (parseInt($("#left_hand").css("left")) > 140) {
                            $("#left_hand").attr("class", "left_hand");
                        }
                    }
                }, 2000);
                $("#right_hand").animate({
                    right: "-64",
                    top: "-38px"
                }, {
                    step: function () {
                        if (parseInt($("#right_hand").css("right")) > -70) {
                            $("#right_hand").attr("class", "right_hand");
                        }
                    }
                }, 2000);
            });
            //失去焦点
            $("#password").blur(function () {
                $("#left_hand").attr("class", "initial_left_hand");
                $("#left_hand").attr("style", "left:100px;top:-12px;");
                $("#right_hand").attr("class", "initial_right_hand");
                $("#right_hand").attr("style", "right:-112px;top:-12px");
            });
        });

        function login() {
            if (!valid()) {
                return;
            }
            $.ajax({
                type: "post",
                data: $("#form_submit").serialize(),
                async: false,
                cache: false,
                url: "${path}/login/userLogin.action",
                dataType: 'json',
                beforeSend: function (xmlHttp) {
                    xmlHttp.setRequestHeader("If-Modified-Since", "0");
                    xmlHttp.setRequestHeader("Cache-Control", "no-cache");
                },
                success: function (data) {
                    if (data.flag == "ok") {
                        window.location.href = "${path}/jsp/index.jsp";
                    } else if (data.flag == "false") {
                        alert(data.msg);
                        flushValidateCode();
                        $("#password").val("");
                        $("input[name='checkcode']").val("");
                    } else {
                        alert("系统错误，请联系管理员处理");
                        $("#password").val("");
                        $("input[name='checkcode']").val("");
                    }

                }
            });
        }


        function valid() {
            var username = $("input[name='login_name']").val();
            var userpwd = $("input[name='login_pass']").val();
            var checkcode = $("input[name='checkcode']").val();
            if (!username) {
                alert("用户登录名称不能为空");
                return false;
            }
            if (!userpwd) {
                alert("用户登录密码不能为空");
                return false;
            }
            if (!checkcode) {
                alert("验证码不能为空");
                return false;
            }
            if (checkcode.length != 4) {
                alert("验证码长度应为4");
                return false;
            }
            return true;
        }

        function flushValidateCode() {
            var validateImgObject = document.getElementById("validateImg");
            validateImgObject.src = "${path}/image.image?time=" + new Date();
        }

    </script>
    <script type="text/javascript" language=JavaScript charset="UTF-8">
        document.onkeydown = function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if (e && e.keyCode == 13) { // enter 键
                login();
            }
        };
    </script>
</head>
<body>
<form action="" id="form_submit">
    <div class="top_div"></div>

    <div style="background: rgb(255, 255, 255); margin: -150px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 190px; text-align: center;">

        <div style="width: 165px; height: 96px; position: absolute;">

            <div class="tou"></div>

            <div class="initial_left_hand" id="left_hand"></div>

            <div class="initial_right_hand" id="right_hand"></div>
        </div>

        <P style="padding: 30px 0px 10px; position: relative;"><span class="u_logo"></span>&nbsp;&nbsp;
            <INPUT class="ipt" type="text" name="login_name" placeholder="请输入用户名或邮箱" value=""></P>

        <P style="padding: 0px 0px 10px;position: relative;"><span class="p_logo"></span>&nbsp;&nbsp;
            <INPUT id="password" class="ipt" name="login_pass" type="password" placeholder="请输入密码" value=""></P>

        <P style="position: relative;"><span class="p_logo"></span>&nbsp;&nbsp;
            <INPUT maxlength="4" class="ipt" name="checkcode" type="text" placeholder="验证码" value=""
                   style="width: 180px;"> <img onclick="flushValidateCode()" id="validateImg" src="${path}/image.image"
                                               align="absmiddle"/><span onclick="flushValidateCode()"
                                                                        style="cursor:pointer;font-size: 20px;"> 刷新</b>
        </P>

        <div style="height: 50px; line-height: 50px; margin-top: 35; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">

            <P style="margin: 0px 35px 20px 45px;">
		<span style="float: right;"><a style="color:greed; margin-right: 10px;" href="#"></a>

        <a style="cursor:pointer;background: rgb(0, 142, 173); padding: 7px 10px; border-radius: 4px; border: 1px solid rgb(26, 117, 152); border-image: none; color: rgb(255, 255, 255); font-weight: bold;"
           onclick="login()">登录</a> </span></P></div>
    </div>

    <div style="text-align:center;">
</form>
</div>
</body>
</html>
