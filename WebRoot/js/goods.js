//鼠标经过type=0和离开type=1上传图片框的时间#E8E8E8
function changeImgFileColor(id, type) {
    if (type == 0) {//经过
        $("#img_id" + id).css("border", "2px solid blue");
    } else {
        $("[name='img_name']").css("background-color", "white");
        $("[name='img_name']").css("border", "3px solid white");
    }
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
    $("#divImg1").append(img_div);
}


var isSaleIndexArray;//数组,保存每个属性行标+展示类型,属性Id,属性名称(是销售属性)
var isNotSaleIndexArray;//数组,保存每个属性行标+展示类型,属性Id,属性名称(不是销售属性)
var isSaleCheckboxIndexArray;//数组,保存销售属性展示方式为checkbox的下标
var allPropValueMap;//Map 用于保存所有属性值列表Key-Value
var hasSku;//是否选择了sku  0是没选择1已选择
var skuList;

//改变分类
function changeClassfyEvent() {
    $("#classifyValueDiv").html("");
    //初始化
    isSaleIndexArray = new Array();
    isNotSaleIndexArray = new Array();
    isSaleCheckboxIndexArray = new Array();
    allPropValueMap = {};
    var index = 0;
    hasSku = 0;
    //classifyDiv 用于展现分类的层id
    var classifyId = $("#listClassfyId").val();
    var goodsId = $("#goodsId").val();
    if (classifyId == '0') {
        $("#classifyDiv").empty();
        return;
    }

    var data = {classifyId: classifyId, goodsId: goodsId};
    //根据分类查询属性和属性值
    $.ajax({
        type: "post",
        data: data,
        async: false,
        cache: false,
        url: "../manage/searClassifyInfosById.action",
        dataType: "json",
        success: function (data) {
            var obj = data.msg.propValue;
            skuList = data.msg.listGoodsSku;
            var html = "<table  class='table table-bordered table-hover m10'>";
            html = html + "<tr><th colspan='2' style='text-align:center;' >商品sku</th></tr>";
            for (var i = 0; i < obj.length; i++) {
                var propValues = obj[i].propvalues;
                if (null != propValues && propValues.length > 0) {
                    index = index + 1;//定义每列下标
                    html = html + "<tr>";
                    //属性Id
                    var propId = obj[i].prop_id;
                    //属性名称
                    var propName = obj[i].prop_name;
                    //是否销售属性
                    var isSales = obj[i].is_sales;
                    var isSalesMsg = "";
                    if (isSales == '1') {
                        isSalesMsg = "<span style='color: red;'>* </span>";
                    }
                    html = html + "<td class='tableleft' style='text-align:center;vertical-align:middle;'>" + isSalesMsg + propName + "</td>";
                    html = html + "<td>";
                    //展示方式
                    var showView = obj[i].show_view;
                    if (isSales == '1') {
                        isSaleIndexArray.push(index + ";" + showView + ";" + propId + ";" + propName);
                    } else {
                        isNotSaleIndexArray.push(index + ";" + showView + ";" + propId + ";" + propName);
                    }
                    if (showView == 'checkbox') {
                        if (isSales == '1') {
                            isSaleCheckboxIndexArray.push(index);
                            for (var j = 0; j < propValues.length; j++) {
                                //属性值Id
                                var propValueId = propValues[j].value_id;
                                var propValue = propValues[j].value;//属性值value
                                var propValueName = propValues[j].value_name;//属性值名称
                                //是否选中 1 选中
                                var isChecked = propValues[j].isChecked;
                                if (isChecked == '1') {
//									html=html+"<input type='checkbox' name='checkbox"+isSaleCount+"' checked='checked' onclick='getAllPropValueMapValue(\"checkbox\","+propId+","+propValueId+","+isSaleCount+")' >"+propValueName+"&nbsp;&nbsp;&nbsp;";
                                    html = html + "<input type='checkbox' name='checkbox" + index + "' checked='checked' onclick='propValueEvent(" + isSales + ")' value='" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                } else {
                                    html = html + "<input type='checkbox' name='checkbox" + index + "' onclick='propValueEvent(" + isSales + ")' value='" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                }
                                allPropValueMap["" + propValueId] = propValueName;
                            }
                        } else {
                            for (var j = 0; j < propValues.length; j++) {
                                //属性值Id
                                var propValueId = propValues[j].value_id;
                                var propValue = propValues[j].value;//属性值value
                                var propValueName = propValues[j].value_name;//属性值名称
                                //是否选中 1 选中
                                var isChecked = propValues[j].isChecked;
                                if (isChecked == '1') {
//									html=html+"<input type='checkbox' name='checkbox"+isSaleCount+"' checked='checked' onclick='getAllPropValueMapValue(\"checkbox\","+propId+","+propValueId+","+isSaleCount+")' >"+propValueName+"&nbsp;&nbsp;&nbsp;";
                                    html = html + "<input type='checkbox' name='checkbox' checked='checked'  value='" + propId + ";" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                } else {
                                    html = html + "<input type='checkbox' name='checkbox'  value='" + propId + ";" + propValueId + "' >" + propValueName + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                                }
                                allPropValueMap["" + propValueId] = propValueName;
                            }
                        }

                    } else {
//						html=html+"<select onchange='getAllPropValueMapValue(\"select\","+propId+","+propValueId+","+isSaleCount+")'>";
                        if (isSales == '1') {//是
                            html = html + "<select onchange='propValueEvent(" + isSales + ")' id='select" + index + "' >";
                            for (var j = 0; j < propValues.length; j++) {
                                //属性值Id
                                var propValueId = propValues[j].value_id;
                                var propValue = propValues[j].value;//属性值value
                                var propValueName = propValues[j].value_name;//属性值名称
                                //是否选中 1 选中
                                var isChecked = propValues[j].isChecked;
                                if (isChecked == '1') {
                                    html = html + "<option selected='selected' value='" + propValueId + "'>" + propValueName + "</option>";
                                } else {
                                    html = html + "<option value='" + propValueId + "'>" + propValueName + "</option>";
                                }
                                allPropValueMap["" + propValueId] = propValueName;
                            }
                        } else {
                            html = html + "<select  id='select' name='select' >";
                            html = html + "<option value=''>--选择属性值--</option>";
                            for (var j = 0; j < propValues.length; j++) {
                                //属性值Id
                                var propValueId = propValues[j].value_id;
                                var propValue = propValues[j].value;//属性值value
                                var propValueName = propValues[j].value_name;//属性值名称
                                //是否选中 1 选中
                                var isChecked = propValues[j].isChecked;
                                if (isChecked == '1') {
                                    html = html + "<option selected='selected' value='" + propId + ";" + propValueId + "'>" + propValueName + "</option>";
                                } else {
                                    html = html + "<option value='" + propId + ";" + propValueId + "'>" + propValueName + "</option>";
                                }
                                allPropValueMap["" + propValueId] = propValueName;
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
                propValueEvent(1);
            }
        }
    });
}

//选择属性值事件showView展现方属性IdpropId 属性值IdvalueId销售属性下标index
function propValueEvent(isSale) {
    hasSku = 0;
    if (isSale == 0 || isSale == '0') {
        return;
    }
    //判断是否所以销售属性都已选择
    var flag = isCheckedSale();
    if (!flag) {
        //清空sku
        $("#classifyValueDiv").empty();
        return;
    }
    //增加sku
    addGoodsSkuHtml(isSaleIndexArray.length);
}

//增加sku
function addGoodsSkuHtml(lengthIsSale) {
    //销售属性的数量 属性行标+展示类型,属性Id,属性名称(是销售属性)
    if (lengthIsSale > 0) {
        var valuesArrays = new Array();
        var titles = new Array();
        for (var i = 0; i < lengthIsSale; i++) {
            var valuesArray = new Array();
            //对每列销售属性选中的值
            var values = isSaleIndexArray[i];
            var value = values.split(";");
            var index = value[0];
            var showView = value[1];
            var title = value[2] + ";" + value[3];
            titles.push(title);
            if ("checkbox" == showView) {
                var indexCheckBox = $("input[name='checkbox" + index + "']:checked");
                $(indexCheckBox).each(function () {
                    valuesArray.push(this.value);
                });
            } else {
                valuesArray.push($("#select" + index).val());
            }
            valuesArrays.push(valuesArray);
        }
        //
        var values = returnGoodsSku(valuesArrays);
        skuHtml(values, titles);
    } else {
        hasSku = 1;
    }
}

//设置页面
function skuHtml(skus, titles) {
    $("#classifyValueDiv").empty();
    var html = "<table  class='table table-bordered table-hover m10'><thead>";
    var names = skus[1];
    var ids = skus[0];
    //计算标题数
    var h = "<tr>";
    var propIds = "";
    for (var i = 0; i < titles.length; i++) {
        h = h + "<th >" + titles[i].split(";")[1] + "</th>";
        propIds = propIds + titles[i].split(";")[0] + ";"
    }

    h = h + "<th>商品sku原价</th><th>商品sku现价</th><th>商品sku库存</th><th>商品sku编码</th><th>商品sku成本价</th>";

    h = h + "</tr>";
    html = html + h + "</thead>";

    html = html + "<tbody id='tbSku'>";
    for (var i = 0; i < ids.length; i++) {//行数
        html = html + "<tr>";
        var rows = names[i];
        var rowsValue = rows.split(";");
        for (var j = 0; j < rowsValue.length; j++) {
            if (rowsValue[j] != '') {
                html = html + "<td>" + rowsValue[j] + "</td>";
            }
        }
        var trueMoney_ = "0";//原价
        var goodsSkuPrice_ = "0";//现价
        var goodsSkuKc_ = "0";
        var goodsSkuCode_ = "";
        var purchasePrice_ = "0";
        if (null != skuList) {
            for (var k = 0; k < skuList.length; k++) {
                if (skuList[k].propCode == ids[i]) {
                    goodsSkuPrice_ = skuList[k].nowMoney;
                    trueMoney_ = skuList[k].trueMoney;
                    goodsSkuKc_ = skuList[k].skuNum;
                    goodsSkuCode_ = skuList[k].skuCode;
                    purchasePrice_ = skuList[k].purchasePrice;
                }
            }
        }

        html = html + "<td><input type='hidden' name='ids' value='" + ids[i] + "' ><input type='hidden' name='names' value='" + rows + "' >";
        html = html + "<input type='text' style='width: 100px;text-align: center' name='trueMoney' value='" + trueMoney_ + "' ></td>";
        html = html + "<td><input type='text' style='width: 100px;text-align: center' name='goodsSkuPrice' value='" + goodsSkuPrice_ + "' ></td>";
        html = html + "<td><input type='text' style='width: 100px;text-align: center' name='goodsSkuKc' value='" + goodsSkuKc_ + "' ></td>";
        html = html + "<td><input type='text' style='width: 100px;text-align: center' name='goodsSkuCode' value='" + goodsSkuCode_ + "' ></td>";
        html = html + "<td><input type='text' style='width: 100px;text-align: center' name='purchasePrice' value='" + purchasePrice_ + "' ></td>";
        html = html + "</tr>";
    }
    html = html + "<input type='hidden' name='propIds' value='" + propIds + "' ></tbody></table>";
    $("#classifyValueDiv").html(html);
    hasSku = 1;
}

//判断是否所有的销售属性都选择了
function isCheckedSale() {
    if (isSaleIndexArray.length > 0 && isSaleCheckboxIndexArray.length == 0) {//所有的销售属性都为selected
        hasSku = 1;
        return true;
    }
    var f = true;
    var c = 0;
    if (isSaleCheckboxIndexArray.length > 0) {
        for (var i = 0; i < isSaleCheckboxIndexArray.length; i++) {
            f = true;
            var index = isSaleCheckboxIndexArray[i];
            var indexCheckBox = $("input[name='checkbox" + index + "']:checked");
            $(indexCheckBox).each(function () {
                if (f) {
                    c = c + 1;
                    f = false;
                }
            });
        }
    }
    if (c == isSaleCheckboxIndexArray.length && isSaleCheckboxIndexArray.length > 0) {
        hasSku = 1;
        return true;
    }
    return false;
}
