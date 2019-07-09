package com.yufan.weixintest.weixin;

import com.yufan.weixintest.util.XmlConverJsonUtil;
import com.yufan.weixintest.weixin.bean.msg.ResponseMessage;

/**
 * 创建人: lirf
 * 创建时间:  2017-09-11 15:43
 * 功能介绍: 微信消息接口
 */
public class WeixinInformation {


    public static String responseTextMsgXML(String content, String fromUserName, String toUserName) {
        ResponseMessage wresponse = new ResponseMessage();
        wresponse.setFromUserName(fromUserName);//开发者微信号
        wresponse.setToUserName(toUserName);//接收方帐号（收到的OpenID）
        wresponse.setCreateTime("" + System.currentTimeMillis());//消息创建时间 （整型）
        wresponse.setContent(content);//回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
        String xml = XmlConverJsonUtil.toXML(wresponse);
        return xml;
    }
}
