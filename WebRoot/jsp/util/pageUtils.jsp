<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>

<script type="text/javascript">
    //  currentPage当前页  totalPages总页数  totalRows总记录数据
    function getPageList(currentPage, totalPages, totalRows) {
        //处理后的页码
        var str = "";
        //计算页面总共分几页,默认以5分页
        var page_page = 5;//每页显示5条
        var flag = true;
        i = 1;
        while (flag) {
            if ((Number((i - 1) * page_page)) < Number(currentPage) && Number(currentPage) <= Number((page_page * i))) {
                for (var j = (i - 1) * page_page + 1; j <= page_page * i; j++) {
                    if (j > totalPages) {
                        break;
                    }
                    if (currentPage == j) {
                        str = str
                            + "<span onclick='listdata("
                            + j
                            + ")'><a style='cursor:pointer;font-size: 25px;color: red;font-weight: bold;'>"
                            + j + "</a></span>";
                    } else {
                        str = str
                            + "<span onclick='listdata("
                            + j
                            + ")'><a style='cursor:pointer;font-size: 25px;font-weight: bold;'>"
                            + j + "</a></span>";
                    }
                }
                break;
            }
            i = i + 1;
        }
        return str;
    }

    //下5页
    function next5Page(currentPage, totalPages, totalRows) {
        currentPage = currentPage + 5;
        //处理后的页码
        //计算页面总共分几页,默认以5分页
        var page_page = 5;//每页显示5条
        var flag = true;
        i = 1;
        while (flag) {
            if ((Number((i - 1) * page_page)) < Number(currentPage) && Number(currentPage) <= Number((page_page * i))) {
                if (Number((i - 1) * page_page + 1) <= totalPages) {
                    listdata(Number((i - 1) * page_page) + 1);
                }
                break;
            }
            i = i + 1;
        }

    }

    //跳转到page
    function gotoByIndex(totalPages, totalRows) {
        var k = $("#topagecount").val();
        if (k == "") {
            alert("跳转页不能为空");
            return;
        }
        if (k > totalPages) {
            alert("最大为 " + totalPages + " 页");
            return;
        }
        var reg = /^\d{1,10}$/g;
        if (!reg.test(k)) {
            alert("只能输入数字");
            $("#topagecount").val("");
            return;
        }
        listdata(k);
    }

</script>