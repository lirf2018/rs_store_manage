package com.yufan.common.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.yufan.pojo.TbAdmin;


public class LoginFilter implements Filter {

    public void destroy() {

    }


    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        response.setContentType("text/html; charset=utf-8");
        String uri = request.getRequestURI();
        if (uri.endsWith("/") || uri.endsWith("login.jsp")) {
            request.getRequestDispatcher("/").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        String errorMsg = (String) session.getAttribute("errorMsg");
        TbAdmin user = (TbAdmin) session.getAttribute("user");
        if (user == null) {
            if (errorMsg != null) {
                chain.doFilter(arg0, arg1);
            } else {
                PrintWriter pw = response.getWriter();
                pw.print("<script language=javascript>alert('会话结束');</script>");
                pw.print("<script language=javascript>this.parent.location = '" + request.getContextPath() + "/jsp/login/login.jsp'</script>");
            }
        } else {
            chain.doFilter(arg0, arg1);
        }
    }


    public void init(FilterConfig arg0) throws ServletException {

    }

}
