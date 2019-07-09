<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理员管理</title>
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
    登录名称： <input style="width: 200px;" id="login_name" type="text" name="login_name" class="abc input-default"
                 placeholder="" value="">&nbsp;
    用户名称： <input style="width: 200px;" id="user_name" type="text" name="user_name" class="abc input-default"
                 placeholder="" value="">&nbsp;
    身份证号： <input id="idcard" type="text" name="idcard" class="abc input-default" placeholder="" value="">&nbsp;
    <p></p>
    电话： <input style="width: 130px" id="phone" type="text" name="phone" class="abc input-default" placeholder=""
               value="">&nbsp;
    店铺： <input id="shop_name" type="text" name="shop_id" class="abc input-default" placeholder="" value="">&nbsp;
    选择角色：<s:select list="#{0:'--选择角色--'}" listKey="key" cssStyle="width:110px;" listValue="value" value=""
                   id="role_id"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效',2:'冻结',3:'已删除'}" listKey="key" listValue="value" headerKey=""
                  headerValue="--选择状态--" value="" cssStyle="width:110px" id="state"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    密钥： <input id="param_key" type="password" class="abc input-default" placeholder="密钥" value="">&nbsp;
    <button type="button" class="btn btn-success" onclick="paramConfirm()">用户确认</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>账户标识<input type="checkbox" name="checkAll" onclick="checkAll()"></th>
            <th>登录账户</th>
            <th>用户姓名</th>
            <th>身份证号</th>
            <th>电话</th>
            <th>用户到期时间</th>
            <th>所属店铺</th>
            <th>所属角色</th>
            <th>账号类型</th>
            <th>是否已验证</th>
            <th>用户状态</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
        <!-- 			<tr><td colspan="20">465</td></tr>		 -->
    </table>
</div>
</body>
</html>
<script>
    listdata(1);
    var dataList;

    //加载管理员
    function listdata(currentPage) {
        dataList = new Array();
        var login_name = $("#login_name").val();
        var user_name = $("#user_name").val();
        var phone = $("#phone").val();
        var idcard = $("#idcard").val();
        var shop_name = $("#shop_name").val();
        var role_id = $("#role_id").val();
        var state = $("#state").val();
        $.ajax({
            type: "post",
            data: {
                status: state,
                login_name: login_name,
                user_name: user_name,
                phone: phone,
                idcard: idcard,
                shop_name: shop_name,
                role_id: role_id,
                isMakeSure: 0,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/user/loadadminlist.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                dataList.length = 0;
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    //账户标识0	登录账户1	用户姓名3 身份证号8	电话9	用户到期时间14	所属店铺22	所属角色21   账号类型16	用户状态17  操作
                    var obj = data.msg.list[i];
                    strs = strs + "<tr><td><input type='checkbox' name='checkSub' onclick='checkSub()' value='" + obj[0] + "'>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    strs = strs + "<td>" + obj[8] + "</td>";
                    strs = strs + "<td>" + obj[9] + "</td>";
                    strs = strs + "<td>" + obj[14] + "</td>";
                    strs = strs + "<td>" + obj[22] + "</td>";
                    strs = strs + "<td>" + obj[21] + "</td>";
                    //店铺成员类型 0:管理员1:店长2:店员
                    if (obj[16] == "0") {
                        strs = strs + "<td>管理员</td>";
                    } else if (obj[16] == "1") {
                        strs = strs + "<td>店长</td>";
                    } else if (obj[16] == "2") {
                        strs = strs + "<td>店员</td>";
                    }
                    if (obj[23] == "1") {
                        strs = strs + "<td >已验证</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>未验证</td>";
                    }
                    if (obj[17] == "1") {
                        strs = strs + "<td >有效</td>";
                    } else if (obj[17] == "0") {
                        strs = strs + "<td style='color: red;'>无效</td>";
                    } else if (obj[17] == "3") {
                        strs = strs + "<td style='color: red;'>已删除</td>";
                    } else if (obj[17] == "2") {
                        strs = strs + "<td style='color: red;'>已冻结</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>未知</td>";
                    }
                    strs = strs + "</tr>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
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
    function delOrUpdateAdmin(status, admin_id) {

        $.ajax({
            type: "post",
            data: {
                admin_id: admin_id,
                status: status
            },
            async: false,
            cache: false,
            url: "${path}/user/delOrUpdateAdmin.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata(1);
                } else if (data.flag == "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    listRole();

    //加载角色列表
    function listRole() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/user/getListRole.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value='0' selected='selected'>--选择角色--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + data.msg[i].role_id + "'> " + data.msg[i].role_name + " </option>"
                    }
                }
                // 						$("#list_role").html(str);
                $("#role_id").html(str);
            }
        });
    }

    //选择全选
    function checkAll() {
        var checkAll = $("input[name=checkAll]").is(':checked');
        if (checkAll) {
            $("input[name=checkSub]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkSub]:checkbox").prop("checked", false);
        }
    }

    function checkSub() {
        var checkSub = $("input[name=checkSub]:checkbox");
        var flag = false;
        checkSub.each(function () {
            if (false == $(this).prop("checked")) {
                flag = true;
                return false;
            }
        });
        if (!flag) {
            //如果子项全部选中,则全选选中
            $("input[name=checkAll]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkAll]:checkbox").prop("checked", false);
        }
    }

    function paramConfirm() {
        var paramKey = $("#param_key").val() + "";
        if (null == paramKey || "" == paramKey) {
            alert("密钥不能为空");
            return;
        }
        //处理选中的数据
        var ids = "";
        var checkSub = $("input[name=checkSub]:checkbox");
        checkSub.each(function () {
            if (true == $(this).prop("checked")) {
                ids = ids + this.value + ",";
            }
        });
        if ('' == ids) {
            alert("请选中要确认的用户");
            return;
        }

        $.ajax({
            type: "post",
            data: {ids: ids, paramKey: paramKey},
            async: false,
            cache: false,
            url: "${path}/manage/userConfirm.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    listdata(1);
                } else {
                    alert(data.msg);
                }
            }
        });
    }
</script>
