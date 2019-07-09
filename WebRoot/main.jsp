<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="jsp/util/meta.jsp" %>
<%@ include file="jsp/util/taglibs.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>订单系统</title>
    <link type="text/css" rel="stylesheet" href="${path }/css/css.css"/>
</head>
<body>
<div class="navigation color_theme">
    <ul class="header_sys">
        <li style="color: blue;">${user.loginName }你好,欢迎使用订单管理系统</li>
        <li><a href="${path }/login/userExit.action"><i class="icon_dropdown"></i>退出1</a></li>
    </ul>
</div>
<div class="content_wrap">
    <div class="side">
        <s:iterator value="#session.functions" status="root">
            <div class="subnav">
                <div class="subnav_title">
                    <a href="javascript:;"><s:property value="functionName"/></a>
                </div>
                <ul class="sub_menu hide">
                    <s:iterator value="childfunctions">
                        <s:iterator value="#session.user.userRole.roleFunctions">
                            <s:if test="function.functionId == functionId">
                                <li>
                                <s:if test="childfunctions.size>0">
                                    <li level_3="<s:property value="functionId"/>">
                                </s:if>
                                <s:else>
                                    <li>
                                </s:else>
                                <a href="${path }/<s:property value='functionAction'/>"
                                   target="content_frame"><s:property value="functionName"/></a>
                                </li>
                            </s:if>
                        </s:iterator>
                    </s:iterator>
                </ul>
            </div>
        </s:iterator>
        <div class="subnav">
            <div class="subnav_title mg13">
                <a href="javascript:;">我的后台</a>
            </div>
            <ul class="sub_menu">
                <li><a href="${path }/base/toEditModify.action" target="content_frame">修改密码</a></li>
            </ul>
        </div>
    </div>
    <div class="main">
        <s:iterator value="#session.functions" status="root">
            <s:iterator value="childfunctions">
                <s:iterator value="#session.user.userRole.roleFunctions">
                    <s:if test="function.functionid == functionid && childfunctions.size>0">
                        <ul class="iframe_menu hide fl" paren="<s:property value="functionId"/>">
                            <s:iterator value="childfunctions" id="subFuncs">
                                <li>
                                    <a href="${path }/<s:property value='#subFuncs.functionAction'/>"
                                       target="content_frame"><s:property value="#subFuncs.functionName"/></a>
                                </li>
                            </s:iterator>
                        </ul>
                    </s:if>
                </s:iterator>
            </s:iterator>
        </s:iterator>-
        <input type="button" class="side_hide">
        <iframe frameborder="0" id="content_frame" name="content_frame" scrolling="auto"
                src="${path }/base/userMsg.action" onload="SetCwinHeight()"></iframe>
    </div>
</div>
<script type="text/javascript" src="${path }/js/sub_nav.js"></script>
<script>
    $('.no_access').live('click', function () {
        ace.show('该功能即将开放，敬请期待...');
    });
</script>
</body>
</html>