<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改商品</title>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/upload.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>

    <script type="text/javascript" src="${path}/js/jquery-1.10.1.min.js"></script>
    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.config.js"></script>
    <!--     <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.all.min.js"> </script> -->
    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.all.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" src="${path}/js/myeditor.js"></script>
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
        $(document).ready(function () {
            var op = '${op}';
            if ("update" == op) {
                changeClassfyEvent();
            }
        });
    </script>
</head>
<body>
<!-- 商品主图 -->
<form id="form_img0">
    <input type="hidden" name="from" value="goodsMainPic">
    <div style="display: none;"><input type="file" id="file0" name="file" onchange="fileUploadDataGoods(0)"></div>
</form>
<!-- 图片上传控键 -->
<div id="formImgUploadDiv" style="display: none;"></div>

<form action="" method="post" class="definewidth m20" id="form_submit">
    <input type="hidden" value="${goods.goodsImg }" name="goods.goodsImg" id="img_str0">
    <!-- 用于标记是否有未上传控件,只用于页面展现 -->
    <c:choose>
        <c:when test="${listImgSize==4 }">
            <input type="hidden" value="0" id="ct">
        </c:when>
        <c:otherwise>
            <input type="hidden" value="1" id="ct">
        </c:otherwise>
    </c:choose>
    <!-- 商品sku列表 属性ID;属性值ID-->
    <input type="hidden" value="" name="skus" id="skus">
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${goods.goodsId }" name="goods.goodsId">
    <input type="hidden" value="${goods.goodsId }" name="goodsId" id="goodsId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> *</span>一级分类
            </td>
            <td colspan="3">
                <s:select list="listClassfyCatogry" listKey="level_id" listValue="level_name" id="level"
                          name="goods.leve1Id" value="goods.leve1Id" headerKey="0" headerValue="--选择分类--"
                          onchange="changeCatogry()"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>类目列表
            </td>
            <td>
                <c:choose>
                    <c:when test="${op=='update' }">
                        <s:select list="listClassfy" listKey="category_id" listValue="category_name_code"
                                  id="listClassfyId" name="goods.classifyId" value="goods.classifyId" headerKey="0"
                                  headerValue="--选择类目--" onchange="changeClassfyEvent()"/>
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{0:'--选择类目--'}" listKey="key" listValue="value" value="0" id="listClassfyId"
                                  name="goods.classifyId" onchange="changeClassfyEvent()"/>&nbsp;
                    </c:otherwise>
                </c:choose>
            </td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;">商品主图<br><span
                    style="color: red;">建议尺寸:150*150</span></td>
            <td rowspan="3">
                <c:choose>
                    <c:when test="${!empty goods.goodsImg && goods.goodsImg !='' }">
                        <div class="upload_div_">
                            <div class="img_div_">
                                <div id="img_id0" name="img_name" class="img_file_size_"
                                     onmousemove="changeImgFileColor(0, 0)" onmouseout="changeImgFileColor(0, 1)"
                                     onclick="imgOnclick(0)"><img id="img0" src="${imgPath }"></div>
                                <img id="img_delete0" style="position: relative;top: -150px;right: 8px;" width="30px"
                                     height="30px" src="../image/delete.png" onclick="deteleImg(this,0)"></div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="upload_div_">
                            <div class="img_div_">
                                <div id="img_id0" name="img_name" class="img_file_size_"
                                     onmousemove="changeImgFileColor(0, 0)" onmouseout="changeImgFileColor(0, 1)"
                                     onclick="imgOnclick(0)"><img id="img0" src="../image/upload.png"></div>
                                <img id="img_delete0" style="position: relative;top: -150px;right: 8px;display: none;"
                                     width="30px" height="30px" src="../image/delete.png" onclick="deteleImg(this,0)">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>商品名称
            </td>
            <td><input type="text" name="goods.goodsName" id="goodsName" value="${goods.goodsName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>副标题
            </td>
            <td><input type="text" name="goods.title" id="title" value="${goods.title}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>原价
            </td>
            <td><input type="text" name="goods.trueMoney" id="trueMoney" value="${goods.trueMoney}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>店铺标识
            </td>
            <td>
                <c:choose>
                <c:when test="${!empty listShops}">
                <s:select list="listShops" listKey="shop_id" listValue="shop_name" id="shopId" name="goods.shopId"
                          value="goods.shopId"/></td>
            </c:when>
            <c:otherwise>
                <select name="goods.shopId" id="shopId">
                    <option value="0" selected="selected">--选择店铺--</option>
                </select>
            </c:otherwise>
            </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>现价
            </td>
            <td><input type="text" name="goods.nowMoney" id="nowMoney" value="${goods.nowMoney}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">商品编码</td>
            <td><input type="text" name="goods.goodsCode" id="goodsCode" value="${goods.goodsCode}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">成本价</td>
            <td><input type="text" name="goods.purchasePrice" id="purchasePrice" value="${goods.purchasePrice}"/>
                单品有效,sku页面设置对应价格
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">押金</td>
            <td><input type="text" name="goods.depositMoney" id="depositMoney" value="${goods.depositMoney}"/>
                免费商品的时候用该字段
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>商品单位
            </td>
            <td><input type="text" name="goods.goodsUnit" id="goodsUnit" value="${goods.goodsUnit}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>库存
            </td>
            <td><input type="text" name="goods.goodsNum" id="goodsNum" value="${goods.goodsNum}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">限购数量</td>
            <td><input type="text" name="goods.limitNum" id="limitNum" value="${goods.limitNum}"/><span
                    style="color: red;"> 0表示不限购;对商品有效,sku无效</span></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>商品预付款
            </td>
            <td><input type="text" name="goods.advancePrice" id="advancePrice" value="${goods.advancePrice}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">限购统计开始时间</td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="goods.limitBeginTime"
                       value="<fmt:formatDate value="${goods.limitBeginTime}" pattern="yyyy-MM-dd"/> "
                       placeholder="限购开始时间"/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>限购方式
            </td>
            <td><s:select list="#{1:'每天一次',2:'每月一次',3:'每年一次',5:'只允许购买一次'}" listKey="key" listValue="value" headerKey="4"
                          headerValue="不限购" id="limitWay" name="goods.limitWay" value="goods.limitWay"
                          cssStyle="width:180px;"/><span style="color: red;"> 不选表示不限购;对商品有效,sku无效</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否卡券
            </td>
            <td><s:select list="listDJTickets" listKey="tikcet_id" listValue="tikcet_name" id="listDJTicketsId"
                          name="goods.ticketId" value="goods.ticketId" headerKey="0" headerValue="--不是卡券--"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>商品类型
            </td>
            <td><s:select list="listGoodsType" listKey="param_key" listValue="param_value" id="listGoodsTypeId"
                          name="goods.goodsType" value="goods.goodsType" headerKey="" headerValue="--选择商品类型--"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否可以预定
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.isYuding=='1'}">
                        <input type="radio" name="goods.isYuding" value="1" checked/> 预定
                        <input type="radio" name="goods.isYuding" value="0"/> 非预定
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.isYuding" value="1"/> 预定
                        <input type="radio" name="goods.isYuding" value="0" checked/> 非预定
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否线上支付
            </td>
            <td>
                <%--<c:choose>
                    <c:when test="${goods.isPayOnline=='1'}">
                        <input type="radio" name="goods.isPayOnline" value="0"  /> 不用线上支付
                        <input type="radio" name="goods.isPayOnline" value="1" checked /> 只能线上支付
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.isPayOnline" value="0" checked/> 不用线上支付
                        <input type="radio" name="goods.isPayOnline" value="1"/> 只能线上支付
                    </c:otherwise>
                </c:choose>--%>
                <input type="radio" name="goods.isPayOnline" value="0" checked/> 不用线上支付
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否开发票
            </td>
            <td colspan="3">
                <%--<c:choose>
                    <c:when test="${goods.isInvoice=='0'}">
                        <input type="radio" name="goods.isInvoice" value="0" checked /> 不开发票
                        <input type="radio" name="goods.isInvoice" value="1" /> 开发票
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.isInvoice" value="0" /> 不开发票
                        <input type="radio" name="goods.isInvoice" value="1" checked/> 开发票
                    </c:otherwise>
                </c:choose>--%>
                <input type="radio" name="goods.isInvoice" value="0" checked/> 不开发票
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>上架状态
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.isPutaway=='0'}">
                        <input type="radio" name="goods.isPutaway" value="1"/> 等待上架确认
                        <input type="radio" name="goods.isPutaway" value="0" checked/> 下架
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.isPutaway" value="1" checked/> 等待上架确认
                        <input type="radio" name="goods.isPutaway" value="0"/> 下架
                    </c:otherwise>
                </c:choose>
                <span style="color: red;">需要其它方式验证上架</span>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>商品属性
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.property=='0'}">
                        <input type="radio" name="goods.property" value="1"/> 实体商品
                        <input type="radio" name="goods.property" value="0" checked/> 虚拟商品
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.property" value="1" checked/> 实体商品
                        <input type="radio" name="goods.property" value="0"/> 虚拟商品
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否长期有效
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.validDate=='1' }">
                        <input type="radio" name="goods.validDate" value="0" onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="goods.validDate" value="1" checked="checked"
                               onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.validDate" value="0" checked="checked"
                               onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="goods.validDate" value="1" onclick="onclickRadio()"> 长期有效(不需要设置开始结束时间)
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否可以退货
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.isReturn=='0'}">
                        <input type="radio" name="goods.isReturn" value="0" checked/> 不可以退货
                        <input type="radio" name="goods.isReturn" value="1"/> 可以退货
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.isReturn" value="0"/> 不可以退货
                        <input type="radio" name="goods.isReturn" value="1" checked/> 可以退货
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>开始时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="goods.startTime" id="beginTime" placeholder="生效时间"
                       value="<fmt:formatDate value="${goods.startTime}" pattern="yyyy-MM-dd"/> "/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>结束时间
            </td>
            <td>
                <c:choose>
                    <c:when test="${goods.validDate=='1' }">
                        <input readonly="readonly" disabled="disabled"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd'})"
                               type="text" name="goods.endTime" id="endTime"
                               value="<fmt:formatDate value="${goods.endTime}" pattern="yyyy-MM-dd"/> "
                               placeholder="结束时间"/>
                    </c:when>
                    <c:otherwise>
                        <input readonly="readonly"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd'})"
                               type="text" name="goods.endTime" id="endTime"
                               value="<fmt:formatDate value="${goods.endTime}" pattern="yyyy-MM-dd"/> "
                               placeholder="结束时间"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>排序
            </td>
            <td><input readonly="readonly" type="text" name="goods.weight" id="weight" value="${goods.weight}"/><span
                    style="color: red;">数字越大越靠前</span></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${goods.status=='0'}">
                        <input type="radio" name="goods.status" value="1"/> 启用
                        <input type="radio" name="goods.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="goods.status" value="1" checked/> 启用
                        <input type="radio" name="goods.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>取货方式
            </td>
            <td>
                <input type="radio" name="goods.getWay" value="1" ${getWayCheck1}/> 邮寄
                <input type="radio" name="goods.getWay" value="4" ${getWayCheck4}/> 自取
                <input type="radio" name="goods.getWay" value="5" ${getWayCheck5}/> 配送
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>所属商家
            </td>
            <td>
                <s:select list="listPartners" listKey="id" listValue="partners_name" headerKey="" headerValue="--选择商家--"
                          value="goods.partnersId" name="goods.partnersId" cssStyle="width:130px" id="partnersId"/>
                <span style="color: red;"><!-- 如果是第三方店铺，则商家只能是，登录人所在店铺所关联的商家 --></span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">自取配送说明</td>
            <td colspan="3"><textarea id="peisongZcDesc" name="goods.peisongZcDesc"
                                      style="width: 800px; margin: 0 auto;resize:none">${goods.peisongZcDesc }</textarea>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">快递配送说明</td>
            <td colspan="3"><textarea id="peisongPeiDesc" name="goods.peisongPeiDesc"
                                      style="width: 800px; margin: 0 auto;resize:none">${goods.peisongPeiDesc }</textarea>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">商品bannel图片(214x175)</td>
        </tr>
        <tr>
            <td colspan="4">
                <div class="upload_div" id="divImg1" data-div="1"><!-- 图片层标识 -->
                    <c:choose>
                        <c:when test="${listImg.size() == 0 }">
                            <div class="img_div">
                                <div class="img_file_size" name="img_name"
                                     onmousemove="javascript:onmousemoveImg($(this))"
                                     onmouseout="javascript:onmouseoutImg($(this))"
                                     onclick="javascript:onclickImg($(this))"><img src="../image/upload.png"></div>
                                <img style="position: relative;top: -370px;right: 8px;display: none;z-index: 100"
                                     width="50px" height="50px" src="../image/delete.png"
                                     onclick="javascript:imgDelete($(this))"> <input type="hidden" value=""
                                                                                     name="imgName1"><!-- 图片名称 --></div>
                        </c:when>
                        <c:when test="${listImg.size() == 4 }">
                            <c:forEach items="${listImg }" var="map">
                                <div class="img_div">
                                    <div class="img_file_size" name="img_name"
                                         onmousemove="javascript:onmousemoveImg($(this))"
                                         onmouseout="javascript:onmouseoutImg($(this))"
                                         onclick="javascript:onclickImg($(this))"><img src="${urlPath}${map.img_url }">
                                    </div>
                                    <img style="position: relative;top: -370px;right: 8px;z-index: 100" width="50px"
                                         height="50px" src="../image/delete.png"
                                         onclick="javascript:imgDelete($(this))"> <input type="hidden"
                                                                                         value="${map.img_url }"
                                                                                         name="imgName1"><!-- 图片名称 -->
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${listImg }" var="map">
                                <div class="img_div">
                                    <div class="img_file_size" name="img_name"
                                         onmousemove="javascript:onmousemoveImg($(this))"
                                         onmouseout="javascript:onmouseoutImg($(this))"
                                         onclick="javascript:onclickImg($(this))"><img src="${urlPath}${map.img_url }">
                                    </div>
                                    <img style="position: relative;top: -370px;right: 8px;z-index: 100" width="50px"
                                         height="50px" src="../image/delete.png"
                                         onclick="javascript:imgDelete($(this))"> <input type="hidden"
                                                                                         value="${map.img_url }"
                                                                                         name="imgName1"><!-- 图片名称 -->
                                </div>
                            </c:forEach>
                            <div class="img_div">
                                <div class="img_file_size" name="img_name"
                                     onmousemove="javascript:onmousemoveImg($(this))"
                                     onmouseout="javascript:onmouseoutImg($(this))"
                                     onclick="javascript:onclickImg($(this))"><img src="../image/upload.png"></div>
                                <img style="position: relative;top: -370px;right: 8px;display: none;z-index: 100"
                                     width="50px" height="50px" src="../image/delete.png"
                                     onclick="javascript:imgDelete($(this))"> <input type="hidden" value=""
                                                                                     name="imgName1"><!-- 图片名称 --></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">商品图片介绍</td>
        </tr>
        <td colspan="4">
            <div class="upload_div" id="divImg2" data-div="2"><!-- 图片层标识 -->
                <c:choose>
                    <c:when test="${listImgInfo.size() == 0 }">
                        <div class="img_div">
                            <div class="img_file_size" name="img_name" onmousemove="javascript:onmousemoveImg($(this))"
                                 onmouseout="javascript:onmouseoutImg($(this))"
                                 onclick="javascript:onclickImg($(this))"><img src="../image/upload.png"></div>
                            <img style="position: relative;top: -370px;right: 8px;display: none;z-index: 100"
                                 width="50px" height="50px" src="../image/delete.png"
                                 onclick="javascript:imgDelete($(this))"> <input type="hidden" value="" name="imgName2">
                            <!-- 图片名称 --></div>
                    </c:when>
                    <c:when test="${listImgInfo.size() == 4 }">
                        <c:forEach items="${listImgInfo }" var="map">
                            <div class="img_div">
                                <div class="img_file_size" name="img_name"
                                     onmousemove="javascript:onmousemoveImg($(this))"
                                     onmouseout="javascript:onmouseoutImg($(this))"
                                     onclick="javascript:onclickImg($(this))"><img src="${urlPath}${map.img_url }">
                                </div>
                                <img style="position: relative;top: -370px;right: 8px;z-index: 100" width="50px"
                                     height="50px" src="../image/delete.png" onclick="javascript:imgDelete($(this))">
                                <input type="hidden" value="${map.img_url }" name="imgName2"><!-- 图片名称 --></div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${listImgInfo }" var="map">
                            <div class="img_div">
                                <div class="img_file_size" name="img_name"
                                     onmousemove="javascript:onmousemoveImg($(this))"
                                     onmouseout="javascript:onmouseoutImg($(this))"
                                     onclick="javascript:onclickImg($(this))"><img src="${urlPath}${map.img_url }">
                                </div>
                                <img style="position: relative;top: -370px;right: 8px;z-index: 100" width="50px"
                                     height="50px" src="../image/delete.png" onclick="javascript:imgDelete($(this))">
                                <input type="hidden" value="${map.img_url }" name="imgName2"><!-- 图片名称 --></div>
                        </c:forEach>
                        <div class="img_div">
                            <div class="img_file_size" name="img_name" onmousemove="javascript:onmousemoveImg($(this))"
                                 onmouseout="javascript:onmouseoutImg($(this))"
                                 onclick="javascript:onclickImg($(this))"><img src="../image/upload.png"></div>
                            <img style="position: relative;top: -370px;right: 8px;display: none;z-index: 100"
                                 width="50px" height="50px" src="../image/delete.png"
                                 onclick="javascript:imgDelete($(this))"> <input type="hidden" value="" name="imgName2">
                            <!-- 图片名称 --></div>
                    </c:otherwise>
                </c:choose>
            </div>
        </td>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">商品图文介绍</td>
        </tr>
        <tr>
            <td colspan="4"><textarea id="container" name="goods.intro"
                                      style="width: 100%; height: 400px; margin: 0 auto;resize:none">${goods.intro }</textarea>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="goods.remark"
                                      id="remark">${goods.remark }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">商品添加说明</td>
            <td colspan="3"><textarea readonly="readonly" style="resize: none;width: 700px;color: red" rows="9"
                                      cols="30">
        	1:上下架说明
        	 		商品销售状态有三种:已下架,等待确认,销售中。其中后台只能处理前面两种状态。
        	     销售中状态,作为前端展现的前提。如果商品其它条件都正常,而商品销售状态不为销售中,则
        	    商品也不能在前端展现。
        	2.将商品处理成销售中状态有以下方式：
        	  2.1.绑定微信和店铺账号
        	     流程1：关注官方提供的相关公众号或者订阅号
        	     流程2：通过回复关键代码或者编号来完成对等待上架的商品,修改成销售中  </textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">已关联规则</td>
        </tr>
        <tr>
            <td colspan="4">
                <c:forEach items="${listRelRule}" var="items">
                    <input disabled="disabled" type="checkbox" checked="checked"> ${items.rule_name}&nbsp;
                </c:forEach>
            </td>
        </tr>
        <tr>
            <td colspan="10">
                <!-- 属性及属性值展示层 -->
                <div id="classifyDiv"></div>
                <!-- sku列表展示层 -->
                <div id="classifyValueDiv"></div>
            </td>
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
<script type="text/javascript" src="${path}/js/goodsjs.js"></script>
<script type="text/javascript" src="${path}/js/goods.js"></script>
<script type="text/javascript" src="${path}/js/goods-img.js"></script>
</html>
<script>
    //提交表单
    function submitForm() {
        var flag = checkData();
        if (!flag) {
            return;
        }
        //上传完得到id 或者路径 标志上传成功
        $.ajax({
            url: "../manage/addOrUpdateGoods.action",
            type: 'POST',
            data: $("#form_submit").serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
                } else if (flag === "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_goods_only.jsp";
    }

    //检查数据的完整性
    function checkData() {
        //必填字段
        //类目
        var listClassfyId = $("#listClassfyId").val();
        if (listClassfyId == '0' || listClassfyId == '') {
            alert("请选择类目");
            return false;
        }
        //名称
        var goodsName = $("#goodsName").val() + "";
        if (goodsName.trim() == '') {
            alert("名称不能为空");
            return false;
        }
        //副标题
        var title = $("#title").val() + "";
        if (title.trim() == '') {
            alert("标题不能为空");
            return false;
        }
        //原价
        var trueMoney = $("#trueMoney").val();
        if (!checkMoney(trueMoney)) {
            alert("原价金额非法");
            return false;
        }
        //现价
        var nowMoney = $("#nowMoney").val();
        if (!checkMoney(nowMoney)) {
            alert("现价金额非法");
            return false;
        }
        if (Number(trueMoney) < Number(nowMoney)) {
            alert("现价不能大于原价");
            return false;
        }

//		//单位
//		var goodsUnit = $("#goodsUnit").val()+"";
//		if(goodsUnit.trim()==''){
//			alert("单位不能为空");
//			return false;
//		}

        var limitNum = $("#limitNum").val() + "";
        if (limitNum == '' || !checkNumber(limitNum)) {
            alert("非法限购数");
            return false;
        }
        if (limitNum == '0') {
            $("#limitWay").val("4");
        }
        var limitWay = $("#limitWay").val() + "";
        if (limitWay == "4") {
            $("#limitNum").val(0)
            limitNum = "0";
        }

        if (limitNum != '0') {
            var limitWay = $("#limitWay").val() + "";
            if (limitWay == 4 || limitWay == '4') {
                alert("请选择限购方式");
                return false;
            }
        }

        if (limitNum == '0') {
            $("#limitWay").val(4);
        }

        //开始时间
        var beginTime = $("#beginTime").val() + "";
        if (null == beginTime || beginTime.trim() == '') {
            alert("开始时间不能为空");
            return false;
        }
        //店铺标识
        var shopId = $("#shopId").val();
        if (shopId == '0' || shopId == '') {
            alert("当前登录用户不能增加商品,请先给当前用户指定店铺");
            return false;
        }
        //库存
        var goodsNum = $("#goodsNum").val() + "";
        if (!checkNumber(goodsNum) || $.trim(goodsNum) == '') {
            alert("库存非法");
            return false;
        }
        //排序
        var weight = $("#weight").val() + "";
        if (weight != 0) {
            if (!checkNumber(weight)) {
                alert("库存非法");
                return false;
            } else {
                $("#weight").val(0);
            }
        }
        //商品类型
        var listGoodsTypeId = $("#listGoodsTypeId").val();
        if (listGoodsTypeId == '') {
            alert("请选择商品类型");
            return false;
        }

        //其它规则
        var listGoodsTypeId = $("#listGoodsTypeId").val();//商品类型 0虚拟商品1商品券2话费商品3租赁商品
        var isYuding = $("input[name='goods.isYuding']:checked").val();
        var isInvoice = $("input[name='goods.isInvoice']:checked").val();
        var isReturn = $("input[name='goods.isReturn']:checked").val();
        var property = $("input[name='goods.property']:checked").val();//0虚拟商品1实体商品
        var getWay = $("input[name='goods.getWay']:checked").val();
        /*//1.非实体商品，0不预定goods.isYuding，0不邮寄goods.isPost，0不配送goods.isPeisong，0不开发票goods.isInvoice，0不可退goods.isReturn
        if ((listGoodsTypeId != 0 || property == 0)&&(isYuding!=0||getWay!=4||isInvoice!=0||isReturn!=0)) {
            alert("非实体商品:不预定,不邮寄,不配送,不开发票,不可退");
            return false;
        }
        //2.商品类型listGoodsTypeId与商品属性----实体商品=实体商品
        if ((listGoodsTypeId == 0 && property != 1) || (listGoodsTypeId != 0 && property == 1)) {
            alert("商品类型与商品属性不一致");
            return false;
        }*/
        if ((listGoodsTypeId == 4 || listGoodsTypeId == 2 || property == 0) && (isYuding != 0 || getWay != 4 || isInvoice != 0 || isReturn != 0)) {
            alert("非实体商品:不预定,不邮寄,不配送,不开发票,不可退");
            return false;
        }

        //如果是限时有效时候,是否设置开始时间和结束时间,且开始时间不能大于==结束时间
        var validDate = $("input[name='goods.validDate']:checked").val();
        if (validDate == 0) {
            //结束时间
            var endTime = $("#endTime").val() + "";
            if (null == endTime || endTime.trim() == '') {
                alert("结束时间不能为空");
                return false;
            }
            if (endTime <= beginTime) {
                alert("结束时间必须大于开始时间");
                return false;
            }
        }
        var partnersId = $("#partnersId").val();
        if (partnersId == '') {
            alert("选择商家");
            return;
        }
        //如果有sku库存是否超过总库存
        if (isSaleIndexArray.length > 0) {//存在sku
            if (hasSku == '0') {//是否已选择必须的销售属性
                alert("选择商品sku");
                return false;
            }
            //sku必填参数校验
            var goodsSkuPrice = $("input[name='goodsSkuPrice']");
            var f = true;
            var maxPrice = trueMoney;//原价
            var minPrice = nowMoney;//现价
//            var jinghuojia=
            $(goodsSkuPrice).each(function () {
                if (!checkMoney(this.value) || $.trim(this.value) == '') {
                    alert("非法商品sku现价");
                    f = false;
                    return false;
                } else {
                    if (minPrice > this.value) {
                        minPrice = this.value;
                    }
                }
            });
            var goodsSkuTrueMoney = $("input[name='trueMoney']");
            $(goodsSkuTrueMoney).each(function () {
                if (!checkMoney(this.value) || $.trim(this.value) == '') {
                    alert("非法商品sku原价");
                    f = false;
                    return false;
                } else {
                    if (maxPrice < this.value) {
                        maxPrice = this.value;
                    }
                }
            });
            var purchasePrice = $("input[name='purchasePrice']");
            $(purchasePrice).each(function () {
                if (!checkMoney(this.value) || $.trim(this.value) == '') {
                    alert("非法商品sku进货价");
                    f = false;
                    return false;
                }
            });

            if (!f) {
                return false;
            }
            var goodsSkuKc = $("input[name='goodsSkuKc']");
            var skuCount = 0;
            $(goodsSkuKc).each(function () {
                if (!checkNumber(this.value) || $.trim(this.value) == '') {
                    alert("非法商品sku库存");
                    f = false;
                    return false;
                } else {
                    skuCount = Number(skuCount) + Number(this.value);
                }
            });
            if (!f) {
                return false;
            }
            $("#goodsNum").val(skuCount);
//			$("#trueMoney").val(maxPrice);//原价取一个最高价
//			$("#nowMoney").val(minPrice);//现价取一个最低价

        }

        return true;
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

    function onclickRadio() {
        var validDate = $("input[name='goods.validDate']:checked").val();
        if (validDate == '1') {
            $("#endTime").attr("disabled", "disabled");
        } else {
            $("#endTime").attr("disabled", false);
        }
    }

    //修改类目
    function changeCatogry() {
        var leveId = $("#level").val();
        if ("0" == leveId || leveId == "") {
            $("#listClassfyId").html("<option value='0'>--选择类目--</option>");
            return;
        }
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
                    $("#listClassfyId").html("<option value='0'>--选择类目--</option>");
                    return;
                }
                var str = "<option value='0'>--选择类目--</option>";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        str = str + "<option value='" + msg[i].category_id + "'> " + msg[i].category_name_code + " </option>";
                    }
                } else {
                    $("#listClassfyId").html("<option value='0'>--选择类目--</option>");
                }
                $("#listClassfyId").html(str);
            }
        });
    }

    //点击上传
    function fileUploadDataGoods(id) {
        //上传图片
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_img' + id)[0]);
        $.ajax({
            url: "../image/uploadFile.action",
            type: 'POST',
            data: data,
            dataType: 'JSON',
            cache: false,
            processData: false,
            contentType: false
        }).done(function (ret) {
            var flag = ret.flag;
            if (flag === "ok") {//如果图片上传成功
                var value = $("#img_str" + id).val();
                $("#img" + id).attr("src", ret.msg.imgpath);
                $("#img_str" + id).val(ret.msg.img);
                $("#img_delete" + id).show();
                $("#pxvalue" + id).val(ret.msg.img);

                var file = $("#file" + id);
                file.after(file.clone());
                file.remove();

                var index = returnImgIndex();
                if (index != 0 && index != '0') {
                    if (value == '') {
                        if (id != 0) {
                            addImgDiv(id);
                        }
                    }
                } else {
                    $("#ct").val("0");
                }

            } else if (flag === "false") {
                alert("图片上传失败:" + ret.msg);
            } else {
                alert("操作失败");
            }

        });


    }

</script>