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

        .sidebar-nav {
            padding: 9px 0;
        }

        #left {
            width: 50%;
            height: 60%;
            float: left;
            overflow: scroll;
        }

        #right {
            width: 50%;
            height: 60%;
            float: left;
            overflow: scroll;
        }

        .category {
            width: 100%;
            height: 20%;
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
    <script type="text/javascript">
        var ifchangeImg = "0";

        function previewImage(file) {
            var MAXWIDTH = 100;
            var MAXHEIGHT = 100;
            var div = document.getElementById('preview');
            if (file.files && file.files[0]) {
                div.innerHTML = '<img id=imghead>';
                var img = document.getElementById('imghead');
                img.onload = function () {
                    //var imgrealywidth=img.offsetWidth;//图片实际宽
                    //var imgrealyheigth=img.offsetHeight;//图片实际高
                    var imgrealywidth = 100;
                    var imgrealyheigth = 100;

                    var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, imgrealywidth, imgrealyheigth);
                    img.width = rect.width;
                    img.height = rect.height;
                    img.style.marginLeft = rect.left + 'px';
                    img.style.marginTop = rect.top + 'px';
                }
                var reader = new FileReader();
                reader.onload = function (evt) {
                    img.src = evt.target.result;
                }
                reader.readAsDataURL(file.files[0]);
                ifchangeImg = "1";
            } else {
                var sFilter = 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
                file.select();
                var src = document.selection.createRange().text;
                div.innerHTML = '<img id=imghead>';
                var img = document.getElementById('imghead');
                img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
                status = ('rect:' + rect.top + ',' + rect.left + ',' + rect.width + ',' + rect.height);
                div.innerHTML = "<div id=divhead style='width:" + rect.width + "px;height:" + rect.height + "px;margin-top:" + rect.top + "px;margin-left:" + rect.left + "px;" + sFilter + src + "\"'></div>";
                ifchangeImg = "1";
            }
        }

        function clacImgZoomParam(maxWidth, maxHeight, width, height) {
            var param = {top: 0, left: 0, width: width, height: height};
            if (width > maxWidth || height > maxHeight) {
                rateWidth = width / maxWidth;
                rateHeight = height / maxHeight;
                if (rateWidth > rateHeight) {
                    param.width = maxWidth;
                    param.height = Math.round(height / rateWidth);
                } else {
                    param.width = Math.round(width / rateHeight);
                    param.height = maxHeight;
                }
            }
            param.left = Math.round((maxWidth - param.width) / 2);
            param.top = Math.round((maxHeight - param.height) / 2);
            return param;
        }
    </script>
