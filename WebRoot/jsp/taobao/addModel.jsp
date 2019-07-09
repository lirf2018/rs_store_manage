<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
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

    <!-- markItUp! skin -->
    <link rel="stylesheet" type="text/css" href="${path}/markitup/skins/markitup/style.css">
    <!--  markItUp! toolbar skin -->
    <link rel="stylesheet" type="text/css" href="${path}/markitup/sets/default/style.css">
    <!-- jQuery -->
    <!-- markItUp! -->
    <script type="text/javascript" src="${path}/markitup/jquery.markitup.js"></script>
    <!-- markItUp! toolbar settings -->
    <script type="text/javascript" src="${path}/markitup/sets/default/set.js"></script>

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
    <input type="hidden" value="${handleType }" name="handleType">
    <input type="hidden" value="${bean.id }" name="id">
    <input type="hidden" name="bean.id" id="id" value="${bean.id}"/>
    <!-- <input type="hidden" name="bean.AContents" id="AContents"   value="${bean.AContents}"/> -->
    <table class="table table-bordered table-hover m10">
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">名称</td>
            <td>
                <input type="text" name="bean.pageName" id="pageName" value="${bean.pageName}"/>
            </td>
            <td rowspan="3" width="10%" class="tableleft" style="text-align:center;vertical-align:middle;">预览图片</td>
            <td rowspan="3">
                <div id="preview" style="text-align:center;vertical-align:middle;">
                    <img id="imghead" width="100px" height="100px" border="0" src='${photoPath}'></div>
                <!--无预览时的默认图像，自己弄一个-->
                <br/><input type="file" onchange="previewImage(this)" name="file"/>
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">图片数量</td>
            <td><input type="text" name="bean.imgNum" id="imgNum" value="${bean.imgNum }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/> *只能输入数字
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">排序</td>
            <td><input type="text" name="bean.sort" id="sort" value="${bean.sort }"
                       onkeyup="this.value=this.value.replace(/[^\d]/g,'')"/> *只能输入数字
            </td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">跳转链接</td>
            <td colspan="3"><textarea style="resize: none;width: 100%;" rows="3"
                                      name="bean.toPage">${bean.toPage }</textarea></td>
        </tr>
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">模板代码</td>
            <td colspan="3"><textarea id="bContents" style="resize: none;width:  60%;" cols="80" rows="20"
                                      name="bean.BContents">${bean.BContents }</textarea> <em style="color: red">*不建议局部修改,若修改,建议粘贴全部代码</em>
            </td>
        </tr>
        <!--     <tr> -->
        <!--         <td class="tableleft" style="text-align:center;vertical-align:middle;">替换后代码</td> -->
        <!--         <td colspan="3"><textarea style="resize: none;width: 60%;"  cols="80" readonly="readonly" rows="7"  name="bean.AContents"   >${bean.AContents }</textarea></td> -->
        <!--     </tr> -->
        <c:if test="${handleType=='add' }">
            <tr>
                <td class="tableleft" style="text-align:center;vertical-align:middle;">源码</td>
                <td colspan="3"><textarea style="resize: none;width: 60%;" cols="80" rows="20"
                                          name="bean.yuanMa">${bean.yuanMa }</textarea></td>
            </tr>
        </c:if>
        <tr>
            <!-- 	        <td class="tableleft" style="text-align:center;vertical-align:middle;">源码</td> -->
            <!-- 	        <td colspan="3"><textarea style="resize: none;width: 60%;"  cols="80" rows="20"  name="bean.yuanMa">${bean.yuanMa }</textarea></td> -->
            <!-- 	    </tr> -->
        <tr>
            <td class="tableleft" style="text-align:center;vertical-align:middle;">备注</td>
            <td colspan="3"><textarea style="resize: none;width: 60%;" rows="7" cols="30" name="bean.remark"
                                      id="remark">${bean.remark }</textarea></td>
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
        $("#ifchangeImg").val(ifchangeImg);
        //上传完得到id 或者路径 标志上传成功
        var data = new FormData($('#form_submit')[0]);
        $.ajax({
            url: "${path}/taobao/saveOrUpdate.action",
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
</script>

<script type="text/javascript">
    $(function () {

        // Add markItUp! to your textarea in one line
        // $('textarea').markItUp( { Settings }, { OptionalExtraSettings } );
        // 	$('#markItUp').markItUp(mySettings);
        // You can add content from anywhere in your page
        // $.markItUp( { Settings } );
        // 	$('.add').click(function() {
        //  		$('#markItUp').markItUp('insert',
        // 			{ 	openWith:'<opening tag>',
        // 				closeWith:'<\/closing tag>',
        // 				placeHolder:"New content"
        // 			}
        // 		);
        //  		return false;
        // 	});

        // And you can add/remove markItUp! whenever you want
        // $(textarea).markItUpRemove();
        // 	$('.toggle').click(function() {
        // 		if ($("#markItUp.markItUpEditor").length === 1) {
        //  			$("#markItUp").markItUp('remove');
        // 		} else {
        // 			$('#markItUp').markItUp(mySettings);
        // 		}
        //  		return false;
        // 	});
        var result = $("#bContents").val();
        result = result.replace(/_##/g, "&");
        result = result.replace(/_#/g, ";");
        $("#bContents").val(result);
    });
</script>