/**
 * Created by user on 2017-09-27.
 */
// $(document).ready(function () {
//     //上传图片层控制
//     //绑定onmousemove
//     $(".img_div_goodsinfo").bind("mousemove", function () {
//         var click = $(this).children("div").css("border", "2px solid blue");
//     });
//     //绑定onmouseout
//     $(".img_div_goodsinfo").bind("mouseout", function () {
//         var click = $(this).children("div").css("background-color", "white");
//         var click = $(this).children("div").css("border", "3px solid white");
//     });
//     //绑定onclick(删除图片)
//     $(".img_div_goodsinfo_del").bind("click", function () {
//         var click = $(this).parent().children("li");
//     });
//     //点击上传图片
//     $(".img_div_goodsinfo").bind("click", function () {
//         //生成上传文件控键
//         $("#formImgUploadDiv").html("");
//         var uploadImgDivIndex = $(this).parent().attr("data-div");//图片层标识
//         $("#clickImgDivId").val(uploadImgDivIndex);//记录点击图层
//
//         var html = "<form id='formImg'> <div><input type='file' id='fileImg' name='file' onchange='javascript:fileUploadDataImg()'></div></form>";
//         $("#formImgUploadDiv").html(html);
//
//         return  $("#fileImg").click();
//     });
// })

//鼠标经过时
function onmousemoveImg(obj) {
    $("[name='img_name']").css("background-color", "white");
    $("[name='img_name']").css("border", "3px solid white");
    obj.css("border", "2px solid blue");
}

//鼠标离开时
function onmouseoutImg(obj) {
    $("[name='img_name']").css("background-color", "white");
    $("[name='img_name']").css("border", "3px solid white");
}

var clickImgObj;
var clickImgDiv;

//鼠标点击
function onclickImg(obj) {
    //点击层
    clickImgDiv = obj.parent().parent().attr("data-div");
    clickImgObj = obj;
    $("#formImgUploadDiv").html("");
    var html = "<form id='formImg'> <div><input type='file' id='fileImg' name='file' onchange='javascript:fileUploadDataImg()'></div></form>";
    $("#formImgUploadDiv").html(html);
    return $("#fileImg").click();
}

function fileUploadDataImg() {
    //上传图片
    //上传完得到id 或者路径 标志上传成功
    var data = new FormData($('#formImg')[0]);
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
            //根据点击的图层标识
            var imgPath = ret.msg.imgpath;
            var imgName = ret.msg.img;
            clickImgObj.children("img").attr("src", imgPath);
            clickImgObj.parent().children("input").val(imgName);
            clickImgObj.parent().children("img").show();
            //增加上传图片按钮
            addUploadButton(0);
        }
    });
}

//uploadDivX div层,最多4个图片
function addUploadButton(type) {
    var divImg;
    if (type == 1 || type == '1') {
        divImg = $("#divImg" + clickImgDiv);
    } else {
        divImg = clickImgObj.parent().parent();//点击的图层
    }
    //判断图片中是否有未上传的按键
    var divImgDiv = divImg.children("div");
    var flag = true;
    divImgDiv.each(function () {
        var value = $(this).children("input").val();
        if ("" == value) {
            flag = false;
            return false;
        }
    })
    if (flag && divImg.children("div").length < 4) {
        var html = "<div class='img_div'><div  class='img_file_size'  name='img_name'  onmousemove='javascript:onmousemoveImg($(this))' onmouseout='javascript:onmouseoutImg($(this))' onclick='javascript:onclickImg($(this))'>"
            + "<img  src='../image/upload.png'></div><img style='position: relative;top: -370px;right: 8px;display: none;z-index: 100' width='50px' height='50px' src='../image/delete.png' onclick='javascript:imgDelete($(this))'><input type='hidden' value=''  name='imgName" + clickImgDiv + "' ></div>";
        divImg.append(html);
    }
}

//删除图片
function imgDelete(obj) {
    clickImgDiv = obj.parent().parent().attr("data-div");
    obj.parent().remove();
    //增加上传图片按钮
    addUploadButton(1);
}
