<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>类目列表</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css"
          href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css"
          href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css"
          href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>

    <style type="text/css">
        body {
            padding-bottom: 40px;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        #left {
            width: 40%;
            height: 95%;
            float: left;
        }

        #right {
            width: 60%;
            height: 95%;
            float: right;
        }

        .table td, .table th {
            padding-top: 8px;
            padding-bottom: 4px;
            line-height: 20 ppx;
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

        #preview {
            width: 50px;
            height: 50px;
            border: 1px solid #000;
            overflow: hidden;
        }

        #imghead {
            filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);
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
    <script type="text/javascript">
        function previewImage(file) {
            var MAXWIDTH = 50;
            var MAXHEIGHT = 50;
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
<!-- 	<form class="form-inline definewidth m20" action="index.html" method="get"> -->
<input type="hidden" value="${category_id }" id="categoryId_">
<input type="hidden" value="${leveId }" id="leveId_">
<table class="table table-bordered table-hover definewidth m10">
    <tr>
        <td>
            选择一级分类:&nbsp;<s:select list="#{0:'--一级分类--'}" listKey="key" cssStyle="width:210px" listValue="value"
                                   value="1" id="leveId" onchange="listdata()"/>&nbsp;
            <button type="button" class="btn btn-primary" onclick="listdata()">刷新</button>
        </td>
    </tr>
</table>
<div id="left">
    <table class="table table-bordered table-hover definewidth m10">
        <thead>
        <tr>
            <th>编号</th>
            <th>类目名称</th>
            <th>编码</th>
            <th>图片</th>
            <th>排序</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="tb">
        <c:forEach items="${list_category}" var="category">
            <tr>
                <td onclick="clickCategory(${category.category_id })"
                    style="cursor:pointer;">${category.category_id }</td>
                    <%-- <c:choose>
                        <c:when test="${category.index<8 }">
                            <td onclick="clickCategory(${category.category_id })" style="cursor:pointer;color: #4A4AFF">${category.category_name }</td>
                        </c:when>
                        <c:otherwise>
                            <td onclick="clickCategory(${category.category_id })" style="cursor:pointer;">${category.category_name }</td>
                        </c:otherwise>
                    </c:choose> --%>
                <td onclick="clickCategory(${category.category_id })"
                    style="cursor:pointer;">${category.category_name }</td>
                <td onclick="clickCategory(${category.category_id })"
                    style="cursor:pointer;">${category.category_code }</td>
                <td onclick="clickCategory(${category.category_id })" style="cursor:pointer;">
                    <img src="${category.category_img }" width="50px" height="50px">
                </td>
                <td onclick="clickCategory(${category.category_id })"
                    style="cursor:pointer;">${category.category_short }</td>
                <td onclick="clickCategory(${category.category_id })" style="cursor:pointer;">
                    <c:choose>
                        <c:when test="${category.status=='1' }">
                            <span>有效</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: red;">无效</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${category.status=='1' }">
                            <a style='cursor:pointer;' onclick="delorupdatecategory(0,${category.category_id})">删除</a>
                        </c:when>
                        <c:otherwise>
                            <a style='cursor:pointer;' onclick="delorupdatecategory(1,${category.category_id})">启用</a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <c:if test="${category.category_id==category_id }">
                <tr>
                    <td colspan="20">
                        <table class="table table-bordered table-hover definewidth m10">
                            <tr>
                                <th>属性名称</th>
                                <th>状态</th>
                                <th>排序</th>
                                <th>是否销售属性</th>
                                <th>操作</th>
                            </tr>
                            <c:forEach items="${list_itemprops }" var="itemprops">
                                <tr>
                                    <td onclick="clickItemprops(${category.category_id }, ${itemprops.prop_id })"
                                        style="cursor:pointer;">${itemprops.prop_name }</td>
                                    <td onclick="clickItemprops(${category.category_id }, ${itemprops.prop_id })"
                                        style="cursor:pointer;">
                                        <c:choose>
                                            <c:when test="${itemprops.status=='1' }">
                                                <span>有效</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;">无效</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td onclick="clickItemprops(${category.category_id }, ${itemprops.prop_id })"
                                        style="cursor:pointer;">${itemprops.sort }</td>
                                    <td onclick="clickItemprops(${category.category_id }, ${itemprops.prop_id })"
                                        style="cursor:pointer;">
                                        <c:choose>
                                            <c:when test="${itemprops.is_sales==0 }">
                                                不是
                                            </c:when>
                                            <c:otherwise>
                                                是
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${itemprops.status=='1' }">
                                                <a style='cursor:pointer;'
                                                   onclick="delorupdateitemprops(0,${itemprops.prop_id},${category.category_id })">删除</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a style='cursor:pointer;'
                                                   onclick="delorupdateitemprops(1,${itemprops.prop_id},${category.category_id })">启用</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${fn:length(list_itemprops)==0 || empty  list_itemprops}">
                                <tr>
                                    <td colspan="10" style="color: red;">暂无数据</td>
                                </tr>
                            </c:if>
                        </table>
                    </td>
                </tr>
            </c:if>
        </c:forEach>
        </tbody>
    </table>
</div>
<div id="right">
    <table class="table definewidth m10">
        <tr>
            <td style="border-top: 0px;">
                <button type="button" class="btn btn-success" onclick="toAddOrUpdatecategoryPage()">新增类目</button>
                <button type="button" class="btn btn-success" onclick="toItempropsPage()">新增类目属性</button>
            </td>
        </tr>
    </table>
    <!-- 类目增加表单 -->
    <c:if test="${clickObj=='category'&&click=='1' }">
    <div id="category">
        <form id="category_form" action="">
            <input type="hidden" value="${category_id }" name="category.categoryId" id="categoryId">
            <input type="hidden" value="${category_id }" name="category_id" id="category_id">
            <input type="hidden" value="${haddle }" name="haddle" id="haddle">
            <input type="hidden" value="0" name="leveId_rel" id="leveId_rel">
            <input type="hidden" value="${click }" name="click">
            <table class="table table-bordered table-hover definewidth m10">
                <tr>
                    <td><span><c:if test="${haddle=='add' }">新增类目</c:if><c:if
                            test="${haddle=='update' }">修改类目</c:if></span></td>
                </tr>
            </table>
            <table class="table table-bordered table-hover definewidth m10">
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                            style="color: red;">* </span>类目名称:
                    </td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <input type="text" name="category.categoryName" value="${category.categoryName }"
                               id="categoryName"/>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                            style="color: red;">* </span>类目编码:
                    </td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <input type="text" name="category.categoryCode" value="${category.categoryCode }"
                               id="categoryCode"/>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;">图片:<br><span
                            style="color: red;">建议尺寸:80*80</span></td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <div id="preview" style="text-align:center;vertical-align:middle;">
                            <img id="imghead" width="50px" height="50px" border="0" src='${photoPath}'></div>
                        <!--无预览时的默认图像，自己弄一个-->
                        <br/><input type="file" onchange="previewImage(this)" name="file"/>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;">外部标识:</td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <input type="text" name="category.outeId" value="${category.outeId }"/></td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                            style="color: red;">* </span>排序:
                    </td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <input type="text" name="category.short_" value="${category.short_ }" id="short_"
                               onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/><span style="color: red;">只能输入数字,数值越大越靠前</span>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                            style="color: red;">*</span>是否前端显示:
                    </td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <%-- 	<c:choose>
                                   <c:when test="${category.isShow=='0' }">
                                         <input type="radio" name="category.isShow"  value="1"  /> 前端显示  &nbsp;&nbsp;&nbsp;
                                        <input type="radio" name="category.isShow"  value="0" checked /> 前端不显示
                                   </c:when>
                                   <c:otherwise>
                                       <input type="radio" name="category.isShow"  value="1" checked/> 前端显示  &nbsp;&nbsp;&nbsp;
                                      <input type="radio" name="category.isShow"  value="0"  /> 前端不显示
                                   </c:otherwise>
                               </c:choose> --%>
                        <input type="radio" name="category.isShow" value="1" checked/> 前端显示
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                            style="color: red;">*</span>状态:
                    </td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <c:choose>
                            <c:when test="${category.status=='0' }">
                                <input type="radio" name="category.status" value="1"/> 启用&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="category.status" value="0" checked/> 无效
                            </c:when>
                            <c:otherwise>
                                <input type="radio" name="category.status" value="1" checked/> 启用&nbsp;&nbsp;&nbsp;
                                <input type="radio" name="category.status" value="0"/> 无效
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td class="tableleft" style="text-align:center;vertical-align:middle;">备注:</td>
                    <td class="tableleft" style="text-align:left;vertical-align:middle;">
                        <textarea style="resize: none;width: 500px;" rows="7" cols="30" name="category.remark"
                                  id="remark">${category.remark}</textarea></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="button" class="btn btn-primary" onclick="addcategory()">提交</button>
                    </td>
                </tr>
            </table>
        </form>
        </c:if>
        <!-- 类目属性增加表单 -->
        <c:if test="${clickObj=='itemprops'&&click=='1' }">
        <div id="itemprops">
            <form id="itemprops_form" action="">
                <input type="hidden" value="${category_id }" name="category.categoryId">
                <input type="hidden" value="${category_id }" name="category_id" id="category_id">
                <input type="hidden" value="${category_id }" name="itemprops.categoryId">
                <input type="hidden" value="${itemprops_id }" name="itemprops_id">
                <input type="hidden" value="${itemprops_id }" name="itemprops.propId">
                <input type="hidden" value="${haddle }" name="haddle" id="haddle">
                <input type="hidden" value="${click }" name="click">
                <input type="hidden" value="fromcategory" name="from_page">
                <table class="table table-bordered table-hover definewidth m10">
                    <tr>
                        <td><span><c:if test="${haddle=='add' }">新增类目属性</c:if><c:if
                                test="${haddle=='update' }">修改类目属性和属性值</c:if></span></td>
                    </tr>
                </table>
                <table class="table table-bordered table-hover definewidth m10">
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;">所属类目:</td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <input type="text" readonly="readonly" disabled="disabled"
                                   value="${category.categoryName }"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">* </span>属性名称:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <input type="text" name="itemprops.propName" value="${itemprops.propName }" id="propName"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">* </span>属性编码:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <input type="text" name="itemprops.propCode" value="${itemprops.propCode }" id="propCode"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;">外部标识:</td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <input type="text" name="itemprops.outeId" value="${itemprops.outeId }"/></td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">* </span>排序:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <input type="text" name="itemprops.sort" value="${itemprops.sort }" id="sort"
                                   onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/><span style="color: red;">只能输入数字,数值越大越靠前</span>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">*</span>是否销售属性:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <c:choose>
                                <c:when test="${itemprops.isSales=='1' }">
                                    <input type="radio" name="itemprops.isSales" value="0"/> 不是销售属性 &nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="itemprops.isSales" value="1" checked/> 是销售属性
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="itemprops.isSales" value="0"
                                           checked/> 不是销售属性 &nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="itemprops.isSales" value="1"/> 是销售属性
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">*</span>显示类型:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <select name="itemprops.showView">
                                <c:choose>
                                    <c:when test="${itemprops.showView=='select'}">
                                        <option value="checkbox">多选</option>
                                        <option value="select" selected="selected">下拉列表</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="checkbox" selected="selected">多选</option>
                                        <option value="select">下拉列表</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">*</span>是否前端显示:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                                <%-- <c:choose>
                                   <c:when test="${itemprops.isShow=='0' }">
                                         <input type="radio" name="itemprops.isShow"  value="1"  /> 前端显示
                                        <input type="radio" name="itemprops.isShow"  value="0" checked  /> 前端不显示   &nbsp;&nbsp;&nbsp;
                                   </c:when>
                                   <c:otherwise>
                                       <input type="radio" name="itemprops.isShow"  value="1" checked /> 前端显示
                                      <input type="radio" name="itemprops.isShow"  value="0" /> 前端不显示  &nbsp;&nbsp;&nbsp;
                                   </c:otherwise>
                               </c:choose> --%>
                            <input type="radio" name="itemprops.isShow" value="1" checked/> 前端显示
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                                style="color: red;">*</span>状态:
                        </td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <c:choose>
                                <c:when test="${itemprops.status=='0' }">
                                    <input type="radio" name="itemprops.status" value="1"/> 启用&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="itemprops.status" value="0" checked/> 无效
                                </c:when>
                                <c:otherwise>
                                    <input type="radio" name="itemprops.status" value="1" checked/> 启用&nbsp;&nbsp;&nbsp;
                                    <input type="radio" name="itemprops.status" value="0"/> 无效
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td class="tableleft" style="text-align:center;vertical-align:middle;">备注:</td>
                        <td class="tableleft" style="text-align:left;vertical-align:middle;">
                            <textarea style="resize: none;width: 500px;" rows="7" cols="30" name="itemprops.remark"
                                      id="remark">${itemprops.remark}</textarea></td>
                    </tr>
                    <c:if test="${haddle=='update' }">
                        <tr>
                            <td colspan="10">
                                <table class="table table-bordered table-hover definewidth m10">
                                    <tr>
                                        <td class="tablecenter">
                                            属性名称:<b style="color:blue;font-size: 18px;">${itemprops.propName }</b>&nbsp;&nbsp;
                                            <button type="button" class="btn btn-success"
                                                    onclick="addPropsValueTable()">新增类目属性值
                                            </button>
                                        </td>
                                    </tr>
                                </table>
                                <table id="propsValue_divId" class="table table-bordered table-hover definewidth m10">
                                    <c:if test="${!empty list_propsValue&&fn:length(list_propsValue)>0 }">
                                        <c:forEach items="${list_propsValue }" var="propsValue">
                                            <tr>
                                                <td class="tableleft" style="text-align:center;vertical-align:middle;">
                                                    <span style="color: red;">* </span>值名称:
                                                </td>
                                                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                                                    <input type="hidden" style="width: 100px" name="value_id"
                                                           value="${propsValue.value_id }"/>
                                                    <input type="text" style="width: 100px" name="value_name"
                                                           value="${propsValue.value_name }"/></td>
                                                <td class="tableleft" style="text-align:center;vertical-align:middle;">
                                                    外部标识:
                                                </td>
                                                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                                                    <input type="text" style="width: 100px" name="oute_id"
                                                           value="${propsValue.oute_id }"/></td>
                                                <td class="tableleft" style="text-align:center;vertical-align:middle;">
                                                    <span style="color: red;">* </span>属性值:
                                                </td>
                                                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                                                    <input type="text" style="width: 80px" name="propsValue_value"
                                                           value="${propsValue.value }"/></td>
                                                <td class="tableleft" style="text-align:center;vertical-align:middle;">
                                                    <span style="color: red;">* </span>排序:
                                                </td>
                                                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                                                    <input type="text" style="width: 30px" name="value_sort"
                                                           value="${propsValue.short_ }"/></td>
                                                <td><input type="button" onclick="delTableRow(this)" value="移除"></td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                        <%-- <c:if test="${fn:length(list_propsValue)==0 || empty  list_propsValue}">
                                            <tr><th style="color: red;">暂无数据</th></tr>
                                        </c:if> --%>
                                </table>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td colspan="2">
                            <button type="button" class="btn btn-primary" onclick="addItemprops()">提交</button>
                        </td>
                    </tr>
                </table>
            </form>
            </c:if>
        </div>
    </div>
    <!-- 增加属性值表格 -->
    <div id="p_table" style="display: none;">
        <table id="propsValue_table" class="table table-bordered table-hover definewidth m10">
            <tr>
                <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                        style="color: red;">* </span>值名称:
                </td>
                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                    <input type="hidden" style="width: 100px" name="value_id" value="0"/>
                    <input type="hidden" style="width: 100px" name="prop_sort" value="0"/>
                    <input type="text" style="width: 100px" name="value_name" value=""/></td>
                <td class="tableleft" style="text-align:center;vertical-align:middle;">外部标识:</td>
                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                    <input type="text" style="width: 100px" name="oute_id" value=""/></td>
                <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                        style="color: red;">* </span>属性值:
                </td>
                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                    <input type="text" style="width: 80px" name="propsValue_value" value=""/></td>
                <td class="tableleft" style="text-align:center;vertical-align:middle;"><span
                        style="color: red;">* </span>排序:
                </td>
                <td class="tableleft" style="text-align:left;vertical-align:middle;">
                    <input type="text" style="width: 30px" name="value_sort" value="0"/></td>
                <td><input type="button" onclick="delTableRow(this)" value="移除"></td>
            </tr>
        </table>
    </div>
    <div style="height: 80px;"></div>
    <!-- 	 </form> -->
    </body>
</html>
<script type="text/javascript">
    var click = '${click}';
    if (click == '1') {
    } else {
        listdata();
    }

    function listdata() {
        $("#categoryId_").val("");
        $.ajax({
            type: "post",
            data: {leveId: $("#leveId").val()},
            async: false,
            cache: false,
            url: "${path}/manage/loadlistCategory.action",
            dataType: "json",
            success: function (data) {
                //清空保存的数据
                $("#tb").empty();
                $("#category").empty();
                for (var i = 0; i < data.msg.length; i++) {
                    var strs = "";
                    var obj = data.msg[i];
                    strs = strs + "<tr><td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'>" + obj.category_id + "</td>";
                    if (i < 8) {
// 						strs=strs+"<td onclick='clickCategory("+obj.category_id+")' style='cursor:pointer;'><span style='color: #4A4AFF;'>"+obj.category_name+"</span></td>";
                        strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'><span>" + obj.category_name + "</span></td>";
                    } else {
                        strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'>" + obj.category_name + "</td>";
                    }

                    strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'>" + obj.category_code + "</td>";
                    strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'><img src=" + obj.category_img + " width='50px' height='50px'></td>";
                    strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'>" + obj.category_short + "</td>";
                    if (obj.status == "1") {
                        strs = strs + "<td onclick='clickCategory(" + obj.category_id + ")' style='cursor:pointer;'>有效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delorupdatecategory(0," + obj.category_id + ")'>删除</a></td></tr>";
                    } else {
                        strs = strs + "<td style='color: red;' onclick='clickCategory(" + obj.category_id + ")'>无效</td>";
                        strs = strs + "<td><a style='cursor:pointer;' onclick='delorupdatecategory(1," + obj.category_id + ")'>启用</a></td></tr>";
                    }
                    $("#tb").append(strs.toString());
                }
                if (data.msg.length > 0) {
                } else {
                    $("#tb").append("<tr><td colspan='20' style='font-size: 20px;font-weight: bold;color: red'>暂时没有数据</td></tr>");
                }
            }
        });
    }

    //点击类目
    function clickCategory(category_id) {
// 		alert("点击类目"+category_id);
        var leveId = $("#leveId").val();
        window.location.href = "${path}/manage/toLoadlistCategoryPage.action?category_id=" + category_id + "&clickObj=category&leveId=" + leveId;
    }

    //删除或者启用类目
    function delorupdatecategory(status, category_id) {
        var leveId = $("#leveId").val();
        $.ajax({
            type: "post",
            data: {category_id: category_id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delorupdatecategory.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert("操作成功");
                    window.location.href = "${path}/manage/toLoadlistCategoryPage.action?category_id=" + category_id + "&leveId=" + leveId;
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //点击类目属性
    function clickItemprops(category_id, itemprops_id) {
        var leveId = $("#leveId").val();
// 		window.location.href="${path}/manage/toAddOrUpdatecategoryPage.action?haddle=add&category_id="+category_id+"&itemprops_id="+itemprops_id+"&clickObj=itemprops";
        window.location.href = "${path}/manage/clickitempropspage.action?haddle=update&category_id=" + category_id + "&itemprops_id=" + itemprops_id + "&clickObj=itemprops&leveId=" + leveId;
    }

    //删除或者启用类目属性
    function delorupdateitemprops(status, itemprops_id, category_id) {
        var leveId = $("#leveId").val();
        $.ajax({
            type: "post",
            data: {itemprops_id: itemprops_id, status: status},
            async: false,
            cache: false,
            url: "${path}/manage/delorupdateitemprops.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
// 					listdata();
                    window.location.href = "${path}/manage/toLoadlistCategoryPage.action?category_id=" + category_id + "&clickObj=category&leveId=" + leveId;
                } else {
                    alert(data.msg);
                }
            }
        });
    }

    //新增类目页面
    function toAddOrUpdatecategoryPage() {
        var leveId = $("#leveId").val();
        window.location.href = "${path}/manage/toAddOrUpdatecategoryPage.action?haddle=add&category_id=" + $("#category_id").val() + "&clickObj=category&leveId=" + leveId;
    }

    //增加或者修改类目
    function addcategory() {
        //类目名称
        var categoryName = $("#categoryName").val() + "";
        if (null == categoryName || categoryName.trim() == '') {
            alert("类目名称不能为空");
            return;
        }
        //类目编码
        var categoryCode = $("#categoryCode").val() + "";
        if (null == categoryCode || categoryCode.trim() == '') {
            alert("类目编码不能为空");
            return;
        }
        //排序
        var sort = $("#short_").val() + "";
        if (sort == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return;
        }
        var leveId = $("#leveId").val();
        $("#leveId_rel").val(leveId);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#category_form')[0]);
        $.ajax({
            url: "${path}/manage/addorupdatecategory.action",
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
                if ("update" == $("#haddle").val()) {
                    var category_id = $("#category_id").val();
                    window.location.href = "${path}/manage/toLoadlistCategoryPage.action?category_id=" + category_id + "&clickObj=category&leveId=" + leveId;
                } else {
                    listdata();
                }
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //跳转到新增类目属性页面(点击新增类目属性)
    function toItempropsPage() {
        var leveId = $("#leveId").val();
        var categoryId_ = $("#categoryId_").val();
        if (null == categoryId_ || categoryId_ == '' || categoryId_ == '0') {
            alert("请先点击选中类目!");
            return;
        }
        window.location.href = "${path}/manage/toitempropsPage.action?haddle=add&category_id=" + categoryId_ + "&clickObj=itemprops&leveId=" + leveId;
    }

    //增加或者修改类目属性
    function addItemprops() {
        //属性名称
        var propName = $("#propName").val() + "";
        if (null == propName || propName.trim() == '') {
            alert("属性名称不能为空");
            return;
        }
        //属性编码
        var propCode = $("#propCode").val() + "";
        if (null == propCode || propCode.trim() == '') {
            alert("属性编码不能为空");
            return;
        }
        //排序
        var sort = $("#sort").val() + "";
        if (sort == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return;
        }
        var leveId = $("#leveId").val();
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#itemprops_form')[0]);
        $.ajax({
            url: "${path}/manage/addorupdateitemprops.action",
            type: 'POST',
            data: data,
            dataType: 'JSON',
            cache: false,
            processData: false,
            contentType: false
        }).done(function (ret) {
            var flag = ret.flag;
            if (flag === "ok") {
                alert("操作成功!");
                var category_id = ret.msg.categoryId;
                var itemprops_id = ret.msg.itempropsId;
                window.location.href = "${path}/manage/clickitempropspage.action?haddle=update&category_id=" + category_id + "&itemprops_id=" + itemprops_id + "&clickObj=itemprops&leveId=" + leveId;
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }

        });
    }

    //得到新的table tr
    //添加增加属性值的value
    var table = $("#propsValue_table").html();
    $("#propsValue_table").empty();

    function addPropsValueTable() {
        //得到新的table tr
// 		var table=$("#propsValue_table").html();
        //得到已有的
// 		var table_old=$("#propsValue_table").html();
        $("#propsValue_divId").prepend(table);
// 		$("#propsValue_divId").prepend(table);
    }

    function delTableRow(obj) {
        $(obj).parent().parent().remove();
    }

    listCatogry();

    //加载列表
    function listCatogry() {
        var leveId_ = $("#leveId_").val();
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/manage/listClassfyMap.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "<option value=''>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        if (data.msg[i].level_id == leveId_) {
                            str = str + "<option  selected='selected' value='" + data.msg[i].level_id + "'> " + data.msg[i].level_name + " </option>";
                        } else {
                            str = str + "<option value='" + data.msg[i].level_id + "'> " + data.msg[i].level_name + " </option>";
                        }
                    }
                }
                $("#leveId").html(str);
            }
        });
    }

</script>
