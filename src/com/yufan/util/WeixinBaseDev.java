package com.yufan.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @功能名称
 * @作者 lirongfan
 * @时间 2015年9月1日 上午11:50:15
 */
public class WeixinBaseDev {

    /**
     * 获取微信参数
     */
    private String appId; // 必填，公众号的唯一标识
    private String timestamp = ""; // 必填，生成签名的时间戳
    private String nonceStr = ""; // 必填，生成签名的随机串
    private String signature = "";// 必填，签名，见附录1
    private String access_token = "";//全局唯一票据
    private String jsapi_ticket = "";
    private String secret;//AppSecret(应用密钥)

    //
    private String jsapi_ticket_url;
    private String scan_url;//扫码地址


    public WeixinBaseDev() {

    }


    /**
     * @param appId
     * @param secret
     */
    public WeixinBaseDev(String appId, String secret) {
        super();
        this.appId = appId;
        this.secret = secret;
    }


    /**
     * 获取用户access_token全局唯一票据 (获取微信用户授权)
     *
     * @return
     */
    public String getAccess_token() {
        if (null == access_token || "".equals(access_token)) {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret + "&grant_type=client_credential";
            String result = RequestMethod.sendGet(url, null);
            JSONObject json = JSONObject.parseObject(result);
            access_token = json.getString("access_token");
        }
//		access_token="i5VSZjqms1WffhGqgIGc35v5Gj5KnKDo72rpoYfRj3m9zBFo01Qw7GBed1V4XK6c6Da8M_n3XjmeCoKPi3bgSxf-80KW0IfXQ5g9-owGeMoJNBjAHAWAO";

        System.out.println("------------->access_token=" + access_token);
        return access_token;
    }

    /**
     * 获取时间搓
     *
     * @return
     */
    public String getTimestamp() {
        timestamp = String.valueOf(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳

        System.out.println("------------->timestamp（时间戳）=" + timestamp);
        return timestamp;
    }

    /**
     * 获得jsapi_ticket
     *
     * @param access_token
     * @return
     */
    public String getJsapi_ticket(String access_token) {

        String param = "access_token=" + access_token + "&type=jsapi";
        String result = RequestMethod.sendGet(jsapi_ticket_url, param);
        JSONObject json = JSONObject.parseObject(result);
        jsapi_ticket = json.getString("ticket");

        System.out.println("------------->有效的jsapi_ticket=" + jsapi_ticket);
        return jsapi_ticket;

    }

    /**
     * 生成MD5随机字符串
     *
     * @return
     */
    public String getNonceStr() {
        Random random = new Random();
        nonceStr = MD5.MD5Encode(String.valueOf(random.nextInt(10000)), "UTF-8"); // 必填，生成签名的随机串

        System.out.println("------------->noncestr（随机字符串=" + nonceStr);
        return nonceStr;
    }

    /**
     * 获取accesstoken
     *
     * @return
     */
    public Map<String, String> getJsApiTicket() {
        Map<String, String> map = new HashMap();
        String access_token = getAccess_token();
        jsapi_ticket = getJsapi_ticket(access_token);
        return map;
    }

    /**
     * 设置WX参数  生成签名=noncestr（随机字符串）+ 有效的jsapi_ticket + timestamp（时间戳）+ url（当前网页的URL，不包含#及其后面部分）
     * 注意事项:
     * 1. 签名用的noncestr和timestamp必须与wx.config中的nonceStr和timestamp相同。
     * 2. 签名用的url必须是调用JS接口页面的完整URL。
     * 3. 出于安全考虑，开发者必须在服务器端实现签名的逻辑。
     */
    public String getSignature() {
        try {
            //设置加密参数
            SortedMap<String, String> signParams = new TreeMap<String, String>();
            signParams.put("jsapi_ticket", jsapi_ticket);
            signParams.put("noncestr", nonceStr);
            signParams.put("timestamp", timestamp);
            signParams.put("url", scan_url);

            signature = Sha1Util.createSHA1Sign(signParams);// 必填，签名，见附录1

            System.out.println("------------->appId=" + appId);
            System.out.println("------------->url（当前网页的URL，不包含#及其后面部分）=" + scan_url);
            System.out.println("------------->签名=" + signature);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static void main(String[] args) {
        WeixinBaseDev d = new WeixinBaseDev("wx0bdabad84b781956", "fc25df579812bbc6d80ac74d1198cb6f");
        d.getAccess_token();
    }

}
