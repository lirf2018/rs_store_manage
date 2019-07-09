<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改商家规则</title>
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
    <input type="hidden" value="" name="relId" id="relId">
    <div id="right">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>商家名称：<input id="partnersName" type="text" value="">
                    <button type="button" class="btn btn-primary" onclick="listPartners()">搜索</button>
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
            <tbody id="tb_ticket">

            </tbody>
        </table>
    </div>
    <div id="left">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>规则名称：<input id="ruleName" type="text" value="">
                    <button type="button" class="btn btn-primary" onclick="listRule()">搜索</button>&nbsp;&nbsp;
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>选择&nbsp;<input type="checkbox" name="checkAll" class="checkAll"></th>
                <th>规则名称</th>
            </tr>
            </thead>
            <tbody id="tb_rule">

            </tbody>
        </table>
    </div>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否长期有效
            </td>
            <td colspan="3">
                <input type="radio" name="rel.validDate" value="0" checked="checked" onclick="onclickRadio()"> 限时有效
                <input type="radio" name="rel.validDate" value="1" onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>开始时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text"
                       name="rel.startTime" id="beginTime" placeholder="生效时间" value=""/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>结束时间
            </td>
            <td>
                <input readonly="readonly"
                       onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                       type="text" name="rel.endTime" id="endTime" value="" placeholder="结束时间"/>
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
        listRule();
        listPartners();
        $("#beginTime").val(getNowFormatDate());
    });

    //查询规则列表
    function listRule() {
        var partnersId = $("#relId").val();
        $.ajax({
            type: "post",
            data: {ruleName: $("#ruleName").val(), partnersId: partnersId},
            async: false,
            cache: false,
            url: "${path}/manage/getAddPartnersRulePageListRule.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_rule").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr style='cursor:pointer;'  onclick='trCheckSubOnclick(this)'><td><input value='" + obj[i].rule_id + "' type='checkbox' name='checkSub' onclick='checkSubOnclick()' ></td><td>" + obj[i].rule_name + "</td></tr>";
                        $("#tb_rule").append(str);
                    }
                } else {
                    $("#tb_rule").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    //查询商家列表
    function listPartners() {
        $.ajax({
            type: "post",
            data: {partnersName: $("#partnersName").val()},
            async: false,
            cache: false,
            url: "${path}/manage/getAddPartnersRulePageListPartners.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_ticket").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr style='cursor:pointer;' onclick='onclickRelRadio(this)'><td><input  value='" + obj[i].id + "' type='radio' name='rel.relId' ></td><td>" + obj[i].partners_name + "</td></tr>";
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
            url: "${path}/manage/savePartnersRule.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_partnersrule.jsp";
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    function checkData() {
        var relId = $("input[name='relId']").val() + "";
        if (null == relId || relId.trim() == '') {
            alert("请选择商家");
            return false;
        }
        var checksub = $("input[name='checkSub']:checked");
        if (checksub.length == 0) {
            alert("请选择规则");
            return false;
        }
        var validDate = $("input[name='rel.validDate']:checked").val();
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
        window.location.href = history.go(-1);
    }

    //如果长期有效,不考虑结束时间
    function onclickRadio() {
        var validDate = $("input[name='rel.validDate']:checked").val();
        if (validDate == '1') {
            $("#endTime").attr("disabled", "disabled");
        } else {
            $("#endTime").attr("disabled", false);
        }
    }

    //选择商家
    function onclickRelRadio(obj) {
        var radio = $(obj).children("td").children("input");
        var isCheck = radio.prop("checked");
        if (!isCheck) {
            radio.prop("checked", true);
        }
        var value = radio.val();
        $("#relId").val(value);
        listRule();
    }

    //选择规则
    function trCheckSubOnclick(obj) {
        var checkSub = $(obj).children("td").children("input");
        var isCheck = checkSub.prop("checked");
        if (isCheck) {
            checkSub.prop("checked", false);
        } else {
            checkSub.prop("checked", true);
        }
        checkSubOnclick();
    }

    //全选
    $('.checkAll').click(function () {
        var checkAll = $(this).is(':checked');
        if (checkAll) {
            $("input[name=checkSub]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkSub]:checkbox").prop("checked", false);
        }
    });

    function checkSubOnclick() {
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
</script>