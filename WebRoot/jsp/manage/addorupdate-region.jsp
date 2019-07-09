<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>增加主页菜单</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>

    <script type="text/javascript" src="${path}/js/DatePicker/WdatePicker.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        #region {
            width: 100%;
            height: 40%;
            float: left;
            overflow: scroll;
        }

        .table td, .table th {
            padding-top: 8px;
            padding-bottom: 4px;
            line-height: 20px;
            text-align: center;
            vertical-align: middle;
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
    <div id="region">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>行政区划代码：<input id="regionCode" type="text" value=""> 名称：<input id="regionName" type="text" value="">
                    <button type="button" class="btn btn-primary" onclick="listRegion()"> 搜索</button>
                    &nbsp;&nbsp;
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>标识</th>
                <th>类目名称</th>
            </tr>
            </thead>
            <tbody id="tbRegion">

            </tbody>
        </table>
    </div>
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${id }" name="region.regionId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>行政区划代码
            </td>
            <td><input type="text" name="region.regionCode" id="regionCode_" value="${region.regionCode}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>行政区划名称
            </td>
            <td><input type="text" name="region.regionName" id="regionName_" value="${region.regionName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>行政区划短名称
            </td>
            <td><input type="text" name="region.regionShortname" id="regionShortname"
                       value="${region.regionShortname}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>父行政区划代码
            </td>
            <td><input type="text" name="region.parentId" id="parentId" value="${region.parentId}"/><span
                    style="color: red;"> 如果层级是1,则填 000000000000 </span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>层级
            </td>
            <td colspan="3"><s:select list="#{1:'省/自治区/直辖市/特别行政区',2:'市/省(自治区)直辖县/省直辖区/自治州',3:'市辖区/县/自治县',4:'乡/镇/街道'}"
                                      listKey="key" listValue="value" headerKey="5" headerValue="村"
                                      value="region.regionLevel" cssStyle="width:250px" id="regionLevel"
                                      name="region.regionLevel"/> <span>层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>行政区划英文名称
            </td>
            <td><input type="text" name="region.regionNameEn" id="regionNameEn" value="${region.regionNameEn}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>行政区划英文简称
            </td>
            <td><input type="text" name="region.regionShortnameEn" id="regionShortnameEn"
                       value="${region.regionShortnameEn}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>区域分类
            </td>
            <td><s:select list="#{1:'省',2:'自治区',3:'直辖市',4:'特别行政区'}" listKey="key" listValue="value" headerKey="0"
                          headerValue="其它" value="region.regionType" name="region.regionType" cssStyle="width:110px"
                          id="regionType"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td><input type="text" name="region.regionOrder" id="regionOrder" value="${region.regionOrder}"/> <span
                    style="color: red;"> 数字越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>运费
            </td>
            <td><input type="text" name="region.freight" id="freight" value="${region.freight}"/> <span
                    style="color: red;"> </span></td>
            <td><span style="color: red;"> </span>
            </td>
            <td></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button onclick="submitForm()" class="btn btn-primary" type="button">保存</button>
                &nbsp;&nbsp;
                <button type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack()">返回列表
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

        var regionCode_ = $("#regionCode_").val() + "";
        if (regionCode_.trim() == '') {
            alert("行政区划代码不能为空");
            return;
        }

        var regionName_ = $("#regionName_").val() + "";
        if (regionName_.trim() == '') {
            alert("行政区划名称不能为空");
            return;
        }

        var parentId = $("#parentId").val() + "";
        if (parentId.trim() == '') {
            alert("父行政区划代码不能为空");
            return;
        }


        //排序
        var regionOrder = $("#regionOrder").val() + "";
        if (regionOrder != 0) {
            if (!checkNumber(regionOrder)) {
                alert("排序非法");
                return;
            }
        }

        var freight = $("#freight").val() + "";
        if (!checkMoney(freight)) {
            alert("运费金额非法");
            return false;
        }

        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/manage/saveRegionStatus.action",
            type: 'POST',
            data: data,
            dataType: 'JSON',
            cache: false,
            processData: false,
            contentType: false
        }).done(function (ret) {
            var flag = ret.flag;
            if (flag === "ok") {
                alert(ret.msg);
                window.location.href = "${path}/jsp/manage/list_addr_manage.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //金钱校验
    function checkMoney(value) {
        var re = /^(\d*)?(\.)?\d+$/;
        if (!re.test($.trim(value))) {
            return false;
        }
        return true;
    }

    listRegion();

    function listRegion() {
        var regionCode = $("#regionCode").val();
        var regionName = $("#regionName").val();
        $.ajax({
            type: "post",
            data: {regionCode: regionCode, regionName: regionName},
            async: false,
            cache: false,
            url: "${path}/manage/loadTbRegionListMap",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tbRegion").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr><td>" + obj[i].region_code + "</td><td>" + obj[i].region_name + "</td></tr>";
                        $("#tbRegion").append(str);
                    }
                } else {
                    $("#tbRegion").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_addr_manage.jsp";
    }

    //数字校验
    function checkNumber(value) {
        var reg = /^[0-9]*$/;
        if (!reg.test($.trim(value))) {
            return false;
        }
        return true;
    }

</script>