package com.yufan.util;

/**
 * 功能名称: 常量定义接口
 * 开发人: lirf
 * 开发时间: 2015上午1:11:40
 * 其它说明：
 */
public interface RsConstants {

    /**
     * 分页:每页显示记录数
     */
    public static final Integer PAGE_SIZE = 20;

    /**
     * 删除权限角色  user_ids 设置有删除权限的用户
     */
    public static final String user_ids = "1";

    /**
     * 图片访问地址
     */
    public static final String phone_url = ConfigProperty.getConfigValue("phone_url");
}
