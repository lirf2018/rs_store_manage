<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>配送地址管理</title>
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
    地址前缀： <input style="width: 100px" id="addr_prefix" type="text" name="addr_prefix" class="abc input-default"
                 placeholder="" value="" onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    详细地址： <input id="detail_addr" type="text" name="detail_addr" class="abc input-default" placeholder="" value=""
                 onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    负责人/电话： <input style="width: 150px" id="responsible_man" type="text" name="responsible_man"
                   class="abc input-default" placeholder="" value=""
                   onkeyup="this.value=this.value.replace(/(^\s+)|(\s+$)/g,'')">&nbsp;
    地址类型： <s:select list="#{5:'配送地址',4:'自取地址',6:'还货地址'}" listKey="key" listValue="value" headerKey=""
                    headerValue="--地址类型--" value="" cssStyle="width:110px" id="addrType"/>&nbsp;
    状态： <s:select list="#{1:'有效',0:'无效'}" listKey="key" listValue="value" headerKey="" headerValue="--选择状态--" value=""
                  cssStyle="width:110px" id="status"/>&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
    <button type="button" class="btn btn-success" id="addParentMenu" onclick="toAddOrUpdatePage(0)">新增配送地址</button>
    &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>地址前缀</th>
            <th>排序字母</th>
            <th>地址名称</th>
            <th>负责人</th>
            <th>负责人电话</th>
            <th>运费</th>
            <th>排序</th>
            <th>地址类型</th>
            <th>详细地址</th>
            <th>状态</th>
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

    //加载渠道
    function listdata(currentPage) {
        var addr_prefix = $("#addr_prefix").val();//前缀
        var detail_addr = $("#detail_addr").val();//详细地址
        var responsible_man = $("#responsible_man").val();//负责人
        var status = $("#status").val();//状态
        var addrType = $("#addrType").val();//
        $.ajax({
            type: "post",
            data: {
                addrPrefix: addr_prefix,
                detailAddr: detail_addr,
                responsible: responsible_man,
                addrType: addrType,
                status: status,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadDistributionAddrList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var obj = data.msg.list[i];
                    var strs = "";
                    strs = strs + "<tr><td>" + obj[0] + "</td>";
                    strs = strs + "<td>" + obj[7] + "</td>";
                    strs = strs + "<td>" + obj[5] + "</td>";
                    strs = strs + "<td>" + obj[11] + "</td>";
                    strs = strs + "<td>" + obj[2] + "</td>";
                    strs = strs + "<td>" + obj[3] + "</td>";
                    strs = strs + "<td>" + obj[4] + "</td>";
                    strs = strs + "<td>" + obj[9] + "</td>";

                    if (obj[6] == 4) {
                        strs = strs + "<td>自取</td>";
                    } else if (obj[6] == 5) {
                        strs = strs + "<td>配送</td>";
                    } else if (obj[6] == 6) {
                        strs = strs + "<td>还货</td>";
                    } else {
                        strs = strs + "<td></td>";
                    }


                    strs = strs + "<td>" + obj[1] + "</td>";
                    if (obj[8] == "1") {
                        strs = strs + "<td >有效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(0," + obj[0] + ")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;'>无效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delOrUpdate(1," + obj[0] + ")'>启用</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
                    }
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

    //删除或者启用菜单
    function delOrUpdate(status, id) {
        $.ajax({
            type: "post",
            data: {id: id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delDistributionAddrstatus.action",
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

    //跳转到新增或者修改页面
    function toAddOrUpdatePage(id) {
        if ("0" == id) {//新增
            window.location.href = "${path}/manage/toauDistributionAddrPage.action?op=add";
        } else {//修改
            var op = "update";
            window.location.href = "${path}/manage/toauDistributionAddrPage.action?op=" + op + "&id=" + id;
        }
    }
</script>
