<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>增加caleve1</title>
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
    <input type="hidden" value="${haddle }" name="haddle">
    <input type="hidden" value="${caleve1.levelId }" name="caleve1.levelId" id="levelId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>分类名称
            </td>
            <td><input type="text" name="caleve1.levelName" id="levelName" value="${caleve1.levelName}"/></td>
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
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>编码
            </td>
            <td>
                <input type="text" name="caleve1.levelCode" id="levelCode" value="${caleve1.levelCode}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>状态
            </td>
            <td>
                <c:if test="${caleve1.status=='1'|| caleve1.status!='0' }">
                    <input type="radio" name="caleve1.status" value="1" checked/> 启用
                    <input type="radio" name="caleve1.status" value="0"/> 无效
                </c:if>
                <c:if test="${caleve1.status=='0' }">
                    <input type="radio" name="caleve1.status" value="1"/> 启用
                    <input type="radio" name="caleve1.status" value="0" checked/> 无效
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>权重
            </td>
            <td colspan="3"><input type="text" name="caleve1.levelSort" id="levelSort" required="required"
                                   value="${caleve1.levelSort }" onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
                                   style="width: 100px"/> <span style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td colspan="4" style="width: 100%">
                <table class="table table-bordered table-hover m10">
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;">类目列表</td>
                        <td colspan="3">
                            类目关键词: <input id="keyWord" type="text" class="abc input-default" placeholder="" value=""
                                          onkeyup="searchData()"> &nbsp;
                            <button type="button" class="btn btn-primary" onclick="searchData()">搜索</button>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">是否保留当前已存在且类目为正常状态下的分类类目关系</span></td>
                        <td colspan="3">
                            <input type="radio" name="ifLeaveRel" value="0" checked/> 不保留
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
                            <!-- 		   		  		<tbody id="tb"> -->
                            <div style="line-height: 38px" id="tb">
                                <c:forEach items="${list_category}" var="item">
                                    <c:choose>
                                        <c:when test="${item.level_id==caleve1.levelId }">
                                            <input type="checkbox" checked="checked" name="categoryIds"
                                                   value="${item.category_id }"/> ${item.category_name_code }&nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${empty item.level_id || item.level_id==''}">
                                                <input type="checkbox" name="categoryIds"
                                                       value="${item.category_id }"/> ${item.category_name_code }&nbsp;&nbsp;
                                            </c:if>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <!-- 		   		  		</tbody> -->
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="caleve1.remark"
                                      id="remark">${caleve1.remark }</textarea></td>
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

        if (checkData()) {
            return;
        }
        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/manage/addOrUpdateCatogryLeve1.action",
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
                window.location.href = "${path}/jsp/manage/list_classfy_catogry_only.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    function checkData() {
        //名称
        var levelName = $("#levelName").val() + "";
        if (levelName.trim() == '') {
            alert("名称不能为空");
            return true;
        }
        //编码
        var levelCode = $("#levelCode").val() + "";
        if (levelCode.trim() == '') {
            alert("编码不能为空");
            return true;
        }
        //权重
        var weight = $("#levelSort").val() + "";
        if (weight == '' || (weight.length > 1 && weight.substring(0, 1) == '0')) {
            alert("权重为正整数");
            return true;
        }

        return false;
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_classfy_catogry_only.jsp";
    }

    function searchData() {
        var keyWord = $("#keyWord").val() + "";
        var levelId_ = $("#levelId").val() + "";
        $.ajax({
            type: "post",
            data: {keyWord: keyWord.trim()},
            async: false,
            cache: false,
            url: "${path}/manage/listClassfyCatogryMap.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                var msg = data.msg;
                var str = "";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        var levelId = data.msg[i].level_id;
                        var categoryId = data.msg[i].category_id;
                        var categoryNameCode = data.msg[i].category_name_code;
                        if (levelId == levelId_ && levelId_ != '') {
                            str = str + "<input type='checkbox' checked='checked' name='categoryIds' value='" + categoryId + "'/> " + categoryNameCode + "&nbsp;&nbsp;";
                        } else {
                            str = str + "<input type='checkbox'  name='categoryIds' value='" + categoryId + "'/> " + categoryNameCode + "&nbsp;&nbsp;";
                        }
                    }
                }
                $("#tb").html(str);
            }
        });
    }
</script>