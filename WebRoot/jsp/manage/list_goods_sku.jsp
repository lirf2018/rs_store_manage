<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>商品sku管理</title>
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
    商品标识： <input id="goodsId" type="text" class="abc input-default" placeholder="" value="" style="width: 50px">&nbsp;
    商品名称： <input id="goodsName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    商品sku名称： <input id="skuName" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    商品sku属性编码： <input id="propCode" type="text" class="abc input-default" placeholder="" value="">&nbsp;
    <button type="button" class="btn btn-primary" onclick="listdata(1)">查询</button> &nbsp;
</form>
<div id="right">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>商品标识</th>
            <th>sku标识</th>
            <th>商品名称</th>
            <th>sku名称</th>
            <th>sku编码</th>
            <th>sku图片</th>
            <th>成本价</th>
            <th>原价</th>
            <th>现价</th>
            <th>sku属性编码</th>
            <th>库存</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">

        </tbody>
    </table>
    <!-- 		<img width="100px" height="100px" style="border: solid 6px;border-color: black;"  src="http://pic1a.nipic.com/2008-12-04/2008124215522671_2.jpg"> -->
</div>
</body>
</html>
<script>
    listdata(1);
    var currentPage;//当前页
    var index;//下标
    var totalPages;//总共页数
    var str_page;//当前页html

    //加载渠道
    function listdata(currentPage) {
        var goodsName = $("#goodsName").val();//
        var skuName = $("#skuName").val();//
        var propCode = $("#propCode").val();//
        var goodsId = $("#goodsId").val();//
        $.ajax({
            type: "post",
            data: {
                goodsName: goodsName,
                skuName: skuName,
                propCode: propCode,
                goodsId: goodsId,
                currentPage: currentPage
            },
            async: false,
            cache: false,
            url: "${path}/manage/loadGoodsSKUDataList.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                for (var i = 0; i < data.msg.list.length; i++) {
                    var strs = "";
                    var obj = data.msg.list[i];
                    // 1商品标识 sku标识0      商品名称2      sku名称3      sku编码6      sku图片9      原价4      现价5      sku属性编码7      库存 8  成本 价10
                    strs = strs + "<tr><td>" + obj[1] + "</td>";//
                    strs = strs + "<td>" + obj[0] + "</td>";//
                    strs = strs + "<td>" + obj[2] + "</td>";//
                    strs = strs + "<td>" + obj[3] + "</td>";//
                    strs = strs + "<td>" + obj[6] + "</td>";//
                    strs = strs + "<td><img width='100px' height='100px' style='border: solid 1px;border-color: black;' src='" + obj[9] + "'></td>";
                    strs = strs + "<td>" + obj[10] + "</td>";//
                    strs = strs + "<td>" + obj[4] + "</td>";//
                    strs = strs + "<td>" + obj[5] + "</td>";//
                    strs = strs + "<td>" + obj[7] + "</td>";//
                    strs = strs + "<td>" + obj[8] + "</td>";//
                    strs = strs + "<td><a style='cursor:pointer;'onclick='toAddOrUpdatePage(" + obj[0] + ")'>查看/编辑</a></td></tr>";
// 							strs=strs+"<td><a style='cursor:pointer;' onclick='delOrUpdate("+obj[0]+","+obj[1]+")'>删除</a>|<a style='cursor:pointer;'onclick='toAddOrUpdatePage("+obj[0]+")'>查看/编辑</a></td></tr>";
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

    //删除(由于存在其它属性值,所以不能删除)
    /* function delOrUpdate(skuId,goodsId){

        if(!confirm("确认要删除？")){
            return;
         }
          $.ajax({
                type: "post",
                data: {skuId:skuId,goodsId:goodsId},
                async: false,
                cache: false,
                url: "${path}/manage/deleteGoodsSKUStatus.action",
					dataType:"json",
					success: function(data){
						if(data.flag=="ok"){
							alert(data.msg);
							listdata(1);
						}else{
							alert(data.msg);
						}
					}
				});
		} */

    //跳转到新增或者修改页面
    function toAddOrUpdatePage(id) {
        window.location.href = "${path}/manage/toAddorUpdateGoodsSKUPage.action?op=update&skuId=" + id;
    }
</script>
