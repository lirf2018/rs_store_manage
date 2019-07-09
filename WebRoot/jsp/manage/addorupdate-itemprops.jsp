<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加类目属性</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/bootstrap-responsive.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/assets/css/style.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/jquery.sorted.js"></script>
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

        @media (max-width: 980px) {
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
<form action="" method="post" class="definewidth m20" id="submit_form">
    <input type="hidden" value="${itemprops.propId}" id="propId" name="itemprops.propId"/>
    <input type="hidden" value="${haddle }" name="haddle" id="haddle">
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;"> *</span>一级分类
            </td>
            <td colspan="3">
                <s:select list="listClassfyCatogry" listKey="level_id" listValue="level_name" id="level" name="leveId"
                          value="leveId" headerKey="0" headerValue="--选择分类--" onchange="changeCatogry()"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">* </span>类目列表
            </td>
            <td>
                <c:choose>
                    <c:when test="${haddle=='update' && !empty list_category }">
                        <s:select list="list_category" listKey="category_id" listValue="category_name_code" id="catogry"
                                  name="itemprops.categoryId" value="itemprops.categoryId" headerKey="0"
                                  headerValue="--选择类目--"/>
                    </c:when>
                    <c:otherwise>
                        <s:select list="#{0:'--选择类目--'}" listKey="key" listValue="value" value="0" id="catogry"
                                  name="itemprops.categoryId"/>&nbsp;
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>属性名称
            </td>
            <td><input type="text" name="itemprops.propName" id="propName" value="${itemprops.propName}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">外部标识</td>
            <td><input type="text" name="itemprops.outeId" id="outeId" value="${itemprops.outeId}"/></td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>属性编码
            </td>
            <td><input type="text" name="itemprops.propCode" id="propCode" value="${itemprops.propCode}"/></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否销售属性
            </td>
            <td>
                <c:choose>
                    <c:when test="${itemprops.isSales=='1'}">
                        <input type="radio" name="itemprops.isSales" value="0"/> 不是销售属性
                        <input type="radio" name="itemprops.isSales" value="1" checked/> 是销售属性
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="itemprops.isSales" value="0" checked/> 不是销售属性
                        <input type="radio" name="itemprops.isSales" value="1"/> 是销售属性
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>显示类型
            </td>
            <td>
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
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>是否前端显示
            </td>
            <td>
                <%--     <c:choose>
                      <c:when test="${itemprops.isShow=='1'}">
                          <input type="radio" name="itemprops.isShow" value="0"  /> 前端不显示
                           <input type="radio" name="itemprops.isShow" value="1" checked /> 前端显示
                      </c:when>
                      <c:otherwise>
                          <input type="radio" name="itemprops.isShow" value="0" checked /> 前端不显示
                           <input type="radio" name="itemprops.isShow" value="1" /> 前端显示
                      </c:otherwise>
                      </c:choose> --%>
                <input type="radio" name="itemprops.isShow" value="1" checked/> 前端显示
            </td>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>排序
            </td>
            <td><input type="text" name="itemprops.sort" id="sort" value="${itemprops.sort }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/> <span
                    style="color: red;">只能输入数字,数值越大越靠前</span></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 500px;" rows="7" cols="30" name="itemprops.remark"
                                      id="remark">${itemprops.remark }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;"><span style="color: red;">*</span>状态
            </td>
            <td colspan="3">
                <c:choose>
                    <c:when test="${itemprops.status=='0'}">
                        <input type="radio" name="itemprops.status" value="1"/> 启用
                        <input type="radio" name="itemprops.status" value="0" checked/> 无效
                    </c:when>
                    <c:otherwise>
                        <input type="radio" name="itemprops.status" value="1" checked/> 启用
                        <input type="radio" name="itemprops.status" value="0"/> 无效
                    </c:otherwise>
                </c:choose>
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

    //保存数据
    function submitForm() {
        //选择类目
        var categoryId = $("#catogry").val() + "";
        if (categoryId == "" || categoryId == "0") {
            alert("请选择类目");
            return;
        }
        //属性名称
        var propName = $("#propName").val() + "";
        if (propName.trim() == "") {
            alert("请输入名称");
            return;
        }
        //属性编码
        var propCode = $("#propCode").val() + "";
        if (propCode.trim() == "") {
            alert("请输入编码");
            return;
        }
        //排序
        var sort = $("#sort").val();
        if (sort.trim() == '' || (sort.length > 1 && sort.substring(0, 1) == '0')) {
            alert("排序为正整数");
            return;
        }

        $.ajax({
            type: "post",
            data: $("#submit_form").serialize(),
            async: false,
            cache: false,
            url: "${path}/manage/addorupdateitemprops.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert("操作成功!");
                    window.location.href = "${path}/jsp/manage/list_itemprops.jsp";
                } else if (data.flag = "false") {
                    alert(data.msg);
                } else {
                    alert("操作失败");
                }
            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/manage/list_itemprops.jsp";
    }

    //修改类目
    function changeCatogry() {
        var leveId = $("#level").val();
        if ("0" == leveId || leveId == "") {
            $("#catogry").html("<option value='0'>--选择类目--</option>");
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
</script>