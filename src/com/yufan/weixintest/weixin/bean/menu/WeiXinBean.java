package com.yufan.weixintest.weixin.bean.menu;

import java.util.List;

/**
 * 功能名称: 微信菜单类
 * 开发人: lirf
 * 开发时间: 2016下午6:21:57
 * 其它说明：
 */
public class WeiXinBean {

    /**
     * 按钮名称
     */
    private String name;

    /**
     * 子按钮
     */
    private List<WeiXinSubBean> sub_button;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeiXinSubBean> getSub_button() {
        return sub_button;
    }

    public void setSub_button(List<WeiXinSubBean> sub_button) {
        this.sub_button = sub_button;
    }
}
