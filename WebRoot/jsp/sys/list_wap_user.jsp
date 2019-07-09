<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>wap用户管理</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <!-- <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script> -->
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <link rel="stylesheet" type="text/css" href="${path}/jqueryutil/jquery-ui-1.8.21.custom.css"/>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-ui-1.8.21.custom.min.js"></script>


    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        .table td, .table th {
            padding-top: 8px;
            padding-bottom: 4px;
            line-height: 20 ppx;
            text-align: center;
            vertical-align: middle;
        }

        @media ( max-width: 980px) {
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
<form class="form-inline definewidth m20" action="" method="get">
    登录账户： <input style="width: 200px;" id="loginName" type="text" name="loginName" class="abc input-default"
                 placeholder="" value="">&nbsp;
    用户昵称： <input style="width: 200px;" id="nickName" type="text" name="nickName" class="abc input-default"
                 placeholder="" value="">&nbsp;
    用户邮箱： <input id="userEmail" type="text" name="userEmail" class="abc input-default" placeholder="" value="">&nbsp;
    uid： <input id="uid" type="text" name="uid" class="abc input-default" placeholder="" value="">&nbsp;
    <p></p>
    用户电话： <input style="width: 130px" id="userMobile" type="text" name="userMobile" class="abc input-default"
                 placeholder="" value="">&nbsp;
    用户标识： <input style="width: 130px" id="userId" type="text" name="userId" class="abc input-default" placeholder=""
                 value="">&nbsp;
    状态： <s:select list="#{1:'正常',0:'待验证',2:'已锁定',3:'已注销'}" listKey="key" listValue="value" headerKey=""
                  headerValue="--选择状态--" value="" cssStyle="width:110px" id="userStatus"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddAdminPage(0,0)">新增管理员</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>用户标识</th>
            <th>登录账户</th>
            <th>用户昵称</th>
            <th>用户邮箱</th>
            <th>用户电话</th>
            <th>用户状态</th>
            <th>创建时间</th>
            <th>最后登录时间</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
</div>
</body>
</html>
<script>
    listdata(1);

    //加载管理员
    function listdata(currentPage) {
        var loginName = $("#loginName").val();
        var nickName = $("#nickName").val();
        var userEmail = $("#userEmail").val();
        var userMobile = $("#userMobile").val();
        var userId = $("#userId").val();
        var userStatus = $("#userStatus").val();
        var uid = $("#uid").val();
        $.ajax({
            type: "post",
            data: {
                loginName: loginName,
                nickName: nickName,
                userEmail: userEmail,
                userMobile: userMobile,
                userId: userId,
                userStatus: userStatus,
                uid: uid,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/user/loadWapUserlist",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.resultListMap.length; i++) {
                    var strs = "";
                    var obj = data.msg.resultListMap[i];
                    strs = strs + "<tr style='cursor:pointer;' onclick='showDetail(" + +obj.user_id + ")'><td>" + +obj.user_id + "</td>";
                    strs = strs + "<td>" + obj.login_name + "</td>";
                    strs = strs + "<td>" + obj.nick_name + "</td>";
                    if (null == obj.user_email || obj.user_email == '') {
                        strs = strs + "<td></td>";
                    } else {
                        if (obj.email_valite == '0') {
                            strs = strs + "<td>" + obj.user_email + "<span style='color: red;'>(未验证)</span></td>";
                        } else {
                            strs = strs + "<td>" + obj.user_email + "<span>(已验证)</span></td>";
                        }
                    }
                    if (null == obj.user_mobile || obj.user_mobile == '') {
                        strs = strs + "<td></td>";
                    } else {
                        if (obj.mobile_valite == '0') {
                            strs = strs + "<td>" + obj.user_mobile + "<span style='color: red;'>(未验证)</span></td>";
                        } else {
                            strs = strs + "<td>" + obj.user_mobile + "<span>(已验证)</span></td>";
                        }
                    }
                    if (obj.user_state == '0') {
                        strs = strs + "<td><span style='color: red;'>待验证</span></td>";
                    } else if (obj.user_state == '1') {
                        strs = strs + "<td>正常</td>";
                    } else if (obj.user_state == '2') {
                        strs = strs + "<td><span style='color: red;'>已锁定</span></td>";
                    } else if (obj.user_state == '3') {
                        strs = strs + "<td><span style='color: red;'>已注销</span></td>";
                    } else {
                        strs = strs + "<td><span style='color: red;'>未知</span></td>";
                    }
                    strs = strs + "<td>" + obj.createtime + "</td>";
                    strs = strs + "<td>" + obj.lastlogintime + "</td>";
                    strs = strs + "<td><a style='cursor:pointer;' onclick='updateUserStatus(3," + obj.user_id + ")'>注销</a>|<a style='cursor:pointer;' onclick='updateUserStatus(1," + obj.user_id + ")'>启用</a>|<a style='cursor:pointer;' onclick='updateUserStatus(2," + obj.user_id + ")'>锁定</a></td></tr>";
                    strs = strs + "<tr><td colspan='10'><div id='snsDetail-" + obj.user_id + "' ></div></td></tr>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.resultListMap.length > 0) {
                    var strs = "<tr><td colspan='20'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }


    //删除或者启用菜单
    function updateUserStatus(status, userId) {
        $.ajax({
            type: "post",
            data: {
                userId: userId,
                status: status
            },
            async: false,
            cache: false,
            url: "${path}/user/updateWapStatus",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    function showDetail(userId) {
        $.ajax({
            type: "post",
            data: {userId: userId},
            async: false,
            cache: false,
            url: "${path}/user/loadWapUserSnslistMap",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;//详情列表
                $("#snsDetail-" + userId).empty();
                if (msg.length > 0) {
                    var str = "<table class='table table-bordered table-hover definewidth m10'>";
                    str = str + "<thead>";
                    str = str + "<tr><th>绑定标识</th><th>绑定类型</th><th>uid</th><th>第三方昵称</th><th>第三方账号</th><th>绑定状态</th><th>绑定时间</th><th>更新时间</th></tr>";
                    str = str + "</thead>";
                    str = str + "<tbody id='tbDetail'>";
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<tr>";
                        str = str + "<td>" + msg[i].sns_id + "</td>";
                        var snsType = msg[i].sns_type;
                        if (snsType == 1) {
                            str = str + "<td><span>腾讯微博</span></td>";
                        } else if (snsType == 2) {
                            str = str + "<td><span>新浪微博</span></td>";
                        } else if (snsType == 3) {
                            str = str + "<td><span>人人网</span></td>";
                        } else if (snsType == 4) {
                            str = str + "<td><span>微信</span></td>";
                        } else if (snsType == 5) {
                            str = str + "<td><span>服务窗</span></td>";
                        } else if (snsType == 6) {
                            str = str + "<td><span>一起沃</span></td>";
                        } else if (snsType == 7) {
                            str = str + "<td><span>QQ</span></td>";
                        } else {
                            str = str + "<td><span style='color: red;'>未知</span></td>";
                        }
                        str = str + "<td>" + msg[i].uid + "</td>";
                        str = str + "<td>" + msg[i].sns_name + "</td>";
                        str = str + "<td>" + msg[i].sns_account + "</td>";
                        var status = msg[i].status;
                        if (status == 0) {
                            str = str + "<td><span style='color: red;'>无效</span></td>";
                        } else if (status == 1) {
                            str = str + "<td><span>有效</span></td>";
                        } else if (status == 2) {
                            str = str + "<td><span style='color: red;'>已解绑</span></td>";
                        } else {
                            str = str + "<td><span style='color: red;'>未知</span></td>";
                        }
                        str = str + "<td>" + msg[i].createtime + "</td>";
                        str = str + "<td>" + msg[i].update_time + "</td>";
                        str = str + "</tr>";
                    }
                    str = str + "</tbody></table>";
                    $("#snsDetail-" + userId).append(str.toString());
                } else {
                    alert("未绑定第三方任何信息");
                    return;
                }
            }
        });
        $("#snsDetail-" + userId).toggle();
    }
</script>
