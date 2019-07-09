<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>全国地址查看</title>
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
<form class="form-inline definewidth m20" action="index.html" method="get">
    行政区划前2位代码： <input style="width: 100px" id="regionCode" maxlength="2" type="text" name="regionCode"
                      class="abc input-default" placeholder="" value="">&nbsp;
    行政区划名称(包含下级)： <input id="regionName" type="text" name="regionName" class="abc input-default" placeholder=""
                         value="">&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--全部--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    选择区域分类： <s:select list="#{0:'其它',1:'省',2:'自治区',3:'直辖市',4:'特别行政区'}" listKey="key" listValue="value" headerKey="-1"
                      headerValue="--全部--" value="" cssStyle="width:110px" id="regionType"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>标识</th>
            <th>行政区划代码</th>
            <th>行政区划名称</th>
            <th>行政区划短名称</th>
            <th>行政区划英文名称</th>
            <th>行政区划简称</th>
            <th>区域分类</th>
            <th>运费</th>
            <th>排序</th>
            <th>状态</th>
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

    //加载渠道
    function listdata(currentPage) {
        var regionCode = $("#regionCode").val();//
        var regionName = $("#regionName").val();//
        var regionType = $("#regionType").val();//
        var status = $("#status").val();//状态
        $.ajax({
            type: "post",
            data: {
                regionCode: regionCode,
                regionName: regionName,
                status: status,
                regionType: regionType,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadAddrDetail",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var obj = data.msg.list[i];
                    var strs = "";
                    strs = strs + "<tr style='cursor:pointer' onclick='javaScript:loadAddrSub(2," + obj[1] + "," + obj[0] + ")'><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[1] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    strs = strs + "<td>" + obj[7] + "</td>";
                    strs = strs + "<td>" + obj[8] + "</td>";
                    //区域分类(0:其它1:省2:自治区3:直辖市4:特别行政区)
                    if (obj[9] == 1) {
                        strs = strs + "<td>省</td>";
                    } else if (obj[9] == 2) {
                        strs = strs + "<td>自治区</td>";
                    } else if (obj[9] == 3) {
                        strs = strs + "<td>直辖市</td>";
                    } else if (obj[9] == 4) {
                        strs = strs + "<td>特别行政区</td>";
                    } else {
                        strs = strs + "<td>其它</td>";
                    }
                    strs = strs + "<td>" + obj[15] + "</td>";
                    strs = strs + "<td>" + obj[6] + "</td>";
                    if (obj[13] == "1") {
                        strs = strs + "<td >有效</td>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                    }
                    strs = strs + "</tr>";
                    strs = strs + "<tr><td colspan='15'><div  style='display: none'  name='div-level' id='level" + obj[0] + "'></div></td></tr>";
                    $("#tb").append(strs.toString());
                }
                var totalPages = data.msg.totalPages;
                var currentPage = data.msg.currentPage;
                var totalRows = data.msg.totalRows;
                if (data.msg.list.length > 0) {
                    var strs = "<tr><td colspan='30'>";
                    strs = strs + "<div class='inline pull-right page'>";
                    strs = "共" + strs + data.msg.totalRows + "条记录 " + data.msg.currentPage + "/" + data.msg.totalPages + " 页  <a style='cursor:pointer;' onclick='listdata(" + data.msg.firstPage + ")'>第一页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.previousPage + ")'>上一页</a> ";

                    strs = strs + "<span id='listPage'>" + getPageList(currentPage, totalPages, totalRows) + "  </span>";//onkeyup='this.value=this.value.replace(/[^\d]/g,/'')'

                    strs = strs + "<a style='cursor:pointer;' onclick='listdata(" + data.msg.nextPage + ")'>下一页</a><a style='cursor:pointer;' onclick='next5Page(" + currentPage + "," + totalPages + "," + totalRows + ")'>下5页</a><a style='cursor:pointer;' onclick='listdata(" + data.msg.lastPage + ")'>最后一页</a> <input  id='topagecount'  type='text' name='topagecount'  style='width: 30px;text-align: center;'  class='abc input-default' value=''><a onclick='gotoByIndex(" + totalPages + "," + totalRows + ")' style='cursor:pointer;font-size: 25px;font-weight: bold;'>跳转</a></div>";
                    strs = strs + "</td></tr>";
                    $("#tb").append(strs.toString());
                } else {
                    $("#tb").append("<tr><td colspan='30' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //加载子地区
    function loadAddrSub(level, regionCode, id) {
        var regionName = $("#regionName").val();//详细地址
        if ($("#level" + id).css('display') == 'none') {
            $("#level" + id).show();
        } else {
            $("#level" + id).hide();
            return;
        }
        //层级0:国家1:省/自治区/直辖市/特别行政区2:市/省(自治区)直辖县/省直辖区/自治州3:市辖区/县/自治县4:乡/镇/街道5:村
        var levelName = "";
        if (level == 1) {
            levelName = "《省/自治区/直辖市/特别行政区》";
        } else if (level == 2) {
            levelName = "《市/省(自治区)直辖县/省直辖区/自治州》";
        } else if (level == 3) {
            levelName = "《市辖区/县/自治县》";
        } else if (level == 4) {
            levelName = "《乡/镇/街》";
        } else if (level == 5) {
            levelName = "《村》";
        }
        $.ajax({
            type: "post",
            data: {level: level, regionCode: regionCode, regionName: regionName},
            async: false,
            cache: false,
            url: "${path}/manage/loadAddrListSub.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    var list = data.list;
                    $("#level" + id).empty();
                    if (list.length > 0) {
                        var strs = "";
                        strs = strs + "<table class='table table-bordered table-hover definewidth m10'>"
                            + "<thead><tr><th colspan='15'>《" + data.regionName + "》共查询 " + levelName + " 结果记录：" + list.length + " 条</th></tr>"
                            + "<tr>"
                            + "<th>标识</th>"
                            + "<th>行政区划代码</th>"
                            + "<th>行政区划名称</th>"
                            + "<th>运费</th>"
                            + "<th>排序</th>"
                            + "<th>状态</th>"
                            + "</tr>"
                            + "</thead>"
                            + "<tbody>";
                        var levelSub = Number(level + 1);
                        for (var i = 0; i < list.length; i++) {
                            strs = strs + "<tr style='cursor:pointer' onclick='javaScript:loadAddrSub(" + levelSub + "," + list[i].region_code + "," + list[i].region_id + ")'>";
                            strs = strs + "<td>" + list[i].region_id + "</td>";
                            strs = strs + "<td>" + list[i].region_code + "</td>";
                            strs = strs + "<td>" + list[i].region_name + "</td>";
                            strs = strs + "<td>" + list[i].freight + "</td>";
                            strs = strs + "<td>" + list[i].region_order + "</td>";
                            var status = list[i].status;
                            if (status == 1) {
                                strs = strs + "<td >有效</td>";
                            } else {
                                strs = strs + "<td style='color: red;'>无效</td>";
                            }
                            strs = strs + "<tr><td colspan='15'><div style='display: none'  name='div-level' id='level" + list[i].region_id + "'></div></td></tr>";
                        }
                        strs = strs + "</tbody></table>";
                        $("#level" + id).append(strs.toString());
                    }
                } else {
                    // alert(data.msg);
                }
            }
        });
    }
</script>
