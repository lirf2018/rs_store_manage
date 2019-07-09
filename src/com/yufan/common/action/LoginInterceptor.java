package com.yufan.common.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.yufan.pojo.TbAdmin;

/**
 * 功能名称: 拦截器
 * 开发人: lirf
 * 开发时间: 2015下午5:22:58
 * 其它说明：
 */
public class LoginInterceptor extends AbstractInterceptor {

    public String intercept(ActionInvocation ai) throws Exception {
        ActionContext ac = ai.getInvocationContext();
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html; charset=utf-8");
        String uri = request.getRequestURI();
        if (uri.endsWith("/userLogin.action")) {
            return ai.invoke();
        }
        Map session = ac.getSession();
        TbAdmin user = (TbAdmin) session.get("user");
        if (user == null) {
            PrintWriter pw = response.getWriter();
            pw.print("<script language=javascript>alert('会话结束');</script>");
            pw.print("<script language=javascript>this.parent.parent.location = '" + request.getContextPath() + "/jsp/login/login.jsp'</script>");
            return null;
        }
        return ai.invoke();
    }
}
