package com.yufan.weixin.bean;

/**
 * @功能名称 微信信息类
 * @作者 lirongfan
 * @时间 2016年2月19日 上午10:34:48
 */
public class WeiXinInfoBean {

    /**
     * AppID(应用ID)
     */
    private String appID;

    /**
     * AppSecret(应用密钥)
     */
    private String appsecret;

    /**
     * 获得jsapi_ticket_url
     */
    private String jsapi_ticket_url;

    /**
     * 扫码页面地址
     */
    private String scan_url;


    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getJsapi_ticket_url() {
        return jsapi_ticket_url;
    }

    public void setJsapi_ticket_url(String jsapi_ticket_url) {
        this.jsapi_ticket_url = jsapi_ticket_url;
    }

    public String getScan_url() {
        return scan_url;
    }

    public void setScan_url(String scan_url) {
        this.scan_url = scan_url;
    }


}
