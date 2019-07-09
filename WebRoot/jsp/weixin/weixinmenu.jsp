<%@ page language="java" import="java.util.*" pageEncoding="utf-8" contentType="text/html; charset=utf-8" %>
<%@ include file="../util/taglibs.jsp" %>
<%@ include file="../util/meta.jsp" %>
<%@ include file="../util/pageUtils.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>菜单列表</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${path}/jqueryutil/jquery-ui-1.8.21.custom.css"/>
    <script type="text/javascript" src="${path}/assets/js/jquery.js"></script>
    <script type="text/javascript" src="${path}/assets/js/bootstrap.js"></script>
    <script type="text/javascript" src="${path}/assets/js/ckform.js"></script>
    <script type="text/javascript" src="${path}/assets/js/common.js"></script>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-1.7.2.js"></script>
    <script type="text/javascript" src="${path}/jqueryutil/jquery-ui-1.8.21.custom.min.js"></script>
    <style type="text/css">
        body {
            padding-bottom: 40px;
            font-family: "楷体", Helvetica, Arial, sans-serif;
        }

        .sidebar-nav {
            padding: 9px 0;
        }

        #left {
            width: 25%;
            height: 600px;
            float: left;
            font-family: "楷体", Helvetica, Arial, sans-serif;
        }

        #right {
            width: 75%;
            height: 600px;
            float: right;
            font-family: "楷体", Helvetica, Arial, sans-serif;
            /* 		background-color: #F4F5F9;	 */
        }

        .table td, .table th {
            padding-top: 8px;
            padding-bottom: 4px;
            line-height: 20 ppx;
            text-align: center;
            vertical-align: middle;
        }

        footer > div > dl {
            position: relative;
            padding: 0;
            margin: 0;
        }

        footer > div > dl > dt {
            width: 44px;
            position: absolute;
            left: 0;
            top: 0;
        }

        footer > div > dl > dd {
            margin-left: 44px;
            display: flex;
            flex-flow: row;
        }

        footer > div > dl > dd > span {
            display: block;
            float: left;
            flex: 2;
            position: relative;
        }

        footer > div > dl > dd > span:after {
            content: '';
            display: block;
            height: 100%;
            width: 0px;
            position: absolute;
            right: 0;
            top: 0;
            background-color: gainsboro;
        }

        footer > div > dl > dd > span:last-child:after {
            display: none;
        }

        footer > div > dl > dd > span > input {
            border: none;
            padding: 0;
            width: 99%;
            height: 100%;
            background-color: aliceblue;
        }

        footer > div > dl > dd > span > input:focus {
            background-color: rgba(0, 0, 0, 0.1);
        }

        footer > div > dl > dd:after {
            display: block;
            content: '';
            clear: both;
        }

        nav > div > span > input:focus {
            background-color: rgba(0, 0, 0, 0.1);
        }

        @media ( max-width: 980px) {
            /* Enable use of floated navbar text */
            .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
            }
        }

        #addOrEditDialog {
            font-family: "楷体", Helvetica, Arial, sans-serif;
        }
    </style>
</head>
<body>
<!--微信渠道 -->
<input type="hidden" value="" id="weixin_channel_param">
<!--是否有子菜单 0 没有 1有 -->
<input type="hidden" value="0" id="if_have_sub">
<input type="hidden" value="0" id="menu_id">
<input type="hidden" value="0" id="smenu_id">
<!--当前点击的菜单 0 一级菜单 1子菜单 -->
<input type="hidden" value="0" id="click_menu_id">
<!--按钮类型列表 -->
<input type="hidden" value="" id="menu_type_list">
<!--页面代码信息 -->
<input type="hidden" value="" id="value_0">
<input type="hidden" value="" id="value_1">
<input type="hidden" value="" id="value_2">

<input type="hidden" value="" id="value1">
<input type="hidden" value="" id="value2">

<div style="width: 100%;height: 50px;">
    <div style="width: 100%;height: 100%;padding-left: 100px">选择微信渠道： <s:select list="#{0:'--选择微信渠道--'}"
                                                                                onchange="selectWeixinChannel()"
                                                                                listKey="key"
                                                                                cssStyle="width:120px;border-radius:7px;"
                                                                                listValue="value" value=""
                                                                                id="weixin_channel_list"/>
        <input type="button" onclick="loadMenuFronWX(1)" value=" 获取微信菜单  "
               style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;"><span
                style="color: red;margin-left: 7px;padding-left: 20px;">*警告:点击获取微信菜单,则会清除当前已经设置好的菜单</span>
    </div>
