<%-- <%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %> --%>
<link rel="stylesheet" type="text/css" href="../css/toolbar.css"/>
<input style="display:none"/>
<s:if test="null!=page&&page.pageNo!=0">
    <input type="hidden" id="page.pageNo" name="page.pageNo" value="<s:property value="page.getPageNo()"/>"/>
    <input type="hidden" id="page.pageSize" name="page.pageSize" value="<s:property value="page.pageSize"/>"/>
    <s:if test="page.getTotalPages()==1">
        <table width="90%" border="0" align="center" style="display: none;">
    </s:if>
    <s:else>
        <table width="90%" border="0" align="center" >
    </s:else>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr height="1" class="toolbar" valign="right" border=0>
        <td valign="bottom" align="right">
            共<s:property value="page.getTotalCount()"/>笔&nbsp;&nbsp;第<s:property
                value="page.getPageNo()"/>页/共<s:property value="page.getTotalPages()"/>页&nbsp;
            <s:if test="page.getPageNo()>1">
                <a href="#" onclick="toPage(0)" class="p_redirect">首页</a>
                <a href="#" onclick="toPage(<s:property value="page.getPageNo()-1"/>)" class="p_redirect">上一页</a>
            </s:if>
            <s:else>
                首页&nbsp;&nbsp;上一页
            </s:else>
            <s:if test="page.getPageNo()!=page.getTotalPages()">
                <a href="#" onclick="toPage(<s:property value="page.getPageNo()+1"/>);" class="p_redirect">下一页</a>
                <a href="#" onclick="toPage(<s:property value="page.getTotalPages()"/>);" class="p_redirect">末页</a>
            </s:if>
            <s:else>
                下一页&nbsp;&nbsp;末页
            </s:else>
            <input type="text" id="go" name="go" size="2" maxlength="8"/>&nbsp;&nbsp;<input type="button" class="vbtn"
                                                                                            value="翻页"
                                                                                            onclick="goPage();"/>
        </td>
    </tr>
    </table>
    <script language="javascript">
        function toPage(page) {
            document.getElementById('page.pageNo').value = page;
            document.forms[0].submit();
        }

        function goPage() {
// 	$("#go").formValidator({displayName:"翻页",required:true,maxlength:8,validateType:"Plus"});
            var goPage = $.trim($("#go").val());
            if (goPage.length == 0) {
                alert("翻页的页码不能为空");
                return false;
            }
//     if (checkValid()) {
            document.getElementById('page.pageNo').value = parseInt(document.getElementById('go').value);
            document.forms[0].submit();
//     }
        }
    </script>
</s:if>