package com.yufan.weixin.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 功能名称: 输出实体类 OutputMessage 开发人: lirf 开发时间: 2016下午2:51:45 其它说明：
 */
@XmlRootElement(name = "xml")
public class OutputMessage implements Serializable {

    private String ToUserName;

    private String FromUserName;

    private String CreateTime;

    private String MsgType = "text";

    // private ImageMessage Image;
    @XmlElement(name = "ToUserName")
    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    @XmlElement(name = "FromUserName")
    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    @XmlElement(name = "CreateTime")
    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    @XmlElement(name = "MsgType")
    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }


    // public ImageMessage getImage() {
    // return Image;
    // }
    //
    // public void setImage(ImageMessage image) {
    // Image = image;
    // }

}