</div>
<div id="left">
    <nav>
        <div id="sub_menu"
             style="border: 1px solid #DCDCDC;background-color: aliceblue;width: 120px;text-align: center;position: absolute;display: none;">
            <!-- <span><input name="sub_menu" type="button" value="子菜单名称" style="height: 50px;text-align: center;font-weight: 100;width: 120px;"></span>
            <span><input name="sub_menu" type="button" value="+" style="height: 50px;text-align: center;font-weight: 100;width: 120px;"></span> -->
        </div>
    </nav>
    <div style="border: 1px gainsboro solid;height: 530px;border-bottom: 0px;">
        <img src="${path}/image/09.png" width="100%"/>
    </div>
    <footer>
        <div style="border: 1px gainsboro solid;height: 50px;background-color: rgba(0,0,0,0.1);">
            <dl>
                <dt><img src="${path}/image/42.png"></dt>
                <dd id="pmenu">
                    <!-- 	<span><input type="button" value="+" style="height: 50px;text-align: center;font-weight: 100;width: none;"></span>
                        <span><input type="button" value="上级菜单" style="height: 50px;text-align: center;font-weight: 100;width: 99%;"></span>
                        <span><input type="button" value="上级菜单" style="height: 50px;text-align: center;font-weight: 100;width: 99%;"></span>
                         -->
                </dd>
            </dl>
        </div>
    </footer>
    <!-- 底部操作 -->
    <div style="width: 100%">
        <table style="width: 100%;padding: 25px;margin-right: 10px;text-align: left;">
            <tr>
                <td>
                    <span><input onclick="addOrEditDialog(0)" type="button" value="一级菜单排序 "
                                 style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;"></span>
                </td>
                <td>
                    <span><input onclick="addOrEditDialog(1)" type="button" value="子菜单排序 "
                                 style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;"></span>
                </td>
            </tr>
        </table>
    </div>
