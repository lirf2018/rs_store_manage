<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改规则</title>
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
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${rule.ruleId }" name="rule.ruleId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>规则名称
            </td>
            <td><input type="text" name="rule.ruleName" id="ruleName" value="${rule.ruleName}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>规则编码
            </td>
            <td><input type="text" name="rule.ruleCode" id="ruleCode" value="${rule.ruleCode}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>规则类别
            </td>
            <td><s:select list="#{0:'商家规则',2:'卡券规则'}" listKey="key" listValue="value" headerKey="1" headerValue="商品规则"
                          name="rule.ruleType" value="rule.ruleType" cssStyle="width:180px;"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${rule.status=='0'}">
                        <input type="radio" name="rule.status" value="1"/> 启用
                        <input type="radio" name="rule.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="rule.status" value="1" checked/> 启用
                        <input type="radio" name="rule.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>规则属于
            </td>
            <td colspan="3"><s:select list="listPartners" listKey="id" listValue="partners_name" headerKey="0"
                                      headerValue="通用" name="rule.ruleBelong" value="rule.ruleBelong"
                                      cssStyle="width:180px;"/><span style="color: red;"> 如果规则指定了商家,则只能对应指定的商家使用</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>是否确认
            </td>
            <td colspan="3"><input type="radio" checked="checked" name="rule.isMakeSure" value="0"/> 需要确认<span
                    style="color: red;">(在重要情况下,只有确认过的才会有效) </span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>规则值
            </td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="rule.ruleValue"
                                      id="ruleValue">${rule.ruleValue }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">规则描述</td>
            <td colspan="3"><input style="width: 700px" type="text" name="rule.ruleDesc" id="ruleDesc"
                                   value="${rule.ruleDesc}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="rule.remark"
                                      id="remark">${rule.remark }</textarea></td>
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
        //规则名称
        var ruleName = $("#ruleName").val() + "";
        if (ruleName.trim() == '') {
            alert("规则名称不能为空");
            return;
        }
        //规则编码
        var ruleCode = $("#ruleCode").val() + "";
        if (ruleCode.trim() == '') {
            alert("规则编码不能为空");
            return;
        }
        //规则值
        var ruleValue = $("#ruleValue").val() + "";
        if (ruleValue.trim() == '') {
            alert("规则值不能为空");
            return;
        }

        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "${path}/manage/addOrUpdateRule.action",
            type: 'POST',
            data: $('#form_submit').serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_rule.jsp";
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
        window.location.href = "${path}/jsp/manage/list_rule.jsp";
    }

</script>