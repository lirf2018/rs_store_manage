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