</div>
<div id="right">
    <!--默认编辑页面-->
    <div id="div_2" style="display: none;">
        <div style="width: 80%;height: 100%;background-color: #F4F5F9;">
            <table style="width: 100%;padding: 25px;margin-right: 10px;height: 50%;">
                <tr>
                    <td style="text-align: center;">
						<span><span style="color: green;">温馨提示：</span>编辑中的菜单不会马上被用户看到，点击发布后，
						会在24小时后在手机端同步显示，粉丝不会收到更新提示，若多次编辑，以最后一次保存为准。</span><br><br><br><br><br><br><br>
                        <span>点击左侧菜单进行编辑操作</span></td>
                </tr>
            </table>
        </div>
        <!-- 底部操作 -->
        <div style="width: 100%">
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
                        <span><input type="button" onclick="saveAndReleaseWeixinMenu()" value="  保存并发布  "
                                     style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;color: white;background-color: green;"></span>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <!--无子菜单编辑页面-->
    <div id="div_0" style="display: none;">
        <div style="width: 80%;height: 100%;background-color: #F4F5F9;">
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;"><span id="v_menu_name"></span></td>
                    <td style="text-align: right;"><span onclick="delMenu()"
                                                         style="color: blue;cursor:pointer;text-decoration:underline">删除菜单</span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: left;" colspan="2">
                        <hr>
                    </td>
                </tr>
            </table>
            <table style="width: 100%;padding-left: 25px;padding-right: 25px;">
                <tr>
                    <td style="text-align: left;"><span id="btn_msg"></span></td>
                </tr>
            </table>
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
                        <span style="color: red;">* </span><span>菜单名称:<input onblur="updateMenu()" id="v_menu_name_"
                                                                             type="text" value=""
                                                                             style="padding-left: 10px;margin-left: 20px;height: 30px;border-radius:7px;border: 1px solid #cccccc;">
							<span style="color: red;margin-left: 7px;">*字数不超过4个汉字或8个字母</span></span>
                    </td>
                </tr>
            </table>
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
							<span id="p_type"><span style="color: red;">* </span>按钮类型:
	 							<s:select list="#{0:'--选择按钮类型--'}" onchange="updateMenu()" listKey="key"
                                          cssStyle="width:120px;border-radius:7px;" listValue="value" value=""
                                          id="button_type"/>
							</span>
                        <span id="btn_msg1" style="padding-left: 20px;"></span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: left;">
                        <div style="width: 100%;margin-top: 15px;" id="menu_type_view">

                        </div>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 底部操作 -->
        <div style="width: 100%">
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
                        <span><input type="button" onclick="saveAndReleaseWeixinMenu()" value="  保存并发布  "
                                     style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;color: white;background-color: green;"></span>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <!--有菜单编辑页面-->
    <div id="div_1" style="display: none;">
        <div style="width: 80%;height: 100%;background-color: #F4F5F9;">
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;"><span id="v_menu_name"></span></td>
                    <td style="text-align: right;"><span onclick="delMenu()"
                                                         style="color: blue;cursor:pointer;text-decoration:underline">删除菜单</span>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: left;" colspan="2">
                        <hr>
                    </td>
                </tr>
                <tr>
                    <td style="text-align: left;" colspan="2">已添加子菜单，仅可设置菜单名称。</td>
                </tr>
            </table>
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
                        <span style="color: red;">* </span><span>菜单名称:<input onblur="updateMenu()" id="v_menu_name_"
                                                                             type="text" value=""
                                                                             style="padding-left: 10px;margin-left: 20px;height: 30px;border-radius:7px;border: 1px solid #cccccc;">
							<span style="color: red;margin-left: 7px;">*字数不超过4个汉字或8个字母</span></span>
                    </td>
                </tr>
            </table>
        </div>
        <!-- 底部操作 -->
        <div style="width: 100%">
            <table style="width: 100%;padding: 25px;margin-right: 10px">
                <tr>
                    <td style="text-align: left;">
                        <span><input type="button" onclick="saveAndReleaseWeixinMenu()" value="  保存并发布  "
                                     style="margin-left: 10px;height: 30px;border-radius:5px;border: 1px solid #cccccc;font-size: 20px;color: white;background-color: green;"></span>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <!--按钮类型为click的编辑页面 -->
    <div style="display: none;" id="div1">
        <table style="width: 100%;padding: 25px;margin-right: 10px;background-color: white;">
            <tr>
                <td><span>自定义的key值与用户进行交互</span><span style="color: red;"></span></td>
            </tr>
            <tr>
                <td><span style="color: red;">* </span><span>key:<input onblur="updateMenu()" id="v_menu_key"
                                                                        type="text" value=""
                                                                        style="padding-left: 10px;margin-left: 20px;margin-top:15px;height: 30px;border-radius:7px;border: 1px solid #cccccc;width: 80%"></span>
                </td>
            </tr>
        </table>
    </div>
    <!--按钮类型为view的编辑页面 -->
    <div style="display: none;" id="div2">
        <table style="width: 100%;padding: 25px;margin-right: 10px;background-color: white;">
            <tr>
                <td><span>订阅者点击该菜单会跳到以下链接</span><span style="color: red;"> 注意：跳转链接请加 http:// 或  https://</span></td>
            </tr>
            <tr>
                <td><span style="color: red;">* </span><span>跳转地址:<input onblur="updateMenu()" id="v_menu_foward"
                                                                         type="text" value=""
                                                                         style="padding-left: 10px;margin-left: 20px;margin-top:15px;height: 30px;border-radius:7px;border: 1px solid #cccccc;width: 80%"></span>
                </td>
            </tr>
        </table>
    </div>

</div>
<!-- 按钮类型备注 -->
<div style="display: none;" id="btn_remark">

</div>
<!-- 微信渠道列表 -->
<div style="display: none;" id="wixin_channel_remark">

</div>

<!-- dialog -->
<div id="addOrEditDialog" style="display: none;">
    <form action="" id="submit_form">
        <table style="width: 100%;text-align: center; ">
            <tr>
                <th>标识</th>
                <th>名称</th>
                <th>排序</th>
            </tr>
            <tbody id="tb">

            </tbody>
        </table>
    </form>
