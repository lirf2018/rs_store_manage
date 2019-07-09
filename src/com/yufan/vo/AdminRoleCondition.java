package com.yufan.vo;

import java.io.Serializable;

/**
 * @功能名称 用户角色查询条件
 * @作者 lirongfan
 * @时间 2017年4月6日 下午10:53:55
 */
public class AdminRoleCondition implements Serializable {

    private String roleId;
    private String userName;
    private String loginName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
