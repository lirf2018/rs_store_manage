package com.yufan.weixin.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能名称: 图片信息实体类
 * 开发人: lirf
 * 开发时间: 2016下午2:52:33
 * 其它说明：
 */
public class MediaIdMessage {

    @XStreamAlias("MediaId")
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