</div>
</body>
<script type="text/javascript">

    //初始化页面信息
    pageCode_();

    function pageCode_() {
        $("#value_0").val($("#div_0").html());//无子菜单
        $("#value_1").val($("#div_1").html());//有子菜单
        $("#value_2").val($("#div_2").html());//默认

        $("#value1").val($("#div1").html());
        $("#value2").val($("#div2").html());

        //清空
        $("#right").html("");

        $("#right").html($("#value_2").val());
        loadWeixinChannel();
    }

    //加载微信渠道列表
    function loadWeixinChannel() {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeixinChannel.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "";
                var str_btn_remark = "";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        if (i == 0) {
                            $("#weixin_channel_param").val(data.msg[i].param_key);
                            str = str + "<option selected='selected' value='" + data.msg[i].param_id + "'> " + data.msg[i].param_value + " </option>";
                        } else {
                            str = str + "<option value='" + data.msg[i].param_id + "'> " + data.msg[i].param_value + " </option>";
                        }
                        str_btn_remark = str_btn_remark + "<input type='hidden' value='" + data.msg[i].param_key + "' id='wx" + data.msg[i].param_id + "'>";
                    }
                    $("#weixin_channel_list").html(str.toString());
                    $("#wixin_channel_remark").html(str_btn_remark.toString());
                    loadMenu();
                } else if (msg.length == 0) {
                    alert("请先添加微信渠道");
                    $("input").css("display", "none");
                } else {
                    alert("数据加载失败");
                    $("input").css("display", "none");
                }
            }
        });
    }

    //同步微信菜单
    function loadMenuFronWX(from_) {
        var appId_secret = $("#weixin_channel_param").val();
        $.ajax({
            type: "post",
            data: {menu_parent: "0", appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/loadMenuFronWeiXin.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert("微信数据拉取成功");
                    if (from_ == "1") {
                        //隐藏子级菜单
                        $("#sub_menu").hide();
                        getViewCode("3", "0", menu_id, "1");//背景页
                        loadMenu();
                    }
                } else if (data.flag == "false") {
                    alert(data.msg);
                } else {
                    alert("微信数据拉取失败");
                }
            }
        });
    }

    //加载父级菜单
    function loadMenu() {
        var appId_secret = $("#weixin_channel_param").val();
        $.ajax({
            type: "post",
            data: {menu_parent: "0", appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeiXinMenu.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                if (data.flag == "ok") {
                    $("#pmenu").empty();
                    if (obj.length > 0) {
                        var strs = "";
                        for (var i = 0; i < obj.length; i++) {
                            strs = strs + "<span id='span" + obj[i].menu_id + "'><input onclick=clickMenuViewP(" + obj[i].menu_id + ") type='button' value='" + obj[i].menu_name + "' style='height: 50px;text-align: center;font-weight: 100;width: none;'></span>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_id + "' id='menu_id" + obj[i].menu_id + "'>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_name + "' id='menu_name" + obj[i].menu_id + "'>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_foward + "' id='menu_foward" + obj[i].menu_id + "'>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_parent + "' id='menu_parent" + obj[i].menu_id + "'>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_type + "' id='menu_type" + obj[i].menu_id + "'>";
                            strs = strs + "<input type='hidden' value='" + obj[i].menu_key + "' id='menu_key" + obj[i].menu_id + "'>";
                        }
                        if (obj.length < 3) {
                            strs = strs + "<span onclick='addPmenu()'><input type='button' value='+' style='height: 50px;text-align: center;font-weight: 100;width: none;'></span>";
                        }
                        $("#pmenu").html(strs.toString());
                    } else {
                        var strs = "<span onclick='addPmenu()'><input type='button' value='+' style='height: 50px;text-align: center;font-weight: 100;width: none;'></span>";
                        $("#pmenu").html(strs.toString());
                    }
                } else {
                    alert("数据加载失败");
                }
            }
        });
    }

    //加载子菜单
    function loadSubMenu(menu_id) {
        var appId_secret = $("#weixin_channel_param").val();
        $.ajax({
            type: "post",
            data: {menu_parent: menu_id, appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeiXinMenu.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#sub_menu").empty();
                if (obj.length > 0) {
                    var strs = "";
                    for (var i = 0; i < obj.length; i++) {
                        var smenu_name = obj[i].menu_name;
                        if (smenu_name.length > 5 && smenu_name != "子菜单名称") {
                            smenu_name = smenu_name.substring(0, 4);
                            smenu_name = smenu_name + "...";
                        }
                        strs = strs + "<span><input onclick=clickSubMenuView(" + obj[i].menu_id + ") name='sub_menu' type='button' value='" + smenu_name + "' style='height: 50px;text-align: center;font-weight: 100;width: 120px;'></span>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_id + "' id='smenu_id" + obj[i].menu_id + "'>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_name + "' id='smenu_name" + obj[i].menu_id + "'>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_foward + "' id='smenu_foward" + obj[i].menu_id + "'>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_parent + "' id='smenu_parent" + obj[i].menu_id + "'>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_type + "' id='smenu_type" + obj[i].menu_id + "'>";
                        strs = strs + "<input type='hidden' value='" + obj[i].menu_key + "' id='smenu_key" + obj[i].menu_id + "'>";
                    }
                    if (obj.length < 5) {
                        strs = strs + "<span><input onclick='addSubPmenu()' name='sub_menu' type='button' value='+' style='height: 50px;text-align: center;font-weight: 100;width: 120px;'></span>";
                    }
                    $("#sub_menu").html(strs.toString());
                } else {
                    var strs = "<span><input onclick='addSubPmenu()' name='sub_menu' type='button' value='+' style='height: 50px;text-align: center;font-weight: 100;width: 120px;'></span>";
                    $("#sub_menu").html(strs.toString());
                }
            }
        });

        //得到点击位置
        var obj = $("#span" + menu_id).offset();
        var offsetTop = obj.top;
        var offsetLeft = obj.left;

        var offsetWidth = $("#span" + menu_id).width();

        var sub_menu = $("input[name='sub_menu']");
        $("#sub_menu").css("width", offsetWidth + "px");
        sub_menu.css("width", offsetWidth + "px");
        var obj_div = $("#sub_menu");
        var left = Number(offsetLeft) - Number(1);
        obj_div.css("left", left + "px");

        var obj_ = $("#sub_menu").height();
        var top = Number(offsetTop) - Number(obj_) - Number(5);
        obj_div.css("top", top + "px");

    }

    //加载按钮列表  当前菜单的menu_type
    function loadButtonType(menu_type) {
        $.ajax({
            type: "post",
            data: {},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeixinButtonType.action",
            dataType: "json",
            success: function (data) {
                var msg = data.msg;
                var str = "";
                var str_btn_remark = "";
                if (msg.length > 0) {
                    for (var i = 0; i < msg.length; i++) {
                        if (data.msg[i].param_value == menu_type) {
                            str = str + "<option selected='selected' value='" + data.msg[i].param_value + "'> " + data.msg[i].param_key + " </option>";
                        } else {
                            str = str + "<option value='" + data.msg[i].param_value + "'> " + data.msg[i].param_key + " </option>";
                        }
                        str_btn_remark = str_btn_remark + "<input type='hidden' value='" + data.msg[i].remark + "' id='btn" + data.msg[i].param_value + "'>";
                        str_btn_remark = str_btn_remark + "<input type='hidden' value='" + data.msg[i].param_value1 + "' id='btn1" + data.msg[i].param_value + "'>";
                    }
                } else {
                    str = "<option value='0' selected='selected'>--选择按钮类型--</option>";
                }
                $("#menu_type_list").val(str.toString());
                $("#btn_remark").html(str_btn_remark.toString());
            }
        });
    }

    //选择微信渠道
    function selectWeixinChannel() {
        $("#weixin_channel_param").val($("#wx" + $("#weixin_channel_list").val()).val());
        //隐藏子级菜单
        $("#sub_menu").hide();
        getViewCode("3", "0", menu_id, "1");//背景页
        loadMenu();
    }

    //增加一级菜单+
    function addPmenu() {
        var appId_secret = $("#weixin_channel_param").val();
        var menu_name = "菜单名称";
        var menu_type = "1";//默认为click类按钮
        $.ajax({
            type: "post",
            data: {
                menu_parent: "0",
                menu_name: menu_name,
                handleType: "add",
                menu_type: menu_type,
                appId_secret: appId_secret
            },
            async: false,
            cache: false,
            url: "${path}/weixin/addWeixinMenu.action",
            dataType: "json",
            success: function (data) {
                //隐藏子级菜单
                $("#sub_menu").hide();
                getViewCode("3", "0", menu_id, "1");//背景页
                loadMenu();
            }
        });
    }

    //点击增加子菜单+
    function addSubPmenu() {
        var appId_secret = $("#weixin_channel_param").val();
        var menu_name = "子菜单名称";
        var menu_type = "1";//默认为click类按钮
        $.ajax({
            type: "post",
            data: {
                menu_parent: $("#menu_id").val(),
                menu_name: menu_name,
                handleType: "add",
                appId_secret: appId_secret,
                menu_type: menu_type
            },
            async: false,
            cache: false,
            url: "${path}/weixin/addWeixinMenu.action",
            dataType: "json",
            success: function (data) {
                loadSubMenu($("#menu_id").val());
                getViewCode("3", "0", menu_id, "1");//背景页
            }
        });
    }

    //更新菜单
    function updateMenu() {
        var appId_secret = $("#weixin_channel_param").val();
        var menu_id = "";
        var menu_parent = "";
        var menu_name = $("#v_menu_name_").val();
        var menu_foward = $("#v_menu_foward").val();
        var menu_type = $("#button_type").val();
        var menu_key = $("#v_menu_key").val();
        //0一级菜单 1子菜单
        var now_click_menu = $("#click_menu_id").val();

        if (now_click_menu == "0") {//一级菜单
            menu_id = $("#menu_id").val();
            menu_parent = $("#menu_parent" + menu_id).val();
            if (menu_name == "" || null == menu_name) {
                menu_name = "菜单名称";
// 				$("#no_menu_name").html("请输入菜单名称");
            } else {
// 				$("#no_menu_name").html("");
            }
        } else {
            menu_id = $("#smenu_id").val();
            menu_parent = $("#smenu_parent" + menu_id).val();
            if (menu_name == "" || null == menu_name) {
                menu_name = "子菜单名称";
// 				$("#no_menu_name").html("请输入菜单名称");
            } else {
// 				$("#no_menu_name").html("");
            }
        }
        if ($("#if_have_sub").val() == "1") {//有子菜单
            menu_type = "0";
        } else {
            if (menu_type == "0" || menu_type == "") {
                alert("请添加按钮类型");
                return;
            }
            if (now_click_menu == "0") {//一级菜单
                $("#menu_type" + menu_id).val(menu_type);
                $("#menu_foward").val($("#v_menu_foward").val());
                $("#menu_key").val($("#v_menu_key").val());
            } else {
                $("#menu_type" + menu_id).val(menu_type);
                $("#smenu_foward").val($("#v_menu_foward").val());
                $("#smenu_key").val($("#v_menu_key").val());
            }
        }
        $.ajax({
            type: "post",
            data: {
                menu_name: menu_name,
                menu_foward: menu_foward,
                menu_parent: menu_parent,
                id: menu_id,
                menu_type: menu_type,
                appId_secret: appId_secret,
                menu_key: menu_key
            },
            async: false,
            cache: false,
            url: "${path}/weixin/addWeixinMenu.action",
            dataType: "json",
            success: function (data) {
                if ($("#click_menu_id").val() == "0") {//更新一级菜单
                    loadMenu();
                    getViewCode($("#if_have_sub").val(), menu_type, menu_id, "0");//
                } else {
                    loadSubMenu($("#menu_id").val());
                    getViewCode($("#if_have_sub").val(), menu_type, menu_id, "1");//
                }

            }
        });
    }

    //点击菜单
    function clickMenuViewP(menu_id) {
        var appId_secret = $("#weixin_channel_param").val();
        $("#menu_id").val($("#menu_id" + menu_id).val());
        $("#click_menu_id").val("0");
        //加载子菜单
        loadSubMenu(menu_id);
        //显示子级菜单
        $("#sub_menu").show();
        //查询是否有子菜单
        $.ajax({
            type: "post",
            data: {menu_parent: menu_id, appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeiXinMenu.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                //有子菜单
                if (obj.length > 0) {
                    getViewCode("1", $("#menu_type" + menu_id).val(), menu_id, "0");
                    $("#if_have_sub").val("1");
                } else {
                    loadButtonType($("#menu_type" + menu_id).val());
                    getViewCode("0", $("#menu_type" + menu_id).val(), menu_id, "0");
                    $("#if_have_sub").val("0");
                    $("#v_menu_foward").val($("#menu_foward" + menu_id).val());
                    $("#v_menu_key").val($("#menu_key" + menu_id).val());
                }
            }
        });
    }

    //点击子菜单
    function clickSubMenuView(menu_id) {
        $("#click_menu_id").val("1");
        $("#smenu_id").val($("#smenu_id" + menu_id).val());
        $("#if_have_sub").val("0");
        loadButtonType($("#smenu_type" + menu_id).val());
        getViewCode("0", $("#smenu_type" + menu_id).val(), menu_id, "1");
        $("#v_menu_foward").val($("#smenu_foward" + menu_id).val());
        $("#v_menu_key").val($("#smenu_key" + menu_id).val());
    }

    //删除菜单
    function delMenu() {
        var from_ = $("#click_menu_id").val();
        if (!confirm("删除后“菜单名称”菜单下设置的内容将被删除,确认删除吗?")) {
            return;
        }
        var id = $("#smenu_id").val();
        if (from_ == 0 || from_ == "0") {
            id = $("#menu_id").val();
        }
        $.ajax({
            type: "post",
            data: {id: id},
            async: false,
            cache: false,
            url: "${path}/weixin/delWeixinMenu.action",
            dataType: "json",
            success: function (data) {
                if (from_ == 0 || from_ == "0") {
                    //隐藏子级菜单
                    $("#sub_menu").hide();
                    loadMenu();
                    getViewCode("3", "", "", "");
                } else {
                    loadSubMenu($("#menu_id").val());
                    getViewCode("3", "", "", "");
                }
            }
        });
    }

    //菜单窗口
    function addOrEditDialog(from_) {
        var title = "排序";
        var count = "0";
        if (from_ == 0 || from_ == "0") {
            title = "排序:          当前编辑一级菜单排序";
            count = setDate("0");
        } else {
            if ($("#menu_id").val() == "" || $("#menu_id").val() == "0") {
                alert("请先选择一级菜单");
                return;
            }
            count = setDate($("#menu_id").val());
            title = "排序:          当前编辑一级菜单为《" + $("#menu_name" + $("#menu_id").val()).val() + "》的子菜单排序";
        }
        var arr = new Array();
        //0,1,2,3,4,5,6,7
        arr = [150, 180, 250, 330, 410, 490, 550, 620];
        //设置值
        $("#addOrEditDialog").dialog({
            autoOpen: true,
            height: arr[count],
            width: 600,
            title: "排序",
            hide: "slide",
            show: "slide",
            position: ['center', 'buttom'],
            modal: true,//蒙层（弹出会影响页面大小）
            buttons: {
                "保存": function () {
                    if (count > 0) {
                        saveMenuIndex(from_);
                    }
                },
                "取消": function () {
                    $("#addOrEditDialog").dialog("close");
                }
            }
        }).show();
    }

    //处理菜单
    function setDate(menu_parent) {
        var appId_secret = $("#weixin_channel_param").val();
        var count = "0";
        $.ajax({
            type: "post",
            data: {menu_parent: menu_parent, appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/loadWeiXinMenu.action",
            dataType: "json",
            success: function (data) {
                var obj = data.msg;
                $("#tb").empty();
                count = obj.length;
                if (obj.length > 0) {
                    for (var i = 0; i < obj.length; i++) {
                        var strs = "";
                        strs = strs + "<tr>";
                        strs = strs + "<td><span>" + obj[i].menu_id + "</span><input type='hidden' value='" + obj[i].menu_id + "' name='i_id'></td>";
                        strs = strs + "<td><span>" + obj[i].menu_name + "</span></td>";
                        strs = strs + "<td><input id='index" + obj[i].menu_id + "' onkeyup='nullString(" + obj[i].menu_id + ")' type='text' value='" + obj[i].menu_index + "' name='index' style='padding-left: 10px;margin-left: 20px;margin-top:15px;height: 30px;border-radius:7px;border: 1px solid #cccccc;width: 10%'></td>";
                        strs = strs + "</tr>";
                        if (i != obj.length - 1) {
                            strs = strs + "<tr><td colspan='3'><span><hr></span></td></tr>";
                        }
                        $("#tb").append(strs.toString());
                    }
                } else {
                    $("#tb").append("<tr><td colspan='3'><span style='color: red;'>无数据</span></td></tr>");
                }
            }
        });
        return count;
    }

    //保存菜单排序
    function saveMenuIndex(from_) {

        var index = $("input[name='index']");
        var reg = /^[0-9]*$/;
        for (var i = 0; i < index.length; i++) {
            if (!reg.test(index[i].value)) {
                alert("排序只能为数字!");
                return;
            }
        }
        $.ajax({
            type: "post",
            data: $("#submit_form").serialize(),
            async: false,
            cache: false,
            url: "${path}/weixin/saveIndex.action",
            dataType: "json",
            success: function (data) {
                alert(data.msg);
                $("#addOrEditDialog").dialog("close");
                if (from_ == 0 || from_ == "0") {//如果修改是一级菜单
                    //隐藏子级菜单
                    $("#sub_menu").hide();
                    loadMenu();
                    getViewCode("3", "", "", "");
                } else {
                    loadSubMenu($("#menu_id").val());
                    getViewCode("3", "", "", "");
                }
            }
        });
    }

    //除去空格
    function nullString(id) {
        var value = $("#index" + id).val();
        $("#index" + id).val(value.trim());
    }

    //返回菜单编辑页面 if_have_sub(是否有子菜单) menu_type(按钮类型) from_(0一级菜单,1子菜单,其它)
    function getViewCode(if_have_sub, menu_type, menu_id, from_) {
        //清空
        $("#right").html("");
        $("#btn_msg").html("");
        $("#btn_msg1").html("");

        if (if_have_sub == "1") {//有子菜单
            $("#right").html($("#value_1").val());

            $("#v_menu_name").html($("#menu_name" + menu_id).val());
            $("#v_menu_name_").val($("#menu_name" + menu_id).val());

        } else if (if_have_sub == "0") {//没有子菜单
            $("#right").html($("#value_0").val());

            if (from_ == "0") {//修改一则菜单
                $("#v_menu_name").html($("#menu_name" + menu_id).val());
                $("#v_menu_name_").val($("#menu_name" + menu_id).val());
            } else if (from_ == "1") {
                $("#v_menu_name").html($("#smenu_name" + menu_id).val());
                $("#v_menu_name_").val($("#smenu_name" + menu_id).val());
            }

            //重新加载按钮菜单下拉列表
            loadButtonType(menu_type);
            $("#btn_msg1").html("当前为 \"" + $("#btn1" + menu_type).val() + "\" 类型按钮");
            if (menu_type == "2") {//view：跳转URL
                //加载按钮类型下拉列表
                $("#button_type").html($("#menu_type_list").val());
                //设置menu_type==2的编辑界面
                $("#menu_type_view").html($("#value" + menu_type).val());
                //设置选中的按钮类型备注
                $("#btn_msg").html("按钮类型说明:<br> " + $("#btn" + menu_type).val());

                //设置menu_foward
                if ($("#click_menu_id").val() == "0") {//点击一级菜单
                    $("#v_menu_foward").val($("#menu_foward" + menu_id).val());
                } else {
                    $("#v_menu_foward").val($("#smenu_foward" + menu_id).val());
                }
            } else if (menu_type == "1") {//click：点击推事件
                //加载按钮类型下拉列表
                $("#button_type").html($("#menu_type_list").val());
                //设置menu_type==1的编辑界面
                $("#menu_type_view").html($("#value" + menu_type).val());
                //设置选中的按钮类型备注
                $("#btn_msg").html("按钮类型说明:<br> " + $("#btn" + menu_type).val());

                //设置menu_key
                if ($("#click_menu_id").val() == "0") {//点击一级菜单
                    $("#v_menu_key").val($("#menu_key" + menu_id).val());
                } else {
                    $("#v_menu_key").val($("#smenu_key" + menu_id).val());
                }
            }

        } else {
            $("#right").html($("#value_2").val());
        }
    }

    //保存和发布
    function saveAndReleaseWeixinMenu() {
        var appId_secret = $("#weixin_channel_param").val();
        $.ajax({
            type: "post",
            data: {appId_secret: appId_secret},
            async: false,
            cache: false,
            url: "${path}/weixin/saveAndReleaseWeixinMenu.action",
            dataType: "json",
            success: function (data) {
                if (data.flag == "ok") {
                    alert(data.msg);
                    //隐藏子级菜单
                    $("#sub_menu").hide();
                    loadMenu();
                    getViewCode("3", "", "", "");
                } else if (data.flag == "false") {
                    alert(data.msg);
                } else {
                    alert("微信菜单发布失败");
                }
            }
        });
    }
</script>
</html>
