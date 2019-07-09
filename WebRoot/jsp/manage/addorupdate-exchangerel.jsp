<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改卡券商家</title>
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

        #left {
            width: 50%;
            height: 80%;
            float: left;
            overflow: scroll;
        }

        #right {
            width: 50%;
            height: 80%;
            float: left;
            overflow: scroll;
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
<form action="" method="post" class="definewidth m20" id="form_submit">
    <div id="right">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>卡券名称：<input id="ticketName" type="text" value="">
                    <button type="button" class="btn btn-primary" onclick="listTicket()">搜索</button>
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>选择</th>
                <th>卡券名称</th>
            </tr>
            </thead>
            <tbody id="tb_ticket">

            </tbody>
        </table>
    </div>
    <div id="left">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>商家名称：<input id="pName" type="text" value="">
                    <button type="button" class="btn btn-primary" onclick="listPartners(0)">搜索</button>
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>选择</th>
                <th>商家名称</th>
            </tr>
            </thead>
            <tbody id="tb_partners">

            </tbody>
        </table>
    </div>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否长期有效
            </td>
            <td colspan="3">
                <input type="radio" name="validDate" value="0" checked="checked" onclick="onclickRadio()"> 限时有效
                <input type="radio" name="validDate" value="1" onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>开始时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text"
                       name="beginTime" id="beginTime" placeholder="生效时间" value=""/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>结束时间
            </td>
            <td>
                <input readonly="readonly"
                       onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                       type="text" name="endTime" id="endTime" value="" placeholder="结束时间"/>
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

    $(document).ready(function () {
        listPartners(0);
        listTicket();
    });

    //查询商家列表
    function listPartners(ticketId) {
        $.ajax({
            type: "post",
            data: {pName: $("#pName").val(), ticketId: ticketId},
            async: false,
            cache: false,
            url: "${path}/manage/listPartners.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_partners").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr style='cursor:pointer;' onclick='partnersCheckBox(" + obj[i].id + ")'><td><input id='partnersCheckBox" + obj[i].id + "' value='" + obj[i].id + "' type='checkbox' name='partnersIds' ></td><td>" + obj[i].partners_name + "</td></tr>";
                        $("#tb_partners").append(str);
                    }
                } else {
                    $("#tb_partners").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    //查询卡券列表
    function listTicket() {
        $.ajax({
            type: "post",
            data: {ticketName: $("#ticketName").val()},
            async: false,
            cache: false,
            url: "${path}/manage/listTicketsMap.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_ticket").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr style='cursor:pointer;' onclick='ticketRadio(" + obj[i].tikcet_id + ")'><td><input id='ticketRadio" + obj[i].tikcet_id + "' value='" + obj[i].tikcet_id + "' type='radio' name='ticketId' ></td><td>" + obj[i].tikcet_name + "</td></tr>";
                        $("#tb_ticket").append(str);
                    }
                } else {
                    $("#tb_ticket").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    //提交表单
    function submitForm() {
        var flag = checkData();
        if (!flag) {
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/manage/addOrUpdateExchange.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_exchangectrol.jsp";
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    function checkData() {

        var ticketId = $("input[name='ticketId']:checked").val();
        if (null == ticketId) {
            alert("请选择卡券");
            return false;
        }
        var partnersId = $("input[name='partnersIds']:checked").val();
        if (null == partnersId) {
            alert("请选择商家");
            return false;
        }
        var validDate = $("input[name='validDate']:checked").val();
        var beginTime = $("#beginTime").val() + "";
        if (beginTime.trim() == '') {
            alert("请选择开始时间");
            return false;
        }
        if (validDate == 0) {
            var endTime = $("#endTime").val() + "";
            if (endTime.trim() == '') {
                alert("请选择结束时间");
                return false;
            }
        }
        return true;
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_exchangectrol.jsp";
    }

    function onclickRadio() {
        var validDate = $("input[name='validDate']:checked").val();
        if (validDate == '1') {
            $("#endTime").attr("disabled", "disabled");
        } else {
            $("#endTime").attr("disabled", false);
        }
    }

    function partnersCheckBox(id) {
        if ($("#partnersCheckBox" + id).prop("checked")) {
            $("#partnersCheckBox" + id).prop("checked", false);
        } else {
            $("#partnersCheckBox" + id).prop("checked", true);
        }

    }

    function ticketRadio(id) {
        listPartners($("#ticketRadio" + id).val());
        $("#ticketRadio" + id).prop("checked", true);
    }
</script>