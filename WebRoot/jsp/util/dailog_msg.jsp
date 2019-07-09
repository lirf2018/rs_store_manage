<%@ page language="java" import="java.util.*" pageEncoding="utf-8"
         contentType="text/html; charset=utf-8" %>

<body>
<div style="display: none;" id="alert_msg">
    <table>
        <tr>
            <td align="center"><b id="msg" style="font-size: 20px;font-weight: 3px;">输入值未符合要求!</b></td>
        </tr>
    </table>
</div>
</body>
<script type="text/javascript">
    function alert_msg(msg) {
        $("#msg").html(msg);
        $("#alert_msg").dialog({
            autoOpen: true,
            height: 100,
            width: 200,
            title: "提示",
            position: ['center', 'buttom'],
            modal: true,//蒙层(弹出会影响页面大小)
            buttons: {
                "确定": function () {
                    $("#alert_msg").dialog("close");
                }
            }
        });
    }
</script>
</html>
