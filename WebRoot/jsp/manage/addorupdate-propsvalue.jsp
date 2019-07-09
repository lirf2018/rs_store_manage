<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加类目属性值</title>
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
    <input type="hidden" value="${propsValue.valueId}" id="valueId" name="propsValue.valueId"/>
    <input type="hidden" value="${haddle }" name="haddle" id="haddle">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> *</span>一级分类
            </td>
            <td>
                <s:select list="listClassfyCatogry" listKey="level_id" listValue="level_name" id="level" name="leveId"
                          value="leveId" headerKey="0" headerValue="--选择分类--" onchange="changeCatogry()"/>
            </td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;"><span
                    style="color: red;">* </span>图片<br><span style="color: red;">建议尺寸:960*480</span></td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>类目列表
            </td>
            <td>
                <c:choose>
                    <c:when test="${haddle=='update' }">
                        <s:select list="list_category" listKey="category_id" listValue="category_name_code" id="catogry"
                                  name="propsValue.categoryId" value="propsValue.categoryId" headerKey="0"
                                  headerValue="--选择类目--" onchange="changeProp()"/>
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{0:'--选择类目--'}" listKey="key" listValue="value" value="0" id="catogry"
                                  name="propsValue.categoryId" onchange="changeProp()"/>&nbsp;
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>类目属性
            </td>
            <td>
                <c:choose>
                    <c:when test="${haddle=='update' }">
                        <s:select list="list_itemprops" listKey="prop_id" listValue="prop_name" id="propId"
                                  name="propsValue.propId" value="propsValue.propId" headerKey="0"
                                  headerValue="--选择类目属性--"/>
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{0:'--选择类目属性--'}" listKey="key" listValue="value" value="0" id="propId"
                                  name="propsValue.propId"/>&nbsp;
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>属性值名称
            </td>
            <td colspan="3"><input type="text" name="propsValue.valueName" id="valueName"
                                   value="${propsValue.valueName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">外部标识</td>
            <td><input type="text" name="propsValue.outeId" id="outeId" value="${propsValue.outeId}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>属性值
            </td>
            <td><input type="text" name="propsValue.value" id="value" value="${propsValue.value}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>状态
            </td>
            <td>
                <%-- 	<c:choose>
                        <c:when test="${propsValue.status=='0'}">
                            <input type="radio" name="propsValue.status" value="1" /> 启用
                               <input type="radio" name="propsValue.status" value="0" checked /> 无效
                        </c:when>
                        <c:otherwise>
                            <input type="radio" name="propsValue.status" value="1" checked/> 启用
                               <input type="radio" name="propsValue.status" value="0"/> 无效
                        </c:otherwise>
                    </c:choose> --%>
                <input type="radio" name="propsValue.status" value="1" checked/> 启用
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td><input type="text" name="propsValue.short_" id="short_" value="${propsValue.short_ }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/> <span
                    style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="propsValue.remark"
                                      id="remark">${propsValue.remark }</textarea></td>
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

    //保存数据
    function submitForm() {
        //选择类目
        var catogry = $("#catogry").val();
        if (catogry == '0') {
            alert("选择类目");
            return;
        }
        //选择类目属性
        var propId = $("#propId").val();
        if (propId == '0') {
            alert("选择类目属性");
            return;
        }
        //属性值名称
        var valueName = $("#valueName").val() + "";
        if (valueName.trim() == '') {
            alert("属性名称不能为空");
            return;
        }
        //属性值
        var value = $("#value").val() + "";
        if (value.trim() == '') {
            alert("属性值不能为空");
            return;
        }
        //排序
        var sort = $("#short_").val();
        if (sort == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return;
        }

        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);

        $.ajax({
            url: "${path}/manage/addorUpdatePropsValue.action",
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
                window.location.href = "${path}/jsp/manage/list_props_value.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_props_value.jsp";
    }

    //修改类目
    function changeCatogry() {
        var leveId = $("#level").val();
        if ("0" == leveId || leveId == "") {
            $("#catogry").html("<option value='0'>--选择类目--</option>");
            return;
        }
        $("#propId").html("<option value='0'>--选择类目属性--</option>");
        $.ajax({
            type: "post",
            data: {leveId: leveId, status: "1"},
            async: false,
            cache: false,
            url: "${path}/manage/loadlistCategory.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                if ("" == msg) {
                    $("#catogry").html("<option value='0'>--选择类目--</option>");
                    return;
                }
                var str = "<option value='0'>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + msg[i].category_id + "'> " + msg[i].category_name_code + " </option>";
                    }
                } else {
                    $("#catogry").html("<option value='0'>--选择类目--</option>");
                }
                $("#catogry").html(str);
            }
        });
    }

    //修改类目属性
    function changeProp() {
        var catogry = $("#catogry").val();
        if ("0" == catogry || catogry == "") {
            $("#propId").html("<option value='0'>--选择类目属性--</option>");
            return;
        }
        $.ajax({
            type: "post",
            data: {category_id: catogry, status: "1"},
            async: false,
            cache: false,
            url: "${path}/manage/loadlistItemprops.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                if ("" == msg) {
                    $("#propId").html("<option value='0'>--选择类目属性--</option>");
                    return;
                }
                var str = "<option value='0'>--选择类目属性--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + msg[i].prop_id + "'> " + msg[i].prop_name + " </option>";
                    }
                } else {
                    $("#propId").html("<option value='0'>--选择类目属性--</option>");
                }
                $("#propId").html(str);
            }
        });
    }

</script>