</head>
<body>
<form action="" method="post" class="definewidth m20" id="form_submit">
    <div id="right">
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>选择</th>
                <th>标识</th>
                <th>一级分类名称</th>
            </tr>
            </thead>
            <tbody id="tb_leve">
            <c:choose>
                <c:when test="${!empty listLeve_}">
                    <c:forEach items="${listLeve_}" var="item">
                        <c:choose>
                            <c:when test="${item.selectLeveid==1}">
                                <tr>
                                    <td><input onclick="changeChoseCategroyLeve1()" value='${item.level_id}'
                                               checked="checked" type='checkbox' name='leveIds'></td>
                                    <td>${item.level_id}</td>
                                    <td>${item.level_name}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td><input onclick="changeChoseCategroyLeve1()" value='${item.level_id}'
                                               type='checkbox' name='leveIds'></td>
                                    <td>${item.level_id}</td>
                                    <td>${item.level_name}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${listLeve}" var="item">
                        <tr>
                            <td><input onclick="changeChoseCategroyLeve1()" value='${item.level_id}' type='checkbox'
                                       name='leveIds'></td>
                            <td>${item.level_id}</td>
                            <td>${item.level_name}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            <c:if test="${listLeveSize==0}">
                <tr>
                    <td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </div>
    <div id="left">
        <table class="table table-bordered table-hover definewidth m10">
            <tr>
                <td>类目名称：<input id="categroyName" type="text" value="" style="width: 100px">
                    一级分类:
                    <select id="Leve1Id">
                        <option value="0">--选择一级分类--</option>
                        <c:forEach items="${listLeve}" var="item">
                            <option value="${item.level_id}">${item.level_name}</option>
                        </c:forEach>
                    </select>
                    <button type="button" class="btn btn-primary" onclick="listCategory()"> 搜索</button>
                    &nbsp;&nbsp;
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>标识</th>
                <th>类目名称</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="tb_category">

            </tbody>
        </table>
    </div>
    <input type="hidden" value="${handdleType }" name="handdleType">
    <input type="hidden" value="${mainMenu.id }" name="mainMenu.id">
    <table class="table table-bordered table-hover definewidth m10">
        <tr>
            <td>已选择的类目名称</td>
        </tr>
        <tr>
            <td>
                <div id="selectCategory" style="text-align: left">
                    <c:if test="${!empty listCategory}">
                        <c:forEach items="${listCategory}" var="item">
                            <span style="padding: 8px 5px;margin: 3px 5px"><input type="checkbox"
                                                                                  onclick="hasChoseCategroyOnclick()"
                                                                                  checked name="hasChoseCategroy"
                                                                                  value=" ${item.category_id}"> ${item.category_id}-${item.category_name}</span>
                        </c:forEach>
                    </c:if>
                </div>
            </td>
        </tr>
    </table>
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>名称
            </td>
            <td><input type="text" name="mainMenu.menuName" id="menuName" value="${mainMenu.menuName}"/></td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;"><span
                    style="color: red;">* </span>图片<br><span style="color: red;">建议尺寸:100*100</span></td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td>
                <input type="text" name="mainMenu.menuSort" id="menuSort" required="required"
                       value="${mainMenu.menuSort }" onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                       style="width: 100px"/> <span style="color: red;">只能输入数字,数值越大越靠前</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>链接模板
            </td>
            <td>
                <input id="menuUrl" name="mainMenu.menuUrl" type="text" style="width: 500px" value="${menuUrl}">
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"> </span>链接地址</td>
            <td colspan="3"><span style="color: red;">${mainMenu.menuUrl}</span>
            </td>
        </tr>
        <tr>
            <td colspan="4" style="text-align: center">手动增加(当手动增加关联时,以手动增加为主)</td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>关联一级分类
            </td>
            <td colspan="3"><textarea id="leveIds" style="resize: none;width: 500px;" rows="3"
                                      name="mainMenu.leve1Ids">${mainMenu.leve1Ids}</textarea><span style="color: red;"> 一级分类标识,用','隔开,12，13</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> </span>关联类目
            </td>
            <td colspan="3"><textarea id="categoryIds" style="resize: none;width: 500px;" rows="3"
                                      name="mainMenu.categoryIds">${mainMenu.categoryIds}</textarea><span
                    style="color: red;"> 类目标识,用','隔开,12，13</span>
            </td>
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

        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/menu/addMainMenu.action",
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
                window.location.href = "${path}/jsp/sys/list-mainmenu.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //    listLeve();
    function listLeve() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/menu/listLeve",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_leve").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr><td><input value='" + obj[i].level_id + "' type='checkbox' name='leveIds' onclick='choseCategroyLeve1(this)' ></td><td>" + obj[i].level_id + "</td><td>" + obj[i].level_name + "</td></tr>";
                        $("#tb_leve").append(str);
                    }
                } else {
                    $("#tb_leve").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    listCategory();

    function listCategory() {
        var categroyName = $("#categroyName").val();
        var leveId = $("#Leve1Id").val();
        var obj = $("input[name='hasChoseCategroy']");
        var categorys = "";//已选择的类目
        obj.each(function () {
            categorys = categorys + $(this).val() + ",";
        });
        $.ajax({
            type: "post",
            data: {categroyName: categroyName, leveId: leveId, categorys: categorys},
            async: false,
            cache: false,
            url: "${path}/menu/listCatogory",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb_category").empty();
                var str = "";
                if (obj.length != 0) {
                    for (var i = 0; i < obj.length; i++) {
                        str = "<tr><td>" + obj[i].category_id + "</td><td>" + obj[i].category_name + "</td><td><input  id='categoryName" + obj[i].category_id + "' value='" + obj[i].category_name + "' type='hidden'><input value='选择' onclick='choseCategroy(" + obj[i].category_id + ")' type='button' class='btn btn-success' ></td></tr>";
                        $("#tb_category").append(str);
                    }
                } else {
                    $("#tb_category").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }

            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_bannel.jsp";
    }

    function choseCategroy(categoryId) {
        var categoryName = $("#categoryName" + categoryId).val();
        var obj = $("#categoryName" + categoryId);
        obj.parent().parent().remove();
        var html = "<span style='padding: 8px 5px;margin: 3px 5px'><input onclick='hasChoseCategroyOnclick()' type='checkbox' checked name='hasChoseCategroy' value=" + categoryId + ">" + categoryId + "-" + categoryName + "</span>";
        $("#selectCategory").append(html);
        changeChoseCategroy();
    }

    function hasChoseCategroyOnclick() {
        var obj = $("input[name='hasChoseCategroy']");
        obj.each(function () {
            if (!$(this).prop("checked")) {
                $(this).parent().remove();
            }
        });
        changeChoseCategroy();
    }

    //选中的一级分类
    function changeChoseCategroyLeve1() {
        var leveIdsChose = $("input[name='leveIds']");
        var leveIds = "";
        leveIdsChose.each(function (o) {
            if ($(this).is(':checked')) {
                leveIds = leveIds + $(this).val().trim() + ",";
            }
        })
        $("textarea[name='mainMenu.leve1Ids']").val(leveIds);
    }

    //选中的类目
    function changeChoseCategroy() {
        var hasChoseCategroy = $("input[name='hasChoseCategroy']");
        var categroyIds = "";
        hasChoseCategroy.each(function (o) {
            if ($(this).is(':checked')) {
                categroyIds = categroyIds + $(this).val().trim() + ",";
            }
        })
        $("textarea[name='mainMenu.categoryIds']").val(categroyIds);
    }
</script>