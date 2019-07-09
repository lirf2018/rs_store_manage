package com.yufan.weixintest.weixin;

import com.alibaba.fastjson.JSONObject;
import com.yufan.util.RequestMethod;
import com.yufan.util.WeixinBaseDev;
import com.yufan.weixintest.util.RequestUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建人: lirf
 * 创建时间:  2017-09-11 14:03
 * 功能介绍: 信息接口调用工具类
 */
public class WeixinDevUtils {

    private volatile static WeixinDevUtils weixinDevUtils = null;

    public static WeixinDevUtils getInstence() {
        if (null == weixinDevUtils) {
            synchronized (WeixinDevUtils.class) {
                if (null == weixinDevUtils) {
                    weixinDevUtils = new WeixinDevUtils();
                }
            }
        }
        return weixinDevUtils;
    }


    private static String appId = "wx0bdabad84b781956";
    private static String secret = "fc25df579812bbc6d80ac74d1198cb6f";

    /**
     * 获取普通的accessToken
     *
     * @return
     */
    public String getAccessToken() {
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + secret + "&grant_type=client_credential";
            String result = RequestUtil.sendGet(url, null);
            JSONObject json = JSONObject.parseObject(result);
            String accessToken = json.getString("access_token");
            System.out.println("---------->accessToken=" + accessToken);
            return accessToken;
        } catch (Exception e) {

        }
        return null;
    }

    //获取用户基本信息（包括UnionID机制）
    public static void getUserInfo(String accessToken, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);
    }

    /**
     * 测试发送模板消息
     */
    public static void sendModoTest(String accesstoken, String openId, JSONObject obj) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accesstoken;
        obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("template_id", "NGMtvXQswNd1sU_0IhfSZtE1YVuiDk_HTUat5TullW8");
        obj.put("url", "www.baidu.com");
        obj.put("topcolor", "#FF0000");
        JSONObject data = new JSONObject();
        /************设置内容**************/
        //商品名称
        Map<String, Object> goodsName = new HashMap<String, Object>();
        goodsName.put("value", "狗不理包子\n");
        goodsName.put("color", "#173177");
        data.put("goodsName", goodsName);
        //商品价格
        Map<String, Object> goodsPrice = new HashMap<String, Object>();
        goodsPrice.put("value", "28.36\n");
        goodsPrice.put("color", "#173177");
        data.put("goodsPrice", goodsPrice);
        //remark
        Map<String, Object> remark = new HashMap<String, Object>();
        remark.put("value", "狗不理包子好吃\n");
        remark.put("color", "#173177");
        data.put("remark", remark);

        obj.put("data", data);
        System.out.println("obj.toString()=" + obj.toString());
        String result = RequestUtil.httpPost(url, obj.toString());
        System.out.println("result=" + result);
    }

    /**
     * 获取openid
     * 第二步：通过code换取网页授权access_token(与基础的access_token不同)
     */
    public JSONObject getOpenId2Json(String code) {
        //            https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        System.out.println("-----getOpenId2Json----->" + url);
//        String result = RequestUtil.sendGet(url, null);
        String result = RequestUtil.sendPostWX(url, null);
        JSONObject json = JSONObject.parseObject(result);
        return json;
    }


    //获取用户列表
    public void getUserList(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=";
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);
        //"openid":["o0h3Ps_RnNdnZxMrgM906qRfpvLI","o0h3Psxp0dgnVYUGME2IC8UEPBuc"]
    }

    /**
     * 生成带参数的二维码(临时)
     */
    private void createTemporaryQR(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
        //7天
        //POST数据例子{"expire_seconds": 604800, "action_name": "QR_SCENE", "action_info": {"scene": {"scene_id": 123}}}
        JSONObject obj = new JSONObject();
        obj.put("expire_seconds", 604800);//该二维码有效时间，以秒为单位。 最大不超过604800（即7天）
        obj.put("action_name", "QR_SCENE");
        JSONObject objInfo = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("scene_id", 888);
        objInfo.put("scene", map);
        obj.put("action_info", objInfo);
//		System.out.println("obj="+obj);
        //获取创建二维码ticket
        //gQGR8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0gwaDQ4MVRsakVqMW9EZFdUbVlXAAIE0tvPVwMEgDoJAA==
        //http://weixin.qq.com/q/H0h481TljEj1oDdWTmYW
        //获取换取二维码的ticket
//		String result=RequestMethod.sendPostWX(url,obj);
//		System.out.println(result);

        String ticket = "gQGR8ToAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL0gwaDQ4MVRsakVqMW9EZFdUbVlXAAIE0tvPVwMEgDoJAA==";
        //换取二维码
        String url_ = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket;

    }

    /**
     * 创建分组
     */
    public void createGroup(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=" + accessToken;
        //POST数据例子：{"group":{"name":"test"}}
        JSONObject obj = new JSONObject();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "groupB");
        obj.put("group", map);
        RequestMethod.sendPostWX(url, obj);
    }

    /**
     * 查询分组
     */
    public void getGroup(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=" + accessToken;
        String result = RequestMethod.sendGetWX1(url);
        System.out.println(result);

    }

    /**
     * 移动用户分组
     */
    public void moveUserGroup(String accessToken) {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=" + accessToken;
        //POST数据例子：{"openid":"oDF3iYx0ro3_7jD4HFRDfrjdCM58","to_groupid":108}
        JSONObject obj = new JSONObject();
        obj.put("openid", "o0h3Psxp0dgnVYUGME2IC8UEPBuc");
        obj.put("to_groupid", 100);
        System.out.println(RequestMethod.sendPostWX(url, obj));
    }


}
