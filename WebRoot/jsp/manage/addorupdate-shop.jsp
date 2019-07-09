<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改店铺</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bui.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/upload.css"/>
    <!--     <script type="text/javascript" src="${path}/assets/js/jquery.js"></script> -->
    <script type="text/javascript" src="${path}/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bui.js"></script>
    <script type="text/javascript" src="${path}/js/DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${path}/js/shop.js"></script>

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
<!-- 店铺主图 -->
<form id="form_img0">
    <div style="display: none;"><input type="file" id="file0" name="file" onchange="fileUploadData(0)"></div>
</form>
<!-- 图片1 -->
<form id="form_img1">
    <div style="display: none;"><input type="file" id="file1" name="file" onchange="fileUploadData(1)"></div>
</form>
<!-- 图片2 -->
<form id="form_img2">
    <div style="display: none;"><input type="file" id="file2" name="file" onchange="fileUploadData(2)"></div>
</form>
<!-- 图片3 -->
<form id="form_img3">
    <div style="display: none;"><input type="file" id="file3" name="file" onchange="fileUploadData(3)"></div>
</form>
<!-- 图片4 -->
<form id="form_img4">
    <div style="display: none;"><input type="file" id="file4" name="file" onchange="fileUploadData(4)"></div>
</form>


<form id="form_submit" class="definewidth m20">
    <!-- 图片1-4 -->
    <input type="hidden" value="${shop.shopLogo }" name="shop.shopLogo" id="img_str0">
    <c:choose>
        <c:when test="${listImgSize==4 }">
            <c:forEach items="${listImg }" var="map">
                <input type="hidden" value="${map.img_url }" name="img_str" id="img_str${map.index }">
            </c:forEach>
        </c:when>
        <c:when test="${listImgSize==3 }">
            <input type="hidden" value="" name="img_str" id="img_str1">
            <c:forEach items="${listImg }" var="map">
                <input type="hidden" value="${map.img_url }" name="img_str" id="img_str${map.index }">
            </c:forEach>
        </c:when>
        <c:when test="${listImgSize==2 }">
            <input type="hidden" value="" name="img_str" id="img_str1">
            <c:forEach items="${listImg }" var="map">
                <input type="hidden" value="${map.img_url }" name="img_str" id="img_str${map.index }">
            </c:forEach>
            <input type="hidden" value="" name="img_str" id="img_str4">
        </c:when>
        <c:when test="${listImgSize==1}">
            <input type="hidden" value="" name="img_str" id="img_str1">
            <c:forEach items="${listImg }" var="map">
                <input type="hidden" value="${map.img_url }" name="img_str" id="img_str${map.index }">
            </c:forEach>
            <input type="hidden" value="" name="img_str" id="img_str3">
            <input type="hidden" value="" name="img_str" id="img_str4">
        </c:when>
        <c:otherwise>
            <input type="hidden" value="" name="img_str" id="img_str1">
            <input type="hidden" value="" name="img_str" id="img_str2">
            <input type="hidden" value="" name="img_str" id="img_str3">
            <input type="hidden" value="" name="img_str" id="img_str4">
        </c:otherwise>
    </c:choose>
    <!-- 用于标记是否有未上传控件,只用于页面展现 -->
    <c:choose>
        <c:when test="${listImgSize==4 }">
            <input type="hidden" value="0" id="ct">
        </c:when>
        <c:otherwise>
            <input type="hidden" value="1" id="ct">
        </c:otherwise>
    </c:choose>
    <input type="hidden" value="${shop.shopId }" name="shop.shopId">
    <input type="hidden" value="${handleType }" name="handleType">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td width="10%" class="tableleft" style="text-align:center;vertical-align:middle;"
                style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>店铺名称
            </td>
            <td><input type="text" name="shop.shopName" value="${shop.shopName }" id="shopName"/></td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;">店铺logo<br><span
                    style="color: red;">*建议尺寸:100*100 </span></td>
            <td rowspan="3">
                <c:choose>
                    <c:when test="${!empty shop.shopLogo && shop.shopLogo !='' }">
                        <div class="supload_div_">
                            <div class="simg_div_">
                                <div id="img_id0" name="img_name" class="simg_file_size_"
                                     onmousemove="changeImgFileColor(0, 0)" onmouseout="changeImgFileColor(0, 1)"
                                     onclick="imgOnclick(0)"><img id="img0" src="${imgPath }"></div>
                                <img id="img_delete0" style="position: relative;top: -98px;right: 4px;" width="30px"
                                     height="30px" src="../image/delete.png" onclick="deteleImg(this,0)"></div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="supload_div_">
                            <div class="simg_div_">
                                <div id="img_id0" name="img_name" class="simg_file_size_"
                                     onmousemove="changeImgFileColor(0, 0)" onmouseout="changeImgFileColor(0, 1)"
                                     onclick="imgOnclick(0)"><img id="img0" src="../image/upload.png"></div>
                                <img id="img_delete0" style="position: relative;top: -98px;right: 4px;display: none;"
                                     width="30px" height="30px" src="../image/delete.png" onclick="deteleImg(this,0)">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>电话1
            </td>
            <td><input type="text" name="shop.shopTel1" value="${shop.shopTel1 }" id="shopTel1"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">电话2</td>
            <td><input type="text" name="shop.shopTel2" value="${shop.shopTel2 }"/></td>

        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">电话3</td>
            <td><input type="text" name="shop.shopTel3" value="${shop.shopTel3 }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">电话4</td>
            <td><input type="text" name="shop.shopTel4" value="${shop.shopTel4 }"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">经度</td>
            <td><input type="text" name="shop.shopLng" value="${shop.shopLng }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>权重
            </td>
            <td><input id="weight" type="text" name="shop.weight" value="${shop.weight }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/><span
                    style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">纬度</td>
            <td><input type="text" name="shop.shopLat" value="${shop.shopLat }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">店铺余额</td>
            <td><input type="text" name="shop.shopMoney" value="${shop.shopMoney }" disabled="disabled"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">保证金</td>
            <td><input type="text" id="deposit" name="shop.deposit" value="${shop.deposit }"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">交保时间</td>
            <td><input id="depositTime" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                       type="text" name="shop.depositTime"
                       value="<fmt:formatDate value="${shop.depositTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"
                       placeholder="交保时间"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>合作到期时间
            </td>
            <td colspan="3"><input id="enterEndTime" readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"
                                   type="text" name="shop.enterEndTime"
                                   value="<fmt:formatDate value="${shop.enterEndTime}" pattern="yyyy-MM-dd"/>"
                                   placeholder="合作到期时间"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>店铺归属
            </td>
            <td><s:select list="#{1:'内部店铺',2:'第三方店铺'}" listKey="key" listValue="value" headerKey=""
                          headerValue="--选择店铺归属--" value="shop.isOutShop" name="shop.isOutShop" cssStyle="width:130px"
                          id="isOutShop"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${shop.status=='1' }">
                        <input type="radio" name="shop.status" value="1" checked/> 启用
                        <input type="radio" name="shop.status" value="0"/> 无效
                        <input type="radio" name="shop.status" value="2"/> 冻结
                        <input type="radio" name="shop.status" value="3"/> 删除
                    </c:when>
                    <c:when test="${shop.status=='2' }">
                        <input type="radio" name="shop.status" value="1"/> 启用
                        <input type="radio" name="shop.status" value="0"/> 无效
                        <input type="radio" name="shop.status" value="2" checked/> 冻结
                        <input type="radio" name="shop.status" value="3"/> 删除
                    </c:when>
                    <c:when test="${shop.status=='3' }">
                        <input type="radio" name="shop.status" value="1"/> 启用
                        <input type="radio" name="shop.status" value="0"/> 无效
                        <input type="radio" name="shop.status" value="2"/> 冻结
                        <input type="radio" name="shop.status" value="3" checked/> 删除
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="shop.status" value="1"/> 启用
                        <input type="radio" name="shop.status" value="0" checked/> 无效
                        <input type="radio" name="shop.status" value="2"/> 冻结
                        <input type="radio" name="shop.status" value="3"/> 删除
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">店铺介绍</td>
            <td colspan="3"><textarea style="resize: none;width: 450px;" rows="7" cols="30"
                                      name="shop.introduce">${shop.introduce}</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">店铺地址</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30"
                                      name="shop.address">${shop.address}</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">到达方式</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30"
                                      name="shop.toway">${shop.toway}</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">店铺介绍图片</td>
        </tr>
        <tr>
            <td colspan="4">
                <div class="upload_div">
                    <c:forEach items="${listImg }" var="map">
                        <div class="img_div">
                            <div id="img_id${map.index }" name="img_name" class="img_file_size"
                                 onmousemove="changeImgFileColor(${map.index }, 0)"
                                 onmouseout="changeImgFileColor(${map.index }, 1)" onclick="imgOnclick(${map.index })">
                                <img id="img${map.index }" src="${map.imgPath }"></div>
                            <img id="img_delete${map.index }" style="position: relative;top: -370px;left: -8px;"
                                 width="50px" height="50px" src="../image/delete.png"
                                 onclick="deteleImg(this,${map.index })"><input id="pxvalue${map.index }" name="px"
                                                                                type="hidden" value="${map.img_url }">
                        </div>
                    </c:forEach>
                    <c:if test="${listImgSize <4}">
                        <div class="img_div">
                            <div id="img_id1" name="img_name" class="img_file_size"
                                 onmousemove="changeImgFileColor(1, 0)" onmouseout="changeImgFileColor(1, 1)"
                                 onclick="imgOnclick(1)"><img id="img1" src="../image/upload.png"></div>
                            <img id="img_delete1" style="position: relative;top: -370px;right: 8px;display: none;"
                                 width="50px" height="50px" src="../image/delete.png" onclick="deteleImg(this,1)"><input
                                id="pxvalue1" name="px" type="hidden" value=""></div>
                    </c:if>
                </div>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 350px;" rows="7" cols="30"
                                      name="shop.remark">${shop.remark}</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">关联商家(店铺关联商家才能进行相关商家的业务处理)<input
                    type="checkbox" name="checkAll" class="checkAll"></td>
        </tr>
        <tr>
            <td style="text-align:left;vertical-align:middle;" colspan="4">
                <c:forEach items="${listShopPartnerRel}" var="data">
                    <c:choose>
                        <c:when test="${!empty shop.shopId && !empty data.rel_id && data.shop_id==shop.shopId}">
                            <input type="checkbox" name="checkSub" value="${data.id}" checked="checked"
                                   class="checkSub"/><span class="checkbox-class">${data.partners_name}</span>&nbsp;
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="checkSub" value="${data.id}" class="checkSub"><span
                                class="checkbox-class">${data.partners_name}</span>&nbsp;
                        </c:otherwise>
                    </c:choose>

                </c:forEach>
            </td>
        </tr>
        <tr>
            <td colspan="4" style="text-align:center;vertical-align:middle;">
                <button class="btn btn-primary" type="button" onclick="form_submit()">保存</button> &nbsp;&nbsp;<button
                    type="button" class="btn btn-success" name="backid" onclick="returnBackUrl()">返回列表
            </button>
            </td>
        </tr>
    </table>
</form>
<script>
    function returnBackUrl() {
        window.location.href = history.go(-1);
    }

    //提交表单
    function form_submit() {
        if (checkData()) {
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/manage/addOrUpdateshop.action",
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
                window.location.href = "${path}/jsp/manage/list_shop.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    function checkData() {
        var shopName = $("#shopName").val() + "";
        if (shopName.trim() == '') {
            alert("店铺名称不能为空");
            return true;
        }
        var shopTel1 = $("#shopTel1").val() + "";
        if (shopTel1.trim() == '') {
            alert("电话1不能为空");
            return true;
        }
        var deposit = $("#deposit").val() + "";
        if (!checkMoney(deposit)) {
            alert("保证金非法");
            return true;
        }
        if (deposit > 0) {
            var depositTime = $("#depositTime").val() + "";
            if (depositTime.trim() == '') {
                alert("交保时间不能为空");
                return true;
            }
        }
        var weight = $("#weight").val() + "";
        if (weight == '' || (weight.length > 1 && weight.substring(0, 1) == '0') || !checkNumber(weight)) {
            alert("权重为正整数");
            return true;
        }
        var isOutShop = $("#isOutShop").val() + "";
        if (isOutShop.trim() == '') {
            alert("请选择店铺归属");
            return true;
        }
        var enterEndTime = $("#enterEndTime").val() + "";
        if (enterEndTime == '') {
            alert("请输入合作到期时间");
            return true;
        }

        return false;
    }

    //金钱校验
    function checkMoney(value) {
        var re = /^(\d*)?(\.)?\d+$/;
        if (!re.test($.trim(value))) {
            return false;
        }
        return true;
    }

    //数字校验
    function checkNumber(value) {
        var reg = /^[0-9]*$/;
        if (!reg.test($.trim(value))) {
            return false;
        }
        return true;
    }

    //
    $('.checkAll').click(function () {
        var checkAll = $(this).is(':checked');
        if (checkAll) {
            $("input[name=checkSub]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkSub]:checkbox").prop("checked", false);
        }
    });

    $('.checkSub').click(function () {
        var checkSub = $("input[name=checkSub]:checkbox");
        var flag = false;
        checkSub.each(function () {
            if (false == $(this).prop("checked")) {
                flag = true;
                return false;
            }
        });
        if (!flag) {
            //如果子项全部选中,则全选选中
            $("input[name=checkAll]:checkbox").prop("checked", true);
        } else {
            $("input[name=checkAll]:checkbox").prop("checked", false);
        }
    });
</script>
</body>
</html>
<!-- script end -->