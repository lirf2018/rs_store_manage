<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>角色功能管理</title>
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

        #left {
            width: 30%;
            height: 100%;
            float: left;
        }

        #right {
            width: 70%;
            height: 100%;
            float: right;
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
<div class="form-inline definewidth m20">
    <table style="width: 100%">
        <tr>
            <td align="center">
                <button type="button" class="btn btn-success" onclick="chooseAll()">全部选择</button>&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-success" onclick="unchooseAll()">全部取消</button>&nbsp;&nbsp;&nbsp;
                <button type="button" class="btn btn-success" onclick="accredit()">授权</button>
            </td>
        </tr>
    </table>
</div>
<input type="hidden" id="f_c" value="0"><!-- 功能数量 -->
<div id="left">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>角色名称</th>
        </tr>
        </thead>
        <tbody id="tb_role">

        </tbody>
    </table>
</div>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>父级菜单名称</th>
            <th>子级菜单名称</th>
        </tr>
        </thead>
        <tbody id="tb_function">

        </tbody>
    </table>
    <div style="height: 50px"></div>
</div>
</body>
</html>
<script>

    listRole();

    //加载角色
    function listRole() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/rolefunction/getListRole_.action",
            dataType: "json",
            success: function (data) {
                $("#tb_role").empty();
                var strs_sub = "";
                for (var i = 0; i < data.msg.length; i++) {
                    strs_sub = "<tr style='cursor:pointer;' onclick='radioOnclick(" + data.msg[i].role_id + ")'>";
                    if (i == 0) {
                        strs_sub = strs_sub + "<td> <input  checked='checked' value=" + data.msg[i].role_id + " type='radio' id='radio" + data.msg[i].role_id + "' name='role_radio'></td>";
                    } else {
                        strs_sub = strs_sub + "<td> <input  value=" + data.msg[i].role_id + " type='radio' id='radio" + data.msg[i].role_id + "' name='role_radio'></td>";
                    }
                    strs_sub = strs_sub + "<td>" + data.msg[i].role_name + "</td>";
                    strs_sub = strs_sub + "</tr>";
                    $("#tb_role").append(strs_sub.toString());
                }
                listFunction($("input[type='radio']:checked").val());
            }
        });
    }

    //用于保存父级菜单数量
    //加载菜单
    function listFunction(role_id) {
        $.ajax({
            type: "post",
            data: {role_id: role_id},
            async: false,
            cache: false,
            url: "${path}/rolefunction/getListFunctions_.action",
            dataType: "json",
            success: function (data) {
                $("#tb_function").empty();
                var stb = "";
                var p_array = new Array();
                var s_array = new Array();
                var index = 0;
                var index_s = 0;
                for (var i = 0; i < data.msg.length; i++) {
                    //是否为主菜单
                    if (data.msg[i].function_type == "0" || data.msg[i].function_type == 0) {
                        if (p_array.length > 0) {
                            if (s_array.length == 0) {
                                stb = "<tr>" + p_array[0] + "<td></td></tr>";
                                stb = stb.replace("_num_", "1");
                            } else {
                                stb = "<tr>" + p_array[0] + "" + s_array[0] + "</tr>";
                                stb = stb.replace("_num_", s_array.length);
                                for (var j = 1; j < s_array.length; j++) {
                                    stb = stb + "<tr>" + s_array[j] + "</tr>";
                                }
                            }
                            $("#tb_function").append(stb.toString());
                            p_array.length = 0;
                            s_array.length = 0;
                        }
                        if (data.msg[i].role_id != 0) {
                            p_array.push("<td rowspan='_num_'><input checked='checked' value='" + data.msg[i].function_id + "' type='checkbox' name='selectAll" + index + "' onclick='selectAll(1," + index + ")'>" + data.msg[i].function_name + "</td>");
                        } else {
                            p_array.push("<td rowspan='_num_'><input value='" + data.msg[i].function_id + "' type='checkbox' name='selectAll" + index + "' onclick='selectAll(1," + index + ")'>" + data.msg[i].function_name + "</td>");
                        }
                        index_s = index;
                        index = index + 1;
                    } else {
                        index_s = index_s + 0;
                        if (data.msg[i].role_id != 0) {
                            s_array.push(" <td><input checked='checked' value='" + data.msg[i].function_id + "' type='checkbox' name='subcheck" + index_s + "' onclick='selectAll(2," + index_s + ")'>" + data.msg[i].function_name + "</td> ");
                        } else {
                            s_array.push(" <td><input value='" + data.msg[i].function_id + "' type='checkbox' name='subcheck" + index_s + "' onclick='selectAll(2," + index_s + ")'>" + data.msg[i].function_name + "</td> ");
                        }
                    }
                    if (i == data.msg.length - 1) {
                        if (p_array.length > 0) {
                            if (s_array.length == 0) {
                                stb = "<tr>" + p_array[0] + "<td></td></tr>";
                                stb = stb.replace("_num_", "1");
                            } else {
                                stb = "<tr>" + p_array[0] + "" + s_array[0] + "</tr>";
                                stb = stb.replace("_num_", s_array.length);
                                for (var j = 1; j < s_array.length; j++) {
                                    stb = stb + "<tr>" + s_array[j] + "</tr>";
                                }
                            }
                            $("#tb_function").append(stb.toString());
                            p_array.length = 0;
                            s_array.length = 0;
                        }
                    }
                }
                $("#f_c").val(index);
            }
        });
    }

    //功能复选框事件
    //功能全选、取消全选的事件  type 用于区分功能   index用于区分主菜单
    function selectAll(type, index) {
        if (type == "1") {//点击主功能菜单
            if ($("input[name='selectAll" + index + "']").attr("checked") == "checked") {//选中一个主功能菜单
                $("input[name='subcheck" + index + "']").attr("checked", true);
            } else {
                $("input[name='subcheck" + index + "']").attr("checked", false);
            }
        } else {//点击子功能菜单
            var obj = $("input[name='subcheck" + index + "']");  //选择所有name="'subcheck+index'"的对象，返回数组
            for (var i = 0; i < obj.length; i++) {
                if (obj.eq(i).attr("checked")) {
                    $("input[name='selectAll" + index + "']").attr("checked", true);
                    break;
                } else {
                    $("input[name='selectAll" + index + "']").removeAttr("checked");//取消选中
                }
            }
        }
    }

    //角色点击事件
    function radioOnclick(role_id) {
        $("#radio" + role_id).prop("checked", true);
        listFunction(role_id);
    }

    //全部选择
    function chooseAll() {
        var f_c = $("#f_c").val();
        for (var i = 0; i < f_c; i++) {
            $("input[name='selectAll" + i + "']").attr("checked", true);
            $("input[name='subcheck" + i + "']").attr("checked", true);
        }
    }

    //全部取消
    function unchooseAll() {
        var f_c = $("#f_c").val();
        for (var i = 0; i < f_c; i++) {
            $("input[name='selectAll" + i + "']").removeAttr("checked");//取消选中
            $("input[name='subcheck" + i + "']").removeAttr("checked");//取消选中
        }
    }

    //授权
    function accredit() {
        //得到用户角色
        var role_id = $("input[type='radio']:checked").val();
        var f_c = $("#f_c").val();
        var s = "";
        for (var i = 0; i < f_c; i++) {
            var obj_p = $("input[name='selectAll" + i + "']");  //选择所有name="'subcheck+index'"的对象，返回数组
            if (obj_p.attr("checked") == "checked") {
                s = s + obj_p.val() + ",";
            }
            var obj = $("input[name='subcheck" + i + "']");  //选择所有name="'subcheck+index'"的对象，返回数组
            for (var j = 0; j < obj.length; j++) {
                if (obj.eq(j).attr("checked")) {
                    s = s + obj.eq(j).val() + ",";
                }
            }
        }
        $.ajax({
            type: "post",
            data: {role_id: role_id, function_ids: s},
            async: false,
            cache: false,
            url: "${path}/rolefunction/addOrupdateRoleFunction.action",
            dataType: "json",
            success: function (data) {
                if (data.flag = "ok") {
                    alert(data.msg);
                } else if (data.flag = "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }
</script>
