package com.yufan.weixintest.other;

import com.opensymphony.xwork2.ActionSupport;
import com.yufan.weixintest.other.WeixinBaseDev;

/**
 * @功能名称 微信入口
 * @作者 lirongfan
 * @时间 2015年9月1日 上午11:22:54
 */
public class IndexAction extends ActionSupport {

    private WeixinBaseDev weixinBaseDev = new WeixinBaseDev();
    /**
     * 获取微信参数
     */
    String appId = ""; // 必填，公众号的唯一标识
    String timestamp = ""; // 必填，生成签名的时间戳
    String nonceStr = ""; // 必填，生成签名的随机串
    String signature = "";// 必填，签名，见附录1

    /**
     * 入口index
     *
     * @return
     */
    public String index() {
        System.out.println("---------->入口index");

        /*扫码begin*/
//		System.out.println("进入微信入口方法access_token------------>"+weixinBaseDev.getAccess_token());
//		System.out.println("进入微信入口方法jsapi_ticket------------>"+weixinBaseDev.getJsapi_ticket(weixinBaseDev.getAccess_token()));
//		appId=Constant.AppID;
//		timestamp=weixinBaseDev.getTimestamp();
//		nonceStr=weixinBaseDev.getNonceStr();
//		weixinBaseDev.getJsApiTicket();
//		signature=weixinBaseDev.getSignature();
        /*扫码end*/
//		WeixinGetMsgAction w=new WeixinGetMsgAction();
//		w.getAccess_token();

        return "success";
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
