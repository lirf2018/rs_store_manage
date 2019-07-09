<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加或者修改卡券</title>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/upload.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>

    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.config.js"></script>
    <!--     <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.all.min.js"> </script> -->
    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/ueditor.all.js"></script>
    <!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
    <!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
    <script type="text/javascript" charset="utf-8" src="${path}/ueditor/lang/zh-cn/zh-cn.js"></script>

    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/js/DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${path}/js/ticketjs.js"></script>
    <script type="text/javascript" src="${path}/js/ticket.js"></script>
    <!--     <script type="text/javascript" src="${path}/js/myeditor.js"></script> -->

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
// 			var classify = $("#listClassfyId").val();
                changeClassfyEvent();
            }
        });
    </script>
</head>
<body>
<!-- 卡券主图 -->
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


<form action="" method="post" class="definewidth m20" id="form_submit">
    <!-- 图片1-4 -->
    <input type="hidden" value="${ticket.ticketImg }" name="ticket.ticketImg" id="img_str0">
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
    <!-- 卡券sku列表 属性ID;属性值ID-->
    <input type="hidden" value="" name="skus" id="skus">
    <input type="hidden" value="${op }" name="op">
    <input type="hidden" value="${ticket.tikcetId }" name="ticket.tikcetId">
    <input type="hidden" value="${ticket.tikcetId }" name="tikcetId" id="tikcetId">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> *</span>一级分类
            </td>
            <td colspan="3">
                <s:select list="listClassfyCatogry" listKey="level_id" listValue="level_name" id="level"
                          name="ticket.leve1Id" value="ticket.leve1Id" headerKey="0" headerValue="--选择分类--"
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
                                  id="listClassfyId" name="ticket.classifyId" value="ticket.classifyId" headerKey="0"
                                  headerValue="--选择类目--"/>
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{0:'--选择类目--'}" listKey="key" listValue="value" value="0" id="listClassfyId"
                                  name="ticket.classifyId"/>&nbsp;
                    </c:otherwise>
                </c:choose>
            </td>
            <td rowspan="3" class="tableleft" style="text-align:center;vertical-align:middle;">卡券主图<br><span
                    style="color: red;">建议尺寸:150*150</span></td>
            <td rowspan="3">
                <c:choose>
                    <c:when test="${!empty ticket.ticketImg && ticket.ticketImg !='' }">
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
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>卡券名称
            </td>
            <td><input type="text" name="ticket.tikcetName" id="tikcetName" value="${ticket.tikcetName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>副标题
            </td>
            <td><input type="text" name="ticket.title" id="title" value="${ticket.title}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>卡券类型
            </td>
            <td><s:select list="#{1:'代金券',2:'优惠券'}" listKey="key" listValue="value" headerKey=""
                          headerValue="--选择卡券类型--" value="ticket.ticketType" name="ticket.ticketType"
                          cssStyle="width:140px" id="ticketType" onchange="oncChangeTicketType()"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>店铺标识
            </td>
            <td>
                <c:choose>
                <c:when test="${!empty listShops}">
                <s:select list="listShops" listKey="shop_id" listValue="shop_name" id="shopId" name="ticket.shopId"
                          value="ticket.shopId"/></td>
            </c:when>
            <c:otherwise>
                <select name="ticket.shopId" id="shopId">
                    <option value="0" selected="selected">--选择店铺--</option>
                </select>
            </c:otherwise>
            </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>卡券面值
            </td>
            <td>
                <c:choose>
                    <c:when test="${op=='add' }">
                        <input disabled="disabled" type="text" name="ticket.ticketValue" id="ticketValue"
                               value="${ticket.ticketValue}"/> <span style="color: red;">只有当为代金券时该字段必填</span>
                    </c:when>
                    <c:when test="${op=='update' && ticket.ticketType=='1'}">
                        <input type="text" name="ticket.ticketValue" id="ticketValue" value="${ticket.ticketValue}"/>
                        <span style="color: red;">只有当为代金券时该字段必填</span>
                    </c:when>
                    <c:otherwise>
                        <input type="text" disabled="disabled" name="ticket.ticketValue" id="ticketValue" value=""/>
                        <span style="color: red;">只有当为代金券时该字段必填</span>
                    </c:otherwise>
                </c:choose>

            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否前端显示
            </td>
            <td>
                <c:choose>
                    <c:when test="${ticket.isShow=='0'}">
                        <input type="radio" name="ticket.isShow" value="0" checked/> 前端不显示
                        <input type="radio" name="ticket.isShow" value="1"/> 前端显示
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="ticket.isShow" value="0"/> 前端不显示
                        <input type="radio" name="ticket.isShow" value="1" checked/> 前端显示
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">限领数量</td>
            <td><input type="text" name="ticket.limitNum" id="limitNum" value="${ticket.limitNum}"/><span
                    style="color: red;"> 0表示不限领</span></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"></span>限领方式
            </td>
            <td><s:select list="#{1:'每天一次',2:'每月一次',3:'每年一次',4:'不限领',5:'只允许领取一次'}" listKey="key" listValue="value"
                          headerKey="4" headerValue="--限领方式--" id="timeWay" name="ticket.limitWay"
                          value="ticket.limitWay" cssStyle="width:180px;"/><span style="color: red;"> 不选表示不限领</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">限领统计开始时间</td>
            <td colspan="3">
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="ticket.limitBeginTime"
                       value="<fmt:formatDate value="${ticket.limitBeginTime}" pattern="yyyy-MM-dd"/> "
                       placeholder="限领取开始时间"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>所属商家
            </td>
            <td>
                <s:select list="listPartners" listKey="id" listValue="partners_name" headerKey="" headerValue="--选择商家--"
                          value="ticket.partnersId" name="ticket.partnersId" cssStyle="width:130px" id="partnersId"/>
                <span style="color: red;"><!-- 如果是第三方店铺，则商家只能是，登录人所在店铺所关联的商家 --></span>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>上架状态
            </td>
            <td>
                <c:choose>
                    <c:when test="${ticket.isPutaway=='0'}">
                        <input type="radio" name="ticket.isPutaway" value="1"/> 等待上架确认
                        <input type="radio" name="ticket.isPutaway" value="0" checked/> 下架
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="ticket.isPutaway" value="1" checked/> 等待上架确认
                        <input type="radio" name="ticket.isPutaway" value="0"/> 下架
                    </c:otherwise>
                </c:choose>
                <span style="color: red;">需要其它方式验证上架</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否长期有效
            </td>
            <td colspan="3">
                <c:choose>
                    <c:when test="${ticket.validDate=='1' }">
                        <input type="radio" name="ticket.validDate" value="0" onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="ticket.validDate" value="1" checked="checked"
                               onclick="onclickRadio()"> 长期有效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="ticket.validDate" value="0" checked="checked"
                               onclick="onclickRadio()"> 限时有效
                        <input type="radio" name="ticket.validDate" value="1" onclick="onclickRadio()"> 长期有效
                    </c:otherwise>
                </c:choose>
                <span style="color: red;"> 长期有效不需要设置开始结束时间和兑换时间</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>开始时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="ticket.startTime" id="beginTime" placeholder="生效时间"
                       value="<fmt:formatDate value="${ticket.startTime}" pattern="yyyy-MM-dd"/> "/>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>结束时间
            </td>
            <td>
                <c:choose>
                    <c:when test="${ticket.validDate=='1' }">
                        <input readonly="readonly" disabled="disabled"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd'})"
                               type="text" name="ticket.endTime" id="endTime"
                               value="<fmt:formatDate value="${ticket.endTime}" pattern="yyyy-MM-dd"/> "
                               placeholder="结束时间"/>
                    </c:when>
                    <c:otherwise>
                        <input readonly="readonly"
                               onClick="WdatePicker({minDate:'#F{$dp.$D(\'beginTime\')}',dateFmt:'yyyy-MM-dd'})"
                               type="text" name="ticket.endTime" id="endTime"
                               value="<fmt:formatDate value="${ticket.endTime}" pattern="yyyy-MM-dd"/> "
                               placeholder="结束时间"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>卡券数量
            </td>
            <td><input type="text" name="ticket.ticketNum" id="ticketNum" value="${ticket.ticketNum}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>兑换过期天数
            </td>
            <td>
                <input type="text" name="ticket.outDate" id="outDate" value="${ticket.outDate}"/> <span
                    style="color: red;"> 默认30天有效</span>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>排序
            </td>
            <td><input type="text" readonly="readonly" name="ticket.weight" id="weight" value="${ticket.weight}"/><span
                    style="color: red;">数字越大越靠前</span></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">状态</td>
            <td>
                <c:choose>
                    <c:when test="${ticket.status=='0'}">
                        <input type="radio" name="ticket.status" value="1"/> 启用
                        <input type="radio" name="ticket.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="ticket.status" value="1" checked/> 启用
                        <input type="radio" name="ticket.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>指定方式
            </td>
            <td><s:select list="#{1:'指定过期时间',2:'指定使用时间'}" listKey="key" listValue="value" headerKey="0"
                          headerValue="不指定" value="ticket.appointType" name="ticket.appointType"
                          cssStyle="width:140px"/> <span style="color: red;">不指定,即按兑换过期天计算过期时间</span></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>指定过期时间
            </td>
            <td>
                <input readonly="readonly" onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" type="text"
                       name="ticket.appointDate" id="appointDate"
                       value="<fmt:formatDate value="${ticket.appointDate}" pattern="yyyy-MM-dd"/> " placeholder=""/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">卡券介绍</td>
            <td colspan="3"><textarea id="intro" name="ticket.intro"
                                      style="width: 800px; height: 300px; margin: 0 auto;">${ticket.intro }</textarea>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;" colspan="4">卡券介绍图片</td>
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
            <td class="tableleft" style="text-align:center;vertical-align:middle;">使用须知</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="10" cols="30" name="ticket.needKnow"
                                      id="needKnow">${ticket.needKnow }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 700px;" rows="5" cols="30" name="ticket.remark"
                                      id="remark">${ticket.remark }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">卡券添加说明</td>
            <td colspan="3"><textarea readonly="readonly" style="resize: none;width: 700px;color: red" rows="9"
                                      cols="30">
        	1:上下架说明
        	 		卡券销售状态有三种:已下架,等待确认,已上线。其中后台只能处理前面两种状态。
        	     已上线状态,作为前端展现的前提。如果卡券其它条件都正常,而卡券销售状态不为已上线,则
        	    卡券也不能在前端展现。
        	2.将卡券处理成已上线状态有以下方式：
        	  2.1.绑定微信和店铺账号
        	     流程1：关注官方提供的相关公众号或者订阅号
        	     流程2：通过回复关键代码或者编号来完成对等待上架的卡券,修改成已上线  </textarea></td>
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
            url: "../manage/addOrUpdateTicket.action",
            type: 'POST',
            data: $("#form_submit").serialize(),// 要提交的表单 ,  
            async: false,
            cache: false,
            dataType: "json",
            success: function (data) {
                var flag = data.flag;
                if (flag === "ok") {
                    alert(data.msg);
                    window.location.href = "${path}/jsp/manage/list_ticket.jsp";
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
        window.location.href = "${path}/jsp/manage/list_ticket.jsp";
    }

    //检查数据的完整性
    function checkData() {
        //必填字段
        //分类
        var listClassfyId = $("#listClassfyId").val();
        if (listClassfyId == '0') {
            alert("请选择分类");
            return false;
        }
        //名称
        var tikcetName = $("#tikcetName").val() + "";
        if (tikcetName.trim() == '') {
            alert("名称不能为空");
            return false;
        }
        //副标题
        var title = $("#title").val() + "";
        if (title.trim() == '') {
            alert("标题不能为空");
            return false;
        }
        //卡券类型
        var ticketType = $("#ticketType").val();
        if (ticketType == '') {
            alert("请选择卡券类型");
            return false;
        }
        //卡券面值非法
        if (ticketType == '1') {
            var ticketValue = $("#ticketValue").val();
            if (ticketValue == '' || !checkMoney(ticketValue)) {
                alert("卡券面值非法");
                return;
            }

        } else {
            $("#ticketValue").val(0);
        }
        var partnersId = $("#partnersId").val();
        if (partnersId == '') {
            alert("选择商家");
            return;
        }
        //开始时间
        var beginTime = $("#beginTime").val() + "";
        if (null == beginTime || beginTime.trim() == '') {
            alert("开始时间不能为空");
            return false;
        }

        var limitNum = $("#limitNum").val() + "";
        if (limitNum == '' || !checkNumber(limitNum)) {
            alert("非法限领数");
            return false;
        }
        if (limitNum != '0') {
            var limitWay = $("#limitWay").val() + "";
            if (limitWay == 4 || limitWay == '4') {
                alert("请选择限领方式");
                return false;
            }
        }

        var outDate = $("#outDate").val() + "";
        if (!checkNumber(outDate) || $.trim(outDate) == '') {
            alert("兑换过期天数不合法");
            return false;
        }

        //店铺标识
        var shopId = $("#shopId").val();
        if (shopId == '0' || shopId == '') {
            alert("当前登录用户不能增加卡券,请先给当前用户指定店铺");
            return false;
        }
        //数量
        var ticketNum = $("#ticketNum").val();
        if (!checkNumber(ticketNum) || $.trim(ticketNum) == '') {
            alert("卡券数量不合法");
            return false;
        }
        //排序
        var weight = $("#weight").val();
        if (weight != 0) {
            if (!checkNumber(weight)) {
                alert("非法排序");
                return false;
            }
        } else if (weight == '') {
            $("#weight").val(0);
        }

        //如果是限时有效时候,是否设置开始时间和结束时间,且开始时间不能大于==结束时间
        var validDate = $("input[name='ticket.validDate']:checked").val();
        if (validDate == '0') {
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
            var outOfTime = $("#outOfTime").val() + "";
            if (null == outOfTime || outOfTime.trim() == '') {
                alert("兑换过期不能为空");
                return false;
            }
            if (outOfTime <= endTime) {
                alert("兑换过期时间不能小于等于结束时间");
                return false;
            }
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
        var validDate = $("input[name='ticket.validDate']:checked").val();
        if (validDate == '1') {
            $("#endTime").attr("disabled", "disabled");
            $("#outOfTime").attr("disabled", "disabled");
        } else {
            $("#outOfTime").attr("disabled", false);
            $("#endTime").attr("disabled", false);
        }
    }

    function oncChangeTicketType() {
        var ticketType = $("#ticketType").val();
        if (ticketType == '1') {
            $("#ticketValue").attr("disabled", false);
        } else {
            $("#ticketValue").attr("disabled", "disabled");
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
</script>