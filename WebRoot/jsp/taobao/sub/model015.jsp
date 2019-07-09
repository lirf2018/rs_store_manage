<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../../util/taglibs.jsp" %>
<%@ include file="../../util/meta.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>增加</title>
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

        #left {
            width: 40%;
            height: 100%;
            float: left;
        }

        #right {
            width: 60%;
            height: 100%;
            float: right;
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
<form action="" method="post" class="definewidth m20" id="form_submit">
    <input type="hidden" value="${handleType }" name="handleType">
    <input type="hidden" value="${bean.id }" name="bean.id">
    <input type="hidden" value="${id }" name="id">
    <input type="hidden" name="bean.sort" value="${bean.sort }"/>
    <input type="hidden" name="bean.pageName" value="${bean.pageName}"/>
    <input type="hidden" name="bean.toPage" value="${bean.toPage}"/>
    <input type="hidden" name="bean.remark" value="${bean.remark}"/>
    <input type="hidden" name="bean.imgNum" value="${bean.imgNum}"/>
    <!-- <input type="hidden" name="bean.BContents" value="${bean.BContents}"/> -->
    <input type="hidden" name="fromPage" value="sub"/>
    <div id="left">
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>模板图片示例</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><img id="img" src="${photoPath }" width="100%" height="400px"></td>
            </tr>
            </tbody>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>结果源码</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><textarea style="resize: none;width: 100%;" rows="15" readonly="readonly"
                              id="aContents">${bean.AContents }</textarea></td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="right">
        <table class="table table-bordered table-hover m10">
            <tr>
                <td style="text-align:center;vertical-align:middle;">
                    <button onclick="submitForm()" class="btn btn-primary" type="button">生成数据并保存</button> &nbsp;&nbsp;<button
                        type="button" class="btn btn-success" name="backid" id="backid" onclick="returnBack()">返回列表
                </button>
                </td>
            </tr>
        </table>
        <table class="table table-bordered table-hover definewidth m10">
            <thead>
            <tr>
                <th>设置名称</th>
                <th>替换值</th>
                <th>关联字段</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td align="right">图片1(310*310像素)</td>
                <td><input type="text" style="width: 100%;" value="${bean.value01}" name="bean.value01"></td>
                <td>value01</td>
            </tr>
            <tr>
                <td align="right">图片1价格</td>
                <td><input type="text" style="width: 100%;" value="${bean.value02}" name="bean.value02"></td>
                <td>value02</td>
            </tr>
            <tr>
                <td align="right">商品链接地址</td>
                <td><input type="text" style="width: 100%;" value="${bean.value03}" name="bean.value03"></td>
                <td>value03</td>
            </tr>
            <tr>
                <td colspan="3" style="background-color: #9393FF;height: 2px;"></td>
            </tr>
            <tr>
                <td align="right">图片2(310*310像素)</td>
                <td><input type="text" style="width: 100%;" value="${bean.value04}" name="bean.value04"></td>
                <td>value04</td>
            </tr>
            <tr>
                <td align="right">图片2价格</td>
                <td><input type="text" style="width: 100%;" value="${bean.value05}" name="bean.value05"></td>
                <td>value05</td>
            </tr>
            <tr>
                <td align="right">商品链接地址</td>
                <td><input type="text" style="width: 100%;" value="${bean.value06}" name="bean.value06"></td>
                <td>value06</td>
            </tr>
            <tr>
                <td colspan="3" style="background-color: #9393FF;height: 2px;"></td>
            </tr>
            <tr>
                <td align="right">图片3(310*310像素)</td>
                <td><input type="text" style="width: 100%;" value="${bean.value07}" name="bean.value07"></td>
                <td>value07</td>
            </tr>
            <tr>
                <td align="right">图片3价格</td>
                <td><input type="text" style="width: 100%;" value="${bean.value08}" name="bean.value08"></td>
                <td>value08</td>
            </tr>
            <tr>
                <td align="right">商品链接地址</td>
                <td><input type="text" style="width: 100%;" value="${bean.value09}" name="bean.value09"></td>
                <td>value09</td>
            </tr>
            </tbody>
        </table>
    </div>
</form>
</body>
</html>
<script>

    //提交表单
    function submitForm() {
        $.ajax({
            url: "${path}/taobao/saveOrUpdate.action",
            type: 'POST',
            data: $('#form_submit').serialize(),
            dataType: 'JSON',
            async: false,
            cache: false,
        }).done(function (ret) {
            var flag = ret.flag;
            if (flag === "ok") {
                alert(ret.msg);
                window.location.href = "${path}/jsp/taobao/list_model.jsp";
            } else if (flag === "false") {
                alert(ret.msg);
            } else {
                alert("操作失败");
            }
        });
    }

    //返回列表
    function returnBack() {
        window.location.href = "${path}/jsp/taobao/list_model.jsp";
    }


    var result = $("#aContents").val();
    result = result.replace(/_##/g, "&");
    result = result.replace(/_#/g, ";");
    $("#aContents").val(result);
</script>