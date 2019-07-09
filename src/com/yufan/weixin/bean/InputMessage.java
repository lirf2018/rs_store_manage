package com.yufan.weixin.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 功能名称: 开发人: lirf 开发时间: 2016下午2:37:58 其它说明：
 */
@XmlRootElement(name = "xml")
public class InputMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String MsgType = "text";
    private Long MsgId;
    // 文本消息
    private String Content;
    // 图片消息
    private String PicUrl;
    // 位置消息
    private String LocationX;
    private String LocationY;
    private Long Scale;
    private String Label;
    // 链接消息
    private String Title;
    private String Description;
    private String URL;
    // 语音信息
    private String MediaId;
    private String Format;
    private String Recognition;
    // 事件
    private String Event;
    private String EventKey;
    private String Ticket;

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
    public Long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Long createTime) {
        CreateTime = createTime;
    }

    @XmlElement(name = "MsgType")
    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    @XmlElement(name = "MsgId")
    public Long getMsgId() {
        return MsgId;
    }

    public void setMsgId(Long msgId) {
        MsgId = msgId;
    }

    @XmlElement(name = "Content")
    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @XmlElement(name = "PicUrl")
    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    @XmlElement(name = "LocationX")
    public String getLocationX() {
        return LocationX;
    }

    public void setLocationX(String locationX) {
        LocationX = locationX;
    }

    @XmlElement(name = "LocationY")
    public String getLocationY() {
        return LocationY;
    }

    public void setLocationY(String locationY) {
        LocationY = locationY;
    }

    @XmlElement(name = "Scale")
    public Long getScale() {
        return Scale;
    }

    public void setScale(Long scale) {
        Scale = scale;
    }

    @XmlElement(name = "Label")
    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @XmlElement(name = "Title")
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @XmlElement(name = "Description")
    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @XmlElement(name = "URL")
    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    @XmlElement(name = "Event")
    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    @XmlElement(name = "EventKey")
    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    @XmlElement(name = "MediaId")
    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    @XmlElement(name = "Format")
    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    @XmlElement(name = "Recognition")
    public String getRecognition() {
        return Recognition;
    }

    public void setRecognition(String recognition) {
        Recognition = recognition;
    }

    @XmlElement(name = "Ticket")
    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }
}
