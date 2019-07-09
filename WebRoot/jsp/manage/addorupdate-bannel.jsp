<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>增加bannel</title>
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
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${bannel.bannelId }" name="bannel.bannelId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>标题
            </td>
            <td><input type="text" name="bannel.bannelTitle" id="bannelTitle" value="${bannel.bannelTitle}"/></td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;"><span
                    style="color: red;">* </span>图片<br><span style="color: red;">建议尺寸:640x238</span></td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>名称
            </td>
            <td>
                <input type="text" name="bannel.bannelName" id="bannelName" value="${bannel.bannelName}"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>状态
            </td>
            <td>
                <c:if test="${bannel.status=='1'|| bannel.status!='0' }">
                    <input type="radio" name="bannel.status" value="1" checked/> 启用
                    <input type="radio" name="bannel.status" value="0"/> 无效
                </c:if>
                <c:if test="${bannel.status=='0' }">
                    <input type="radio" name="bannel.status" value="1"/> 启用
                    <input type="radio" name="bannel.status" value="0" checked/> 无效
                </c:if>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>链接
            </td>
            <td colspan="3"><textarea id="bannelLink" style="resize: none;width: 500px;" rows="3"
                                      name="bannel.bannelLink">${bannel.bannelLink }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>是否长期有效
            </td>
            <td>
                <c:choose>
                    <c:when test="${bannel.validDate=='1' }">
                        <input type="radio" name="bannel.validDate" value="0" onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="bannel.validDate" value="1" checked="checked"
                               onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="bannel.validDate" value="0" checked="checked"
                               onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="bannel.validDate" value="1"
                               onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>开始时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text"
                       name="beginTime" id="beginTime" placeholder="生效时间"
                       value="<fmt:formatDate value="${bannel.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">渠道</td>
            <td><s:select list="list_channel" listKey="channel_id" listValue="channel_name" id="list_role"
                          name="bannel.channelId" value="bannel.channelId" headerKey="0" headerValue="--选择渠道--"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>结束时间
            </td>
            <td>
                <c:choose>
                    <c:when test="${bannel.validDate=='1' }">
                        <input readonly="readonly" disabled="disabled"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                               type="text" name="endTime" id="endTime"
                               value="<fmt:formatDate value="${bannel.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "
                               placeholder="结束时间"/>
                    </c:when>
                    <c:otherwise>
                        <input readonly="readonly"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                               type="text" name="endTime" id="endTime"
                               value="<fmt:formatDate value="${bannel.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/> "
                               placeholder="结束时间"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>所属商家
            </td>
            <td><s:select id="partnersId" list="listPartners" listKey="id" listValue="partners_name"
                          name="bannel.partnersId" value="bannel.partnersId" headerKey="0" headerValue="--选择商家--"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>权重
            </td>
            <td><input type="text" name="bannel.weight" id="weight" required="required" value="${bannel.weight }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')" style="width: 100px"/> <span
                    style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="bannel.remark"
                                      id="remark">${bannel.remark }</textarea></td>
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
            url: "${path}/manage/addOrupdatebannel.action",
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
                window.location.href = "${path}/jsp/manage/list_bannel.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    function checkData() {
        //标题
        var bannelTitle = $("#bannelTitle").val() + "";
        if (bannelTitle.trim() == '') {
            alert("标题不能为空");
            return true;
        }
        //名称
        var bannelName = $("#bannelName").val() + "";
        if (bannelName.trim() == '') {
            alert("名称不能为空");
            return true;
        }
        //链接
        var bannelLink = $("#bannelLink").val() + "";
        if (bannelLink.trim() == '') {
            alert("链接不能为空");
            return true;
        }
        //开始时间
        var beginTime = $("#beginTime").val() + "";
        if (beginTime.trim() == '') {
            alert("开始时间不能为空");
            return true;
        }
        //结束时间
        var validDate = $("input[name='bannel.validDate']:checked").val();
        if (validDate == 0) {
            var endTime = $("#endTime").val() + "";
            if (endTime.trim() == '') {
                alert("结束时间不能为空");
                return true;
            }
        }
        //权重
        var weight = $("#weight").val() + "";
        if (weight == '' || (weight.length > 1 && weight.substring(0, 1) == '0')) {
            alert("权重为正整数");
            return true;
        }
        //所属商家
        var partnersId = $("#partnersId").val() + "";
        if (partnersId == '0') {
            alert("商家不能为空");
            return true;
        }

        var pictrue = '${photoPath}';
        if (ifchangeImg == "0" && pictrue.indexOf("null.jpg") > 0) {
            alert("请上传图片");
            return true;
        }

        return false;
    }

    function onclickRadio() {
        var validDate = $("input[name='bannel.validDate']:checked").val();
        if (validDate == '1') {
            $("#endTime").attr("disabled", "disabled");
        } else {
            $("#endTime").attr("disabled", false);
        }
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_bannel.jsp";
    }
</script>