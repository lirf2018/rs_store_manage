package com.yufan.weixintest.action;

import com.alibaba.fastjson.JSONObject;
import com.yufan.common.action.LotFilterAction;
import com.yufan.util.Sha1Util;
import com.yufan.weixintest.util.XmlConverJsonUtil;
import com.yufan.weixintest.weixin.WeixinDevUtils;
import com.yufan.weixintest.weixin.WeixinInformation;
import com.yufan.weixintest.weixin.bean.msg.RequestMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 创建人: lirf
 * 创建时间:  2017-09-11 11:12
 * 功能介绍:
 */
@Scope("prototype")
@Controller("weixinCallBackAction")
@Namespace("/weixin")
@ParentPackage("struts-default")
public class WeixinCallBackAction extends LotFilterAction {

    //可乐果 o0h3Psxp0dgnVYUGME2IC8UEPBuc
    //李融凡 o0h3Ps_RnNdnZxMrgM906qRfpvLI

    private static String accessToken = "11_S7yES04SR9QKlS1b1btTVYCagcWVaBcNnde_PJNiWo8CFL8AWujRh7WwZOFsJ_xkB8wxSna9LatiCkcohgLyaDzFMdnrYEOFet006oeEihhnTo1oWo2mLUoZv0CziqVHChNFEYNLIYeTjuPORMGaAJADYS";


    /**
     * 获取openId
     * http://120.76.196.189/rs_store_manage/weixin/access
     */
    @Action(value = "openid")
    public void weixinCallBackAction_getOpenId() {
        try {
            initData();
            System.out.println("------------------------获取openId------------------------");
            //第一步：用户同意授权，获取code
            //说明：scope 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo(弹出授权页面)
            // （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
//            String redirect_uri = "http://120.76.196.189/rs_store_manage/weixin/openid";
////        String scope = "snsapi_base";//静默获取
//            String scope = "snsapi_userinfo";//用户有感知
//            String redirect_uri_u = URLEncoder.encode(redirect_uri, "utf-8");
//            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0bdabad84b781956&redirect_uri=" + redirect_uri_u + "&response_type=code&scope=" + scope + "&state=STATE#wechat_redirect";
//            System.out.println(url);


            String message = null;
            message = request.getParameter("message");
            if (null == message || "".equals(message)) {
                message = readStreamParameter(request.getInputStream());
            }
            String code = message;
            System.out.println("-------------message--->" + message);
            if (StringUtils.isEmpty(code)) {
                code = request.getParameter("code");
                System.out.println("-----可经得到---getParameter----code------->" + code);
            }
            if (StringUtils.isEmpty(code)) {
                code = String.valueOf(request.getAttribute("code"));
                System.out.println("-----getAttribute----code------->" + request.getAttribute("code"));
            }

            //2 第二步：通过code换取网页授权access_token(与基础的access_token不同)
            if (StringUtils.isNotEmpty(code)) {
                JSONObject openidJson = WeixinDevUtils.getInstence().getOpenId2Json(code);
                System.out.println("-----可经得到---access_token--openidJson----->" + openidJson);
            }

        } catch (Exception e) {

        }
    }

    /**
     * 验证URL真实性  (在公众平台后台配置接口配置信息)
     * http://120.76.196.189/rs_store_manage/weixin/access
     * 微信平台接入，只能使用80端口
     * 请填写接口配置信息，此信息需要你有自己的服务器资源，填写的URL需要正确响应微信发送的Token验证，请阅读消息接口使用指南。
     */

    @Action(value = "access")
    public void weixinCallBackAction_access() {
        initData();
        System.out.println("------------------------验证URL真实性------------------------");
        access(request, response);//认证
    }


    public void weixinCallBackAction() {
        initData();
        System.out.println("---->微信回调开始");
        try {
            String accessToken = WeixinDevUtils.getInstence().getAccessToken();
//            String accessToken = "1YkNSrQWEEG_zqt5gR2uHeCuzANo4aMTFRxzrsOk9b3f7J2nOXGlINIgN-xyd8pbWFjNR1dvIVTDLtUU99WchSziMMxzy7kJ-u0wGhnrBcXe1dT_cCZEnU5TKUjQsQoRPTKiAJAGKB";
            String message = null;
            message = request.getParameter("message");
            if (null == message || "".equals(message)) {
                message = readStreamParameter(request.getInputStream());
            }
            RequestMessage inputMsg = XmlConverJsonUtil.xmlToBean(message, RequestMessage.class);
//            System.out.println(JSONObject.toJSONString(inputMsg));

//            //被动回复
            String content = "欢迎关注“程序员编程”\n" +
                    "-------------------\n" +
                    "回复数字“1”即可领取微信小程序源码以及视频教程。\n" +
                    "回复数字“2”即可领取2.5G Android项目源码。\n" +
                    "回复数字“3”即可领取IOS、JavaWeb、Hadoop、.net、PHP资料。\n" +
                    "回复数字“4”即可领取C++、Web、Python、微信公众号开发、Java、Linux资料。\n" +
                    "回复数字“5”即可领取Unity3d、reachnative、淘淘商城视频及源码、Web前端、人工智能资料。\n" +
                    "———————————";
            String servername = inputMsg.getToUserName();// 服务端
            String custermname = inputMsg.getFromUserName();// 客户端
            if ("o0h3Ps_RnNdnZxMrgM906qRfpvLI".equals(custermname)) {
                content = "李融凡";
            } else if ("o0h3Psxp0dgnVYUGME2IC8UEPBuc".equals(custermname)) {
                content = "可乐果";
            }


            String outXml = WeixinInformation.responseTextMsgXML(content, servername, custermname);
            PrintWriter writer = response.getWriter();
            writer.write(outXml);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("---->微信回调结束");
    }

    /**
     * 验证URL真实性  http://lrf-13418915218.6655.la/weixingtest/index/weixinCallBack
     *
     * @param request
     * @param response
     * @return String
     * @author morning
     * @date 2015年2月17日 上午10:53:07
     */
    private String access(HttpServletRequest request, HttpServletResponse response) {
        // 验证URL真实性
        System.out.println("进入验证access");
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");// 随机字符串
        List<String> params = new ArrayList<String>();
        params.add("weixintoken");//在配置页面填写的token
        params.add(timestamp);
        params.add(nonce);
        // 1. 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(params, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String temp = Sha1Util.getSha1(params.get(0) + params.get(1) + params.get(2));
        if (temp.equals(signature)) {
            try {
                response.getWriter().write(echostr);
                System.out.println("成功返回 echostr：" + echostr);
                return echostr;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("接口 认证 失败");
        return null;
    }

    /**
     * 从流中读取数据
     *
     * @param in
     * @return
     */
    public String readStreamParameter(ServletInputStream in) {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws Exception {
        String redirect_uri = "http://120.76.196.189/rs_store_manage/weixin/openid";
//        String scope = "snsapi_base";//静默获取
        String scope = "snsapi_userinfo";//用户有感知
        String redirect_uri_u = URLEncoder.encode(redirect_uri, "utf-8");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0bdabad84b781956&redirect_uri=" + redirect_uri_u + "&response_type=code&scope=" + scope + "&state=STATE#wechat_redirect";
        System.out.println(url);
    }

}
