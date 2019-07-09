//鼠标经过type=0和离开type=1上传图片框的时间#E8E8E8
function changeImgFileColor(id, type) {
    if (type == 0) {//经过
//		$("#img_id"+id).css("background-color", "red");
//		$("#img_id"+id).css("background-color", "red");#696969
        $("#img_id" + id).css("border", "2px solid blue");
    } else {
        $("[name='img_name']").css("background-color", "white");
        $("[name='img_name']").css("border", "3px solid white");
    }
}

//点击上传
function fileUploadData(id) {
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
//    		alert("图片上传失败");
        } else {
//    		alert("操作失败");
        }

    });


}

//返回点击图片事件
function imgOnclick(id) {
    return $("#file" + id).click();
}

function returnImgIndex() {
    var value1 = $("#img_str1").val();
    if (null == value1 || value1 == '') {
        return 1;
    }
    var value2 = $("#img_str2").val();
    if (null == value2 || value2 == '') {
        return 2;
    }
    var value3 = $("#img_str3").val();
    if (null == value3 || value3 == '') {
        return 3;
    }
    var value4 = $("#img_str4").val();
    if (null == value4 || value4 == '') {
        return 4;
    }
    return 0;
}

//给图片增加删除图标
function deteleImg(obj, index) {
    var flag = confirm("确定删除吗?");
    if (!flag) {
        return;
    }
    if (index == 0 || index == '0') {//删除主图
        $("#img_str" + index).val("");
        $("#img" + index).attr("src", "../image/upload.png");
        $("#img_delete" + index).hide();
        return;
    }
    if (flag) {
        $(obj).parent().remove();
        $("#img_str" + index).val("");
        $("#pxvalue" + index).val("");
        var index_ = returnImgIndex();
        var ct = $("#ct").val();
        if (index_ != 0 && index_ != '0' && ct == '0') {
            addImgDiv();
        } else {
        }
        $("#ct").val("1");
    }
}

//增加上传图片控件
function addImgDiv() {
    var index = returnImgIndex();
    if (index == 0 || index == '0') {
        return;
    }

    var html = "<img id='img_delete" + index + "' style='position: relative;top: -370px;right: 8px;display: none;' width='50px' height='50px' src='../image/delete.png'  onclick='deteleImg(this," + index + ")'><input id='pxvalue" + index + "' name='px' type='hidden' value=''>";
    var img_div = "<div class='img_div'><div id='img_id" + index + "' name='img_name' class='img_file_size' onmousemove='changeImgFileColor(" + index + ", 0)' onmouseout='changeImgFileColor(" + index + ", 1)' onclick='imgOnclick(" + index + ")' ><img id='img" + index + "' width='100%' height='100%' src='../image/upload.png'></div>" + html + "</div>";
    $(".upload_div").append(img_div);
}


//改变分类
function changeClassfyEvent() {
    //初始化
    //classifyDiv 用于展现分类的层id
    var classifyId = $("#listClassfyId").val();
    var tikcetId = $("#tikcetId").val();
    if (classifyId == '0') {
        $("#classifyDiv").empty();
        return;
    }

    var data = {classifyId: classifyId, tikcetId: tikcetId};
    //根据分类查询属性和属性值
    $.ajax({
        type: "post",
        data: data,
        async: false,
        cache: false,
        url: "../manage/searchTicketClassifyInfosById.action",
        dataType: "json",
        success: function (data) {
            var obj = data.msg.propValue;
            skuList = data.msg.listGoodsSku;
            var html = "<table  class='table table-bordered table-hover m10'>";
            html = html + "<tr><th colspan='2' style='text-align:center;' >卡券分类</th></tr>";
            for (var i = 0; i < obj.length; i++) {
                var propValues = obj[i].propvalues;
                if (null != propValues && propValues.length > 0) {
                    //是否销售属性
                    var isSales = obj[i].is_sales;
                    if (isSales == '1') {
                        continue;
                    }
                    html = html + "<tr>";
                    //属性Id
                    var propId = obj[i].prop_id;
                    //属性名称
                    var propName = obj[i].prop_name;

                    var isSalesMsg = "";
                    html = html + "<td class='tableleft' style='text-align:center;vertical-align:middle;'>" + isSalesMsg + propName + "</td>";
                    html = html + "<td>";
                    //展示方式
                    var showView = obj[i].show_view;
                    if (showView == 'checkbox') {
                        for (var j = 0; j < propValues.length; j++) {
                            //属性值Id
                            var propValueId = propValues[j].value_id;
                            //属性值名称
                            var propValueName = propValues[j].value;
                            //是否选中 1 选中
                            var isChecked = propValues[j].isChecked;
                            if (isChecked == '1') {
                                html = html + "<input type='checkbox' name='checkbox' checked='checked'  value='" + propId + ";" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                            } else {
                                html = html + "<input type='checkbox' name='checkbox'  value='" + propId + ";" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                            }
                        }

                    } else {
                        html = html + "<select  id='select' name='select' >";
                        html = html + "<option value=''>--选择属性值--</option>";
                        for (var j = 0; j < propValues.length; j++) {
                            //属性值Id
                            var propValueId = propValues[j].value_id;
                            //属性值名称
                            var propValueName = propValues[j].value;
                            //是否选中 1 选中
                            var isChecked = propValues[j].isChecked;
                            if (isChecked == '1') {
                                html = html + "<option selected='selected' value='" + propId + ";" + propValueId + "'>" + propValueName + "</option>";
                            } else {
                                html = html + "<option value='" + propId + ";" + propValueId + "'>" + propValueName + "</option>";
                            }
                        }
                        html = html + "</select>";
                    }
                    html = html + "</td>";
                    html = html + "</tr>";
                }
            }
            html = html + "</table>";
            if (null == data || null == obj || obj.length == 0) {
                $("#classifyDiv").empty();
            } else {
                $("#classifyDiv").html(html);
            }
        }
    });
}
