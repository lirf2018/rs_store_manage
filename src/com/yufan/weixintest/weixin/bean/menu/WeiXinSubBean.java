package com.yufan.weixintest.weixin.bean.menu;

/**
 * 功能名称: 微信子菜单bean
 * 开发人: lirf
 * 开发时间: 2016下午6:22:29
 * 其它说明：
 */
public class WeiXinSubBean {

    /**
     * 按钮名称
     */
    private String name;

    /**
     * 按钮类型
     */
    private String type;

    /**
     * 按钮跳转地址
     */
    private String url;

    /**
     * 按钮key
     */
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
