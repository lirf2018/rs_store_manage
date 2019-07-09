<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加管理员</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bui.css"/>
    <!--     <script type="text/javascript" src="${path}/assets/js/jquery.js"></script> -->
    <script type="text/javascript" src="${path}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bui.js"></script>
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
<form id="form_submit" class="definewidth m20">
    <input type="hidden" value="${admin.adminId }" name="admin.adminId">
    <input type="hidden" value="${handleType }" name="handleType">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td width="10%" class="tableleft" style="text-align:center;vertical-align:middle;"
                style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>所属店铺
            </td>
            <td><s:select list="listShop" listKey="shop_id" listValue="shop_name" headerKey="0" headerValue="--选择店铺--"
                          name="admin.shopId" id="shop_id" value="admin.shopId"/><span
                    style="color: red;">*管理员可不选择店铺</span></td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;">头像<br><span
                    style="color: red;">建议尺寸:100*100</span></td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>登录名称
            </td>
            <td><input id="loginName" type="text" name="admin.loginName" value="${admin.loginName }"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>用户姓名
            </td>
            <td><input id="userName" type="text" name="admin.userName" value="${admin.userName }"/></td>

        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">出生日期</td>
            <td><input type="text" readonly="readonly" onClick="WdatePicker({minDate:'%y-%M-%d'})" name="admin.birthday"
                       id="birthday" value="<fmt:formatDate value="${admin.birthday}" pattern="yyyy-MM-dd"/>"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>店铺成员类型
            </td>
            <td><s:select list="#{1:'店长',2:'店员' }" listKey="key" listValue="value" headerKey="0"
                          headerValue="--选择店铺成员类型--" name="admin.shopMenberType" value="admin.shopMenberType"
                          id="shopMenberType"/><span style="color: red;">*管理员可不选择</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>身份证号码
            </td>
            <td><input id="idCart" type="text" name="admin.idcard" value="${admin.idcard }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">QQ</td>
            <td><input type="text" name="admin.qq" value="${admin.qq }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">email</td>
            <td><input type="text" name="admin.email" value="${admin.email }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">昵称</td>
            <td><input type="text" name="admin.nickName" value="${admin.nickName }"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">性别</td>
            <td><s:select list="#{2:'女'}" headerKey="1" headerValue="男" value="admin.sex" name="admin.sex"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>电话
            </td>
            <td><input id="phone" type="text" name="admin.phone" value="${admin.phone }"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>用户到期时间
            </td>
            <td colspan="3">
                <input type="text" readonly="readonly" onClick="WdatePicker({minDate:'%y-%M-%d'})"
                       name="admin.memberBegintime" id="begintime"
                       value="<fmt:formatDate value="${admin.memberBegintime}" pattern="yyyy-MM-dd"/>"/>
                -- <input type="text" readonly="readonly" onClick="WdatePicker({minDate:'%y-%M-%d'})"
                          name="admin.memberEndtime" id="endTimeStr"
                          value="<fmt:formatDate value="${admin.memberEndtime}" pattern="yyyy-MM-dd"/>"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${admin.status=='1' }">
                        <input type="radio" name="admin.status" value="1" checked/> 启用
                        <input type="radio" name="admin.status" value="0"/> 无效
                        <input type="radio" name="admin.status" value="2"/> 冻结
                        <input type="radio" name="admin.status" value="3"/> 删除
                    </c:when>
                    <c:when test="${admin.status=='2' }">
                        <input type="radio" name="admin.status" value="1"/> 启用
                        <input type="radio" name="admin.status" value="0"/> 无效
                        <input type="radio" name="admin.status" value="2" checked/> 冻结
                        <input type="radio" name="admin.status" value="3"/> 删除
                    </c:when>
                    <c:when test="${admin.status=='3' }">
                        <input type="radio" name="admin.status" value="1"/> 启用
                        <input type="radio" name="admin.status" value="0"/> 无效
                        <input type="radio" name="admin.status" value="2"/> 冻结
                        <input type="radio" name="admin.status" value="3" checked/> 删除
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="admin.status" value="1"/> 启用
                        <input type="radio" name="admin.status" value="0" checked/> 无效
                        <input type="radio" name="admin.status" value="2"/> 冻结
                        <input type="radio" name="admin.status" value="3"/> 删除
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>密码
            </td>
            <td>
                <input id="passwd" type="password" name="admin.loginPassword" value="${admin.loginPassword }"
                       maxlength="50"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="admin.remark"
                                      id="remark">${admin.remark}</textarea></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button class="btn btn-primary" type="button" onclick="form_submit()">保存</button> &nbsp;&nbsp;<button
                    type="button" class="btn btn-success" name="backid" id="backid">返回列表
            </button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
<script>

    $(function () {
        $('#backid').click(function () {
            window.location.href = "${path}/jsp/sys/list_admin.jsp";
        });

    });

    //提交表单
    function form_submit() {
        var flag = checkData();
        if (flag) {
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/user/addOrUpdateadmin.action",
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
                window.location.href = "${path}/jsp/sys/list_admin.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    function checkData() {
        var shop_id = $("#shop_id").val();
        var shopMenberType = $("#shopMenberType").val();
        if (!((shop_id == '0' && shopMenberType == '0') || (shop_id != '0' && shopMenberType != '0'))) {
            alert("如果添加账户为管理员，则‘所属店铺’和‘店铺成员类型’项都不用选择,反之则都要选择");
            return true;
        }

        //登录名称
        var loginName = $("#loginName").val() + "";
        if (loginName.trim() == '') {
            alert("登录名称不能为空");
            return true;
        }
        //用户姓名
        var userName = $("#userName").val() + "";
        if (userName.trim() == '') {
            alert("用户姓名不能为空");
            return true;
        }
        //电话
        var phone = $("#phone").val() + "";
        if (phone.trim() == '') {
            alert("电话不能为空");
            return true;
        }
        //身份证号码
        //电话
        var idCart = $("#idCart").val() + "";
        if (idCart.trim() == '') {
            alert("身份证不能为空");
            return true;
        }
        //密码
        var passwd = $("#passwd").val() + "";
        if (passwd.trim() == '') {
            alert("密码不能为空");
            return true;
        }
        //用户到期时间开始
        var bTime = $("#begintime").val() + "";
        if (passwd.trim() == '') {
            alert("用户到期开始时间不能为空");
            return true;
        }
        //用户到期时间结束
        var eTime = $("#begintime").val() + "";
        if (passwd.trim() == '') {
            alert("用户到期结束时间不能为空");
            return true;
        }
        if (bTime > eTime) {
            alert("用户到期结束时间不能小于用户到期开始时间");
            return true;
        }
        return false;
    }

</script>
<!-- script start -->
<script type="text/javascript">
    var Calendar = BUI.Calendar;
    var datepicker = new Calendar.DatePicker({
        trigger: '.calendar',
        delegateTrigger: true,
        autoRender: true
    });
</script>
<!-- script end -->